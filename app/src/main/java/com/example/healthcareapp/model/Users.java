package com.example.healthcareapp.model;

public class Users {
    String uid, name, email, imguri;

    public Users(String uid, String name, String email, String imguri){
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.imguri = imguri;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImguri() {
        return imguri;
    }

    public void setImguri(String imguri) {
        this.imguri = imguri;
    }


}
