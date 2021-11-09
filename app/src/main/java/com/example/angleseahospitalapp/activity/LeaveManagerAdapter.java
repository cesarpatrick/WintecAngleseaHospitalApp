package com.example.angleseahospitalapp.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.angleseahospitalapp.R;
import com.example.angleseahospitalapp.model.Leave;
import com.example.angleseahospitalapp.model.LeaveStatus;

import java.util.ArrayList;
import java.util.List;

public class LeaveManagerAdapter extends RecyclerView.Adapter<LeaveManagerAdapter.LeaveViewHolder>{

    private Context mContext;
    public ArrayList<Leave> leaveList;
    private ArrayAdapter<String> dataAdapter;

    public LeaveManagerAdapter(Context context, ArrayList<Leave> list){
        this.mContext = context;

        List<String> periodSpinnerList = new ArrayList<>();
        periodSpinnerList.add(LeaveStatus.REQUESTED.toString());
        periodSpinnerList.add(LeaveStatus.APPROVED.toString());
        periodSpinnerList.add(LeaveStatus.DISAPPROVED.toString());

        leaveList = list;

        dataAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item, periodSpinnerList);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    public static class LeaveViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameTextView;
        public TextView periodTextView;
        public Spinner leaveStatusSpinner;

        public LeaveViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            periodTextView = itemView.findViewById(R.id.periodTextView);
            leaveStatusSpinner = itemView.findViewById(R.id.leaveStatusSpinner);

        }
    }

    @Override
    public LeaveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_manager_item, parent, false);
        LeaveViewHolder svh = new LeaveViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(LeaveManagerAdapter.LeaveViewHolder holder, int position) {
        Leave leaveItem = leaveList.get(position);

        holder.userNameTextView.setText(leaveItem.getStartDate());
        holder.periodTextView.setText(leaveItem.getEndDate());
        holder.leaveStatusSpinner.setAdapter(dataAdapter);

        if(LeaveStatus.valueOf(leaveItem.getLeaveStatus()).equals(LeaveStatus.APPROVED)){
            holder.leaveStatusSpinner.setSelection(0);
        }else if(LeaveStatus.valueOf(leaveItem.getLeaveStatus()).equals(LeaveStatus.DISAPPROVED)){
            holder.leaveStatusSpinner.setSelection(1);
        }else{
            holder.leaveStatusSpinner.setSelection(2);
        }
    }

    @Override
    public int getItemCount() {
        return leaveList.size();
    }

}
