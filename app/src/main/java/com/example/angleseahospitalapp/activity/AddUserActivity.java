package com.example.angleseahospitalapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.angleseahospitalapp.R;
import com.example.angleseahospitalapp.db.DBHelper;
import com.example.angleseahospitalapp.model.Role;
import com.example.angleseahospitalapp.model.User;
import com.example.angleseahospitalapp.model.UserGroup;
import com.example.angleseahospitalapp.model.Util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddUserActivity extends AppCompatActivity {

    ImageView userPhoto;
    ImageButton photoBtn;
    Button saveBtn;

    byte[] photo;

    EditText nameEditText;
    EditText surnameEditText;
    EditText phoneEditText;
    EditText emailEditText;
    EditText pinEditText;

    Spinner roleSpinner;
    Spinner groupSpinner;

    DBHelper helper = DBHelper.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        Toolbar toolbar = findViewById(R.id.toolbarAddUser);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userPhoto = findViewById(R.id.profileImage);
        photoBtn = findViewById(R.id.addPhotoBtn);
        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        pinEditText = findViewById(R.id.pinEditText);
        phoneEditText = findViewById(R.id.phoneEditText);

        groupSpinner = findViewById(R.id.groupSpinner);
        roleSpinner = findViewById(R.id.roleSpinner);

        List<Role> roles = new ArrayList<>();
        roles.add(Role.NURSE);
        roles.add(Role.MANAGER);

        List<String> groups = new ArrayList<>();
        groups.add("");
        groups.add(UserGroup.WARD.toString());
        groups.add(UserGroup.PACU.toString());
        groups.add(UserGroup.OT.toString());

        // Creating adapter for spinner
        ArrayAdapter<Role> dataAdapter = new ArrayAdapter<Role>(this, android.R.layout.select_dialog_item, roles);
        roleSpinner.setAdapter(dataAdapter);

        // Creating adapter for spinner
        ArrayAdapter<String> groupDataAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, groups);
        groupSpinner.setAdapter(groupDataAdapter);

        //Request for camera runtime permissions
        if(ContextCompat.checkSelfPermission(AddUserActivity.this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddUserActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        photoBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,100);
        });

        User user = loadUser();

        saveBtn = findViewById(R.id.saveUserBtn);
        saveBtn.setOnClickListener(view -> {

            if(validateFields() == true){

                if(user.getUserId() != null && !user.getUserId().isEmpty()){
                    save(user, true);
                }else{
                    save(user, false);
                }

            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); //compress to which format you want.
            photo = stream.toByteArray();

            userPhoto.setImageBitmap(imageBitmap);
        }
    }

    //Method to validate the required fields
    private boolean validateFields(){
        if(TextUtils.isEmpty(nameEditText.getText()))
        {
            nameEditText.setError("Please enter the name");
            return false;
        }

        if(TextUtils.isEmpty(surnameEditText.getText()))
        {
            surnameEditText.setError("Please enter the surname");
            return false;
        }

        if(TextUtils.isEmpty(pinEditText.getText()) && pinEditText.getText().length() < 4)
        {
            pinEditText.setError("The pin number is 4 digits");
            return false;
        }

        if(TextUtils.isEmpty(emailEditText.getText()))
        {
            emailEditText.setError("Please inform the staff email");
            return false;
        }

        return true;
    }

    private void save(User usr, Boolean isUserLoaded){

        User user;

        if(isUserLoaded){
            user = usr;
        }else{
            user = helper.getUserByPin(pinEditText.getText().toString());
            if( user.getUserId() != null){
                Toast.makeText(this, "This pin number is already in use, try another one. ", Toast.LENGTH_LONG).show();
            }
        }

        user.setPin(pinEditText.getText().toString());
        user.setEmail(emailEditText.getText().toString());
        user.setName(nameEditText.getText().toString());
        user.setPhoneNumber(phoneEditText.getText().toString());
        user.setSurname(surnameEditText.getText().toString());
        user.setRole(roleSpinner.getSelectedItem().toString());
        user.setGroup(groupSpinner.getSelectedItem().toString());

        if(photo != null){
            user.setPhoto(photo);
        }

        helper.saveUser(user);

        //Set all the fields back to empty
        cleanFields();
        Toast.makeText(this, "User Saved.", Toast.LENGTH_LONG).show();
        Intent listUse = new Intent(this, ListUserActivity.class);
        startActivity(listUse);

    }

    private void cleanFields(){
        nameEditText.setText("");
        surnameEditText.setText("");
        emailEditText.setText("");
        pinEditText.setText("");
        phoneEditText.setText("");
        roleSpinner.setSelection(0);
        groupSpinner.setSelection(0);

        final Drawable icon = getDrawable(R.drawable.ic_person);
        userPhoto.setImageDrawable(icon);
    }

    private User loadUser(){
        String userId = getIntent().getStringExtra("USER_ID");

        if(userId != null  && !userId.isEmpty()) {

            User user = helper.getUserById(userId);

            nameEditText.setText(user.getName());
            surnameEditText.setText(user.getSurname());
            emailEditText.setText(user.getEmail());
            pinEditText.setText(user.getPin());

            if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
                phoneEditText.setText(user.getPhoneNumber());
            }

            if (user.getRole() != null && !user.getRole().isEmpty()) {
                ArrayAdapter myAdap = (ArrayAdapter) roleSpinner.getAdapter(); //cast to an ArrayAdapter
                int spinnerPosition = myAdap.getPosition(user.getRole());
                roleSpinner.setSelection(spinnerPosition);
            }

            if (user.getRole() != null && !user.getRole().isEmpty()) {
                ArrayAdapter myAdap = (ArrayAdapter) groupSpinner.getAdapter(); //cast to an ArrayAdapter
                int spinnerPosition = myAdap.getPosition(user.getGroup());
                groupSpinner.setSelection(spinnerPosition);
            }

            if(user.getPhoto() != null){
                userPhoto.setImageBitmap(Util.getCircleBitmap(BitmapFactory.decodeByteArray(user.getPhoto(), 0, user.getPhoto().length),100));
            }else{
                final Drawable icon = getDrawable(R.drawable.ic_person);
                userPhoto.setImageDrawable(icon);
            }

            return user;
        }

        return new User();
    }

    //The the file extension
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}