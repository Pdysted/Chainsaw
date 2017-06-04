package com.example.philip.chainsaw.model;

import java.util.ArrayList;

/**
 * Created by philip on 5/22/17.
 */

public class Rec {
    private int distance_mi;
    private String _id;
    private String bio;
    private int gender;
    private String name;
    private ArrayList<String> photoUrls;

    public Rec(int distance_mi, String _id, String bio, int gender, String name, ArrayList<String> photoUrls) {
        this.distance_mi = distance_mi;
        this._id = _id;
        this.bio = bio;
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
}
