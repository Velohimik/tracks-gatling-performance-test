package com.tracks.performance;

import com.tracks.models.Context;
import com.tracks.performance.utils.DatabaseQueries;
import com.tracks.performance.utils.InputDataGenerator;

public class DatabaseGenerator {

    public void addUsers(int amount) {

    }

    public void addContexts(int amount) {
        Context context = new Context()
                .toBuilder()
                .id(DatabaseQueries.getLastContextId())
                .name(InputDataGenerator.generateRandomNameWithLength())
                .state("active")
                .build();
        DatabaseQueries.insertContextToDatabaseTable(context);
    }
}
