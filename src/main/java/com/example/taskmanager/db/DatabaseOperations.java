
package com.example.taskmanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOperations extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "taskmanager.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOperations(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TaskDefinition.TaskEntry.TABLE_NAME + " (" +
                TaskDefinition.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskDefinition.TaskEntry.COLUMN_TASK_NAME + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskDefinition.TaskEntry.TABLE_NAME);
        onCreate(db);
    }
}
