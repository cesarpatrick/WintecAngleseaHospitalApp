package com.example.angleseahospitalapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.chaos.view.PinView;
import com.example.angleseahospitalapp.R;
import com.example.angleseahospitalapp.db.DBHelper;
import com.example.angleseahospitalapp.model.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executor;

public class PinScreenActivity extends AppCompatActivity {

    Button confirmPinBtn;
    Button confirmDisclaimerBtn;

    private RadioButton radio;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String DISCLAIMER_DONE = "disclaimerDone";

    private Boolean disclaimerDone = false;

    Executor executor;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_screen);

        //Set my toolbar because i removed the default one
        Toolbar toolbar = findViewById(R.id.toolbarPin);
        setSupportActionBar(toolbar);

        DBHelper dbHelper = DBHelper.getInstance(this);

        loadData();

        if(!disclaimerDone) {

            Dialog disclaimer = new Dialog(PinScreenActivity.this);
            disclaimer.requestWindowFeature(Window.FEATURE_NO_TITLE);
            disclaimer.setContentView(R.layout.disclaimer);
            disclaimer.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            disclaimer.show();

            TextView disclaimerText = disclaimer.findViewById(R.id.disclaimerMainText);
            disclaimerText.setMovementMethod(new ScrollingMovementMethod());

            radio = disclaimer.findViewById(R.id.radioButtonDisclaimer);

            confirmDisclaimerBtn = disclaimer.findViewById(R.id.confirmDisclaimerBtn);
            confirmDisclaimerBtn.setEnabled(false);

            radio.setOnCheckedChangeListener((compoundButton, b) -> confirmDisclaimerBtn.setEnabled(true));

            Intent intent = new Intent(this, PinScreenActivity.class);

            confirmDisclaimerBtn.setOnClickListener(view -> {
                saveDisclaimerStatus(true);
                startActivity(intent);
            });
        }

        final PinView pinView = findViewById(R.id.pinView);
        pinView.setItemCount(4);
        pinView.setItemHeight(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        pinView.setItemWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        pinView.setItemRadius(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_radius));
        pinView.setItemSpacing(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_spacing));
        pinView.setLineWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_line_width));
        pinView.setAnimationEnable(true);// start animation when adding text
        pinView.setCursorVisible(false);
        pinView.setCursorWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_cursor_width));
        pinView.setHideLineWhenFilled(false);

        pinView.setOnKeyListener((view, i, keyEvent) -> {
            if(!pinView.getText().toString().isEmpty() && pinView.getText().length() > 3){
                confirmPinBtn.setEnabled(true);
            }else{
                confirmPinBtn.setEnabled(false);
            }

            return false;
        });

        Intent intent = new Intent(this, HomeActivity.class);

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(PinScreenActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(PinScreenActivity.this, "It was not possible recognize the fingerprint, please try again", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                User user = dbHelper.getUserByPin("9876");
                save(user.getPin());
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(PinScreenActivity.this, "It was not possible recognize the fingerprint, please try again", Toast.LENGTH_LONG).show();            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login using fingerprint authentication")
                .setNegativeButtonText("User App Password")
                .build();

        ImageButton fingerprintBtn = findViewById(R.id.fingerPrintBtn);
        fingerprintBtn.setOnClickListener(view -> biometricPrompt.authenticate(promptInfo));

        confirmPinBtn = findViewById(R.id.confirmPinBtn);
        confirmPinBtn.setEnabled(false);

        confirmPinBtn.setOnClickListener(view -> {

            User user = dbHelper.getUserByPin(pinView.getText().toString());

            if( user.getUserId() != null && !user.getUserId().isEmpty()) {
                save(pinView.getText().toString());
                startActivity(intent);
            }else{
                Toast toast = Toast.makeText(this, "Pin does not exist.", Toast.LENGTH_LONG);
                View toastView = toast.getView();

                //To change the Background of Toast
                toastView.setBackgroundColor(Color.RED);
                toast.show();
            }
        });
    }

    public void saveDisclaimerStatus(Boolean disclaimerDone) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DISCLAIMER_DONE, disclaimerDone);
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        disclaimerDone = sharedPreferences.getBoolean(DISCLAIMER_DONE,false);
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