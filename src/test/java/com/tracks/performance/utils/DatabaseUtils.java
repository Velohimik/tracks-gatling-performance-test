package com.tracks.performance.utils;

import com.tracks.conf.TestConfiguration;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class DatabaseUtils {

    private static final TestConfiguration TEST_CONFIGURATION = ConfigFactory.create(TestConfiguration.class);
    private static final JdbcTemplate jdbcTemplate = new JdbcTemplate(setupDatabaseConnection());

    private static DataSource setupDatabaseConnection() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(TEST_CONFIGURATION.databaseDriver());
        dataSource.setUrl(TEST_CONFIGURATION.databaseUrl());
        dataSource.setUsername(TEST_CONFIGURATION.databaseUsername());
        dataSource.setPassword(TEST_CONFIGURATION.databasePassword());
        return dataSource;
    }

    public static void insertValuesQuery(String query, Object[] args) {
        jdbcTemplate.update(query, args);
    }

    public static void executeSimpleQuery(String query) {
        jdbcTemplate.update(query);
    }

    public static int getValueQuery(String query) {
        return jdbcTemplate.update(query);
    }
}
