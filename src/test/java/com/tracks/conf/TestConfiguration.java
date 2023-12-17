package com.tracks.conf;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:settings/TestConfiguration.properties")
public interface TestConfiguration extends Config {

    @Key("base.url")
    String baseUrl();

    @Key("context.endpoint")
    String contextEndpoint();

    @Key("admin.login")
    String login();

    @Key("admin.password")
    String password();

    @Key("database.driver")
    String databaseDriver();

    @Key("database.url")
    String databaseUrl();

    @Key("database.username")
    String databaseUsername();

    @Key("database.password")
    String databasePassword();

    @Key("admin.login")
    String adminLogin();

    @Key("admin.password")
    String adminPassword();

    @Key("sql.queries.folder")
    String sqlQueriesFolder();

    @Key("signup.endpoint")
    String signUpEndpoint();

    @Key("users.endpoint")
    String usersEndpoint();

    @Key("login.endpoint")
    String loginEndpoint();
}
