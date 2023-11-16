package com.tracks.performance.chains;

import com.tracks.performance.enums.RequestBodyParameter;
import com.tracks.performance.enums.XPathExpression;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;

import java.util.Objects;

import static com.tracks.performance.ContextsSimulation.TEST_CONFIGURATION;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class ActionsWithContexts extends BaseChain {

    private final static int SHORT_PAUSE_DURATION = 2;
    private final static int LONG_PAUSE_DURATION = 5;
    private final static String XML_APPLICATION_ACCEPT_HEADER = "application/xml";
    private final static String ACCEPT_HEADER = "Accept";
    private final static String TEXT_HTML_ACCEPT_HEADER = "text/html";
    private final static String CSRF_TOKEN = "csrfToken";
    private final static String DSL_CSRF_TOKEN = String.format("#{%s}", CSRF_TOKEN);
    private final static String CONTEXT_ENDPOINT = "contextEndpoint";
    private final static String CONTEXT_NUMBER = "contextNumbers";
    private final static String DSL_CONTEXT_ENDPOINT = String.format("#{%s}", CONTEXT_ENDPOINT);
    private final static String DSL_CONTEXT_NAME = String.format("#{%s}", CONTEXT_NAME);
    private final static String DSL_CONTEXT_STATE = String.format("#{%s}", CONTEXT_STATE);
    private final static String CONTEXT_ENDPOINT_REGEX = "/contexts/\\d+";

    public static ChainBuilder getAllContext = exec(http("Get all contexts")
            .get(TEST_CONFIGURATION.contextEndpoint())
            .header(ACCEPT_HEADER, TEXT_HTML_ACCEPT_HEADER)
            .check(CoreDsl.xpath(XPathExpression.CSRF_TOKEN_XPATH.getExpression()).saveAs(CSRF_TOKEN))
            .check(CoreDsl.xpath(XPathExpression.VIEW_CONTEXT.getExpression()).count().saveAs(CONTEXT_NUMBER))
            .check(regex(CONTEXT_ENDPOINT_REGEX).findRandom().saveAs(CONTEXT_ENDPOINT)));

    public static ChainBuilder postNewContext = feed(customFeeder)
            .exec(getAllContext)
            .pause(LONG_PAUSE_DURATION)
            .exec(http("Create context")
                    .post(TEST_CONFIGURATION.contextEndpoint())
                    .header(ACCEPT_HEADER, XML_APPLICATION_ACCEPT_HEADER)
                    .formParam(RequestBodyParameter.CONTEXT_NAME_PARAM.getParameter(), DSL_CONTEXT_NAME)
                    .formParam(RequestBodyParameter.CONTEXT_STATE_PARAM.getParameter(), DSL_CONTEXT_STATE)
                    .formParam(RequestBodyParameter.AUTHENTICITY_TOKEN.getParameter(), DSL_CSRF_TOKEN))
            .pause(SHORT_PAUSE_DURATION);

    public static ChainBuilder editExistedContext = feed(customFeeder)
            .exec(getAllContext)
            .doIf(session -> (Integer.parseInt(Objects.requireNonNull(session.getString(CONTEXT_NUMBER)))) > 1).then(
                    exec(http("Edit context")
                            .put(DSL_CONTEXT_ENDPOINT)
                            .header(ACCEPT_HEADER, XML_APPLICATION_ACCEPT_HEADER)
                            .formParam(RequestBodyParameter.CONTEXT_NAME_PARAM.getParameter(), DSL_CONTEXT_NAME)
                            .formParam(RequestBodyParameter.CONTEXT_STATE_PARAM.getParameter(), DSL_CONTEXT_STATE)
                            .formParam(RequestBodyParameter.AUTHENTICITY_TOKEN.getParameter(), DSL_CSRF_TOKEN))
                            .pause(SHORT_PAUSE_DURATION));

    public static ChainBuilder deleteExistedContext = feed(customFeeder)
            .exec(getAllContext)
            .doIf(session -> (Integer.parseInt(Objects.requireNonNull(session.getString(CONTEXT_NUMBER)))) > 1).then(
                    exec(http("Delete context")
                            .delete(DSL_CONTEXT_ENDPOINT)
                            .header(ACCEPT_HEADER, XML_APPLICATION_ACCEPT_HEADER))
                            .pause(SHORT_PAUSE_DURATION));
}
