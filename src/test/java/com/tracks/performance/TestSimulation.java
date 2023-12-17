package com.tracks.performance;

import static com.tracks.performance.chains.ContextChain.postNewContext;
import static com.tracks.performance.chains.SignInChain.loginAsUser;
import static com.tracks.performance.chains.SignUpChain.*;
import static com.tracks.performance.enums.RuntimeProperties.POPULATION_PROFILE;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

import com.tracks.conf.TestConfiguration;
import com.tracks.performance.enums.PopulationProfiles;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import org.aeonbits.owner.ConfigFactory;

public class TestSimulation extends Simulation {

  public static final TestConfiguration TEST_CONFIGURATION =
      ConfigFactory.create(TestConfiguration.class);

  // HTTP PROTOCOL
  private final HttpProtocolBuilder httpProtocol =
      http.baseUrl(TEST_CONFIGURATION.baseUrl())
          .basicAuth(TEST_CONFIGURATION.login(), TEST_CONFIGURATION.password());

  // SCENARIOS
  private final ScenarioBuilder usersCreation =
      scenario("Admin create 10 users")
          .exec(getSignUpPage)
          .repeat(10)
          .on(createNewUser);

  private final ScenarioBuilder userExecution =
      scenario("User create 5 contexts")
          .exec(loginAsUser)
          .pause(2)
          .repeat(5)
          .on(postNewContext)
          .pause(2);

  // LOAD SIMULATIONS
  {
    Simulation.SetUp simulation = getPopulationProfile();
  }

  private Simulation.SetUp getPopulationProfile() {
    String profileDescription = POPULATION_PROFILE.getStringValue();
    PopulationProfiles profile = PopulationProfiles.getProfile(profileDescription);
    switch (profile) {
      case STEPS_RPS_POPULATION:
        return setUp(
                usersCreation.injectOpen(atOnceUsers(1)),
                userExecution.injectOpen(TestPopulation.getAtOnceUsersPopulation()))
            .throttle(TestPopulation.getStepsRpsPopulation())
            .protocols(httpProtocol);
      case PLATO_RPS_POPULATION:
        return setUp(
                usersCreation.injectOpen(atOnceUsers(1)),
                userExecution.injectOpen(TestPopulation.getAtOnceUsersPopulation()))
            .throttle(TestPopulation.getPlatoRpsPopulation())
            .protocols(httpProtocol);
      case PEAKS_RPS_POPULATION:
        return setUp(
                usersCreation.injectOpen(atOnceUsers(1)),
                userExecution.injectOpen(TestPopulation.getAtOnceUsersPopulation()))
            .throttle(TestPopulation.getSpikedRpsPopulation())
            .protocols(httpProtocol);
      default:
        return setUp(
                usersCreation.injectOpen(atOnceUsers(1)),
                userExecution.injectOpen(TestPopulation.getPlatoUserPopulation()))
            .protocols(httpProtocol);
    }
  }
}
