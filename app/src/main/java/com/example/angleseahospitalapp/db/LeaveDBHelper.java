package com.example.angleseahospitalapp.db;

import androidx.annotation.NonNull;

import com.example.angleseahospitalapp.model.Leave;
import com.example.angleseahospitalapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LeaveDBHelper {
    private DatabaseReference mLeaveDBRef = FirebaseDatabase.getInstance().getReference("Leave");

    public LeaveDBHelper(){}

    public void saveLeave(Leave leave){
        String uploadId = mLeaveDBRef.push().getKey();
        mLeaveDBRef.child(uploadId).setValue(leave);
    }

    public ArrayList<Leave> getLeaveByKey(String leaveKey){
        ArrayList<Leave> leaveList = new ArrayList<>();
        mLeaveDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                leaveList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    if (postSnapshot.getValue(Leave.class).getId().equals(leaveKey)){
                        Leave leave = postSnapshot.getValue(Leave.class);
                        leave.setId(postSnapshot.getKey());
                        leaveList.add(leave);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });
        return leaveList;
    }

    public ArrayList<User> getAllLeaves(){
        ArrayList<User> users = new ArrayList<>();
        mLeaveDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    User user = postSnapshot.getValue(User.class);
                    user.setUserId(postSnapshot.getKey());
                    users.add(user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });
        return users;
    }

}
