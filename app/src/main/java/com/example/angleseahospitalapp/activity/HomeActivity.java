package com.example.angleseahospitalapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.angleseahospitalapp.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.angleseahospitalapp.databinding.ActivityHomeBinding;
import com.example.angleseahospitalapp.db.DBHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.angleseahospitalapp.model.*;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private ActivityHomeBinding binding;

    private DrawerLayout drawerLayout;

    private MaterialButton startShiftBtn;

    private long pauseOffset;
    private boolean running;

    DBHelper dbHelper = DBHelper.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Set my toolbar because i removed the default one
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);

        NavigationView navigationView = findViewById(R.id.navView);

        User user = dbHelper.getUserByPin(load());
        TextView userName = findViewById(R.id.userName);
        userName.setText(user.getName() +" "+user.getSurname());


        TextView homeTimeTextView = findViewById(R.id.homeTime);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String strDate = sdf.format(cal.getTime());
        homeTimeTextView.setText(strDate);

        navigationView.setNavigationItemSelectedListener(this);

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


        Intent addNurseIntent = new Intent(this, AddUserActivity.class);

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
                Intent leaveManagerIntent = new Intent(this, AddShiftActivity.class);
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

}