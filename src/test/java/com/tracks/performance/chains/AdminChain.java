package com.tracks.performance.chains;

import static com.tracks.performance.TestSimulation.TEST_CONFIGURATION;
import static com.tracks.performance.enums.CssSelectors.AUTHENTICITY_TOKEN_SELECTOR;
import static com.tracks.performance.enums.RequestBodyParameter.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.entity.ContentType.APPLICATION_FORM_URLENCODED;
import static org.apache.http.entity.ContentType.TEXT_HTML;

import io.gatling.javaapi.core.ChainBuilder;

/*
 * AdminChain class contains ChainBuilder instances that configuring GET requests as Admin user to Manage users page,
 * Sign up page and POST request for creation of a new user.
 */
public class AdminChain extends BaseChain {

  /* Get request to Sign up page */
  public static ChainBuilder getSignUpPage =
          exec(http("Get sign up page")
                  .get(TEST_CONFIGURATION.signUpEndpoint())
                  .basicAuth(TEST_CONFIGURATION.adminLogin(), TEST_CONFIGURATION.adminPassword())
                  .header(CONTENT_TYPE, TEXT_HTML.toString())
                  .check(status().is(200))
                  .check(
                          css(AUTHENTICITY_TOKEN_SELECTOR.getSelector(), VALUE_SELECTOR_ATTRIBUTE)
                                  .saveAs(AUTHENTICITY_TOKEN_VAR)))
                  .pause(SHORT_PAUSE_DURATION);

  /* Get request to Manage users page */
  public static ChainBuilder getAllUsersPage =
          exec(http("Get the list of all users")
                  .get(TEST_CONFIGURATION.usersEndpoint())
                  .basicAuth(TEST_CONFIGURATION.adminLogin(), TEST_CONFIGURATION.adminPassword())
                  .header(CONTENT_TYPE, TEXT_HTML.toString())
                  .check(status().is(200))
                  .check(
                          css(AUTHENTICITY_TOKEN_SELECTOR.getSelector(), VALUE_SELECTOR_ATTRIBUTE)
                                  .saveAs(AUTHENTICITY_TOKEN_VAR)))
                  .pause(SHORT_PAUSE_DURATION);

  /* Post request with the new user credentials to /users endpoint */
  public static ChainBuilder createNewUser =
          feed(signUpFeeder)
                  .exec(
                          http("Create a new user")
                                  .post(TEST_CONFIGURATION.usersEndpoint())
                                  .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED.toString())
                                  .formParam(AUTHENTICITY_TOKEN.getParameter(), DSL_AUTHENTICITY_TOKEN)
                                  .formParam(CREATE_USER_LOGIN.getParameter(), DSL_USER_NAME)
                                  .formParam(CREATE_USER_PASSWORD.getParameter(), DSL_USER_PASSWORD)
                                  .formParam(CREATE_USER_PASSWORD_CONFIRM.getParameter(), DSL_USER_PASSWORD))
                  .pause(SHORT_PAUSE_DURATION);

  /* Delete request with existing user's id to remove it */
  public static ChainBuilder deleteExistingUser =
          exec(http("Delete an existing user")
                  .delete(session -> makeUsersEndpoint())
                  .basicAuth(TEST_CONFIGURATION.adminLogin(), TEST_CONFIGURATION.adminPassword())
                  .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED.toString())
                  .header("X-Csrf-Token", DSL_AUTHENTICITY_TOKEN))
                  .pause(SHORT_PAUSE_DURATION);}
