package com.example.student;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Student extends AppCompatActivity {

    EditText fname, lname, course, age, address;
    Button studentToggle;
    RadioGroup gender;
    SQLiteDatabase database;
    TableLayout addTable, editTable;
    ListView studentList;

    EditText fnameEt, lnameEt, courseEt, ageEt, addressEt;
    RadioButton female, male;
    RadioGroup radio;

    TextView studentIdTv;
    ArrayAdapter adapter;

    int editPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);


        studentToggle = (Button) findViewById(R.id.studentToggle);

        addTable = (TableLayout) findViewById(R.id.addStudentTable);
        studentList = (ListView) findViewById(R.id.studentList);
        editTable = (TableLayout) findViewById(R.id.editStudentTable);

        fname = (EditText) findViewById(R.id.firstNameEt);
        lname = (EditText) findViewById(R.id.lastNameEt);
        gender = (RadioGroup) findViewById(R.id.genderGroup);
        course = (EditText) findViewById(R.id.courseStudyEt);
        age = (EditText) findViewById(R.id.ageEt);
        address = (EditText) findViewById(R.id.addressEt);


        studentIdTv = (TextView) findViewById(R.id.studentIdText);
        fnameEt = (EditText) findViewById(R.id.firstNameEdit);
        lnameEt = (EditText) findViewById(R.id.lastNameEdit);
        courseEt = (EditText) findViewById(R.id.courseStudyEdit);
        ageEt = (EditText) findViewById(R.id.ageEdit);
        addressEt = (EditText) findViewById(R.id.addressEdit);
        female = (RadioButton) findViewById(R.id.radioFemaleEdit);
        male = (RadioButton) findViewById(R.id.radioMaleEdit);
        radio = (RadioGroup) findViewById(R.id.genderGroupEdit);



//        database = new DatabaseSQLiteHelper(this).getWritableDatabase();
//
//
//        ContentValues values = new ContentValues();
//
//        values.put(Database.Exam.COL_NAME, "Mobile App Development" );
//        values.put(Database.Exam.COL_LOCATION, "Kingswood");
//        values.put(Database.Exam.COL_DATE_TIME, "02/02/2018 09:30:00" );
//        values.put(Database.Exam.COL_STUDENT_ID, 1 );
//
//        long newRowId = database.insert(Database.Exam.TABLE_NAME, null, values);
//
//        Toast.makeText(this, "Student added! Student ID is: " + newRowId, Toast.LENGTH_LONG).show();

//        database = new DatabaseSQLiteHelper(this).getWritableDatabase();
//
//        ContentValues value = new ContentValues();
//
//        value.put(Database.TodoTask.COL_NAME, "MAD Assignment 1");
//        value.put(Database.TodoTask.COL_LOCATION, "Kingswood");
//        value.put(Database.TodoTask.COL_STATUS, "INCOMPLETE");
//
//        long newRowId = database.insert(Database.TodoTask.TABLE_NAME, null, value);
//
//        Toast.makeText(this, "Student added! STudent ID is: " + newRowId, Toast.LENGTH_LONG).show();



        SQLiteDatabase database = new DatabaseSQLiteHelper(this).getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TodoTask.TABLE_NAME , null);

        cursor.moveToPosition(0);

        Log.i("The message is :  : : : ", cursor.getString(cursor.getColumnIndex("name")));

        setListView();

    }

    private void showLayout(String layout) {
        studentList.setVisibility(View.GONE);
        addTable.setVisibility(View.GONE);
        editTable.setVisibility(View.GONE);

        switch(layout) {
            case "studentList":
                startActivity(getIntent());
//                studentList.setVisibility(View.VISIBLE);
//                adapter.notifyDataSetChanged();

                break;
            case "addTable":
                addTable.setVisibility(View.VISIBLE);
                break;
            case "editTable":
                editTable.setVisibility(View.VISIBLE);
                break;
            default:
                Toast.makeText(this, "Warning: showLayout() layout do not match.", Toast.LENGTH_SHORT).show();
                break;
        }


    }


    public void studentToogle(View v) {
        studentList.setVisibility(View.GONE);
        addTable.setVisibility(View.VISIBLE);
    }


    public void updateStudent(View v) {

        database = new DatabaseSQLiteHelper(this).getWritableDatabase();

        int selectedId = gender.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton)findViewById(selectedId);
        ContentValues values = new ContentValues();

        values.put(Database.Student.COL_FIRST_NAME, fnameEt.getText().toString());
        values.put(Database.Student.COL_LAST_NAME, lnameEt.getText().toString());

        values.put(Database.Student.COL_GENDER, selectedRadioButton.getText().toString());
        values.put(Database.Student.COL_COURSE, courseEt.getText().toString());
        values.put(Database.Student.COL_AGE, Integer.parseInt(ageEt.getText().toString()));
        values.put(Database.Student.COL_ADDRESS, addressEt.getText().toString());
        String id = studentIdTv.getText().toString();


        database.update(Database.Student.TABLE_NAME, values, Database.Student._ID +"="+ id, null);
        Toast.makeText(this, "Student with id: " + id +" updated!", Toast.LENGTH_SHORT).show();

        showLayout("studentList");

    }


    private void fillEdit(int rowSelected) {

        SQLiteDatabase database = new DatabaseSQLiteHelper(this).getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.Student.TABLE_NAME , null);


        cursor.moveToPosition(rowSelected);
        int studentId = cursor.getInt(cursor.getColumnIndex(Database.Student._ID));
        String fname = cursor.getString(cursor.getColumnIndex(Database.Student.COL_FIRST_NAME));
        String lname = cursor.getString(cursor.getColumnIndex(Database.Student.COL_LAST_NAME));
        String gender = cursor.getString(cursor.getColumnIndex(Database.Student.COL_GENDER));
        String course = cursor.getString(cursor.getColumnIndex(Database.Student.COL_COURSE));
        int age = cursor.getInt(cursor.getColumnIndex(Database.Student.COL_AGE));
        String address = cursor.getString(cursor.getColumnIndex(Database.Student.COL_ADDRESS));


//        String name = cursor.getString(cursor.getColumnIndex(Database.Student.COL_FIRST_NAME));

        studentIdTv.setText(Integer.toString(studentId));
        fnameEt.setText(fname.toString());
        lnameEt.setText(lname);
        courseEt.setText(course);
        if(gender.toString().equals("Male")) {
            radio.check(R.id.radioMaleEdit);
        } else {
            radio.check(R.id.radioFemaleEdit);
        }
        ageEt.setText(Integer.toString(age));
        addressEt.setText(address);

    }


    private void setListView() {

        ArrayList students = new <String>ArrayList();

        SQLiteDatabase database = new DatabaseSQLiteHelper(this).getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.Student.TABLE_NAME,null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(Database.Student._ID));
                String fname = cursor.getString(cursor.getColumnIndex(Database.Student.COL_FIRST_NAME));
                String lname = cursor.getString((cursor.getColumnIndex(Database.Student.COL_LAST_NAME)));

                String student = Integer.toString(id) +"        "+fname+"       "+lname;

                Log.i("first name is:  ", student);
                students.add(student);
                cursor.moveToNext();
            }
        }

        studentList = (ListView) findViewById(R.id.studentList);
        adapter = new ArrayAdapter<String>(this, R.layout.activity_student_list_view, R.id.textView, students);

        studentList.setAdapter(adapter);

        studentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                studentList.setVisibility(View.GONE);
                addTable.setVisibility(View.GONE);
                editTable.setVisibility(View.VISIBLE);


                fillEdit(position);
            }
        });

    }



    public void addStudentHandler(View v) {

        database = new DatabaseSQLiteHelper(this).getWritableDatabase();

        int selectedId = gender.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton)findViewById(selectedId);


        ContentValues values = new ContentValues();

        values.put(Database.Student.COL_FIRST_NAME, fname.getText().toString());
        values.put(Database.Student.COL_LAST_NAME, lname.getText().toString());
        values.put(Database.Student.COL_GENDER, selectedRadioButton.getText().toString());
        values.put(Database.Student.COL_COURSE, course.getText().toString());
        values.put(Database.Student.COL_AGE, Integer.parseInt(age.getText().toString()));
        values.put(Database.Student.COL_ADDRESS, address.getText().toString());


        long newRowId = database.insert(Database.Student.TABLE_NAME, null, values);
        Toast.makeText(this, "Student added! Student ID is: " + newRowId, Toast.LENGTH_LONG).show();

        setListView();
        addTable.setVisibility(View.GONE);
        studentList.setVisibility(View.VISIBLE);

    }
}
