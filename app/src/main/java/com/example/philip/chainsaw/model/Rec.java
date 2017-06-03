package com.example.philip.chainsaw.model;

import java.util.ArrayList;

/**
 * Created by philip on 5/22/17.
 */

public class Rec {
    private int distance_mi;
    private int common_friend_count;
    private String _id;
    private String bio;
    private int gender;
    private String name;
    private ArrayList<String> photoUrls;

    public Rec(int distance_mi, int common_friend_count, String _id, String bio, int gender, String name, ArrayList<String> photoUrls) {
        this.distance_mi = distance_mi;
        this.common_friend_count = common_friend_count;
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

    public int getCommon_friend_count() {
        return common_friend_count;
    }

    public void setCommon_friend_count(int common_friend_count) {
        this.common_friend_count = common_friend_count;
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
