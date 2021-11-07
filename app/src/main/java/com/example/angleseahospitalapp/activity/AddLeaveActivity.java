package com.example.angleseahospitalapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.angleseahospitalapp.R;
import com.example.angleseahospitalapp.db.DBHelper;
import com.example.angleseahospitalapp.db.LeaveDBHelper;
import com.example.angleseahospitalapp.model.Leave;
import com.example.angleseahospitalapp.model.LeaveStatus;
import com.example.angleseahospitalapp.model.User;
import com.example.angleseahospitalapp.model.Util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Objects;

import com.example.angleseahospitalapp.model.*;

public class AddLeaveActivity extends AppCompatActivity {

    EditText startDateEditText;
    EditText endDateEditText;

    private DatePickerDialog.OnDateSetListener mDateStartSetListener;
    private DatePickerDialog.OnDateSetListener mDateEndSetListener;
    private ImageButton startDateBtn;
    private ImageButton endDateBtn;

    DBHelper dbHelper = DBHelper.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_leave);

        Toolbar toolbar = findViewById(R.id.toolbarLeaveRequest);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Drawable upArrow = getDrawable(R.drawable.ic_baseline_arrow_back_24);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        startDateEditText = findViewById(R.id.startDateEditText);
        startDateEditText.setEnabled(false);

        endDateEditText = findViewById(R.id.endDateEditText);
        endDateEditText.setEnabled(false);

        startDateBtn =  findViewById(R.id.startDateCalendarBtn);
        startDateBtn.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    AddLeaveActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateStartSetListener,
                    year,month,day);

            //Get yesterday's date
            Calendar calendar = Calendar.getInstance();
            dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        mDateStartSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = month + "/" + Util.formatDayDate(day) + "/" + year;
            startDateEditText.setText(date);
        };

        endDateBtn =  findViewById(R.id.endDateCalendarBtn);
        endDateBtn.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    AddLeaveActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateEndSetListener,
                    year,month,day);

            //Get yesterday's date
            Calendar calendar = Calendar.getInstance();
            dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        mDateEndSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date =Util.formatDayDate(day)  + "/" + month + "/" + year;
            endDateEditText.setText(date);
        };

        Button saveBtn = findViewById(R.id.saveLeaveBtn);
        saveBtn.setOnClickListener(view -> {
            if(!startDateEditText.getText().toString().isEmpty() && !endDateEditText.getText().toString().isEmpty()){
                save();
            }else{
                Toast.makeText(AddLeaveActivity.this,"Both dates must be informed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void save() {

        Leave leave = new Leave();
        leave.setStartDate(startDateEditText.getText().toString());
        leave.setEndDate(endDateEditText.getText().toString());
        leave.setUserId(dbHelper.getUserByPin(load()).getUserId());
        leave.setLeaveStatus(LeaveStatus.REQUESTED.toString());
        dbHelper.saveLeave(leave);

        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(this, NurseLeaveActivity.class);
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