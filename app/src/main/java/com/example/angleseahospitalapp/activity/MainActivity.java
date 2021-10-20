package com.example.angleseahospitalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.angleseahospitalapp.R;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //To remove the action bar
        getSupportActionBar().hide();

        ImageView videoView = findViewById(R.id.splashImage);
        videoView.setImageResource(R.raw.logo_icon);

        Intent intent = new Intent(this, PinScreenActivity.class);

        final Handler handler = new Handler();
        handler.postDelayed(() -> startActivity(intent), 2000);

    }
}