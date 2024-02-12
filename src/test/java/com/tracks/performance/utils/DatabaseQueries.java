package com.tracks.performance.utils;

import static com.tracks.performance.TestSimulation.TEST_CONFIGURATION;

import java.util.List;
import java.util.Random;

public class DatabaseQueries {

  private static final String QUERIES_FOLDER = TEST_CONFIGURATION.sqlQueriesFolder();

  public static int getRandomNotAdminUserId() {
    List<Integer> userIds =
        DatabaseUtils.getListOfValuesQuery(
            FileUtils.readFromFile(QUERIES_FOLDER + "Get_All_Not_Admin_User_Ids.sql"));

    return userIds.isEmpty() ? 0 : userIds.get(InputDataGenerator.returnRandomItemFromList(userIds));
  }

  public static void deleteAllNotAdminUsers() {
    DatabaseUtils.executeSimpleQuery(
        FileUtils.readFromFile(QUERIES_FOLDER + "Delete_All_Not_Admin_Users.sql"));
  }

  public static void deleteAllContexts() {
    DatabaseUtils.executeSimpleQuery(
        FileUtils.readFromFile(QUERIES_FOLDER + "Delete_All_Contexts.sql"));
  }

  public static int getUserIdByUserLogin(String login) {
    List<Integer> users = DatabaseUtils.getListOfValuesQuery(
            String.format(
                    FileUtils.readFromFile(QUERIES_FOLDER + "Get_User_Id_By_User_Login.sql"), login
            )
    );

    return users.isEmpty() ? 0 : users.get(0);
  }

  public static List<Integer> getContextIdsByUserId(int userId) {
    return DatabaseUtils.getListOfValuesQuery(
            String.format(
                    FileUtils.readFromFile(QUERIES_FOLDER + "Get_Context_Ids_By_User_Id.sql"), userId
            )
    );
  }
}
