package com.example.angleseahospitalapp.model;

public class Nurse {

    private String mKey;
    private String name;
    private String surname;
    private String pin;
    private String photoUrl;

    public Nurse(String mKey, String name, String surname, String pin, String photoUrl) {
        this.mKey = mKey;
        this.name = name;
        this.surname = surname;
        this.pin = pin;
        this.photoUrl = photoUrl;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
