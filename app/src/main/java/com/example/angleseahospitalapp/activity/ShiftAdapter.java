package com.example.angleseahospitalapp.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.angleseahospitalapp.R;
import com.example.angleseahospitalapp.model.ShiftItem;

import java.util.ArrayList;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ShiftViewHolder>{

    public ArrayList<ShiftItem> shiftList;

    public static class ShiftViewHolder extends RecyclerView.ViewHolder {
        public TextView dayTextView;
        public TextView dayNameTextView;
        public TextView timeTextView;
        public TextView teamNameTextView;

        public ShiftViewHolder(View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            teamNameTextView = itemView.findViewById(R.id.teamNameTextView);
            dayNameTextView = itemView.findViewById(R.id.dayNameTextView);
        }
    }

    @Override
    public ShiftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shift_item, parent, false);
        ShiftViewHolder svh = new ShiftViewHolder(v);
        return svh;
    }

    public ShiftAdapter(ArrayList<ShiftItem> list) {
        shiftList = list;
    }

    @Override
    public void onBindViewHolder(ShiftAdapter.ShiftViewHolder holder, int position) {
        ShiftItem shiftItem = shiftList.get(position);

        holder.dayTextView.setText(shiftItem.getDay());
        holder.timeTextView.setText(shiftItem.getTime());
        holder.teamNameTextView.setText(shiftItem.getTeamName());
        holder.dayNameTextView.setText(shiftItem.getDayName());
    }

    @Override
    public int getItemCount() {
        return shiftList.size();
    }
}
