package com.example.student;

public class TodoTaskDataModel {

    public int id;
    public String name;
    public String location;
    public String status;

    public TodoTaskDataModel(int id, String name, String location, String status) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.status = status;
    }


    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getlocation() {
        return this.location;
    }

    public String getStatus() {
        return this.status;
    }

}
