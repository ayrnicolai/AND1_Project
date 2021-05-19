package com.example.androidproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.example.androidproject.interfaces.IRecyclerItemSelectedListener;
import com.example.androidproject.common.Common;
import com.example.androidproject.models.Room;

import java.util.ArrayList;
import java.util.List;

public class MyRoomAdapter extends RecyclerView.Adapter<MyRoomAdapter.MyViewHolder> {

    Context context;
    List<Room> roomList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyRoomAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_room, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_room_name.setText(roomList.get(i).getName());
        myViewHolder.txt_room_address.setText(roomList.get(i).getAddress());

        if (!cardViewList.contains(myViewHolder.card_room))
            cardViewList.add(myViewHolder.card_room);

        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //set background for cards not being used
                for (CardView cardView:cardViewList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
                //Set selected BG for only
                myViewHolder.card_room.setCardBackgroundColor(context.getResources()
                .getColor(android.R.color.darker_gray));

                //Sends broadcast to tell booking activity enable button next
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_ROOM_STORE, roomList.get(pos));
                intent.putExtra(Common.KEY_STEP, 1);
                localBroadcastManager.sendBroadcast(intent);


            }
        });


//We need send to room object to intent, so we must implement parcelable for room object
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_room_name,txt_room_address;
        CardView card_room;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_room = (CardView)itemView.findViewById(R.id.card_room);
            txt_room_address = (TextView)itemView.findViewById(R.id.txt_room_address);
            txt_room_name = (TextView)itemView.findViewById(R.id.txt_room_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view, getAdapterPosition());
        }
    }

    }



