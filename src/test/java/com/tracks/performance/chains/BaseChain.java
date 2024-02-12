package com.tracks.performance.chains;

import com.tracks.performance.utils.DatabaseQueries;
import com.tracks.performance.utils.InputDataGenerator;

import static com.tracks.performance.TestSimulation.TEST_CONFIGURATION;
import static com.tracks.performance.utils.InputDataGenerator.generateRandomStatus;
import static com.tracks.performance.utils.InputDataGenerator.generateRandomString;

import java.util.*;
import java.util.stream.Stream;

public class BaseChain {

    public static final int SHORT_PAUSE_DURATION = 2;
    public static final int LONG_PAUSE_DURATION = 5;
    protected static final String AUTHENTICITY_TOKEN_VAR = "authToken";
    protected static final String USER_NAME = "userName";
    protected static final String USER_PASSWORD = "userPassword";
    protected final static String CONTEXT_NAME = "contextName";
    protected final static String CONTEXT_STATE = "contextState";
    protected static final String CSRF_TOKEN = "csrfToken";
    protected static final String CONTEXT_NUMBER = "contextNumbers";
    protected static final String DSL_CSRF_TOKEN = String.format("#{%s}", CSRF_TOKEN);
    protected static final String DSL_CONTEXT_NAME = String.format("#{%s}", CONTEXT_NAME);
    protected static final String DSL_CONTEXT_STATE = String.format("#{%s}", CONTEXT_STATE);
    protected static final String DSL_AUTHENTICITY_TOKEN =
            String.format("#{%s}", AUTHENTICITY_TOKEN_VAR);
    protected static final String DSL_USER_NAME = String.format("#{%s}", USER_NAME);
    protected static final String DSL_USER_PASSWORD = String.format("#{%s}", USER_PASSWORD);
    protected static final String VALUE_SELECTOR_ATTRIBUTE = "value";
    protected static final String CONTENT_SELECTOR_ATTRIBUTE = "content";
    private static final List<Map<String, Object>> USERS_CREDENTIALS_LIST = new ArrayList<>(); // List of users credentials that are generated in sign up feeder
    private static final String SLASH_SIGN = "/";
    private static final String JS_FILE = ".js";

    /* Feeder with context's name and status that is used during creation of new context */
    protected static Iterator<Map<String, Object>> contextFeeder =
            Stream.generate(
                    () ->
                            Map.<String, Object>of(
                                    CONTEXT_NAME, generateRandomString(),
                                    CONTEXT_STATE, generateRandomStatus()
                            )).iterator();

    /* Feeder with random generated user's name and password that are used for creation of new user */
    protected static Iterator<Map<String, Object>> signUpFeeder =
            Stream.generate(
                            () -> {
                                Map<String, Object> userMap =
                                        Map.of(
                                                USER_NAME, generateRandomString(),
                                                USER_PASSWORD, generateRandomString());
                                USERS_CREDENTIALS_LIST.add(userMap);
                                return userMap;
                            })
                    .iterator();

    /* Feeder with name and password of existing user from USER_CREDENTIALS_LIST that are used for log in to application */
    protected static Iterator<Map<String, Object>> loginFeeder =
            Stream.generate(
                            () -> {
                                int randomUsersListIndex = InputDataGenerator.returnRandomItemFromList(USERS_CREDENTIALS_LIST);
                                Map<String, Object> userMap = USERS_CREDENTIALS_LIST.get(randomUsersListIndex);
                                Map<String, Object> loginMap =
                                        Map.of(
                                                USER_NAME, userMap.get(USER_NAME),
                                                USER_PASSWORD, userMap.get(USER_PASSWORD));
                                USERS_CREDENTIALS_LIST.add(loginMap);
                                return loginMap;
                            })
                    .iterator();

    protected static String makeContextsEndpoint(String userName) {
        int userId = DatabaseQueries.getUserIdByUserLogin(userName);
        List<Integer> contextIds = DatabaseQueries.getContextIdsByUserId(userId);
        String contextId = String.valueOf(contextIds.get(InputDataGenerator.returnRandomItemFromList(contextIds)));

        return TEST_CONFIGURATION.contextEndpoint().concat(SLASH_SIGN).concat(contextId).concat(JS_FILE);
    }

    protected static String makeUsersEndpoint() {
        String userId = String.valueOf(DatabaseQueries.getRandomNotAdminUserId());

        return TEST_CONFIGURATION.usersEndpoint().concat(SLASH_SIGN).concat(userId);
    }}
