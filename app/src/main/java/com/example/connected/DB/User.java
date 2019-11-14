package com.example.connected.DB;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class User {
    private String username;
    private int age;
    private boolean isActive;
    private int userImage;

    public User(String username, int age, boolean isActive, int userImage) {
        this.username = username;
        this.age = age;
        this.isActive = isActive;
        this.userImage = userImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
