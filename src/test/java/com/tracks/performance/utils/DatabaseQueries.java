package com.tracks.performance.utils;

import com.tracks.conf.TestConfiguration;
import com.tracks.models.Context;
import org.aeonbits.owner.ConfigFactory;

public class DatabaseQueries {

    private static final TestConfiguration TEST_CONFIGURATION = ConfigFactory.create(TestConfiguration.class);
    private static final String QUERIES_FOLDER = TEST_CONFIGURATION.sqlQueriesFolder();

    public static void insertContextToDatabaseTable(Context context) {
        DatabaseUtils.insertValuesQuery(
                FileUtils.readFromFile(QUERIES_FOLDER + "Insert_Values_To_Context_Table.sql"),
                new Object[]{
                        context.getId(),
                        context.getCreateAt(),
                        context.getName(),
                        context.getPosition(),
                        context.getState(),
                        context.getUpdateAt(),
                        context.getUserId()
                }
        );
    }

    public static void deleteItemFromContextTableCreatedByUser(String user) {
        DatabaseUtils.executeSimpleQuery(
                String.format(FileUtils.readFromFile(QUERIES_FOLDER + "Delete_Values_From_Context_Table_Created_By_User.sql"), user)
        );
    }

    public static int getLastContextId() {
        return DatabaseUtils.getValueQuery(
                FileUtils.readFromFile(QUERIES_FOLDER + "Get_Last_Context_Id.sql")
        );
    }
}
