package com.tracks.performance.enums;

public enum RuntimeProperties {

    POPULATION_PROFILE("injectionProfile", "userPlato"),
    NUMBER_OF_USERS_PER_SECOND_ON_PLATO("numberOfUsersPerSecondOnPlato", "5"),
    PLATO_LENGTH("platoLength", "200"),
    STEP_LENGTH("stepLength", "30"),
    HIGHEST_STEP_RPS("highestStepsRps", "20"),
    RPS_ON_PLATO("rpsOnPlato", "200"),
    PEAK_RPS("peakRps", "100"),
    TIME_BETWEEN_PEAKS("timeBetweenPeak", "20");

    private final String propertyName;
    private final String defaultValue;

    RuntimeProperties(String propertyName, String defaultValue) {
        this.propertyName = propertyName;
        this.defaultValue = defaultValue;
    }

    public Integer getIntegerValue() {
        return Integer.parseInt(System.getProperty(propertyName, defaultValue));
    }

    public String getStringValue() {
        return System.getProperty(propertyName, defaultValue);
    }
}
