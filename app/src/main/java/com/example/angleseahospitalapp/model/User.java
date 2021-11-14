package com.example.angleseahospitalapp.model;

public class User {

    private String userId;
    private String name;
    private String surname;
    private String pin;
    private String email;
    private byte[] photo;
    private String role;
    private String phoneNumber;
    private String group;

    public User(){ }

    public User(String userId, String name, String surname, String pin, byte[] photo, String role, String email, String phoneNumber, String group) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.pin = pin;
        this.photo = photo;
        this.role = role;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.group = group;
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

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
