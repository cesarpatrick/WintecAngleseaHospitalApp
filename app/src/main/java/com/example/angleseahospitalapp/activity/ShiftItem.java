package com.example.angleseahospitalapp.activity;

public class ShiftItem {

    private String day;
    private String time;
    private String teamName;
    private String dayName;
    private String staffID;

    public ShiftItem(String day, String time, String teamName, String dayName) {
        this.day = day;
        this.time = time;
        this.teamName = teamName;
        this.dayName = dayName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }
}
