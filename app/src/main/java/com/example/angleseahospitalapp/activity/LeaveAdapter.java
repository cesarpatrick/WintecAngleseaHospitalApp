package com.example.angleseahospitalapp.activity;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.angleseahospitalapp.R;
import com.example.angleseahospitalapp.model.Leave;
import com.example.angleseahospitalapp.model.LeaveStatus;

import java.util.ArrayList;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.LeaveViewHolder>{

    public ArrayList<Leave> leaveList;

    public static class LeaveViewHolder extends RecyclerView.ViewHolder {
        public TextView fromTextView;
        public TextView toTextView;
        public TextView statusTextView;

        public LeaveViewHolder(View itemView) {
            super(itemView);
            fromTextView = itemView.findViewById(R.id.fromTextView);
            toTextView = itemView.findViewById(R.id.toTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
        }
    }

    @Override
    public LeaveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_item, parent, false);
        LeaveViewHolder svh = new LeaveViewHolder(v);
        return svh;
    }

    public LeaveAdapter(ArrayList<Leave> list) {
        leaveList = list;
    }

    @Override
    public void onBindViewHolder(LeaveAdapter.LeaveViewHolder holder, int position) {
        Leave leaveItem = leaveList.get(position);

        holder.fromTextView.setText(leaveItem.getStartDate());
        holder.toTextView.setText(leaveItem.getEndDate());
        holder.statusTextView.setText(leaveItem.getLeaveStatus());

        if(LeaveStatus.valueOf(leaveItem.getLeaveStatus()).equals(LeaveStatus.APPROVED)){
            holder.statusTextView.setTextColor(Color.GREEN);
        }else if(LeaveStatus.valueOf(leaveItem.getLeaveStatus()).equals(LeaveStatus.DISAPPROVED)){
            holder.statusTextView.setTextColor(Color.RED);
        }else{
            holder.statusTextView.setTextColor(Color.BLUE);
        }
    }

    @Override
    public int getItemCount() {
        return leaveList.size();
    }
}
