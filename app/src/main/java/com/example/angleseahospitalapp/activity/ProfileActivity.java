package com.example.angleseahospitalapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.angleseahospitalapp.R;
import com.example.angleseahospitalapp.db.DBHelper;
import com.example.angleseahospitalapp.model.User;
import com.example.angleseahospitalapp.model.Util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import com.example.angleseahospitalapp.model.*;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileNameTextView;
    private TextView userIdTextView;
    private TextView phoneNumberTextView;
    private TextView emailProfileTextView;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbarUserProfile);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        profileNameTextView = findViewById(R.id.profileNameTextView);
        userIdTextView = findViewById(R.id.userIdTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        emailProfileTextView = findViewById(R.id.emailProfileTextView);
        profileImage = findViewById(R.id.profileImage);

        DBHelper helper = DBHelper.getInstance(this);

        User user = helper.getUserByPin(load());

        profileNameTextView.setText(user.getName() +" " + user.getSurname());
        userIdTextView.setText(user.getUserId());
        phoneNumberTextView.setText(user.getPhoneNumber());
        emailProfileTextView.setText(user.getEmail());

        if(user.getPhoto() != null){
            profileImage.setImageBitmap(Util.getCircleBitmap(BitmapFactory.decodeByteArray(user.getPhoto(), 0, user.getPhoto().length),100));
        }

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

    public String load() {
        FileInputStream fis = null;

        try {
            fis = openFileInput(SystemConstants.FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            return  sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}