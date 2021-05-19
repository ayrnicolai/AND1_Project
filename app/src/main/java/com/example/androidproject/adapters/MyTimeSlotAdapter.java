package com.example.androidproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.example.androidproject.common.Common;
import com.example.androidproject.interfaces.IRecyclerItemSelectedListener;
import com.example.androidproject.models.Consultant;
import com.example.androidproject.models.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {

    Context context;
    List<TimeSlot> timeSlotList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_time_slot, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_time_slot.setText(new StringBuilder(Common.convertTimeSlotToString(i)).toString());
        if (timeSlotList.size() == 0) // if all position is avaialibe just show list
        {

            myViewHolder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

            myViewHolder.txt_time_slot_description.setText("Available");
            myViewHolder.txt_time_slot_description.setTextColor(context.getResources()
            .getColor(android.R.color.black));
            myViewHolder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black));

        }
        else //if have position is booked FUlll
        {
            for (TimeSlot slotValue:timeSlotList) {
                int slot = Integer.parseInt(slotValue.getSlot().toString());
                if(slot == i) //if slot == posiiton
                {
                    //We will set tag for all time slot is full || So base on tag, we can set all remain card background without changing full time slot
                    myViewHolder.card_time_slot.setTag(Common.DISABLE_TAG);
                    myViewHolder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                    myViewHolder.txt_time_slot_description.setText("Booked");
                    myViewHolder.txt_time_slot_description.setTextColor(context.getResources()
                            .getColor(android.R.color.white));
                    myViewHolder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.white));


                }
            }
        }
        //add all card to list
        //No add card already in cardViewList
        if(!cardViewList.contains(myViewHolder.card_time_slot))
            cardViewList.add(myViewHolder.card_time_slot);

        //check if card time slot is avaialbe
          myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
              @Override
              public void onItemSelectedListener(View view, int pos) {
                  //Loop all card in card List
                  for(CardView cardView:cardViewList)
                  {
                      if (cardView.getTag() == null) //Only availabe card time slot be change
                          cardView.setCardBackgroundColor(context.getResources()
                                  .getColor(R.color.white));
                  }
                  //Our selected card will change color
                  myViewHolder.card_time_slot.setCardBackgroundColor(context.getResources()
                          .getColor(android.R.color.holo_blue_dark));
                  //After that, send broadcast to enable button NEXT
                  Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                  intent.putExtra(Common.KEY_TIME_SLOT,i); //put index of time slot we have selected  //Might problem here check later ()I)
                  intent.putExtra(Common.KEY_STEP, 3);
                  localBroadcastManager.sendBroadcast(intent);


              }
          });


    }

    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_time_slot, txt_time_slot_description;
        CardView card_time_slot;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = (CardView)itemView.findViewById(R.id.card_time_slot);
            txt_time_slot = (TextView)itemView.findViewById(R.id.txt_time_slot);
            txt_time_slot_description = (TextView)itemView.findViewById(R.id.txt_time_slot_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view,getAdapterPosition());
        }
    }
}
