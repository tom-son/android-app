package com.example.student;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void examHandler(View v) {
        intent = new Intent(this, Exam.class);
        startActivity(intent);
    }


    public void studentHandler(View v) {
        intent = new Intent(this, Student.class);
        startActivity(intent);
    }

    public void todoTaskHandler(View v) {
        intent = new Intent(this, TodoTask.class);
        startActivity(intent);
    }

}
