package com.example.student;

import android.content.ContentValues;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TodoTask extends AppCompatActivity {

    SQLiteDatabase db;
    EditText name, location;
    EditText nameEt, locationEt;
    RadioGroup statusGroup;
    ListView todoTaskLv;
    TableLayout editTable;
    TextView idTv;

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
        todoTaskLv = (ListView) findViewById(R.id.todoTaskLv);


        editTable = (TableLayout) findViewById(R.id.todoEditTable);


        setListView();

    }

    private void setListView() {
        db = new DatabaseSQLiteHelper(this).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Database.TodoTask.TABLE_NAME, null);

        ArrayList taskData = new ArrayList();

        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(Database.TodoTask._ID));
                String name = cursor.getString(cursor.getColumnIndex(Database.TodoTask.COL_NAME));
                String location = cursor.getString(cursor.getColumnIndex(Database.TodoTask.COL_LOCATION));
                String status = cursor.getString(cursor.getColumnIndex(Database.TodoTask.COL_STATUS));
                Log.i("theafndas 324 as @#$@ : : :", id + name + location);


                String todoTaskRow = Integer.toString(id) + " " + name + " " + location + " " + status;
                taskData.add(todoTaskRow);
//                taskData.add(new TodoTaskDataModel(id, name, location, status));
                cursor.moveToNext();
            }
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_todo_task_list_view, R.id.textView,taskData);
        todoTaskLv.setAdapter(adapter);

        todoTaskLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("asdfsadf asdf asf asdf ", Integer.toString(position));
                fillTodoTask(position);
            }
        });
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
        Log.i("Asdfasdfa s234849831749123 : : ", status);
        if (status.equals("INCOMPLETE")) {
            statusGroup.check(R.id.radioIncomplete);
        } else {
            statusGroup.check(R.id.radioComplete);
        }

        editTable.setVisibility(View.VISIBLE);
        todoTaskLv.setVisibility(View.GONE);



    }


    public void updateTodoTask (View v) {
        db = new DatabaseSQLiteHelper(this).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Database.TodoTask.COL_NAME, nameEt.getText().toString());
        values.put(Database.TodoTask.COL_LOCATION, locationEt.getText().toString());

        int selectedId = statusGroup.getCheckedRadioButtonId();

        RadioButton status = (RadioButton) findViewById(selectedId);

        values.put(Database.TodoTask.COL_STATUS, status.getText().toString());

        Log.i("The updated strings are!fsd %##@ : : ", values.toString() + status.getText().toString() + status.getText().toString() );

        String id = idTv.getText().toString();


        db.update(Database.TodoTask.TABLE_NAME, values, Database.TodoTask._ID + "=" + id, null);

        Toast.makeText(this, "Student with id: " + id +" updated!", Toast.LENGTH_SHORT).show();


    }

    public void addTodoTask(View v) {

        db = new DatabaseSQLiteHelper(this).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Database.TodoTask.COL_NAME, name.getText().toString());
        values.put(Database.TodoTask.COL_LOCATION, location.getText().toString());
        values.put(Database.TodoTask.COL_STATUS, "INCOMPLETE");

        Long newRowId = db.insert(Database.TodoTask.TABLE_NAME,null, values);
        Toast.makeText(this, "Student added! Student ID is: " + newRowId, Toast.LENGTH_LONG).show();


    }

}
