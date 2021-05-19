package com.example.androidproject.models;

import com.google.firebase.Timestamp;

import java.sql.Time;

public class Dates {

    private String id;
    private Timestamp weekday;

    public Timestamp getWeekday() {
        return weekday;
    }

    public void setWeekday(Timestamp weekday) {
        this.weekday = weekday;
    }

    public Dates(Timestamp weekday) {
        this.weekday = weekday;
    }


}
