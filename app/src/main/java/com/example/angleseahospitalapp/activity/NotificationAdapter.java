package com.example.angleseahospitalapp.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.angleseahospitalapp.R;
import com.example.angleseahospitalapp.db.DBHelper;
import com.example.angleseahospitalapp.model.Notification;
import com.example.angleseahospitalapp.model.ShiftPeriod;
import com.example.angleseahospitalapp.model.User;
import com.example.angleseahospitalapp.model.Util;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{

    private Context context;

    public ArrayList<Notification> notificationList;

    DBHelper dbHelper = DBHelper.getInstance(context);

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        public TextView dayTextView;
        public TextView dayNameTextView;
        public TextView descriptionTextView;
        public TextView userNameTextView;
        public RelativeLayout relativeLayout;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            userNameTextView = itemView.findViewById(R.id.teamNameTextView);
            dayNameTextView = itemView.findViewById(R.id.dayNameTextView);
            relativeLayout = itemView.findViewById(R.id.shiftItemRelativeLayout);
        }
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        NotificationViewHolder svh = new NotificationViewHolder(v);
        return svh;
    }

    public NotificationAdapter(Context context, ArrayList<Notification> list) {
        notificationList = list;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        holder.dayTextView.setText(notification.getDate()); //this has to be split up was a date type for db
        holder.descriptionTextView.setText(Util.getTimeByShiftPeriod(ShiftPeriod.valueOf(notification.getDescription())));

        User user = dbHelper.getUserById(notification.getUserId());
        holder.userNameTextView.setText(user.getName() + " " + user.getSurname());
        holder.dayNameTextView.setText(Util.getDayNameText(Util.convertStringToDate(notification.getDate()))); //this has to be split up was a date type for db

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
