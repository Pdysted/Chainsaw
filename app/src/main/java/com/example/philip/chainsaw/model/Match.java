package com.example.philip.chainsaw.model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by philip on 5/29/17.
 */

public class Match {
    private String userId;
    //Combination of both your own id and your match, it's used for message sending
    private String matchId;
    private ArrayList<Message> messages;
    //Will be set by getUser in TinderServiceVolley
    private String name;
    private String photoUrl;

    public Match(String userId, String matchId, ArrayList<Message> messages) {
        this.userId = userId;
        this.matchId = matchId;
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

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
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
