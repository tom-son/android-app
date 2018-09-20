package com.example.student;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class Database {

    private Database() {
    }

    public static class Student implements BaseColumns{

        public static final String TABLE_NAME = "student";
        public static final String COL_FIRST_NAME= "firstName";
        public static final String COL_LAST_NAME = "lastName";
        public static final String COL_GENDER = "gender";
        public static final String COL_COURSE= "course";
        public static final String COL_AGE = "age";
        public static final String COL_ADDRESS = "address";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FIRST_NAME + " TEXT, " +
                COL_LAST_NAME + " TEXT, " +
                COL_GENDER + " TEXT, " +
                COL_COURSE + " TEXT, " +
                COL_AGE + " INTEGER, " +
                COL_ADDRESS + " TEXT)";
    }

    public static class TodoTask implements BaseColumns {
        public static final String TABLE_NAME = "todoTask";
        public static final String COL_NAME = "name";
        public static final String COL_LOCATION = "location";
        public static final String COL_STATUS = "status";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NAME + " TEXT, " +
            COL_LOCATION + " TEXT, " +
            COL_STATUS + " TEXT)";
    }

    public static class Exam implements BaseColumns {
        public static final String TABLE_NAME = "exam";
        public static final String COL_NAME = "name";
        public static final String COL_DATE_TIME = "dateAndTime";
        public static final String COL_LOCATION = "location";
        public static final String COL_STUDENT_ID = "student_id";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_STUDENT_ID + " INTEGER, " +
                COL_NAME + " TEXT, " +
                COL_DATE_TIME + " TEXT, " +
                COL_LOCATION + " TEXT, " +
                 "FOREIGN KEY(" + COL_STUDENT_ID + ") REFERENCES " +
                Student.TABLE_NAME + "(" + Student._ID + ")" + " )";
    }


}
