package com.example.androidproject.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.androidproject.R;
import com.example.androidproject.viewmodels.ShopViewModel;
import com.google.firebase.firestore.CollectionReference;

public class BookingLists extends AppCompatActivity {

    ShopViewModel shopViewModel;
    AlertDialog dialog;
    CollectionReference consultantRef;

    TextView textViewBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_lists);

        textViewBooking = findViewById(R.id.textViewDate);

 /*       shopViewModel.getDate().observe(this, dates -> {
            textView.getText();
        });*/



    }
}