package com.example.whatsappclone.Models;

public class MessageModel {

    private String senderID;
    private String receiverID;
    private String messageID;
    private String message;
    private boolean seen;
    private Long timeStamp;

    public MessageModel(String userID, String message, boolean seen, Long timeStamp) {
        this.senderID = userID;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public MessageModel(String userID, String message) {
        this.senderID = userID;
        this.message = message;
        this.seen = false;
    }

    public MessageModel(String senderID, String receiverID, String message, Long timeStamp) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public MessageModel() {
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }
}

