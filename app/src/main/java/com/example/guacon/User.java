package com.example.guacon;

public class User {
    public String firstName, lastName;
//    private String email;
    public int age;

    public User(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public User() {
        firstName = "Jenny";
        lastName = "Li";
        age = 21;
    }
}
