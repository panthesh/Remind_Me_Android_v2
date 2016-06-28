package codepath.shah.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pshah on 6/15/16.
 */
public class MyDB{

    private MyDatabaseHelper dbHelper;

    public final static String TASK_TABLE="MyTasks"; // name of table

    public final static String TASK_ID="_id"; // id value for task
    public final static String TASK_NAME="name";  // name of task
    /**
     *
     * @param context
     */
    public MyDB(Context context){
        dbHelper = new MyDatabaseHelper(context);
    }


    // Adding new task
    public void addTask(Task task) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(TASK_ID, task.getID()); // Task Name
        values.put(TASK_NAME, task.getName()); // Task Phone Number

        database.insert(TASK_TABLE, null, values);
        database.close(); // Closing database connection
    }

    //Get particular task based on Id
    public Task getTask(int id) {

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(TASK_TABLE, new String[] { TASK_ID,
                        TASK_NAME }, TASK_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Task task = new Task(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1));
        return task;
    }

    // Getting All Tasks
    public List<Task> getAllTasks() {

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        ArrayList<Task> taskList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TASK_TABLE;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setID(Integer.parseInt(cursor.getString(0)));
                task.setName(cursor.getString(1));
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        return taskList;
    }

    // Updating single task
    public int updateTask(Task task) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TASK_NAME, task.getName());

        String taskId = String.valueOf(task.getID());
        // updating row
        return database.update(TASK_TABLE, values, TASK_ID + " = ?",
                new String[] { taskId });
    }

    // Deleting single task
    public void deleteTask(String id) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        database.delete(TASK_TABLE, TASK_ID + " = ?",
                new String[] { id });
        database.close();
    }

}
