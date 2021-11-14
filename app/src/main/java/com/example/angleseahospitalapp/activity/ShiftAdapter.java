package com.example.angleseahospitalapp.activity;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.angleseahospitalapp.R;
import com.example.angleseahospitalapp.db.DBHelper;
import com.example.angleseahospitalapp.model.Shift;
import com.example.angleseahospitalapp.model.ShiftPeriod;
import com.example.angleseahospitalapp.model.User;
import com.example.angleseahospitalapp.model.Util;

import java.util.ArrayList;
import java.util.Date;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ShiftViewHolder>{

    public ArrayList<Shift> shiftList;

    DBHelper dbHelper;

    public static class ShiftViewHolder extends RecyclerView.ViewHolder {
        public TextView dayTextView;
        public TextView dayNameTextView;
        public TextView timeTextView;
        public TextView teamNameTextView;
        public RelativeLayout relativeLayout;

        public ShiftViewHolder(View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            teamNameTextView = itemView.findViewById(R.id.teamNameTextView);
            dayNameTextView = itemView.findViewById(R.id.dayNameTextView);
            relativeLayout = itemView.findViewById(R.id.shiftItemRelativeLayout);
        }
    }

    @Override
    public ShiftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shift_item, parent, false);
        dbHelper = DBHelper.getInstance(parent.getContext());
        ShiftViewHolder svh = new ShiftViewHolder(v);
        return svh;
    }

    public ShiftAdapter(ArrayList<Shift> list) {
        shiftList = list;
    }

    @Override
    public void onBindViewHolder(ShiftAdapter.ShiftViewHolder holder, int position) {
        Shift shiftItem = shiftList.get(position);

        User user = dbHelper.getUserById(shiftItem.getStaffID());

        holder.dayTextView.setText(shiftItem.getDate()); //this has to be split up was a date type for db
        holder.timeTextView.setText(Util.getTimeByShiftPeriod(ShiftPeriod.valueOf(shiftItem.getPeriod())));
        holder.teamNameTextView.setText(user.getGroup());
        holder.dayNameTextView.setText(Util.getDayNameText(Util.convertStringToDate(shiftItem.getDate()))); //this has to be split up was a date type for db

        if(shiftItem.getClockOutTime() != null && !shiftItem.getClockOutTime().isEmpty()){
            holder.relativeLayout.setBackgroundColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return shiftList.size();
    }
}
