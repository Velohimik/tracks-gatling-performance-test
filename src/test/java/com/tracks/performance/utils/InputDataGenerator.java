package com.tracks.performance.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class InputDataGenerator {

    public static String generateRandomNameWithLength() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static String generateRandomStatus() {
        return String.valueOf(new Random().nextInt(2));
    }
}
