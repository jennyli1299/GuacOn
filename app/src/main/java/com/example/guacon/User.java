package com.example.guacon;

import android.os.Build;

import java.io.Serializable;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;

public class User implements Serializable {
    private String First_Name, Last_Name;
    private long Age, Followers_count, Following_count;
    private ArrayList<String> Followers = new ArrayList<>(), Following = new ArrayList<>();

    public User(){}

    public User(User user) {
        First_Name = user.First_Name;
        Last_Name = user.Last_Name;
        Followers.addAll(user.Followers);
        Following.addAll(user.Following);
        Followers_count = user.Followers_count;
        Following_count = user.Following_count;
    }

    public ArrayList<String> getFollowers() {
        return Followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        Followers = followers;
    }

    public ArrayList<String> getFollowing() {
        return Following;
    }

    public void setFollowers_count(long followers_count) {
        Followers_count = followers_count;
    }

    public long getFollowers_count() {
        return Followers_count;
    }

    public long getFollowing_count() {
        return Following_count;
    }

    public void setFollowing_count(long following_count) {
        Following_count = following_count;
    }

    public void setFollowing(ArrayList<String> following) {
        Following = following;
    }

    public int getAge() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(Age);
        return (Calendar.getInstance().get(Calendar.YEAR)-c.get(Calendar.YEAR));
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public String getName() {
        return getFirst_Name() + " " + getLast_Name().charAt(0);
    }

    public void setAge(long age) {
        Age = age;
    }

    public void setName(String first_Name, String last_Name) {
        First_Name = first_Name;
        Last_Name = last_Name;
    }
}
