package com.example.philip.chainsaw.model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by philip on 5/29/17.
 */

public class Match {
    private String userId;
    private ArrayList<Message> messages;
    //Will be set by getUser in TinderServiceVolley
    private String name;
    private String photoUrl;

    public Match(String userId, ArrayList<Message> messages) {
        this.userId = userId;
        this.messages = messages;
        this.name = "";
        this.photoUrl = "";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
