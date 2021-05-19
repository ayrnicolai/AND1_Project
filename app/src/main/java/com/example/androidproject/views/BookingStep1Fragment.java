package com.example.androidproject.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.SpacesItemDecoration;
import com.example.androidproject.adapters.MyRoomAdapter;
import com.example.androidproject.common.Common;
import com.example.androidproject.interfaces.IAllRoamLoadListener;
import com.example.androidproject.R;
import com.example.androidproject.interfaces.IStoreLoadListener;
import com.example.androidproject.models.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep1Fragment extends Fragment implements IAllRoamLoadListener, IStoreLoadListener {

    //Variable
    CollectionReference allRoomRef;
    CollectionReference storeRef;

    IStoreLoadListener iStoreLoadListener;
    IAllRoamLoadListener iAllRoamLoadListener;

    //JackKnife
    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.recycler_Room)
    RecyclerView recyclerView;

    Unbinder unbinder;

    AlertDialog dialog;

    static BookingStep1Fragment instance;

    public static BookingStep1Fragment getInstance() {
        if (instance == null)
            instance = new BookingStep1Fragment();
        return instance;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allRoomRef = FirebaseFirestore.getInstance().collection("BookingSystem");
        iAllRoamLoadListener = this;
        iStoreLoadListener = this;

        dialog = new SpotsDialog.Builder().setContext(getActivity()).setCancelable(false).build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_one, container, false);
        unbinder = ButterKnife.bind(this, itemView);

        initView();
        loadAllRoom();

        return itemView;
    }
    private void initView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(new SpacesItemDecoration(4));
    }

    private void loadAllRoom() {
        allRoomRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> list = new ArrayList<>();
                            list.add("Please Choose A City");
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                                list.add(documentSnapshot.getId());
                            iAllRoamLoadListener.onAllRoomLoadSucces(list);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllRoamLoadListener.onAllRoomLoadFailed(e.getMessage());
            }
        });

    }

    @Override
    public void onAllRoomLoadSucces(List<String> areaNameList) {
        spinner.setItems(areaNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position > 0) {
                    loadRoomOfStore(item.toString());
                }
                else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

    }
    private void loadRoomOfStore(String cityName) {
        dialog.show();

        Common.city = cityName;

        storeRef = FirebaseFirestore.getInstance()
                .collection("BookingSystem")
                .document(cityName)
                .collection("Store");

        storeRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Room> list = new ArrayList<>();
                if (task.isSuccessful()) {
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                    {
                        Room room = documentSnapshot.toObject(Room.class);
                        room.setRoomId(documentSnapshot.getId());
                        list.add(room);
                    }
                    iStoreLoadListener.onBranchLoadSucces(list);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iStoreLoadListener.onBranchLoadFailed(e.getMessage());
            }
        });

    }


    @Override
    public void onAllRoomLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBranchLoadSucces(List<Room> RoomList) {
        MyRoomAdapter adapter = new MyRoomAdapter(getActivity(), RoomList);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);

        dialog.dismiss();

    }

    @Override
    public void onBranchLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();

    }

}
