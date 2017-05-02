package com.hrl.tractorvision.model;

/**
 * Created by Sharukh Hasan on 5/2/17.
 */
public class ChatMessage {

    protected int id;
    protected String message;
    protected String senderName;

    public ChatMessage(int id, String message, String senderName) {
        this.id = id;
        this.message = message;
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
