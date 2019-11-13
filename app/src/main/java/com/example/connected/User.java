package com.example.connected;

public class User {
    private String username;
    private int age;
    private boolean isActive;

    public User(String username, int age, boolean isActive) {
        this.username = username;
        this.age = age;
        this.isActive = isActive;
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
