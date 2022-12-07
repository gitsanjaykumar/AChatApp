package com.darkenergy.achatapp.Model;

import android.net.Uri;

public class Message {
    String meassage;
    String senderId;
    long timeStamp;



    public Message(String message, String senderUid, long time, Uri imageUri) {

    }


    public Message()
    {

    }
    public Message(String meassage, String senderId, long timeStamp) {
        this.meassage = meassage;
        this.senderId = senderId;
        this.timeStamp = timeStamp;
    }

    public String getMeassage() {
        return meassage;
    }

    public void setMeassage(String meassage) {
        this.meassage = meassage;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
