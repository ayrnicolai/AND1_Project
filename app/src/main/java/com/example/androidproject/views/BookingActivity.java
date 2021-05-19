package com.example.androidproject.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.androidproject.R;
import com.example.androidproject.adapters.MyViewPagerAdapter;
import com.example.androidproject.common.Common;
import com.example.androidproject.databinding.FragmentOrderBinding;
import com.example.androidproject.models.Consultant;
import com.example.androidproject.viewmodels.ShopViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class BookingActivity extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;

    BookingStep2Fragment bookingStep2Fragment;

    CollectionReference consultantRef;

    AlertDialog dialog;


    StepView stepView;
    Button btn_next_step;
    Button btn_previous_step;
    ViewPager viewPager;

    //event
    @OnClick(R.id.btn_previous_step)
    void previousStep() {
        if (Common.step == 3 || Common.step > 0) {
            Common.step--;
            viewPager.setCurrentItem(Common.step);
        }
    }

    private void loadConsultantByRoom(String roomId) {
        dialog.show();

        //Select all cusultants of rooms
        //BookingSystem/Aarhus/Store/dRWUbVAzWSBYgDOGbS9C/Consultant
        if(!TextUtils.isEmpty(Common.city)) {
            consultantRef = FirebaseFirestore.getInstance()
                    .collection("BookingSystem")
                    .document(Common.city)
                    .collection("Store")
                    .document(roomId)
                    .collection("Consultant");

            consultantRef.get()
                    .addOnCompleteListener(task -> {
                        ArrayList<Consultant> consultants = new ArrayList<>();
                        for (QueryDocumentSnapshot consultantSnapShot:task.getResult())
                        {
                            Consultant consultant = consultantSnapShot.toObject(Consultant.class);
                            consultant.setPassword(""); //Remove password because in client app
                            consultant.setConsultantId(consultantSnapShot.getId());

                            consultants.add(consultant);
                        }
                        //send broadcast to bookingstep2frament to load recycler
                        Intent intent = new Intent(Common.KEY_CONSULTANT_LOAD_DONE);
                        intent.putParcelableArrayListExtra(Common.KEY_CONSULTANT_LOAD_DONE,consultants);
                        localBroadcastManager.sendBroadcast(intent);

                        dialog.dismiss();

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                        }
                    });
        }


    }

    //Broadcast Reciever
    private BroadcastReceiver buttonNextReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int step = intent.getIntExtra(Common.KEY_STEP, 0);
            if(step == 1)
                Common.currentRoom = intent.getParcelableExtra(Common.KEY_ROOM_STORE);
            else if (step == 2)
                Common.currentConsultant = intent.getParcelableExtra(Common.KEY_CONSULTANT_SELECTED);
            else if (step == 3)
                Common.currentTimeSlot = intent.getIntExtra(Common.KEY_TIME_SLOT, -1);


       //     Common.currentRoom = intent.getParcelableExtra(Common.KEY_ROOM_STORE);
            btn_next_step.setEnabled(true);
/*
            setColorButton();
*/
        }
    };

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReciever);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(BookingActivity.this);

        stepView = findViewById(R.id.step_view);
        viewPager = findViewById(R.id.view_pager);
        btn_next_step = findViewById(R.id.btn_next_step);

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReciever, new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));

        setupStepView();
        btn_next_step.setOnClickListener(v -> {
            Toast.makeText(BookingActivity.this, ""+Common.currentRoom.getRoomId(), Toast.LENGTH_SHORT).show();
            if (Common.step < 3 || Common.step == 0) {
                Common.step++;
                if(Common.step == 1) //Efter man har valgt room
                {
                    if (Common.currentRoom != null)
                        Toast.makeText(this, "Nugget", Toast.LENGTH_SHORT).show();
                        loadConsultantByRoom(Common.currentRoom.getRoomId());
                }
                else if(Common.step == 2)  //Efter man har valgt en tid
                {
                    if (Common.currentConsultant != null)
                        loadTimeSlotOfBarber(Common.currentConsultant.getConsultantId());

                }
                else if(Common.step == 3) //Confirm
                {
                    if (Common.currentTimeSlot != -1)
                        confirmBooking();

                }
                viewPager.setCurrentItem(Common.step);
            }
        });
            //View
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4); // keeps state of the fgramgent. if not do this you wil lose state of all when pressed previous
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int i) {

                stepView.go(i, true);
/*                if(i == 0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);*/

                /*setColorButton();*/

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void confirmBooking() {
        //Sender broadcast til booking fragment step four
        Intent intent = new Intent(Common.KEY_CONFIRM_BOOKING);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadTimeSlotOfBarber(String consultantId) {
        //Send local broadcast to fragment step 3. We use common.currentconsultant to check consultant so we need assign value to it, so we will assign on lisntener broadcast when we receive selceted consultant from fragment two
        Intent intent = new Intent(Common.KEY_DISPLAY_TIME_SLOT);
        localBroadcastManager.sendBroadcast(intent);
    }
    /*    private void setColorButton() {
        if (btn_next_step.isEnabled())
        {
            btn_next_step.setBackgroundResource(R.color.black);

        } else {
            btn_next_step.setBackgroundResource(R.color.design_default_color_background);
        }

        if (btn_previous_step.isEnabled())
        {
            btn_previous_step.setBackgroundResource(R.color.black);

        } else {
            btn_previous_step.setBackgroundResource(R.color.design_default_color_background);
        }
    }*/

   /* public void openNewActivity(){
        Intent intent = new Intent(this, BookingStep2Fragment.class);
        startActivity(intent);
    }*/

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Room");
        stepList.add("Consultant");
        stepList.add("TimeSlot");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}