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
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angleseahospitalapp.R;
import com.example.angleseahospitalapp.db.DBHelper;
import com.example.angleseahospitalapp.model.Shift;
import com.example.angleseahospitalapp.model.ShiftPeriod;
import com.example.angleseahospitalapp.model.User;
import com.example.angleseahospitalapp.model.UserGroup;
import com.example.angleseahospitalapp.model.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class AddShiftActivity extends AppCompatActivity {

    private EditText dateEditText;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private ImageButton calendarBtn;
    private Spinner userSpinner;
    private Spinner periodSpinner;
    private Spinner staffGroupSpinner;
    private CheckBox weekendCheckBox;
    private CheckBox publicHolidayCheckBox;

    DBHelper dbHelper = DBHelper.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shift);

        Toolbar toolbar = findViewById(R.id.toolbarAddShit);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userSpinner = findViewById(R.id.staffSpinner);
        periodSpinner = findViewById(R.id.shiftSpinner);
        staffGroupSpinner = findViewById(R.id.staffGroupSpinner);

        weekendCheckBox = findViewById(R.id.weekendCheckBox);
        publicHolidayCheckBox = findViewById(R.id.publicHolidayCheckBox);

        List<User> users = dbHelper.getAllUsers();
        List<String> staffSpinnerList = new ArrayList<>();
        List<String> periodSpinnerList = new ArrayList<>();
        periodSpinnerList.add("");
        periodSpinnerList.add(ShiftPeriod.MORNING.toString());
        periodSpinnerList.add(ShiftPeriod.AFTERNOON.toString());
        periodSpinnerList.add(ShiftPeriod.NIGHT.toString());

        List<String> groupSpinnerList = new ArrayList<>();
        groupSpinnerList.add("");
        groupSpinnerList.add(UserGroup.WARD.toString());
        groupSpinnerList.add(UserGroup.OT.toString());
        groupSpinnerList.add(UserGroup.PACU.toString());

        staffSpinnerList.add("");
        for (User user: users) {
            staffSpinnerList.add(user.getUserId() + "-" + user.getName() +" "+ user.getSurname());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, staffSpinnerList);
        userSpinner.setAdapter(dataAdapter);

        ArrayAdapter<String> dataAdapterGroup = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, groupSpinnerList);
        staffGroupSpinner.setAdapter(dataAdapterGroup);

        ArrayAdapter<String> dataAdapterPeriod = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, periodSpinnerList);
        periodSpinner.setAdapter(dataAdapterPeriod);

        final Drawable upArrow = getDrawable(R.drawable.ic_baseline_arrow_back_24);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        dateEditText = findViewById(R.id.dateInputTextView);
        calendarBtn =  findViewById(R.id.calendarBtn);

        calendarBtn.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    AddShiftActivity.this,
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

            String date = Util.formatDayDate(day) + "/" + Util.formatDayDate(month)  + "/" + year;
            dateEditText.setText(date);
            dateEditText.setEnabled(false);
        };

        Button saveBtn = findViewById(R.id.addShiftBtn);

        saveBtn.setOnClickListener(view -> save());
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

    public void save(){

        DBHelper helper = DBHelper.getInstance(this);

        if(validateFields())
        {
            if(!TextUtils.isEmpty(userSpinner.getSelectedItem().toString())){
                String[] array = userSpinner.getSelectedItem().toString().split("-");

                Shift shift =  helper.getShiftByUserIdByDate(array[0], dateEditText.getText().toString());

                if(shift.getShiftId() == null) {

                    shift = new Shift();
                    shift.setStaffID(array[0]);
                    shift.setDate(dateEditText.getText().toString());
                    shift.setPeriod(periodSpinner.getSelectedItem().toString());
                    shift.setWeekend(weekendCheckBox.isChecked());
                    shift.setPublicHoliday(publicHolidayCheckBox.isChecked());

                    helper.saveShift(shift);

                    //Set all the fields back to empty
                    cleanFields();
                    Toast.makeText(this, "Shift Saved", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "This nurse has a shift for this day already", Toast.LENGTH_LONG).show();
                }
            }

            if(!TextUtils.isEmpty(staffGroupSpinner.getSelectedItem().toString())){

                List<User> users = dbHelper.getAllUsersByGroup(UserGroup.valueOf(staffGroupSpinner.getSelectedItem().toString()));

                for (User user: users) {

                    Shift shift =  helper.getShiftByUserIdByDate(user.getUserId(), dateEditText.getText().toString());

                    if(shift.getShiftId() == null) {

                        shift = new Shift();
                        shift.setStaffID(user.getUserId());
                        shift.setDate(dateEditText.getText().toString());
                        shift.setPeriod(periodSpinner.getSelectedItem().toString());
                        shift.setWeekend(weekendCheckBox.isChecked());
                        shift.setPublicHoliday(publicHolidayCheckBox.isChecked());

                        helper.saveShift(shift);
                    }else{
                        Toast.makeText(this, "The nurses on this group has a shift for this day already", Toast.LENGTH_LONG).show();
                        break;
                    }
                }

                Toast.makeText(this, "Shifts Saved", Toast.LENGTH_LONG).show();

            }

        }
    }

    private void cleanFields(){
        dateEditText.setText("");
        periodSpinner.setSelection(0);
        userSpinner.setSelection(0);
        weekendCheckBox.setChecked(false);
        publicHolidayCheckBox.setChecked(false);
    }

    private boolean validateFields(){

        if(TextUtils.isEmpty(userSpinner.getSelectedItem().toString()) && TextUtils.isEmpty(staffGroupSpinner.getSelectedItem().toString()))
        {
            ((TextView)userSpinner.getSelectedView()).setError("Please select the nurse or inform the group");
            return false;
        }

        if(TextUtils.isEmpty(periodSpinner.getSelectedItem().toString()))
        {
            ((TextView)periodSpinner.getSelectedView()).setError("Please select the shift period");
            return false;
        }

        if(TextUtils.isEmpty(dateEditText.getText()))
        {
            dateEditText.setError("Please enter the shift date");
            return false;
        }

        return true;
    }
}