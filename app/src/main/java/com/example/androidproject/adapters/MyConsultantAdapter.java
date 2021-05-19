package com.example.androidproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.example.androidproject.common.Common;
import com.example.androidproject.interfaces.IRecyclerItemSelectedListener;
import com.example.androidproject.models.Consultant;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyConsultantAdapter extends RecyclerView.Adapter<MyConsultantAdapter.MyViewHolder> {

    Context context;
    List<Consultant> consultantList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyConsultantAdapter(Context context, List<Consultant> consultantList) {
        this.context = context;
        this.consultantList = consultantList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_consultant, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewholder, int i) {
        myViewholder.txt_consultant_name.setText(consultantList.get(i).getName());
        myViewholder.ratingBar.setRating((float)consultantList.get(i).getRating());
        if(!cardViewList.contains(myViewholder.card_consultant))
            cardViewList.add(myViewholder.card_consultant);

        myViewholder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //set background for all item not chosen
                for (CardView cardView : cardViewList) {
                    cardView.setCardBackgroundColor(context.getResources()
                            .getColor(android.R.color.white));
                }
                myViewholder.card_consultant.setCardBackgroundColor(
                        context.getResources()
                        .getColor(android.R.color.holo_blue_dark)
                );
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_CONSULTANT_SELECTED, consultantList.get(pos));
                intent.putExtra(Common.KEY_STEP, 2);
                localBroadcastManager.sendBroadcast(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return consultantList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_consultant_name;
        RatingBar ratingBar;
        CardView card_consultant;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_consultant_name = (TextView)itemView.findViewById(R.id.txt_consultant_name);
            ratingBar = (RatingBar)itemView.findViewById(R.id.rtb_consultant);
            card_consultant = (CardView)itemView.findViewById(R.id.card_consultant);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view, getAdapterPosition());

        }
    }
}
