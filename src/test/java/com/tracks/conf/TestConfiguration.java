package com.tracks.conf;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:settings/TestConfiguration.properties")
public interface TestConfiguration extends Config {

  @Key("docker.base.url")
  String dockerBaseUrl();

  @Key("maven.base.url")
  String mavenBaseUrl();

  @Key("context.endpoint")
  String contextEndpoint();

  @Key("database.driver")
  String databaseDriver();

  @Key("docker.database.url")
  String dockerDatabaseUrl();

  @Key("maven.database.url")
  String mavenDatabaseUrl();

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
