package com.example.guacon;

import java.util.ArrayList;

public class User {
    public String First_Name, Last_Name;
    public int Age;
    public ArrayList<String> saved_recipes, your_recipes;

    public User(){}

    public User(String firstName, String lastName, int age) {
        First_Name = firstName;
        Last_Name = lastName;
        Age = age;
    }

    public void setYour_recipes(ArrayList<String> your_recipes) {
        this.your_recipes = your_recipes;
    }

    public ArrayList<String> getYour_recipes() {
        return your_recipes;
    }

    public ArrayList<String> getSaved_recipes() {
        return saved_recipes;
    }

    public void setSaved_recipes(ArrayList<String> saved_recipes) {
        this.saved_recipes = saved_recipes;
    }

    public int getAge() {
        return Age;
    }

    public String getName() {
        return First_Name+" "+Last_Name;
    }

    public void setAge(int age) {
        Age = age;
    }

    public void setName(String first_Name, String last_Name) {
        First_Name = first_Name;
        Last_Name = last_Name;
    }
}
