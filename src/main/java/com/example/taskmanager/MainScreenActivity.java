
package com.example.taskmanager;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanager.db.DatabaseOperations;
import com.example.taskmanager.db.TaskDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainScreenActivity extends AppCompatActivity {

    private DatabaseOperations dbOperations;
    private ListView taskListView;
    private List<HashMap<String, String>> taskList;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        dbOperations = new DatabaseOperations(this);
        taskListView = findViewById(R.id.task_list_view);
        taskList = new ArrayList<>();

        adapter = new SimpleAdapter(
                this,
                taskList,
                R.layout.task_list_item,
                new String[]{"task_name"},
                new int[]{R.id.task_name_text}
        );

        taskListView.setAdapter(adapter);
        loadTasks();
    }

    private void loadTasks() {
        taskList.clear();
        SQLiteDatabase db = dbOperations.getReadableDatabase();
        Cursor cursor = db.query(TaskDefinition.TaskEntry.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String taskName = cursor.getString(cursor.getColumnIndex(TaskDefinition.TaskEntry.COLUMN_TASK_NAME));
            HashMap<String, String> map = new HashMap<>();
            map.put("task_name", taskName);
            taskList.add(map);
        }

        adapter.notifyDataSetChanged();
        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_task_option) {
            showAddTaskDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddTaskDialog() {
        final EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("New Task")
                .setMessage("Enter the task:")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String taskName = input.getText().toString();
                    if (!taskName.isEmpty()) {
                        addTaskToDatabase(taskName);
                        loadTasks();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addTaskToDatabase(String taskName) {
        SQLiteDatabase db = dbOperations.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskDefinition.TaskEntry.COLUMN_TASK_NAME, taskName);
        db.insert(TaskDefinition.TaskEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        String taskName = (String) parent.getTag();
        SQLiteDatabase db = dbOperations.getWritableDatabase();
        db.delete(TaskDefinition.TaskEntry.TABLE_NAME, TaskDefinition.TaskEntry.COLUMN_TASK_NAME + "=?", new String[]{taskName});
        db.close();
        loadTasks();
    }
}
