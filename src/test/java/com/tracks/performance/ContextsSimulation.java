package com.tracks.performance;

import static com.tracks.performance.chains.ActionsWithContexts.*;
import static com.tracks.performance.enums.RuntimeProperties.POPULATION_PROFILE;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

import com.tracks.performance.conf.TestConfiguration;
import com.tracks.performance.enums.PopulationProfiles;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import javax.sql.DataSource;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class ContextsSimulation extends Simulation {

    public final static TestConfiguration TEST_CONFIGURATION = ConfigFactory.create(TestConfiguration.class);
    //HTTP PROTOCOL PARAMETERS
    private final static String BASE_URL = TEST_CONFIGURATION.baseUrl();
    private final static String LOGIN = TEST_CONFIGURATION.login();
    private final static String PASSWORD = TEST_CONFIGURATION.password();

    //DATABASE CONFIGURATION
    private final static String INSERT_ONE_CONTEXT_SQL = "INSERT INTO contexts (name, position, state) " +
        "VALUES ('context', '1', 'active');";
    private final static String DELETE_ALL_CONTEXTS_SQL = "DELETE FROM contexts;";
    private final static String DATABASE_DRIVER = TEST_CONFIGURATION.databaseDriver();
    private final static String DATABASE_URL = TEST_CONFIGURATION.databaseUrl();
    private final static String DATABASE_USERNAME = TEST_CONFIGURATION.databaseUsername();
    private final static String DATABASE_PASSWORD = TEST_CONFIGURATION.databasePassword();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(sqlDataSource());

    public DataSource sqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DATABASE_DRIVER);
        dataSource.setUrl(DATABASE_URL);
        dataSource.setUsername(DATABASE_USERNAME);
        dataSource.setPassword(DATABASE_PASSWORD);
        return dataSource;
    }

    //BEFORE SCENARIO
    @Override
    public void before() {
        jdbcTemplate.queryForList(INSERT_ONE_CONTEXT_SQL);
    }

    //AFTER SCENARIO
    @Override
    public void after() {
        jdbcTemplate.queryForList(DELETE_ALL_CONTEXTS_SQL);
    }

    // HTTP PROTOCOL
    private final HttpProtocolBuilder httpProtocol = http
        .baseUrl(BASE_URL)
        .basicAuth(LOGIN, PASSWORD);

    // SCENARIOS
    private final ScenarioBuilder users = scenario("Manage perf")
        .randomSwitch().on(
            Choice.withWeight(60.0, exec(getAllContext)),
            Choice.withWeight(30.0, exec(postNewContext)),
            Choice.withWeight(5.0, exec(editExistedContext)),
            Choice.withWeight(5.0, exec(deleteExistedContext))
        );

    // LOAD SIMULATIONS
    {
        Simulation.SetUp simulation = getPopulationProfile();
    }

    private Simulation.SetUp getPopulationProfile() {
        String profileDescription = POPULATION_PROFILE.getStringValue();
        PopulationProfiles profile = PopulationProfiles.getProfile(profileDescription);
        switch (profile) {
            case STEPS_RPS_POPULATION:
                return setUp(users.injectOpen(TestPopulation.getAtOnceUsersPopulation()))
                        .throttle(TestPopulation.getStepsRpsPopulation())
                        .protocols(httpProtocol);
            case PLATO_RPS_POPULATION:
                return setUp(users.injectOpen(TestPopulation.getAtOnceUsersPopulation()))
                        .throttle(TestPopulation.getPlatoRpsPopulation())
                        .protocols(httpProtocol);
            case PEAKS_RPS_POPULATION:
                return setUp(users.injectOpen(TestPopulation.getAtOnceUsersPopulation()))
                        .throttle(TestPopulation.getSpikedRpsPopulation())
                        .protocols(httpProtocol);
            default: return setUp(users.injectOpen(TestPopulation.getPlatoUserPopulation()))
                    .protocols(httpProtocol);
        }
    }
}