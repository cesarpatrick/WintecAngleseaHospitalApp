package com.example.angleseahospitalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.angleseahospitalapp.R;

public class AddNurseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nurse);

        //To remove the action bar
        getSupportActionBar().hide();
    }
}