package com.example.student;

public class ExamDataModel {

    private int id;
    private String name;
    private String location;
    private String dateAndTime;
    private boolean checked;

    ExamDataModel(int id, String name, String location, String dateAndTime) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.dateAndTime = dateAndTime;
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

}
