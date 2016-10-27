package com.example.lenovo.todo_list3;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
ListView list;
    ArrayList<String>tasks;
    ArrayAdapter<String>tasksAdapter;
    private TaskDbHelper db;
    CheckBox c1;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         db=new TaskDbHelper(this);

        SQLiteDatabase db3= db.getReadableDatabase();
        list=(ListView)findViewById(R.id.list);
        Cursor cursor = db3.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
        }
        cursor.close();
        db.close();
        updateUI();

        b1=(Button)findViewById(R.id.remove);






    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);//Menu Resource, Menu
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.item1)
        {

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            final View view = MainActivity.this.getLayoutInflater().inflate(R.layout.custom_dialog, null);

            dialog.setView(view);
                   dialog .setTitle("Add a  task")
                    .setMessage("What do you want to do next?")
                    .setView(view)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final EditText taskEditText = (EditText)view.findViewById(R.id.task_name);
                            String s1=String.valueOf(taskEditText.getText());
                            SQLiteDatabase db3 = db.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put(TaskContract.TaskEntry.COL_TASK_TITLE,s1 );
                            db3.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                    null,
                                    values,
                                    SQLiteDatabase.CONFLICT_REPLACE);
                            db3.close();
                            updateUI();







                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();

        }
        return true;
    }
    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db3 = db.getReadableDatabase();
        Cursor cursor = db3.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }

        if (tasksAdapter == null) {
            tasksAdapter = new ArrayAdapter<String>(this,
                    R.layout.list_item,
                    R.id.task_title,
                    taskList);
            list.setAdapter(tasksAdapter);
        } else {
            tasksAdapter.clear();
            tasksAdapter.addAll(taskList);
            tasksAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }
    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db3 = db.getWritableDatabase();
        db3.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db3.close();
        updateUI();
    }

}
