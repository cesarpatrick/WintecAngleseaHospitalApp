package com.example.angleseahospitalapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angleseahospitalapp.R;
import com.example.angleseahospitalapp.db.DBHelper;
import com.example.angleseahospitalapp.model.*;
import com.example.angleseahospitalapp.model.User;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SendEmailActivity extends AppCompatActivity {

    private Spinner userSpinner;
    private EditText subjectEditText;
    private EditText messageEditText;
    private Button sendEmailBtn;


    DBHelper dbHelper = DBHelper.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        Toolbar toolbar = findViewById(R.id.toolbarSendEmail);
        setSupportActionBar(toolbar);

        userSpinner = findViewById(R.id.staffSpinner);
        sendEmailBtn = findViewById(R.id.sendEmailBtn);
        subjectEditText = findViewById(R.id.subjectEditText);
        messageEditText = findViewById(R.id.messageEditText);

        List<User> users = dbHelper.getAllUsers();

        List<String> staffSpinnerList = new ArrayList<>();
        staffSpinnerList.add("");
        for (User user: users) {
            staffSpinnerList.add(user.getUserId() + "-" + user.getName() +" "+ user.getSurname());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, staffSpinnerList);
        userSpinner.setAdapter(dataAdapter);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sendEmailBtn.setOnClickListener(view -> sendEmail());
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

    private void sendEmail(){

        DBHelper helper = DBHelper.getInstance(this);

        if(validateFields())
        {
            String[] array = userSpinner.getSelectedItem().toString().split("-");
            User staff = helper.getUserById(array[0]);
            User manager = helper.getUserByPin(load());

            if(staff.getEmail() != null && !staff.getEmail().isEmpty()){
                // Get the Session object.// and pass username and password
                try {
                    GMailSender sender = new GMailSender("cesarawswintec@gmail.com", "33775644");
                    sender.sendMail(subjectEditText.getText().toString(),
                            messageEditText.getText().toString(),
                            manager.getEmail(),
                            staff.getEmail());

                    Toast.makeText(this,"Email was sent with success",Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }

    }

    private boolean validateFields(){

        if(TextUtils.isEmpty(userSpinner.getSelectedItem().toString()))
        {
            ((TextView)userSpinner.getSelectedView()).setError("Please select a staff member");
            return false;
        }

        if(TextUtils.isEmpty(subjectEditText.getText()))
        {
            subjectEditText.setError("Please inform the email subject");
            return false;
        }

        if(TextUtils.isEmpty(messageEditText.getText()))
        {
            messageEditText.setError("Please type the message");
            return false;
        }

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