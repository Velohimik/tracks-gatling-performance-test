package com.tracks.performance.utils;

import static com.tracks.performance.TestSimulation.TEST_CONFIGURATION;
import static com.tracks.performance.enums.RuntimeProperties.RUNTIME_ENV;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DatabaseUtils {

    private static final JdbcTemplate jdbcTemplate = new JdbcTemplate(setupDatabaseConnection());

    private static DataSource setupDatabaseConnection() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(TEST_CONFIGURATION.databaseDriver());
        dataSource.setUrl(getDatabaseUrl());
        dataSource.setUsername(TEST_CONFIGURATION.databaseUsername());
        dataSource.setPassword(TEST_CONFIGURATION.databasePassword());

        return dataSource;
    }

    public static void executeSimpleQuery(String query) {
        jdbcTemplate.update(query);
    }

    public static List<Integer> getListOfValuesQuery(String query) {
        return jdbcTemplate.queryForList(query, Integer.class);
    }

    private static String getDatabaseUrl() {
        return "maven".equals(RUNTIME_ENV.getStringValue())
                ? TEST_CONFIGURATION.mavenDatabaseUrl()
                : TEST_CONFIGURATION.dockerDatabaseUrl();
    }
}
