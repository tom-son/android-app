package com.example.student;

import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.ImageView;
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
    Button studentToggle, showLv;
    RadioGroup gender;
    SQLiteDatabase database;
    TableLayout addTable, editTable;
    ListView studentList;

    EditText fnameEt, lnameEt, courseEt, ageEt, addressEt;
    RadioButton female, male;
    RadioGroup radio;

    TextView studentIdTv;
    ArrayAdapter adapter;

    ImageView dp;
    int editPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);


        studentToggle = (Button) findViewById(R.id.studentToggle);
        showLv = (Button) findViewById(R.id.showLv);

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

        dp = (ImageView) findViewById(R.id.dp);

        studentToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayout("addTable");
            }
        });
        showLv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayout("studentList");
            }
        });


        setListView();

    }

    // go to home activity
    public void redirectHome(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // get student address and send user to map activity to show student address location
    public void mapStudent(View v) {
        database = new DatabaseSQLiteHelper(this).getReadableDatabase();

        Log.i("studentId: ", studentIdTv.getText().toString()+":");
        Cursor cursor = database.rawQuery("SELECT "+ Database.Student.COL_ADDRESS +" FROM " + Database.Student.TABLE_NAME + " WHERE " +
                Database.Student._ID + "="+studentIdTv.getText().toString(), null);

        cursor.moveToFirst();
        String address = cursor.getString(cursor.getColumnIndex(Database.Student.COL_ADDRESS));
        Log.i("Map location is:", cursor.getString(cursor.getColumnIndex(Database.Student.COL_ADDRESS)));
        Intent intent = new Intent(this, MapsActivity.class);

        intent.putExtra("location", address);

        startActivity(intent);

    }

    // Delete student from student table
    // Gets _ID from the TextView which was inserted by fillEdit()
    public void deleteStudentHandler(View v) {
        database = new DatabaseSQLiteHelper(this).getWritableDatabase();

        String value = studentIdTv.getText().toString();

        int numberDeleted = database.delete(Database.Student.TABLE_NAME, Database.Student._ID +"=?" ,new String[]{value});
        Toast.makeText(this, Integer.toString(numberDeleted) + " Deleted", Toast.LENGTH_SHORT).show();

        Student.this.recreate();
    }


    // display appropriate layout. e.g. edit student -> editTable
    private void showLayout(String layout) {
        studentList.setVisibility(View.GONE);
        addTable.setVisibility(View.GONE);
        editTable.setVisibility(View.GONE);

        switch(layout) {
            case "studentList":
                Student.this.recreate();
//                startActivity(getIntent());
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


    // update student from student table gets the data from editTable layout
    public void updateStudent(View v) {

        database = new DatabaseSQLiteHelper(this).getWritableDatabase();

        int selectedId = radio.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton)findViewById(selectedId);

        ContentValues values = new ContentValues();

        Log.i("The gender is :"+selectedRadioButton.getText().toString(), ":");

        values.put(Database.Student.COL_FIRST_NAME, fnameEt.getText().toString());
        values.put(Database.Student.COL_LAST_NAME, lnameEt.getText().toString());

        values.put(Database.Student.COL_GENDER, selectedRadioButton.getText().toString());
        values.put(Database.Student.COL_COURSE, courseEt.getText().toString());
        values.put(Database.Student.COL_AGE, Integer.parseInt(ageEt.getText().toString()));
        values.put(Database.Student.COL_ADDRESS, addressEt.getText().toString());
        String id = studentIdTv.getText().toString();


        database.update(Database.Student.TABLE_NAME, values, Database.Student._ID +"="+ id, null);
        Toast.makeText(this, "Student with id: " + id +" updated!", Toast.LENGTH_SHORT).show();

        Student.this.recreate();
//        showLayout("studentList");
    }

    // Fill the editTable layout with student info from database.
    // e.g. name EditText field will have student name to be updated
    // NOTE: updateStudent() will getText() from these EditText
    private void fillEdit(int rowSelected) {

        SQLiteDatabase database = new DatabaseSQLiteHelper(this).getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.Student.TABLE_NAME , null);


        cursor.moveToPosition(rowSelected);
        String dpSrc = cursor.getString(cursor.getColumnIndex(Database.Student.COL_DP));
        int studentId = cursor.getInt(cursor.getColumnIndex(Database.Student._ID));
        String fname = cursor.getString(cursor.getColumnIndex(Database.Student.COL_FIRST_NAME));
        String lname = cursor.getString(cursor.getColumnIndex(Database.Student.COL_LAST_NAME));
        String gender = cursor.getString(cursor.getColumnIndex(Database.Student.COL_GENDER));
        String course = cursor.getString(cursor.getColumnIndex(Database.Student.COL_COURSE));
        int age = cursor.getInt(cursor.getColumnIndex(Database.Student.COL_AGE));
        String address = cursor.getString(cursor.getColumnIndex(Database.Student.COL_ADDRESS));


//        String name = cursor.getString(cursor.getColumnIndex(Database.Student.COL_FIRST_NAME));

        int id = this.getResources().getIdentifier("drawable/"+ dpSrc, null, this.getPackageName());

        dp.setImageResource(id);
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


    // Gets all students from Student table and display it in a listview
    // and sets onItemClick handler for each item listview
    private void setListView() {

        ArrayList students = new <String>ArrayList();

        SQLiteDatabase database = new DatabaseSQLiteHelper(this).getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.Student.TABLE_NAME,null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String dp = cursor.getString(cursor.getColumnIndex(Database.Student.COL_DP));
                int id = cursor.getInt(cursor.getColumnIndex(Database.Student._ID));
                String fname = cursor.getString(cursor.getColumnIndex(Database.Student.COL_FIRST_NAME));
                String lname = cursor.getString((cursor.getColumnIndex(Database.Student.COL_LAST_NAME)));

                String student = Integer.toString(id) +"        "+fname+"       "+lname ;

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

        values.put(Database.Student.COL_DP, "orange");
        values.put(Database.Student.COL_FIRST_NAME, fname.getText().toString());
        values.put(Database.Student.COL_LAST_NAME, lname.getText().toString());
        values.put(Database.Student.COL_GENDER, selectedRadioButton.getText().toString());
        values.put(Database.Student.COL_COURSE, course.getText().toString());
        values.put(Database.Student.COL_AGE, Integer.parseInt(age.getText().toString()));
        values.put(Database.Student.COL_ADDRESS, address.getText().toString());


        long newRowId = database.insert(Database.Student.TABLE_NAME, null, values);
        Toast.makeText(this, "Student added! Student ID is: " + newRowId, Toast.LENGTH_LONG).show();

        setListView();

        Student.this.recreate();
    }
}
