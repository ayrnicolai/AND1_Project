package com.example.androidproject.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.androidproject.models.Dates;
import com.example.androidproject.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DatesLiveData extends LiveData<Dates> {

    private final ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Dates dates = snapshot.getValue(Dates.class);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    DatabaseReference databaseReference;
    public DatesLiveData(DatabaseReference ref) {
        databaseReference = ref;
    }
    @Override
    protected void onActive() {
        super.onActive();
        databaseReference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        databaseReference.removeEventListener(listener);
    }
    }
