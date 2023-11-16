package com.tracks.performance.conf;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:TestConfiguration.properties")
public interface TestConfiguration extends Config {

    @Key("base.url")
    String baseUrl();

    @Key("context.endpoint")
    String contextEndpoint();

    @Key("login")
    String login();

    @Key("password")
    String password();

    @Key("database.driver")
    String databaseDriver();

    @Key("database.url")
    String databaseUrl();

    @Key("database.username")
    String databaseUsername();

    @Key("database.password")
    String databasePassword();
}
