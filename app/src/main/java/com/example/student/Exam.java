package com.example.student;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Exam extends AppCompatActivity {


    ArrayList<ExamDataModel> examDataModels;
    private static ExamCustomAdapter adapter;
    private SQLiteDatabase db;
    private LinearLayout ExamLv;
    private ListView pastExamLv, currentExamLv;
    private EditText name, location, examDate, examTime;
    private Button showAddExam, redirectHome;
    private Spinner studentSpinner, displayStudentSpinner;
    private Calendar myCalendar;

    private TableLayout addExamTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);


        examDataModels = new ArrayList<>();

        name = (EditText) findViewById(R.id.name);
        location = (EditText) findViewById(R.id.location);
        examDate = (EditText) findViewById(R.id.examDate);
        examTime = (EditText) findViewById(R.id.examTime);

        studentSpinner= (Spinner) findViewById(R.id.studentSpinner);
        displayStudentSpinner = (Spinner) findViewById(R.id.spinner);

        showAddExam = (Button) findViewById(R.id.showAddExam);

        redirectHome = (Button)findViewById(R.id.redirectHome);


        ExamLv = (LinearLayout) findViewById(R.id.ExamLv);
        pastExamLv = (ListView) findViewById(R.id.pastExam);
        currentExamLv = (ListView) findViewById(R.id.currentExam);

        addExamTable = (TableLayout) findViewById(R.id.addExamTable);


        showAddExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayout("addExamTable");
            }
        });
        redirectHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectHome();
            }
        });


        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        examDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Exam.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        setStudentSpinner(displayStudentSpinner);

//        fillListView();
    }

    private void redirectHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        examDate.setText(sdf.format(myCalendar.getTime()));
    }

    public void displayExamList(View v) {


        ArrayList<ExamDataModel> currentExam, pastExam;
        currentExam = new ArrayList<>();
        pastExam = new ArrayList<>();

        db = new DatabaseSQLiteHelper(this).getReadableDatabase();
        String selection = displayStudentSpinner.getSelectedItem().toString();

        String rawQuery = "SELECT * FROM " + Database.Exam.TABLE_NAME + " ee INNER JOIN " + Database.Student.TABLE_NAME
                + " ss ON ee." + Database.Exam.COL_STUDENT_ID + " = ss." + Database.Student._ID
                + " WHERE ss." + Database.Student._ID + " = " + selection  ;
        Cursor c = db.rawQuery(
                rawQuery,
                null
        );

        Log.i("cursor count: ", Integer.toString(c.getCount()));
        try{

            if(c.moveToFirst()) {
                while(!c.isAfterLast()) {
                    String examDt = c.getString(c.getColumnIndex(Database.Exam.COL_DATE_TIME));

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Date d1 = sdf.parse(examDt);


                    Date date = new Date();

                    Date d2 = sdf.parse(sdf.format(date)); //sdf.parse(dateFormat.format(date));


                    int compareResult = d1.compareTo(d2);


                    int id = c.getInt(c.getColumnIndex(Database.Exam._ID));
                    String name = c.getString(c.getColumnIndex(Database.Exam.COL_NAME));
                    String location = c.getString(c.getColumnIndex(Database.Exam.COL_LOCATION));
                    String dateAndTime = c.getString(c.getColumnIndex(Database.Exam.COL_DATE_TIME));



                    if (compareResult > 0) {
                        Log.i("a", "d2 is younger than1 d1" );
                        // Current exam
                        currentExam.add(new ExamDataModel(id, name, location, dateAndTime));
                    } else if (compareResult < 0) {
                        // Past exam
                        Log.i("b", "d1 is younger than d2" + d2);
                        pastExam.add(new ExamDataModel(id, name, location, dateAndTime));
                    } else {
                        // Current Exam
                        Log.i("c", "d1 is equals d2 ");
                    }

                    c.moveToNext();
                }
            }


        } catch (ParseException e){
            e.printStackTrace();
        }


        fillListView(pastExamLv, pastExam);
        fillListView(currentExamLv, currentExam);

        showLayout("examListView");
    }


    private void setStudentSpinner(Spinner spinner) {
        db = new DatabaseSQLiteHelper(this).getReadableDatabase();

        ArrayList<String> studentIds = new ArrayList<String>();


        Cursor cursor = db.rawQuery("SELECT " + Database.Student._ID +", " + Database.Student.COL_FIRST_NAME + " FROM " + Database.Student.TABLE_NAME, null);

        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                Log.i(" The student Id is : : : ", cursor.getString(cursor.getColumnIndex(Database.Student._ID)) + " " + cursor.getString(cursor.getColumnIndex(Database.Student.COL_FIRST_NAME)));
                studentIds.add(cursor.getString(cursor.getColumnIndex(Database.Student._ID)));

                cursor.moveToNext();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_student_id_spinner_item, R.id.studentIdItem, studentIds);

        spinner.setAdapter(adapter);
    }

    private void showLayout(String layout) {
        ExamLv.setVisibility(View.GONE);
        addExamTable.setVisibility(View.GONE);
        switch(layout) {
            case "addExamTable":
                addExamTable.setVisibility(View.VISIBLE);
                setStudentSpinner(studentSpinner);
                break;
            case "examListView":
                ExamLv.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void clearForm() {
        name.setText("");
        location.setText("");
        examDate.setText("");
        examTime.setText("");
    }

    public void addExam(View v) {
        Log.i("asdf on clicked clicked: : ", " ");
        db = new DatabaseSQLiteHelper(this).getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(Database.Exam.COL_NAME, name.getText().toString());
        contentValues.put(Database.Exam.COL_LOCATION, location.getText().toString());


        String date = examDate.getText().toString();
        String time = examTime.getText().toString();
        String dt = date + " " + time;

        contentValues.put(Database.Exam.COL_DATE_TIME, dt);

        contentValues.put(Database.Exam.COL_STUDENT_ID, Integer.parseInt(studentSpinner.getSelectedItem().toString()));

        long rowId = db.insert(Database.Exam.TABLE_NAME, null, contentValues);

//        db.close();
        Toast.makeText(this, "Created rowId: " +rowId, Toast.LENGTH_SHORT).show();

        clearForm();
        showLayout("examListView");
//        Exam.this.recreate();
    }





    public void fillListView(ListView lv, ArrayList<ExamDataModel> dataModel) {
        db = new DatabaseSQLiteHelper(this).getReadableDatabase();

//        dataModel.clear();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + Database.Exam.TABLE_NAME, null);

//        if(cursor.moveToFirst()) {
//            while(!cursor.isAfterLast()) {
//                int id = cursor.getInt(cursor.getColumnIndex(Database.Exam._ID));
//                String name = cursor.getString(cursor.getColumnIndex(Database.Exam.COL_NAME));
//                String location = cursor.getString(cursor.getColumnIndex(Database.Exam.COL_LOCATION));
//                String dateAndTime = cursor.getString(cursor.getColumnIndex(Database.Exam.COL_DATE_TIME));
//

//            dataModel.add(new ExamDataModel(id, name, location, dateAndTime));
//                cursor.moveToNext();
//            }
//        }

        adapter = new ExamCustomAdapter(getApplicationContext(), dataModel);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("From on click item : : ", Integer.toString(position));
            }
        });

    }



}
