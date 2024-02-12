package com.tracks.performance;

import static com.tracks.performance.chains.ContextChain.deleteExistedContext;
import static com.tracks.performance.chains.ContextChain.postNewContext;
import static com.tracks.performance.chains.SignInChain.loginAsUser;
import static com.tracks.performance.chains.AdminChain.*;
import static com.tracks.performance.enums.PopulationProfiles.*;
import static com.tracks.performance.enums.RuntimeProperties.POPULATION_PROFILE;
import static com.tracks.performance.enums.RuntimeProperties.RUNTIME_ENV;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

import com.tracks.conf.TestConfiguration;
import com.tracks.performance.enums.PopulationProfiles;
import com.tracks.performance.utils.DatabaseQueries;
import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import org.aeonbits.owner.ConfigFactory;

import java.util.List;
import java.util.Map;

public class TestSimulation extends Simulation {

  public static final TestConfiguration TEST_CONFIGURATION =
      ConfigFactory.create(TestConfiguration.class);

  // HTTP PROTOCOL
  private final HttpProtocolBuilder httpProtocol =
      http.baseUrl(getBaseUrl());

  // SCENARIOS
  private final ScenarioBuilder admin =
      scenario("Admin create 10 users and remove one")
              .exec(getSignUpPage)
              .repeat(10)
              .on(createNewUser)
              .pause(LONG_PAUSE_DURATION)
              .exec(getAllUsersPage)
              .exec(deleteExistingUser);

  private final ScenarioBuilder users =
      scenario("User create 5 contexts")
              .exec(loginAsUser)
              .repeat(5)
              .on(postNewContext)
              .exec(deleteExistedContext);

  // LOAD SIMULATIONS
  {
    getPopulationProfile().protocols(httpProtocol);
  }

  /* Clear database tables after simulation run */
  @Override
  public void after() {
    DatabaseQueries.deleteAllNotAdminUsers();
    DatabaseQueries.deleteAllContexts();
  }

  /* Method returns simulation with selected injection profile that is based value of injectionProfile system property */
  private Simulation.SetUp getPopulationProfile() {
    String profileDescription = POPULATION_PROFILE.getStringValue();
    PopulationProfiles profile = PopulationProfiles.getProfile(profileDescription);
    Map<PopulationProfiles, List<PopulationBuilder>> populationBuildersMap =
            Map.of(
                    STEPS_RPS_POPULATION,
                    List.of(
                            admin.injectOpen(atOnceUsers(1)),
                            users.injectOpen(TestPopulation.getAtOnceUsersPopulation())
                                    .throttle(TestPopulation.getStepsRpsPopulation())
                    ),
                    PLATO_RPS_POPULATION,
                    List.of(
                            admin.injectOpen(atOnceUsers(1)),
                            users.injectOpen(TestPopulation.getAtOnceUsersPopulation())
                                    .throttle(TestPopulation.getPlatoRpsPopulation())
                    ),
                    PEAKS_RPS_POPULATION,
                    List.of(
                            admin.injectOpen(atOnceUsers(1)),
                            users.injectOpen(TestPopulation.getAtOnceUsersPopulation())
                                    .throttle(TestPopulation.getSpikedRpsPopulation())
                    ),
                    PLATO_USER_POPULATION,
                    List.of(
                            admin.injectOpen(atOnceUsers(1)),
                            users.injectOpen(TestPopulation.getPlatoUserPopulation())
                    )
            );
    return setUp(populationBuildersMap.get(profile));
  }

  /* Returns baseUrl string depended on runtimeEnv system property value */
  private String getBaseUrl() {
    return "maven".equals(RUNTIME_ENV.getStringValue())
            ? TEST_CONFIGURATION.mavenBaseUrl()
            : TEST_CONFIGURATION.dockerBaseUrl();
  }
}
