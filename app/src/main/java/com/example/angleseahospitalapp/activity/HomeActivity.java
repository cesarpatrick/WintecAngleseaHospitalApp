package com.example.angleseahospitalapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.angleseahospitalapp.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.angleseahospitalapp.databinding.ActivityHomeBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private ActivityHomeBinding binding;

    private DrawerLayout drawerLayout;

    private MaterialButton startShiftBtn;

    private long pauseOffset;
    private boolean running;


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
        navigationView.setNavigationItemSelectedListener(this);

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


}