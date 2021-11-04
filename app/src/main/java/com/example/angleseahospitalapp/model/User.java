package com.example.angleseahospitalapp.model;

public class User {

    private String userId;
    private String name;
    private String surname;
    private String pin;
    private String email;
    private String photoPath;
    private String role;
    private String phoneNumber;

    public User(){ }

    public User(String userId, String name, String surname, String pin, String photoPath, String role, String email, String phoneNumber) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.pin = pin;
        this.photoPath = photoPath;
        this.role = role;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
