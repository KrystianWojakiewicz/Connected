package com.example.connected.DB;

public class Contact {
    public String name;
    public String image;
    public String status;
    public String uid;

    public Contact() {

    }

    public Contact(String name, String image, String status, String uid) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
