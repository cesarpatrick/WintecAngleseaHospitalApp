package com.example.angleseahospitalapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.angleseahospitalapp.R;
import com.example.angleseahospitalapp.model.Role;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NurseLeaveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse_leave);

        Toolbar toolbar = findViewById(R.id.toolbarLeave);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Drawable upArrow = getDrawable(R.drawable.ic_baseline_arrow_back_24);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Spinner period = findViewById(R.id.periodSpinner);

        List<String> periods = new ArrayList<>();
        periods.add("This Year");
        periods.add("Last 6 Month");
        periods.add("Last Month");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, periods);
        period.setAdapter(dataAdapter);

        FloatingActionButton fab = findViewById(R.id.addLeave);
        fab.setOnClickListener(view -> {
            Intent addLeaveIntent = new Intent(this, AddLeaveActivity.class);
            startActivity(addLeaveIntent);
        });
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