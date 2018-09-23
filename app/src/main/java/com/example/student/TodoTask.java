package com.example.student;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TodoTask extends AppCompatActivity {

    SQLiteDatabase db;
    EditText name, location;
    EditText nameEt, locationEt;
    RadioGroup statusGroup;

    LinearLayout todoTaskLv;
    ListView incompleteLv, completeLv;
    TableLayout addTable, editTable;
    TextView idTv;

    Button showAddTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_task);

        name = (EditText) findViewById(R.id.name);
        location = (EditText) findViewById(R.id.location);

        idTv = (TextView) findViewById(R.id.idTv);
        nameEt = (EditText) findViewById(R.id.nameEt);
        locationEt = (EditText) findViewById(R.id.locationEt);
        statusGroup = (RadioGroup) findViewById(R.id.statusRadio);


        todoTaskLv = (LinearLayout) findViewById(R.id.todoTaskLv);
        incompleteLv = (ListView) findViewById(R.id.incompleteLv);
        completeLv = (ListView) findViewById(R.id.completeLv);

        addTable = (TableLayout) findViewById(R.id.addTable);
        editTable = (TableLayout) findViewById(R.id.todoEditTable);

        showAddTask = (Button) findViewById(R.id.showAddTask);

        showAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayout("addTable");
            }
        });

        displayLv();

    }

    public void cancel(View v) {
        showLayout("todoListView");
    }

    public void redirectHome(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void displayLv() {
        db = new DatabaseSQLiteHelper(this).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Database.TodoTask.TABLE_NAME + " WHERE " + Database.TodoTask.COL_STATUS + "= 'Incomplete'", null);

        setListView(incompleteLv, cursor);
        Log.i("cursor count for incomplete : : : ", Integer.toString(cursor.getCount() ));

        cursor = db.rawQuery("SELECT * FROM " + Database.TodoTask.TABLE_NAME + " WHERE " + Database.TodoTask.COL_STATUS + "= 'Complete'", null);
        Log.i("cursor count for complete : : : ", Integer.toString(cursor.getCount() ));
        setListView(completeLv, cursor);
    }

    private void showLayout(String layout) {
        addTable.setVisibility(View.GONE);
        editTable.setVisibility(View.GONE);
        todoTaskLv.setVisibility(View.GONE);
        switch(layout) {
            case "addTable":
                addTable.setVisibility(View.VISIBLE);
                break;
            case "editTable":
                editTable.setVisibility(View.VISIBLE);
                break;
            case "todoListView":
                todoTaskLv.setVisibility(View.VISIBLE);
                break;
            default:
                Log.i("     showlayout(string): string not found ", "");

        }
    }


    private void setListView(ListView lv, Cursor cursor) {
//        db = new DatabaseSQLiteHelper(this).getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + Database.TodoTask.TABLE_NAME, null);

        ArrayList taskData = new ArrayList();

        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(Database.TodoTask._ID));
                String name = cursor.getString(cursor.getColumnIndex(Database.TodoTask.COL_NAME));
                String location = cursor.getString(cursor.getColumnIndex(Database.TodoTask.COL_LOCATION));
                String status = cursor.getString(cursor.getColumnIndex(Database.TodoTask.COL_STATUS));


                String todoTaskRow = Integer.toString(id) + " " + name + " " + location + " " + status;
                taskData.add(todoTaskRow);
//                taskData.add(new TodoTaskDataModel(id, name, location, status));
                cursor.moveToNext();
            }
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_todo_task_list_view, R.id.textView,taskData);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tv = view.findViewById(R.id.textView);

                String value = tv.getText().toString();

                value = value.substring(0, value.indexOf(' '));

                Log.i("value id is:::"+value,": <--" );



                fillTodoTask(Integer.parseInt(value)-1);
                showLayout("editTable");
            }
        });



//        todoTaskLv.setAdapter(adapter);
//
//        todoTaskLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.i("asdfsadf asdf asf asdf ", Integer.toString(position));
//                fillTodoTask(position);
//                showLayout("editTable");
//            }
//        });
    }

    public void fillTodoTask(int row) {
        db = new DatabaseSQLiteHelper(this).getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Database.TodoTask.TABLE_NAME, null);

        cursor.moveToPosition(row);
        int id = cursor.getInt(cursor.getColumnIndex(Database.TodoTask._ID));
        String name = cursor.getString(cursor.getColumnIndex(Database.TodoTask.COL_NAME));
        String location = cursor.getString(cursor.getColumnIndex(Database.TodoTask.COL_LOCATION));
        String status = cursor.getString(cursor.getColumnIndex(Database.TodoTask.COL_STATUS));

        this.idTv.setText(Integer.toString(id));
        this.nameEt.setText(name);
        this.locationEt.setText(location);
        if (status.equals("Incomplete")) {
            statusGroup.check(R.id.radioIncomplete);
        } else {
            statusGroup.check(R.id.radioComplete);
        }

        showLayout("todoListView");
    }


    public void updateTodoTask (View v) {
        db = new DatabaseSQLiteHelper(this).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Database.TodoTask.COL_NAME, nameEt.getText().toString());
        values.put(Database.TodoTask.COL_LOCATION, locationEt.getText().toString());

        int selectedId = statusGroup.getCheckedRadioButtonId();

        RadioButton status = (RadioButton) findViewById(selectedId);

        values.put(Database.TodoTask.COL_STATUS, status.getText().toString());

        String id = idTv.getText().toString();


        db.update(Database.TodoTask.TABLE_NAME, values, Database.TodoTask._ID + "=" + id, null);

        Toast.makeText(this, "Student with id: " + id +" updated!", Toast.LENGTH_SHORT).show();


        TodoTask.this.recreate();

//        showLayout("todoListView");




    }

    public void addTodoTask(View v) {

        db = new DatabaseSQLiteHelper(this).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Database.TodoTask.COL_NAME, name.getText().toString());
        values.put(Database.TodoTask.COL_LOCATION, location.getText().toString());
        values.put(Database.TodoTask.COL_STATUS, "Incomplete");

        Long newRowId = db.insert(Database.TodoTask.TABLE_NAME,null, values);
        Toast.makeText(this, "Student added! Student ID is: " + newRowId, Toast.LENGTH_LONG).show();

        TodoTask.this.recreate();
//        showLayout("todoListView");
    }

}
