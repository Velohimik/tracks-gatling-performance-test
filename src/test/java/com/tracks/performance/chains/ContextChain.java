package com.tracks.performance.chains;

import static com.tracks.performance.TestSimulation.TEST_CONFIGURATION;
import static com.tracks.performance.enums.CssSelectors.CSRF_TOKEN_XPATH;
import static com.tracks.performance.enums.CssSelectors.VIEW_CONTEXT;
import static com.tracks.performance.enums.RequestBodyParameter.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.entity.ContentType.*;

import io.gatling.javaapi.core.ChainBuilder;
import java.util.Objects;

public class ContextChain extends BaseChain {

  private static final String CONTEXT_ENDPOINT_REGEX = "/contexts/\\d+";

  public static ChainBuilder getAllContext =
      exec(
          http("Get all contexts")
              .get(TEST_CONFIGURATION.contextEndpoint())
              .header(CONTENT_TYPE, TEXT_HTML.toString())
              .check(css(CSRF_TOKEN_XPATH.getExpression(), "content").saveAs(CSRF_TOKEN))
              .check(css(VIEW_CONTEXT.getExpression()).count().saveAs(CONTEXT_NUMBER)));

  public static ChainBuilder postNewContext =
      feed(contextFeeder)
          .exec(getAllContext)
          .pause(LONG_PAUSE_DURATION)
          .exec(
              http("Create context")
                  .post(TEST_CONFIGURATION.contextEndpoint())
                  .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED.toString())
                  .formParam(CONTEXT_NAME_PARAM.getParameter(), DSL_CONTEXT_NAME)
                  .formParam(CONTEXT_STATE_PARAM.getParameter(), DSL_CONTEXT_STATE)
                  .formParam(AUTHENTICITY_TOKEN.getParameter(), DSL_CSRF_TOKEN))
          .pause(SHORT_PAUSE_DURATION);

  public static ChainBuilder editExistedContext =
      feed(contextFeeder)
          .exec(getAllContext)
          .doIf(
              session ->
                  (Integer.parseInt(Objects.requireNonNull(session.getString(CONTEXT_NUMBER)))) > 1)
          .then(
              exec(http("Edit context")
                      .put(DSL_CONTEXT_ENDPOINT)
                      .header(CONTENT_TYPE, APPLICATION_XML.toString())
                      .formParam(CONTEXT_NAME_PARAM.getParameter(), DSL_CONTEXT_NAME)
                      .formParam(CONTEXT_STATE_PARAM.getParameter(), DSL_CONTEXT_STATE)
                      .formParam(AUTHENTICITY_TOKEN.getParameter(), DSL_CSRF_TOKEN))
                  .pause(SHORT_PAUSE_DURATION));

  public static ChainBuilder deleteExistedContext =
      feed(contextFeeder)
          .exec(getAllContext)
          .doIf(
              session ->
                  (Integer.parseInt(Objects.requireNonNull(session.getString(CONTEXT_NUMBER)))) > 1)
          .then(
              exec(http("Delete context")
                      .delete(DSL_CONTEXT_ENDPOINT)
                      .header(CONTENT_TYPE, APPLICATION_XML.toString()))
                  .pause(SHORT_PAUSE_DURATION));
}
