package com.example.angleseahospitalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.angleseahospitalapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //To remove the action bar
        getSupportActionBar().hide();

        Intent intent = new Intent(this, PinScreenActivity.class);

        final Handler handler = new Handler();
        handler.postDelayed(() -> startActivity(intent), 2000);

    }
}