package com.example.androidproject.views;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.example.androidproject.SpacesItemDecoration;
import com.example.androidproject.adapters.MyConsultantAdapter;
import com.example.androidproject.common.Common;
import com.example.androidproject.databinding.FragmentOrderBinding;
import com.example.androidproject.models.Consultant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep2Fragment extends Fragment {

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    CollectionReference consultantRef;

    AlertDialog dialog;

    @BindView(R.id.recycler_consultant)
    RecyclerView recycler_consultant;

    static BookingStep2Fragment instance;

    private BroadcastReceiver consultantDoneReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Consultant> consultantArrayList = intent.getParcelableArrayListExtra(Common.KEY_CONSULTANT_LOAD_DONE);

            MyConsultantAdapter adapter = new MyConsultantAdapter(getContext(), consultantArrayList);
            recycler_consultant.setAdapter(adapter);
        }
    };

    public static BookingStep2Fragment getInstance() {
        if (instance == null)
            instance = new BookingStep2Fragment();
        return instance;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(consultantDoneReciever, new IntentFilter(Common.KEY_CONSULTANT_LOAD_DONE));

    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(consultantDoneReciever);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_two, container, false);
        unbinder = ButterKnife.bind(this, itemView);
        
        initView();
     //   loadConsultantByRoom(Common.currentRoom.getRoomId());

        return itemView;
    }

    private void initView() {
        recycler_consultant.setHasFixedSize(true);
        recycler_consultant.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_consultant.addItemDecoration(new SpacesItemDecoration(4));
    }

  /*  private void loadConsultantByRoom(String roomId) {
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


    }*/

}
