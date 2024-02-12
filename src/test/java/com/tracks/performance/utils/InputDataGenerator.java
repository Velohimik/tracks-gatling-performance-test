package com.tracks.performance.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.Random;

public class InputDataGenerator {

    public static String generateRandomString() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static String generateRandomStatus() {
        return String.valueOf(new Random().nextInt(2));
    }

    /* Return random item from the List */
    public static int returnRandomItemFromList(List<?> objects) {
        return objects.isEmpty() ? 0 : new Random().nextInt(objects.size());
    }
}
