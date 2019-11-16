package com.example.connected.DB;


public class User {
    private String name;
    private String statusText;
    private boolean isActive;
    private String image;

    public User() {

    };

    public User(String name, boolean isActive, String image) {
        this.name = name;

        this.isActive = isActive;
        this.image = image;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String isActive() {
        if (isActive) {
            return "Online";
        }
        else {
            return "Offline";
        }
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
