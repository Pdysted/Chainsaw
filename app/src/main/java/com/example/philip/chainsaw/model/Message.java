package com.example.philip.chainsaw.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by philip on 5/29/17.
 */

public class Message implements Serializable {
    private String messageText;
    private Date timeSent;

    public Message(String messageText, Date timeSent) {
        this.messageText = messageText;
        this.timeSent = timeSent;
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

