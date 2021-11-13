package com.example.angleseahospitalapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.angleseahospitalapp.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Objects;

import com.example.angleseahospitalapp.db.DBHelper;
import com.example.angleseahospitalapp.model.*;

public class ReportAProblemActivity extends AppCompatActivity {

    private EditText descriptionEditText;

    DBHelper dbHelper = DBHelper.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);

        Toolbar toolbar = findViewById(R.id.notificationToolBar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        descriptionEditText = findViewById(R.id.messageEditText);

        Button sendNotificationsBtn = findViewById(R.id.sendNotificationsBtn);
        sendNotificationsBtn.setOnClickListener(view -> save());
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

    private void save(){

        if(TextUtils.isEmpty(descriptionEditText.getText()))
        {
            descriptionEditText.setError("Please write down the issues that you are having");
        }else {
            User user = dbHelper.getUserByPin(load());

            Notification notification = new Notification();
            notification.setDescription(descriptionEditText.getText().toString());
            notification.setUserId(user.getUserId());
            notification.setDate(Util.convertDateToString(new Date()));

            dbHelper.saveNotification(notification);

            Toast.makeText(this, "The notification was sent to your manager", Toast.LENGTH_LONG).show();

            descriptionEditText.getText().clear();
        }
    }
}