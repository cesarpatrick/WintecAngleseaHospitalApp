package com.example.angleseahospitalapp.model;

public class Notification {
    String id;
    String userId;
    String description;
    String date;

    public Notification(){}

    public Notification(String id, String userId, String description, String date) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
