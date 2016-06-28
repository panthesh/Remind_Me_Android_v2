package codepath.shah.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import codepath.shah.db.MyDB;
import codepath.shah.db.Task;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    List<Task> tasks;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    private final int REQUEST_CODE = 20;
    MyDB myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB = new MyDB(this);
        //readItemsFromTextFile();
        readItemsFromDB();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lvItems = (ListView) findViewById(R.id.lvItems);
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                items);
        lvItems.setAdapter(itemsAdapter);
        setUpListViewListener();
    }

    private void setUpListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int taskId=0;
                for (Task task: tasks) {
                    if (task.getName().equals(items.get(position))) {
                        taskId = task.getID();
                    }
                }
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                removeTaskFromDB(String.valueOf(taskId));
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("itemText", items.get(position));
                i.putExtra("position", position);
                i.putExtra("code", 400);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    public void onAddItem(View v) {
        EditText edNewItem = (EditText) findViewById(R.id.etNewItem);
        String newItem = edNewItem.getText().toString();
        itemsAdapter.add(newItem);
        edNewItem.setText("");
        addTaskToDB(newItem);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("CALLED", "OnActivity Result");

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String newName = data.getExtras().getString("editedText");
            String oldName = data.getExtras().getString("itemText");
            int position = data.getExtras().getInt("position", 0);
            items.set(position, newName);
            itemsAdapter.notifyDataSetChanged();
            //writeItemsToFile();
            updateTaskToDB(oldName, newName);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readItemsFromDB() {
        try {
            items = new ArrayList<String>();
            tasks = myDB.getAllTasks();
            for (Task task : tasks) {
                items.add(task.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeTaskFromDB(String id) {
        try {
            myDB.deleteTask(id);
            String logMessage =  "Id" + id;
            Log.d("Message:", logMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTaskToDB(String name) {
        try {
            Task task = new Task(name);
            myDB.addTask(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTaskToDB(String oldName, String newName) {
        try {
            for (Task task: tasks) {
                if (task.getName().equals(oldName)) {
                    task.setName(newName);
                   myDB.updateTask(task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readItemsFromTextFile() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeItemsToFile() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

