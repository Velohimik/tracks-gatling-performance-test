package com.tracks.performance.chains;

import com.tracks.performance.utils.InputDataGenerator;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;
import java.util.stream.Stream;

public class BaseChain {

  protected static final String AUTHENTICITY_TOKEN_VAR = "authToken";
  protected static final String USER_NAME = "userName";
  protected static final String USER_PASSWORD = "userPassword";
  protected static final String CONTEXT_NAME = "contextName";
  protected static final String CONTEXT_STATE = "contextState";
  protected static final String CSRF_TOKEN = "csrfToken";
  protected static final String CONTEXT_ENDPOINT = "contextEndpoint";
  protected static final String CONTEXT_NUMBER = "contextNumbers";
  protected static final String DSL_CSRF_TOKEN = String.format("#{%s}", CSRF_TOKEN);
  protected static final String DSL_CONTEXT_ENDPOINT = String.format("#{%s}", CONTEXT_ENDPOINT);
  protected static final String DSL_CONTEXT_NAME = String.format("#{%s}", CONTEXT_NAME);
  protected static final String DSL_CONTEXT_STATE = String.format("#{%s}", CONTEXT_STATE);
  protected static final String DSL_AUTHENTICITY_TOKEN =
      String.format("#{%s}", AUTHENTICITY_TOKEN_VAR);
  protected static final String DSL_USER_NAME = String.format("#{%s}", USER_NAME);
  protected static final String DSL_USER_PASSWORD = String.format("#{%s}", USER_PASSWORD);
  protected static final int SHORT_PAUSE_DURATION = 2;
  protected static final int LONG_PAUSE_DURATION = 5;
  private static final List<Map<String, Object>> USERS_CREDENTIALS_LIST = new ArrayList<>();

  // FEEDER
  protected static Iterator<Map<String, Object>> contextFeeder =
      Stream.generate(
              () ->
                  Map.<String, Object>of(
                      CONTEXT_NAME, RandomStringUtils.randomAlphanumeric(10),
                      CONTEXT_STATE, InputDataGenerator.generateRandomStatus()))
          .iterator();

  protected static Iterator<Map<String, Object>> authFeeder =
      Stream.generate(
              () -> {
                Map<String, Object> userMap =
                    Map.of(
                        USER_NAME, RandomStringUtils.randomAlphanumeric(10),
                        USER_PASSWORD, RandomStringUtils.randomAlphanumeric(10));
                USERS_CREDENTIALS_LIST.add(userMap);
                return userMap;
              })
          .iterator();

  protected static Iterator<Map<String, Object>> loginFeeder =
      Stream.generate(
              () -> {
                int randomUsersListIndex = new Random().nextInt(USERS_CREDENTIALS_LIST.size());
                Map<String, Object> userMap = USERS_CREDENTIALS_LIST.get(randomUsersListIndex);
                Map<String, Object> loginMap =
                    Map.of(
                        USER_NAME, userMap.get(USER_NAME),
                        USER_PASSWORD, userMap.get(USER_PASSWORD));
                USERS_CREDENTIALS_LIST.add(loginMap);
                return loginMap;
              })
          .iterator();
}
