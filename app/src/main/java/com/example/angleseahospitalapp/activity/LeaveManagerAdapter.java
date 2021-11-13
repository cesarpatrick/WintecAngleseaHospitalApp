package com.example.angleseahospitalapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.angleseahospitalapp.R;
import com.example.angleseahospitalapp.db.DBHelper;
import com.example.angleseahospitalapp.model.Leave;
import com.example.angleseahospitalapp.model.LeaveStatus;
import com.example.angleseahospitalapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class LeaveManagerAdapter extends RecyclerView.Adapter<LeaveManagerAdapter.LeaveViewHolder>{

    private Context context;
    public ArrayList<Leave> leaveList;

    public static class LeaveViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameTextView;
        public TextView periodTextView;
        public Spinner leaveStatusSpinner;
        public TextView idTextView;

        public LeaveViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            periodTextView = itemView.findViewById(R.id.periodTextView);
            leaveStatusSpinner = itemView.findViewById(R.id.leaveStatusSpinner);
            idTextView = itemView.findViewById(R.id.idTextView);
        }
    }

    @Override
    public LeaveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_manager_item, parent, false);
        LeaveViewHolder svh = new LeaveViewHolder(v);
        return svh;
    }

    public LeaveManagerAdapter(Context context, ArrayList<Leave> list) {
        leaveList = list;
        this.context = context;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(LeaveManagerAdapter.LeaveViewHolder holder, int position) {

        Leave leaveItem = leaveList.get(position);

        if(leaveItem != null && leaveItem.getId() != null &&  !leaveItem.getId().isEmpty()) {
            List<String> periodSpinnerList = new ArrayList<>();
            periodSpinnerList.add(LeaveStatus.REQUESTED.toString());
            periodSpinnerList.add(LeaveStatus.APPROVED.toString());
            periodSpinnerList.add(LeaveStatus.DISAPPROVED.toString());

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item, periodSpinnerList);
            holder.leaveStatusSpinner.setAdapter(dataAdapter);

            DBHelper dbHelper = DBHelper.getInstance(context);
            User nurse = dbHelper.getUserById(leaveItem.getUserId());

            holder.idTextView.setText(nurse.getUserId());
            holder.userNameTextView.setText(nurse.getName() + " " + nurse.getSurname());
            holder.periodTextView.setText(leaveItem.getStartDate()+ "-" + leaveItem.getEndDate());

            if(LeaveStatus.valueOf(leaveItem.getLeaveStatus()).equals(LeaveStatus.APPROVED) || LeaveStatus.valueOf(leaveItem.getLeaveStatus()).equals(LeaveStatus.DISAPPROVED)){
                holder.leaveStatusSpinner.setEnabled(false);
            }

            holder.leaveStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    Object item = holder.leaveStatusSpinner.getSelectedItem();
                    leaveItem.setLeaveStatus(item.toString().toUpperCase());
                    dbHelper.saveLeave(leaveItem);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            ArrayAdapter myAdap = (ArrayAdapter) holder.leaveStatusSpinner.getAdapter(); //cast to an ArrayAdapter
            int spinnerPosition = myAdap.getPosition(leaveItem.getLeaveStatus());
            //set the default according to value
            holder.leaveStatusSpinner.setSelection(spinnerPosition);
        }
    }

    @Override
    public int getItemCount() {
        return leaveList.size();
    }

}
