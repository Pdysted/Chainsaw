package com.example.philip.chainsaw.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by philip on 5/22/17.
 */

public class Rec {
    private int distance_mi;
    private String _id;
    private String bio;
    private Date birthDate;
    private int gender;
    private String name;
    private ArrayList<String> photoUrls;


    public Rec(int distance_mi, String _id, String bio, Date birthDate, int gender, String name, ArrayList<String> photoUrls) {
        this.distance_mi = distance_mi;
        this._id = _id;
        this.bio = bio;
        this.birthDate = birthDate;
        this.gender = gender;
        this.name = name;
        this.photoUrls = photoUrls;
    }

    public int getDistance_mi() {
        return distance_mi;
    }

    public void setDistance_mi(int distance_mi) {
        this.distance_mi = distance_mi;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(ArrayList<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public int getAge() {
        Calendar calCurrent = Calendar.getInstance();
        calCurrent.setTime(new Date(System.currentTimeMillis()));
        Calendar calBirth = Calendar.getInstance();
        calBirth.setTime(birthDate);
        int yearDiff = (calCurrent.get(Calendar.YEAR) - calBirth.get(Calendar.YEAR));
        int birthMonth = calBirth.get(Calendar.MONTH);
        int currMonth = calCurrent.get(Calendar.MONTH);
        int birthDay = calBirth.get(Calendar.DAY_OF_MONTH);
        int currDay = calCurrent.get(Calendar.DAY_OF_MONTH);
        if (currMonth > birthMonth) {
            yearDiff -= 1;
        } else if (currMonth == birthMonth) {
            if (currDay > birthDay) {
                yearDiff -= 1;
            }
        }
        return yearDiff;
    }
}
