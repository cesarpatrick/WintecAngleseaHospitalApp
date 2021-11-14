package com.example.angleseahospitalapp.activity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class ShiftDashboardAdapter extends RecyclerView.Adapter<ShiftDashboardAdapter.ShiftDashboardViewHolder>{

    public ArrayList<Shift> shiftList;
    public Context context;

    DBHelper dbHelper;

    public static class ShiftDashboardViewHolder extends RecyclerView.ViewHolder {
        public TextView teamTimeTextView;
        public TextView nameTextView;
        public ImageView imageView;
        public RelativeLayout relativeLayout;

        public ShiftDashboardViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            teamTimeTextView = itemView.findViewById(R.id.teamTimeTextView);
            imageView = itemView.findViewById(R.id.nurseImage);
            relativeLayout = itemView.findViewById(R.id.shiftItemRelativeLayout);
        }
    }

    @Override
    public ShiftDashboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shift_dashboard_item, parent, false);
        dbHelper = DBHelper.getInstance(parent.getContext());
        context = parent.getContext();
        ShiftDashboardViewHolder svh = new ShiftDashboardViewHolder(v);
        return svh;
    }

    public ShiftDashboardAdapter(ArrayList<Shift> list) {
        shiftList = list;
    }

    @Override
    public void onBindViewHolder(ShiftDashboardAdapter.ShiftDashboardViewHolder holder, int position) {
        Shift shiftItem = shiftList.get(position);

        User user = dbHelper.getUserById(shiftItem.getStaffID());

        holder.teamTimeTextView.setText(user.getGroup() +" " + Util.getTimeByShiftPeriod(ShiftPeriod.valueOf(shiftItem.getPeriod()))); //this has to be split up was a date type for db
        holder.nameTextView.setText(user.getName() + " " + user.getSurname());

        if(user.getPhoto() != null) {
            holder.imageView.setImageBitmap(Util.getCircleBitmap(BitmapFactory.decodeByteArray(user.getPhoto(), 0, user.getPhoto().length), 100));
        }else{
            final Drawable icon = context.getDrawable(R.drawable.ic_person);
            holder.imageView.setImageDrawable(icon);
        }
    }

    @Override
    public int getItemCount() {
        return shiftList.size();
    }
}
