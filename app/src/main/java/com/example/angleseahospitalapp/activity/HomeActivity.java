package com.example.angleseahospitalapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.angleseahospitalapp.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.angleseahospitalapp.databinding.ActivityHomeBinding;
import com.example.angleseahospitalapp.db.DBHelper;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.angleseahospitalapp.model.*;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private ActivityHomeBinding binding;

    private DrawerLayout drawerLayout;

    private RelativeLayout loggedRelativeLayout;
    private RelativeLayout timeRelativeLayout;

    private User user;

    private TextView shiftStartTimeTextView;
    private TextView durationTimeTextView;
    private TextView homeTimeTextView;

    private SwitchCompat homeSwitch;

    private Button clockInBtn;
    private Button clockOutBtn;

    private boolean isClockedIn = false;

    private DBHelper dbHelper = DBHelper.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = dbHelper.getUserByPin(load());

        if(isClockedIn){
            Shift shift = dbHelper.getShiftByUserIdByDate(user.getUserId(), Util.convertDateToString(new Date()));

            Thread thread = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(1000);
                            runOnUiThread(() -> durationTimeTextView.setText(Util.shiftDuration(shift.getClockInTime(), Util.convertDateTimeToString(new Date()))));
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };

            thread.start();
        }

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(!isClockedIn) {

            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(1000);
                            runOnUiThread(() -> updateTimeTextView());
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };

            t.start();
        }

        //Set my toolbar because i removed the default one
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        shiftStartTimeTextView = findViewById(R.id.shiftStartTime);
        durationTimeTextView = findViewById(R.id.durationTimeTextView);

        drawerLayout = findViewById(R.id.drawerLayout);

        homeSwitch = findViewById(R.id.homeSwitch);
        homeSwitch.setEnabled(false);

        NavigationView navigationView = findViewById(R.id.navView);

        TextView userName = findViewById(R.id.userName);
        userName.setText(user.getName() +" "+user.getSurname());

        navigationView.setNavigationItemSelectedListener(this);

        loggedRelativeLayout = findViewById(R.id.relativeLayout);
        loggedRelativeLayout.setVisibility(View.INVISIBLE);

        timeRelativeLayout = findViewById(R.id.relativeLayout2);

        if(user.getRole().equals(Role.MANAGER.toString())){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_manager);
        }else{
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu);
        }

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        clockInBtn = findViewById(R.id.clockInBtn);
        clockOutBtn = findViewById(R.id.clockOutBtn);

        clockInBtn.setVisibility(View.INVISIBLE);
        clockOutBtn.setVisibility(View.INVISIBLE);

        if(Role.valueOf(user.getRole().toUpperCase()).equals(Role.NURSE)){
            Shift shift = dbHelper.getShiftByUserIdByDate(user.getUserId(), Util.convertDateToString(new Date()));

            if(shift.getShiftId() != null && !shift.getShiftId().isEmpty()){
                clockInBtn.setVisibility(View.VISIBLE);
                if(shift.getClockInTime() != null && !shift.getClockInTime().isEmpty()){
                    clockInBtn.setVisibility(View.INVISIBLE);
                    clockOutBtn.setVisibility(View.VISIBLE);

                    shiftStartTimeTextView.setText(Util.getTimeFromStringDate(shift.getClockInTime()));
                    durationTimeTextView.setText("00:00");
                    isClockedIn = true;

                    loggedRelativeLayout.setVisibility(View.VISIBLE);
                    timeRelativeLayout.setVisibility(View.INVISIBLE);
                }

            }else{
                clockInBtn.setVisibility(View.INVISIBLE);
                clockOutBtn.setVisibility(View.INVISIBLE);
            }
        }else{
            clockInBtn.setVisibility(View.INVISIBLE);
        }

        clockInBtn.setOnClickListener(view -> clockIn());
        clockOutBtn.setOnClickListener(view -> clockOut());
    }

    //To close the navigation draw on back pressed
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.navHelp:
                Intent helpIntent = new Intent(this, HelpActivity.class);
                startActivity(helpIntent);
                break;
            case R.id.navShifts:
                Intent shiftIntent = new Intent(this, NurseShiftActivity.class);
                startActivity(shiftIntent);
                break;
            case R.id.navMyLeave:
                Intent myLeaveIntent = new Intent(this, NurseLeaveActivity.class);
                startActivity(myLeaveIntent);
                break;
            case R.id.addUser:
                Intent addUserIntent = new Intent(this, AddUserActivity.class);
                startActivity(addUserIntent);
                break;
            case R.id.addShift:
                Intent addShiftIntent = new Intent(this, AddShiftActivity.class);
                startActivity(addShiftIntent);
                break;
            case R.id.navLeaveManager:
                Intent leaveManagerIntent = new Intent(this, ListLeaveActivity.class);
                startActivity(leaveManagerIntent);
                break;
            case R.id.navLogOut:
                save("");
                Intent pinActivity = new Intent(this, PinScreenActivity.class);
                startActivity(pinActivity);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        //To select the item
        return true;
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

    public void save(String text) {

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(SystemConstants.FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateTimeTextView(){
        homeTimeTextView = findViewById(R.id.homeTime);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String strDate = sdf.format(cal.getTime());
        homeTimeTextView.setText(strDate);
    }

    private void clockIn(){
        Shift shift = dbHelper.getShiftByUserIdByDate(user.getUserId(), Util.convertDateToString(new Date()));

        if(shift.getShiftId() != null && !shift.getShiftId().isEmpty()){

            shift.setClockInTime(Util.convertDateTimeToString(new Date()));

            dbHelper.saveShift(shift);

            shiftStartTimeTextView.setText(homeTimeTextView.getText());
            durationTimeTextView.setText("00:00");

            loggedRelativeLayout.setVisibility(View.VISIBLE);
            timeRelativeLayout.setVisibility(View.INVISIBLE);

            clockInBtn.setVisibility(View.INVISIBLE);
            clockOutBtn.setVisibility(View.VISIBLE);
        }
    }

    private void clockOut(){

        Shift shift = dbHelper.getShiftByUserIdByDate(user.getUserId(), Util.convertDateToString(new Date()));

        if(shift.getShiftId() != null && !shift.getShiftId().isEmpty()){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Clock out");
            builder.setMessage("Are you sure you want to clock out ?");
            builder.setPositiveButton("Confirm",
                    (dialog, which) -> {

                        shift.setClockOutTime(Util.convertDateTimeToString(new Date()));

                        dbHelper.saveShift(shift);

                        shiftStartTimeTextView.setText(homeTimeTextView.getText());

                        loggedRelativeLayout.setVisibility(View.INVISIBLE);
                        timeRelativeLayout.setVisibility(View.VISIBLE);

                        clockInBtn.setVisibility(View.INVISIBLE);
                        clockOutBtn.setVisibility(View.INVISIBLE);

            });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {

            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

}