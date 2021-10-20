package com.example.angleseahospitalapp.activity;

public class ShiftItem {

    private String day;
    private String time;
    private String teamName;

    public ShiftItem(String day, String time, String teamName) {
        this.day = day;
        this.time = time;
        this.teamName = teamName;
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
}
