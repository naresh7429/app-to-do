
package com.example.taskmanager.db;

import android.provider.BaseColumns;

public final class TaskDefinition {
    private TaskDefinition() {}

    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_TASK_NAME = "task_name";
    }
}
