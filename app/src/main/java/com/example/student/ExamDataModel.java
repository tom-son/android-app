package com.example.student;

public class ExamDataModel {

    private int id;
    private String name;
    private String location;
    private String dateAndTime;
    private boolean checked;


    // Data model to hold exam data for exam list view
    // as well as keeping keep track of checkbox value
    // to help with deleting if user selects and delete
    ExamDataModel(int id, String name, String location, String dateAndTime) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.dateAndTime = dateAndTime;
        this.checked = false;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getLocation() {
        return this.location;
    }

    public String getDateAndTime() {
        return this.dateAndTime;
    }

    public boolean getChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) { this.checked = checked; }
}
