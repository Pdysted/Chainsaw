package com.example.philip.chainsaw.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by philip on 5/29/17.
 */

public class Message implements Serializable {
    private String senderId;
    private String messageText;
    private Date timeSent;

    public Message(String senderId, String messageText, Date timeSent) {
        this.senderId = senderId;
        this.messageText = messageText;
        this.timeSent = timeSent;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Date getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent = timeSent;
    }
}

