package com.example.angleseahospitalapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.angleseahospitalapp.R;

import java.util.Objects;

public class ListUserActivity extends AppCompatActivity {

    ImageButton editUserBtn;
    Button newUserBtn;
    Button manageGroupsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);

        Toolbar toolbar = findViewById(R.id.toolbarAddShit);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editUserBtn = findViewById(R.id.editUserBtn);
        newUserBtn = findViewById(R.id.addUserBtn);
        manageGroupsBtn = findViewById(R.id.addUserGroupBtn);

    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        startActivity(homeIntent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}