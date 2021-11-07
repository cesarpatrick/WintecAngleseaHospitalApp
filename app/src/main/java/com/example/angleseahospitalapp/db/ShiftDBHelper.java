package com.example.angleseahospitalapp.db;

import androidx.annotation.NonNull;

import com.example.angleseahospitalapp.model.Shift;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShiftDBHelper {
    private DatabaseReference mStaffDBRef = FirebaseDatabase.getInstance().getReference("Shifts");

    public ShiftDBHelper(){}

    public void uploadShift(Shift shiftUpload){
        String uploadId = mStaffDBRef.push().getKey();
        mStaffDBRef.child(uploadId).setValue(shiftUpload);
    }

    public ArrayList<Shift> retrieveShift(String staffIDKey){
        ArrayList<Shift> shiftList = new ArrayList<>();
        mStaffDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shiftList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    if (postSnapshot.getValue(Shift.class).getStaffID().equals(staffIDKey)){
                        Shift shift = postSnapshot.getValue(Shift.class);
                        shift.setShiftId(postSnapshot.getKey());
                        shiftList.add(shift);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });
        return shiftList;
    }

    public ArrayList<Shift> retrieveAllShifts(){
        ArrayList<Shift> shiftList = new ArrayList<>();
        mStaffDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shiftList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    Shift shift = postSnapshot.getValue(Shift.class);
                    shift.setShiftId(postSnapshot.getKey());
                    shiftList.add(shift);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });
        return shiftList;
    }

}
