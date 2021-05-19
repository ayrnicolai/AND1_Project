package com.example.androidproject.views;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.example.androidproject.SpacesItemDecoration;
import com.example.androidproject.adapters.MyTimeSlotAdapter;
import com.example.androidproject.common.Common;
import com.example.androidproject.interfaces.ITimeSlotLoadListener;
import com.example.androidproject.models.TimeSlot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;

public class BookingStep3Fragment extends Fragment implements ITimeSlotLoadListener {

    //Variable
    DocumentReference consultantDoc;
    ITimeSlotLoadListener iTimeSlotLoadListener;
    AlertDialog dialog;

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;


    @BindView(R.id.recycler_time_slot)
    RecyclerView recycler_time_slot;
    @BindView(R.id.calendarView)
    HorizontalCalendarView calendarView;
    SimpleDateFormat simpleDateFormat;

    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE,0);
            loadAvailableTimeSlotOfBarber(Common.currentConsultant.getConsultantId(),
            simpleDateFormat.format(date.getTime()));
        }
    };

    private void loadAvailableTimeSlotOfBarber(String consultantId, String format) {
        dialog.show();

        ///BookingSystem/Copenhagen/Store/S6rBsYT0yk1Bx9qWU7qS/Consultant/o8NlvVSqbdg20wBr945N
        consultantDoc = FirebaseFirestore.getInstance()
                .collection("BookingSystem")
                .document(Common.city)
                .collection("Store")
                .document(Common.currentRoom.getRoomId())
                .collection("Consultant")
                .document(Common.currentConsultant.getConsultantId());

        //Get information of this barber
        consultantDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) //if consutlant avaialbe
                    {
                        //get information of booking, if not created, return empty
                        CollectionReference date = FirebaseFirestore.getInstance()
                                .collection("BookingSystem")
                                .document(Common.city)
                                .collection("Store")
                                .document(Common.currentRoom.getRoomId())
                                .collection("Consultant")
                                .document(Common.currentConsultant.getConsultantId())
                                .collection("bookDate");

                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if(querySnapshot.isEmpty())
                                        iTimeSlotLoadListener.onTimeSlotLoadEmpty();
                                    else
                                    {
                                        List<TimeSlot> timeSlots = new ArrayList<>();
                                        for (QueryDocumentSnapshot document:task.getResult())
                                            timeSlots.add(document.toObject(TimeSlot.class));
                                        iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iTimeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());
                            }
                        });
                    }
                }
            }
        });


    }

    static BookingStep3Fragment instance;

    public static BookingStep3Fragment getInstance() {
        if (instance == null)
            instance = new BookingStep3Fragment();
        return instance;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iTimeSlotLoadListener = this;

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTimeSlot, new IntentFilter(Common.KEY_DISPLAY_TIME_SLOT));

        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();



    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(displayTimeSlot);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_three, container, false);
        unbinder = ButterKnife.bind(this, itemView);

        init(itemView);

        return itemView;
    }

    private void init(View itemView) {
        recycler_time_slot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recycler_time_slot.setLayoutManager(gridLayoutManager);
        recycler_time_slot.addItemDecoration(new SpacesItemDecoration(8));

        //Calendar
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 0);
        Calendar endDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 2); // 2 day left

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(itemView, R.id.calendarView)
                .range(startDate,endDate)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if (Common.currentDate.getTimeInMillis() != date.getTimeInMillis())
                {
                    Common.currentDate = date; //this code will not load again if you selected new day same with day selected | Vi giver currentDate en value her
                    loadAvailableTimeSlotOfBarber(Common.currentConsultant.getConsultantId(),
                    simpleDateFormat.format(date.getTime()));
                }
            }
        });


    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext());
        recycler_time_slot.setAdapter(adapter);

        dialog.dismiss();

    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();

    }

    @Override
    public void onTimeSlotLoadEmpty() {
        //Because when we load barber appointment infromation, if it is empty,just return an default scheduled
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext());
        recycler_time_slot.setAdapter(adapter);

        dialog.dismiss();

    }
}
