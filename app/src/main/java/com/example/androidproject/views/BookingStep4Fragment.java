package com.example.androidproject.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.androidproject.R;
import com.example.androidproject.common.Common;
import com.example.androidproject.models.BookingInformation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BookingStep4Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;

    @BindView(R.id.txt_booking_consultant_text)
    TextView txt_booking_consultant_text;
    @BindView(R.id.txt_booking_time_text)
    TextView txt_booking_time_text;
    @BindView(R.id.txt_room_address)
    TextView txt_room_address;
    @BindView(R.id.txt_room_name)
    TextView txt_room_name;
    @BindView(R.id.txt_room_open_hours)
    TextView txt_room_open_hours;
    @BindView(R.id.txt_room_phone)
    TextView txt_room_phone;
    @BindView(R.id.txt_room_website)
    TextView txt_room_website;

    @OnClick(R.id.btn_confirm)
    void confirmBooking() {
        //Laver Booking Information
        BookingInformation bookingInformation = new BookingInformation();

        bookingInformation.setConsultantId(Common.currentConsultant.getConsultantId());
        bookingInformation.setConsultantName(Common.currentConsultant.getName());
 //       bookingInformation.setCustomerName(FirebaseUser.);
 //       bookingInformation.setCustomerPhone(FirebaseUser.);
        bookingInformation.setRoomId(Common.currentRoom.getRoomId());
        bookingInformation.setRoomAddress(Common.currentRoom.getAddress());
        bookingInformation.setRoomName(Common.currentRoom.getName());
        bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append(" at ")
                .append(simpleDateFormat.format(Common.currentDate.getTime())).toString());
        bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));

//Submitter til Room Document ()FIRbaser)
        DocumentReference bookingDate = FirebaseFirestore.getInstance()
                .collection("BookingSystem")
                .document(Common.city)
                .collection("Store")
                .document(Common.currentRoom.getRoomId())
                .collection("Consultant")
                .document(Common.currentConsultant.getConsultantId())
                .collection(Common.simpleFormatDate.format(Common.currentDate.getTime()))
                .document(String.valueOf(Common.currentTimeSlot));

        //write Data
        bookingDate.set(bookingInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        getActivity().finish();
                        Toast.makeText(getContext(), "Greeat Succes!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    BroadcastReceiver confirmBookingReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };


    private void setData() {
        txt_booking_consultant_text.setText(Common.currentConsultant.getName());
        txt_booking_time_text.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
        .append(" at ")
        .append(simpleDateFormat.format(Common.currentDate.getTime())));

        txt_room_address.setText(Common.currentRoom.getAddress());
        txt_room_website.setText(Common.currentRoom.getWebsite());
        txt_room_name.setText(Common.currentRoom.getName());
        txt_room_open_hours.setText(Common.currentRoom.getOpenHours());


    }


    static BookingStep4Fragment instance;

    public static BookingStep4Fragment getInstance() {
        if (instance == null)
            instance = new BookingStep4Fragment();
        return instance;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Apply format for display on confirm
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReciever, new IntentFilter(Common.KEY_CONFIRM_BOOKING));


    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReciever);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_four, container, false);
        unbinder = ButterKnife.bind(this, itemView);
        return itemView;
    }
}
