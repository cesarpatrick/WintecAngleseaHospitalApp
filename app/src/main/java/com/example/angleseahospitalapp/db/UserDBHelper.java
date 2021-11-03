package com.example.angleseahospitalapp.db;

import androidx.annotation.NonNull;

import com.example.angleseahospitalapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserDBHelper {
    private DatabaseReference mUserDBRef = FirebaseDatabase.getInstance().getReference("Users");

    public UserDBHelper(){}

    public void saveUser(User user){
        String uploadId = mUserDBRef.push().getKey();
        mUserDBRef.child(uploadId).setValue(user);
    }

    public ArrayList<User> getUserByKey(String userKey){
        ArrayList<User> users = new ArrayList<>();
        mUserDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    if (postSnapshot.getValue(User.class).getmKey().equals(userKey)){
                        User user = postSnapshot.getValue(User.class);
                        user.setmKey(postSnapshot.getKey());
                        users.add(user);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });
        return users;
    }

    public User getUserByPin(String pin){

        final User[] user = {new User()};
        mUserDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    if (postSnapshot.getValue(User.class).getPin().equals(pin)){
                        User mUser = postSnapshot.getValue(User.class);
                        //User.setmKey(postSnapshot.getKey());
                        user[0] = (mUser);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });

        return user[0];
    }

    public ArrayList<User> getAllUsers(){
        ArrayList<User> users = new ArrayList<>();
        mUserDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    User user = postSnapshot.getValue(User.class);
                    user.setmKey(postSnapshot.getKey());
                    users.add(user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });
        return users;
    }

}
