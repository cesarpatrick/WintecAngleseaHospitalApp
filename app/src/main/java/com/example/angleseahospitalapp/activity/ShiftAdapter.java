package com.example.angleseahospitalapp.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.angleseahospitalapp.R;

import java.util.ArrayList;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ExampleViewHolder>{

    private ArrayList<ShiftItem> shiftList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView dayTextView;
        public TextView timeTextView;
        public TextView teamNameTextView;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            teamNameTextView = itemView.findViewById(R.id.teamNameTextView);
        }
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shift_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    public ShiftAdapter(ArrayList<ShiftItem> list) {
        shiftList = list;
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftAdapter.ExampleViewHolder holder, int position) {
        ShiftItem shiftItem = shiftList.get(position);

        holder.dayTextView.setText(shiftItem.getDay());
        holder.timeTextView.setText(shiftItem.getTime());
        holder.teamNameTextView.setText(shiftItem.getTeamName());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
