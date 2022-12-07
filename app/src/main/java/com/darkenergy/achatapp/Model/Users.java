package com.darkenergy.achatapp.Model;

public class Users {

    String uid;
    String username;
    String email;
    String imageUri;
    String Status;

    public Users() {
    }

    public Users(String uid, String username, String email, String imageUri, String Status) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.imageUri = imageUri;
        this.Status=Status;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
