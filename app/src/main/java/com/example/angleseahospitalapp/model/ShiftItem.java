package com.example.angleseahospitalapp.model;

public class ShiftItem {

    private String clockInTime;
    private String clockOutTime;
    private String teamName;
    private String date;
    private String staffID;
    private String shiftKey;

    public ShiftItem(String staffID, String date, String clockInTime, String clockOutTime, String teamName) {
        this.staffID = staffID;
        this.date = date;
        this.clockInTime = clockInTime;
        this.clockOutTime = clockOutTime;
        this.teamName = teamName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClockInTime() {
        return clockInTime;
    }

    public void setClockInTime(String clockInTime) {
        this.clockInTime = clockInTime;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }


    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getShiftKey() { return shiftKey; }

    public void setShiftKey(String shiftKey) { this.shiftKey = shiftKey; }

    public String getClockOutTime() {
        return clockOutTime;
    }

    public void setClockOutTime(String clockOutTime) {
        this.clockOutTime = clockOutTime;
    }
}
