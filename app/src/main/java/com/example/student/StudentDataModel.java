package com.example.student;

public class StudentDataModel {

    String firstName, lastName, gender, course ,address;
    int age;

    StudentDataModel(String firstName, String lastName, String gender, int age, String course, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.course = course;
        this.address = address;

    }
}
