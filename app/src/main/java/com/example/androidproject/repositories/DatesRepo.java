package com.example.androidproject.repositories;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatesRepo {
    private DatesLiveData weekdays;
    private static DatesRepo instance;
    private DatabaseReference myRef;


    public DatesRepo() {
    }

    public static synchronized DatesRepo getInstance() {
        if(instance == null)
            instance = new DatesRepo();
        return instance;
    }

    public void init(String weekdaysId) {
        myRef = FirebaseDatabase.getInstance().getReference().child("Dates").child(weekdaysId);
        weekdays = new DatesLiveData(myRef);
    }

    public DatesLiveData getWeekdays() {
        return weekdays;
    }


}
