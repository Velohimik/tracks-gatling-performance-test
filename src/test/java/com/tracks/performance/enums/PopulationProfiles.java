package com.tracks.performance.enums;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum PopulationProfiles {

    PLATO_USER_POPULATION("userPlato"),
    STEPS_RPS_POPULATION("rpsSteps"),
    PLATO_RPS_POPULATION("rpsPlato"),
    PEAKS_RPS_POPULATION("rpsPeaks");

    private final String profileDescription;

    PopulationProfiles(String description) {
        this.profileDescription = description;
    }

    public static PopulationProfiles getProfile(String profileDescription) {
        for (PopulationProfiles profile:PopulationProfiles.values()) {
            if(profile.getProfileDescription().equals(profileDescription)) {
                log.info("Selected Population Profile is: " + profile.name());
                return profile;
            }
        }
        log.info("No Population Profiles description matches the entered one, the default value is used");
        return PLATO_USER_POPULATION;
    }

    public String getProfileDescription() {
        return profileDescription;
    }
}
