package com.example.angleseahospitalapp.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angleseahospitalapp.R;
import com.example.angleseahospitalapp.db.DBHelper;
import com.example.angleseahospitalapp.model.Shift;
import com.example.angleseahospitalapp.model.User;
import com.example.angleseahospitalapp.model.Util;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ShiftDashboardActivity extends AppCompatActivity {

    private ImageButton startDateCalendarBtn;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private EditText dateEditText;

    private TextView dayName1TextView;
    private TextView dayName2TextView;
    private TextView dayName3TextView;
    private TextView dayName4TextView;
    private TextView dayName5TextView;

    private Button calendar1Btn;
    private Button calendar2Btn;
    private Button calendar3Btn;
    private Button calendar4Btn;
    private Button calendar5Btn;

    DBHelper dbHelper = DBHelper.getInstance(this);

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbarShiftDashboard);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Drawable upArrow = getDrawable(R.drawable.ic_baseline_arrow_back_24);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        FloatingActionButton addShiftFab = findViewById(R.id.addShiftFab);

        dateEditText = findViewById(R.id.dateEditText);

        dayName1TextView = findViewById(R.id.dayName1TextView);
        dayName2TextView = findViewById(R.id.dayName2TextView);
        dayName3TextView = findViewById(R.id.dayName3TextView);
        dayName4TextView = findViewById(R.id.dayName4TextView);
        dayName5TextView = findViewById(R.id.dayName5TextView);

        calendar1Btn = findViewById(R.id.calendar1Btn);
        calendar2Btn = findViewById(R.id.calendar2Btn);
        calendar3Btn = findViewById(R.id.calendar3Btn);
        calendar4Btn = findViewById(R.id.calendar4Btn);
        calendar5Btn = findViewById(R.id.calendar5Btn);

        dayName1TextView.setText(Util.getDayNameText(Util.getMinusDay(new Date(), 2)));
        dayName2TextView.setText(Util.getDayNameText(Util.getMinusDay(new Date(), 1)));
        dayName3TextView.setText(Util.getDayNameText(new Date()));
        dayName4TextView.setText(Util.getDayNameText(Util.getPlusDay(new Date(), 1)));
        dayName5TextView.setText(Util.getDayNameText(Util.getPlusDay(new Date(), 2)));

        calendar1Btn.setText(Util.getMinusDayString(new Date(), 2));
        calendar2Btn.setText(Util.getMinusDayString(new Date(), 1));
        calendar3Btn.setText(Util.getDay(new Date()));
        calendar4Btn.setText(Util.getPlusDayString(new Date(), 1));
        calendar5Btn.setText(Util.getPlusDayString(new Date(), 2));

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        dateEditText.setText(Util.getMonthNameText(new Date()) + " - "+ Util.getYear(new Date()));

        ArrayList<Shift> shiftItems = new ArrayList<>(dbHelper.getAllShiftByDate(Util.convertDateToString(new Date())));

        RecyclerView mRecyclerView = findViewById(R.id.shiftDashboardRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter mAdapter = new ShiftDashboardAdapter(shiftItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        startDateCalendarBtn =  findViewById(R.id.startDateCalendarBtn);
        startDateCalendarBtn.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    ShiftDashboardActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener,
                    year,month,day);

            //Get yesterday's date
            Calendar calendar = Calendar.getInstance();
            dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        mDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String dateString = Util.formatDayDate(day)  + "/" + Util.formatDayDate(month) + "/" + year;
            Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            dateEditText.setText(Util.getMonthNameText(Util.convertStringToDate(dateString)) + "-"+ year);

            Date date = Util.convertStringToDate(dateString);

            dayName1TextView.setText(Util.getDayNameText(Util.getMinusDay(date, 2)));
            dayName2TextView.setText(Util.getDayNameText(Util.getMinusDay(date, 1)));
            dayName3TextView.setText(Util.getDayNameText(date));
            dayName4TextView.setText(Util.getDayNameText(Util.getPlusDay(date, 1)));
            dayName5TextView.setText(Util.getDayNameText(Util.getPlusDay(date, 2)));

            calendar1Btn.setText(Util.getMinusDayString(date, 2));
            calendar2Btn.setText(Util.getMinusDayString(date, 1));
            calendar3Btn.setText(Util.getDay(date));
            calendar4Btn.setText(Util.getPlusDayString(date, 1));
            calendar5Btn.setText(Util.getPlusDayString(date, 2));

            loadShifts(mRecyclerView, mLayoutManager, calendar3Btn.getText().toString());
        };

        calendar1Btn.setOnClickListener(view -> loadShifts(mRecyclerView, mLayoutManager, calendar1Btn.getText().toString()));
        calendar2Btn.setOnClickListener(view -> loadShifts(mRecyclerView, mLayoutManager, calendar2Btn.getText().toString()));
        calendar3Btn.setOnClickListener(view -> loadShifts(mRecyclerView, mLayoutManager, calendar3Btn.getText().toString()));
        calendar4Btn.setOnClickListener(view -> loadShifts(mRecyclerView, mLayoutManager, calendar4Btn.getText().toString()));
        calendar5Btn.setOnClickListener(view -> loadShifts(mRecyclerView, mLayoutManager, calendar5Btn.getText().toString()));

        Intent addShiftIntent = new Intent(this, AddShiftActivity.class);
        addShiftFab.setOnClickListener(view -> startActivity(addShiftIntent));

        ImageButton downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setOnClickListener(view -> {

            String day = "01";
            String[] mothYear = dateEditText.getText().toString().split("-");

            Date dateMonth = null;
            try {
                dateMonth = new SimpleDateFormat("MMMM").parse(mothYear[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateMonth);
            cal.add(Calendar.MONTH, 1);
            String month = cal.get(Calendar.MONTH)+"";
            String year = mothYear[1];

            String from = day+"/"+month+"/"+year.trim();
            String to = Util.convertDateTimeToString(Util.getLastDayOfMonth(from));

            generateXlsFile(from, to);
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


    private void loadShifts(RecyclerView mRecyclerView, RecyclerView.LayoutManager mLayoutManager, String btnText){


        String day = btnText;
        String[] mothYear = dateEditText.getText().toString().split("-");

        Date dateMonth = null;
        try {
            dateMonth = new SimpleDateFormat("MMMM").parse(mothYear[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateMonth);
        cal.add(Calendar.MONTH, 1);
        String month = cal.get(Calendar.MONTH)+"";
        String year = mothYear[1];

        String date = day+"/"+month+"/"+year.trim();

        RecyclerView.Adapter mAdapter = new ShiftDashboardAdapter(new ArrayList<>(dbHelper.getAllShiftByDate(date)));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void generateXlsFile(String from, String to){

        File filePath = new File(Environment.getExternalStorageDirectory() + "/Anglesea Roster-"+ Util.getMonthNameText(Util.convertStringToDate(from))+ "-"+Util.getYearText(Util.convertStringToDate(from)) +".xlsx");

        List<Shift> shiftList = dbHelper.getShiftByPeriod(from, to);

        String[] headers = new String[] { "Nurse", "Clock in", "Clock Out", "Duration" };

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Aglesea Roster");

        int row = 0;

        HSSFRow hssfRowTitle = hssfSheet.createRow(row);
        HSSFCell title1 = hssfRowTitle.createCell(0);
        title1.setCellValue("Nurse");

        HSSFCell title2 = hssfRowTitle.createCell(1);
        title2.setCellValue("Clock in");

        HSSFCell title3 = hssfRowTitle.createCell(2);
        title3.setCellValue("Clock Out");

        HSSFCell title4 = hssfRowTitle.createCell(3);
        title4.setCellValue("Duration");

        for(int i=0; i < shiftList.size(); i++){

            row++;
            Shift s = shiftList.get(i);
            User user = dbHelper.getUserById(s.getStaffID());

            HSSFRow hssfRow = hssfSheet.createRow(row);
            HSSFCell hssfCell = hssfRow.createCell(0);
            hssfCell.setCellValue(user.getName() +" " + user.getSurname());

            HSSFCell hssfCell2 = hssfRow.createCell(1);
            hssfCell2.setCellValue(s.getClockInTime());

            HSSFCell hssfCell3 = hssfRow.createCell(2);
            hssfCell3.setCellValue(s.getClockOutTime());

            HSSFCell hssfCell4 = hssfRow.createCell(3);
            hssfCell4.setCellValue(Util.shiftDuration(s.getClockInTime(), s.getClockOutTime()));
        }

        try {
            if (!filePath.exists()){
                filePath.createNewFile();
            }

            FileOutputStream fileOutputStream= new FileOutputStream(filePath);
            hssfWorkbook.write(fileOutputStream);

            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }

            Toast.makeText(this,"The file was saved on " + filePath, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}