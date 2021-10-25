package com.example.angleseahospitalapp.activity;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ShiftDBHelper {
    private DatabaseReference mStaffDBRef = FirebaseDatabase.getInstance().getReference("Shifts");

    public ShiftDBHelper(){}

    public void uploadShift(String staffID, ShiftItem shiftUpload){
        mStaffDBRef.child(staffID).setValue(shiftUpload);
    }

    public ArrayList<ShiftItem> retrieveShift(String staffIDKey){
        ArrayList<ShiftItem> shiftList = new ArrayList<>();

        mStaffDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shiftList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    if (postSnapshot.getKey().equals(staffIDKey)){
                        ShiftItem shift = postSnapshot.getValue(ShiftItem.class);
                        shift.setStaffID(postSnapshot.getKey());
                        shiftList.add(shift);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        return shiftList;
    }

    public ArrayList<ShiftItem> retrieveAllShifts(){
        ArrayList<ShiftItem> shiftList = new ArrayList<>();

        mStaffDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shiftList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    ShiftItem shift = postSnapshot.getValue(ShiftItem.class);
                    shift.setStaffID(postSnapshot.getKey());
                    shiftList.add(shift);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return shiftList;
    }

}