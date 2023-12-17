package com.tracks.performance.chains;

import com.tracks.performance.enums.RequestBodyParameter;
import io.gatling.javaapi.core.ChainBuilder;

import static com.tracks.performance.TestSimulation.TEST_CONFIGURATION;
import static com.tracks.performance.enums.CssSelectors.AUTHENTICITY_TOKEN_SELECTOR;
import static io.gatling.javaapi.core.CoreDsl.css;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.http.HttpDsl.http;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.entity.ContentType.APPLICATION_FORM_URLENCODED;
import static org.apache.http.entity.ContentType.TEXT_HTML;

public class SignInChain extends BaseChain {

  public static ChainBuilder loginAsUser =
      exec(http("Get login page")
              .get(TEST_CONFIGURATION.loginEndpoint())
              .header(CONTENT_TYPE, TEXT_HTML.toString())
              .check(
                  css(AUTHENTICITY_TOKEN_SELECTOR.getExpression(), "value")
                      .saveAs(AUTHENTICITY_TOKEN_VAR)))
          .feed(loginFeeder)
          .exec(
              http("Login as user " + DSL_USER_NAME)
                  .post(TEST_CONFIGURATION.loginEndpoint())
                  .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED.toString())
                  .formParam(
                      RequestBodyParameter.AUTHENTICITY_TOKEN.getParameter(),
                      DSL_AUTHENTICITY_TOKEN)
                  .formParam(RequestBodyParameter.USE_USER_LOGIN.getParameter(), DSL_USER_NAME)
                  .formParam(
                      RequestBodyParameter.USE_USER_PASSWORD.getParameter(), DSL_USER_PASSWORD));
}
