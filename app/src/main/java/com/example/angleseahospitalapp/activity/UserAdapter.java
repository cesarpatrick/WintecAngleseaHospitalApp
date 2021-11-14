package com.example.angleseahospitalapp.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.angleseahospitalapp.R;
import com.example.angleseahospitalapp.model.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    public ArrayList<User> userList;
    public Context context;

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView userIdTextView;
        public TextView userNameTextView;
        public TextView userGroupTextView;

        public RelativeLayout userItemRelativeLayout;

        public UserViewHolder(View itemView) {
            super(itemView);
            userIdTextView = itemView.findViewById(R.id.userIdTextView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            userGroupTextView = itemView.findViewById(R.id.userGroupTextView);
            userItemRelativeLayout = itemView.findViewById(R.id.userItemRelativeLayout);
        }
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        UserViewHolder svh = new UserViewHolder(v);
        return svh;
    }

    public UserAdapter(Context context, ArrayList<User> list) {
        userList = list;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(UserAdapter.UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.userIdTextView.setText(user.getUserId());
        holder.userNameTextView.setText(user.getName() + " " + user.getSurname());
        holder.userGroupTextView.setText(user.getGroup());

        Intent addUserIntent = new Intent(context, AddUserActivity.class);
        holder.userItemRelativeLayout.setOnClickListener(view -> {
            addUserIntent.putExtra("USER_ID", user.getUserId());
            context.startActivity(addUserIntent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
