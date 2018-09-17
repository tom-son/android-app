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

    int editPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);


        studentToggle = (Button) findViewById(R.id.studentToggle);

        addTable = (TableLayout) findViewById(R.id.addStudentTable);
        studentList = (ListView) findViewById(R.id.studentList);
        editTable = (TableLayout) findViewById(R.id.editStudentTable);

        setStudentList();
    }



    public void studentToogle(View v) {
        studentList.setVisibility(View.GONE);
        addTable.setVisibility(View.VISIBLE);
    }



    private void fillEdit(int rowSelected) {

        SQLiteDatabase database = new DatabaseSQLiteHelper(this).getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.Student.TABLE_NAME , null);


        cursor.moveToPosition(rowSelected);
        String fname = cursor.getString(cursor.getColumnIndex(Database.Student.COL_FIRST_NAME));
        String lname = cursor.getString(cursor.getColumnIndex(Database.Student.COL_LAST_NAME));
        String gender = cursor.getString(cursor.getColumnIndex(Database.Student.COL_GENDER));
        String course = cursor.getString(cursor.getColumnIndex(Database.Student.COL_COURSE));
        int age = cursor.getInt(cursor.getColumnIndex(Database.Student.COL_AGE));
        String address = cursor.getString(cursor.getColumnIndex(Database.Student.COL_ADDRESS));




//        String name = cursor.getString(cursor.getColumnIndex(Database.Student.COL_FIRST_NAME));

        EditText fnameEt, lnameEt, courseEt, ageEt, addressEt;
        RadioButton female, male;
        RadioGroup radio;

        fnameEt = (EditText) findViewById(R.id.firstNameEdit);
        lnameEt = (EditText) findViewById(R.id.lastNameEdit);
        courseEt = (EditText) findViewById(R.id.courseStudyEdit);
        ageEt = (EditText) findViewById(R.id.ageEdit);
        addressEt = (EditText) findViewById(R.id.addressEdit);
        female = (RadioButton) findViewById(R.id.radioFemaleEdit);
        male = (RadioButton) findViewById(R.id.radioMaleEdit);
        radio = (RadioGroup) findViewById(R.id.genderGroupEdit);

        fnameEt.setText(gender.toString());
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


    private void setStudentList() {

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
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_student_list_view, R.id.textView, students);

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

        fname = (EditText) findViewById(R.id.firstNameEt);
        lname = (EditText) findViewById(R.id.lastNameEt);
        gender = (RadioGroup) findViewById(R.id.genderGroup);
        course = (EditText) findViewById(R.id.courseStudyEt);
        age = (EditText) findViewById(R.id.ageEt);
        address = (EditText) findViewById(R.id.addressEt);

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

        setStudentList();
        addTable.setVisibility(View.GONE);
        studentList.setVisibility(View.VISIBLE);

    }
}
