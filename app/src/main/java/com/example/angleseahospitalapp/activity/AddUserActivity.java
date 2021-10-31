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
import com.example.angleseahospitalapp.db.UserDBHelper;
import com.example.angleseahospitalapp.model.Role;
import com.example.angleseahospitalapp.model.Upload;
import com.example.angleseahospitalapp.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AddUserActivity extends AppCompatActivity {

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri fileUri;

    ImageView userPhoto;
    ImageButton photoBtn;
    Button saveBtn;

    EditText nameEditText;
    EditText surnameEditText;
    EditText phoneEditText;
    EditText emailEditText;
    EditText pinEditText;

    Spinner roleSpinner;

    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        Toolbar toolbar = findViewById(R.id.toolbarAddUser);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("images/");

        userPhoto = findViewById(R.id.profileImage);
        photoBtn = findViewById(R.id.addPhotoBtn);
        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        pinEditText = findViewById(R.id.pinEditText);
        phoneEditText = findViewById(R.id.phoneEditText);

        roleSpinner = findViewById(R.id.roleSpinner);

        List<Role> roles = new ArrayList<>();
        roles.add(Role.NURSE);
        roles.add(Role.MANAGER);

        // Creating adapter for spinner
        ArrayAdapter<Role> dataAdapter = new ArrayAdapter<Role>(this, android.R.layout.select_dialog_item, roles);
        roleSpinner.setAdapter(dataAdapter);

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

        saveBtn = findViewById(R.id.saveUserBtn);
        saveBtn.setOnClickListener(view -> {

            if(validateFields() == true){
                save();
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

            //filePath = Uri.fromFile()
            userPhoto.setImageBitmap(imageBitmap);
            fileUri = Uri.fromFile(userPhoto.getContext().getFilesDir());
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

        return true;
    }

    private void save(){

        User user = new User();

        // Creating second StorageReference.
        StorageTask storageTask = null;
        if(fileUri != null) {

            StorageReference ref = storageReference.child(UUID.randomUUID().toString() +"." + getFileExtension(fileUri));
            storageTask = ref.putFile(fileUri).addOnSuccessListener(taskSnapshot -> {

                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();

                while(!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();

                Upload upload = new Upload("0",
                        downloadUrl.toString());
                String uploadId = mDatabaseRef.push().getKey();

                mDatabaseRef.child(uploadId).setValue(upload);

                if(downloadUrl != null) {
                    user.setPhotoPath(downloadUrl.getPath());
                }
            })
                    .addOnFailureListener(e -> Toast.makeText(AddUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    });;
        }

        UserDBHelper helper = new UserDBHelper();


        user.setEmail(emailEditText.getText().toString());
        user.setName(nameEditText.getText().toString());
        user.setPhoneNumber(phoneEditText.getText().toString());
        user.setSurname(surnameEditText.getText().toString());
        user.setPin(pinEditText.getText().toString());
        user.setRole(roleSpinner.getSelectedItem().toString());


        helper.saveUser(user);

        //Set all the fields back to empty
        cleanFields();

        Toast.makeText(this, "User Saved.", Toast.LENGTH_LONG).show();
    }

    private void cleanFields(){
        nameEditText.setText("");
        surnameEditText.setText("");
        emailEditText.setText("");
        pinEditText.setText("");
        phoneEditText.setText("");
        roleSpinner.setSelection(0);

        final Drawable icon = getDrawable(R.drawable.ic_person);
        userPhoto.setImageDrawable(icon);
    }

    //The the file extension
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}