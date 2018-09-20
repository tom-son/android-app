package com.example.student;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "assignment1Database";

    public DatabaseSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Database.Student.CREATE_TABLE);
        sqLiteDatabase.execSQL(Database.TodoTask.CREATE_TABLE);
        sqLiteDatabase.execSQL((Database.Exam.CREATE_TABLE));

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Database.Student.TABLE_NAME);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Database.TodoTask.TABLE_NAME);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Database.Exam.TABLE_NAME);

        if(i == 0 && i1 == 4) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Database.TodoTask.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Database.Exam.TABLE_NAME);
        }
        else {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Database.Student.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Database.TodoTask.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Database.Exam.TABLE_NAME);
        }
        onCreate(sqLiteDatabase);
    }


}
