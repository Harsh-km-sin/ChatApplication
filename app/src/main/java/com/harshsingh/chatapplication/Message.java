package com.harshsingh.chatapplication;

public class Message {
    String message = null;
    String senderId = null;

    public Message(){}

    public Message(String message, String senderId) {
        this.message = message;
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }
}
