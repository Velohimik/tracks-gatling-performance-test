package com.tracks.performance.chains;

import static com.tracks.performance.TestSimulation.TEST_CONFIGURATION;
import static com.tracks.performance.enums.CssSelectors.CSRF_TOKEN_SELECTOR;
import static com.tracks.performance.enums.CssSelectors.VIEW_CONTEXT_SELECTOR;
import static com.tracks.performance.enums.RequestBodyParameter.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.entity.ContentType.*;

import io.gatling.javaapi.core.ChainBuilder;

/*
 * ContextChain class contains ChainBuilder instances that configuring requests to Context page for getting the list,
 * creating, updating and deleting existing contexts.
 */
public class ContextChain extends BaseChain {

  /* Get request to get the list of existing contexts */
  public static ChainBuilder getAllContext = exec(http("Get all contexts")
          .get(TEST_CONFIGURATION.contextEndpoint())
          .basicAuth(DSL_USER_NAME, DSL_USER_PASSWORD)
          .header(CONTENT_TYPE, TEXT_HTML.toString())
          .check(css(CSRF_TOKEN_SELECTOR.getSelector(), CONTENT_SELECTOR_ATTRIBUTE).saveAs(CSRF_TOKEN))
          .check(css(VIEW_CONTEXT_SELECTOR.getSelector()).count().saveAs(CONTEXT_NUMBER)));

  /* Post request to create a new context */
  public static ChainBuilder postNewContext = feed(contextFeeder)
          .exec(getAllContext)
          .pause(LONG_PAUSE_DURATION)
          .exec(http("Create context")
                  .post(TEST_CONFIGURATION.contextEndpoint())
                  .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED.toString())
                  .formParam(CONTEXT_NAME_PARAM.getParameter(), DSL_CONTEXT_NAME)
                  .formParam(CONTEXT_STATE_PARAM.getParameter(), DSL_CONTEXT_STATE)
                  .formParam(AUTHENTICITY_TOKEN.getParameter(), DSL_CSRF_TOKEN))
          .pause(SHORT_PAUSE_DURATION);

  /* Delete request to remove the existing context */
  public static ChainBuilder deleteExistedContext = exec(getAllContext)
          .exec(http("Delete context")
                  .delete(session -> makeContextsEndpoint(session.getString(USER_NAME)))
                  .header(CONTENT_TYPE, APPLICATION_XML.toString())
                  .header("X-Csrf-Token", DSL_CSRF_TOKEN))
          .pause(SHORT_PAUSE_DURATION);}
