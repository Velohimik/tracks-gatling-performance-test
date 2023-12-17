package com.tracks.performance.chains;

import com.tracks.performance.enums.RequestBodyParameter;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;

import static com.tracks.performance.TestSimulation.TEST_CONFIGURATION;
import static com.tracks.performance.enums.CssSelectors.AUTHENTICITY_TOKEN_SELECTOR;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.entity.ContentType.APPLICATION_FORM_URLENCODED;
import static org.apache.http.entity.ContentType.TEXT_HTML;

public class SignUpChain extends BaseChain {

  public static ChainBuilder getSignUpPage =
      exec(
          http("Get sign up page")
              .get(TEST_CONFIGURATION.signUpEndpoint())
              .basicAuth(TEST_CONFIGURATION.adminLogin(), TEST_CONFIGURATION.adminPassword())
              .header(CONTENT_TYPE, TEXT_HTML.toString())
              .check(status().is(200))
              .check(
                  CoreDsl.css(AUTHENTICITY_TOKEN_SELECTOR.getExpression(), "value")
                      .find()
                      .exists()
                      .saveAs(AUTHENTICITY_TOKEN_VAR)));

  public static ChainBuilder createNewUser =
      feed(authFeeder)
          .exec(
              http("Create a new user " + DSL_USER_NAME)
                  .post(TEST_CONFIGURATION.usersEndpoint())
                  .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED.toString())
                  .formParam(
                      RequestBodyParameter.AUTHENTICITY_TOKEN.getParameter(),
                      DSL_AUTHENTICITY_TOKEN)
                  .formParam(RequestBodyParameter.CREATE_USER_LOGIN.getParameter(), DSL_USER_NAME)
                  .formParam(
                      RequestBodyParameter.CREATE_USER_PASSWORD.getParameter(), DSL_USER_PASSWORD)
                  .formParam(
                      RequestBodyParameter.CREATE_USER_PASSWORD_CONFIRM.getParameter(),
                      DSL_USER_PASSWORD))
          .pause(SHORT_PAUSE_DURATION);
}
