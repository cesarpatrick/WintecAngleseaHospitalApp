package com.example.angleseahospitalapp.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.angleseahospitalapp.model.Leave;
import com.example.angleseahospitalapp.model.ShiftItem;
import com.example.angleseahospitalapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "AngleseaDatabase.db";
    public static final int DATABASE_VERSION = 1;

    private static DBHelper instance;

    private SQLiteDatabase db;

    private DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DBHelper getInstance(Context context){
        if (instance == null){
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;

        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " +
                DBContract.UsersTable.TABLE_NAME + "( " +
                DBContract.UsersTable.COLUMN_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBContract.UsersTable.COLUMN_NAME + " TEXT, " +
                DBContract.UsersTable.COLUMN_SURNAME + " TEXT, " +
                DBContract.UsersTable.COLUMN_PIN + " INTEGER, " +
                DBContract.UsersTable.COLUMN_EMAIL + " TEXT, " +
                DBContract.UsersTable.COLUMN_ROLENAME + " TEXT, " +
                DBContract.UsersTable.COLUMN_FINGERPRINT + " TEXT " +
                ")";

        final String SQL_CREATE_LEAVE_TABLE = "CREATE TABLE " +
                DBContract.LeaveTable.TABLE_NAME + "( " +
                DBContract.LeaveTable.COLUMN_LEAVEID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBContract.LeaveTable.COLUMN_USERID + " INTEGER, " +
                DBContract.LeaveTable.COLUMN_STARTDATETIME + " DATE, " +
                DBContract.LeaveTable.COLUMN_ENDDATETIME + " DATE, " +
                DBContract.LeaveTable.COLUMN_STATUS + " TEXT, " +
                "FOREIGN KEY(" + DBContract.LeaveTable.COLUMN_USERID + ") REFERENCES " +
                DBContract.UsersTable.TABLE_NAME + "(" + DBContract.UsersTable.COLUMN_USERID + ")" + "ON DELETE CASCADE" +
                ")";

        final String SQL_CREATE_SHIFTS_TABLE = "CREATE TABLE " +
                DBContract.ShiftsTable.TABLE_NAME + "( " +
                DBContract.ShiftsTable.COLUMN_SHIFTID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBContract.ShiftsTable.COLUMN_USERID + " INTEGER, " +
                DBContract.ShiftsTable.COLUMN_DATE + " DATE, " +
                DBContract.ShiftsTable.COLUMN_CLOCKIN  + " TIME, " +
                DBContract.ShiftsTable.COLUMN_CLOCKOUT + " TIME, " +
                DBContract.ShiftsTable.COLUMN_PERIOD + " TEXT, " +
                "FOREIGN KEY(" + DBContract.ShiftsTable.COLUMN_USERID + ") REFERENCES " +
                DBContract.UsersTable.TABLE_NAME + "(" + DBContract.UsersTable.COLUMN_USERID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_SHIFTS_TABLE);
        db.execSQL(SQL_CREATE_LEAVE_TABLE);
        fillUsers();
        fillShifts();
        fillLeave();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.LeaveTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.ShiftsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.UsersTable.TABLE_NAME);
        onCreate(db);
    }


    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    @SuppressLint("Range")
    public User getUserByPin(String pin){
        User usr = new User();
        db = getReadableDatabase();

        String selection = DBContract.UsersTable.COLUMN_PIN + " = ?";
        String[] selectionArs = new String[]{pin};
        Cursor c = db.query(
                DBContract.UsersTable.TABLE_NAME,
                null,
                selection,
                selectionArs,
                null,
                null,
                null);

        if (c.moveToFirst()){
            do {
                usr.setUserId(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_USERID)));
                usr.setName(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_NAME)));
                usr.setSurname(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_SURNAME)));
                usr.setPin(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_PIN)));
                usr.setEmail(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_EMAIL)));
                usr.setRole(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_ROLENAME)));
            } while (c.moveToNext());
        }

        c.close();
        return usr;
    }

    @SuppressLint("Range")
    public User getUserById(String userId){
        User usr = new User();
        db = getReadableDatabase();

        String selection = DBContract.UsersTable.COLUMN_USERID + " = ?";
        String[] selectionArs = new String[]{userId};
        Cursor c = db.query(
                DBContract.UsersTable.TABLE_NAME,
                null,
                selection,
                selectionArs,
                null,
                null,
                null);

        if (c.moveToFirst()){
            do {
                usr.setUserId(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_USERID)));
                usr.setName(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_NAME)));
                usr.setSurname(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_SURNAME)));
                usr.setPin(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_PIN)));
                usr.setEmail(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_EMAIL)));
                usr.setRole(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_ROLENAME)));
            } while (c.moveToNext());
        }

        c.close();
        return usr;
    }

    public void insertUser(User user){
        ContentValues cv = new ContentValues();
        cv.put(DBContract.UsersTable.COLUMN_USERID, user.getUserId());
        cv.put(DBContract.UsersTable.COLUMN_NAME, user.getName());
        cv.put(DBContract.UsersTable.COLUMN_SURNAME, user.getSurname());
        cv.put(DBContract.UsersTable.COLUMN_PIN, user.getPin());
        cv.put(DBContract.UsersTable.COLUMN_EMAIL, user.getEmail());
        cv.put(DBContract.UsersTable.COLUMN_ROLENAME, user.getRole());
        db.insert(DBContract.UsersTable.TABLE_NAME, null, cv);
    }

    private void insertShift(ShiftItem shift){
        ContentValues cv = new ContentValues();
        cv.put(DBContract.ShiftsTable.COLUMN_USERID, shift.getStaffID());
        cv.put(DBContract.ShiftsTable.COLUMN_DATE, shift.getDate());
        cv.put(DBContract.ShiftsTable.COLUMN_CLOCKIN, shift.getClockInTime());
        cv.put(DBContract.ShiftsTable.COLUMN_CLOCKOUT, shift.getClockOutTime());
        cv.put(DBContract.ShiftsTable.COLUMN_PERIOD, ""); //
        db.insert(DBContract.ShiftsTable.TABLE_NAME, null, cv);
    }

    private void insertLeave(Leave leave){
        ContentValues cv = new ContentValues();
        cv.put(DBContract.LeaveTable.COLUMN_USERID, leave.getUserId());
        cv.put(DBContract.LeaveTable.COLUMN_STARTDATETIME, leave.getStartDate()); //Date type
        cv.put(DBContract.LeaveTable.COLUMN_ENDDATETIME, leave.getEndDate()); //Date type
        cv.put(DBContract.LeaveTable.COLUMN_STATUS, leave.getLeaveStatus());
        db.insert(DBContract.LeaveTable.TABLE_NAME, null, cv);
    }

    private void fillUsers(){
        User u1 = new User("1234", "Jordan", "Laing", "9876", "This is excluded for now", "Dev", "Jordy@mail", "0210000000");
        insertUser(u1);
    }

    private void fillShifts(){
        ShiftItem s1 = new ShiftItem("1234", "24-11-21", "09:00", "12:00", "Dev");
        insertShift(s1);
    }

    private void fillLeave(){
        Leave l1 = new Leave("1234", "29-11-21", "1-12-21", "Approved");
        insertLeave(l1);
    }


    //----------------------------old------------------------------------\\
    /**
    public void saveUser(User user){
        String uploadId = mUserDBRef.push().getKey();
        mUserDBRef.child(uploadId).setValue(user);
    }

    public ArrayList<User> getUserByKey(String userKey){
        ArrayList<User> users = new ArrayList<>();
        mUserDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    if (postSnapshot.getValue(User.class).getmKey().equals(userKey)){
                        User user = postSnapshot.getValue(User.class);
                        user.setmKey(postSnapshot.getKey());
                        users.add(user);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });
        return users;
    }

    public User getUserByPin(String pin){
        ArrayList<User> users = new ArrayList<>();
        mUserDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    if (postSnapshot.getValue(User.class).getPin().equals(pin)){
                        User mUser = postSnapshot.getValue(User.class);
                        //User.setmKey(postSnapshot.getKey());
                        users.add(mUser);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });

        User user = new User();
        return user;
    }

    public ArrayList<User> getAllUsers(){
        ArrayList<User> users = new ArrayList<>();
        mUserDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    User user = postSnapshot.getValue(User.class);
                    user.setmKey(postSnapshot.getKey());
                    users.add(user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });
        return users;
    }
     **/
}
