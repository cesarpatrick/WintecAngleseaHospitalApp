package com.example.angleseahospitalapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.angleseahospitalapp.R;
import com.example.angleseahospitalapp.model.ShiftItem;

import java.util.ArrayList;
import java.util.Objects;

public class NurseShiftActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse_shift);

        Toolbar toolbar = findViewById(R.id.toolbarShift);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Drawable upArrow = getDrawable(R.drawable.ic_baseline_arrow_back_24);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        ArrayList<ShiftItem> shiftItems = new ArrayList<>();
        shiftItems.add(new ShiftItem("111222333", "21", "08:00 - 06:00", "OT Team (OT 3)","Sunday"));
        shiftItems.add(new ShiftItem("111333222","22", "03:00 - 12:00", "OT Team (OT 3)","Tuesday"));
        shiftItems.add(new ShiftItem("222111333","23", "07:00 - 05:00", "OT Team (OT 3)", "Friday"));

        RecyclerView mRecyclerView = findViewById(R.id.shiftsRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter mAdapter = new ShiftAdapter(shiftItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

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