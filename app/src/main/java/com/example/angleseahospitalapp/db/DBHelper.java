package com.example.angleseahospitalapp.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.angleseahospitalapp.model.Leave;
import com.example.angleseahospitalapp.model.Role;
import com.example.angleseahospitalapp.model.Shift;
import com.example.angleseahospitalapp.model.User;
import com.example.angleseahospitalapp.model.Util;

import java.util.ArrayList;
import java.util.List;

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
                DBContract.UsersTable.COLUMN_FINGERPRINT + " TEXT, " +
                DBContract.UsersTable.COLUMN_PHOTO + " BLOB " +
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
                DBContract.ShiftsTable.COLUMN_CLOCKIN  + " DATE, " +
                DBContract.ShiftsTable.COLUMN_CLOCKOUT + " DATE, " +
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
    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        db = getReadableDatabase();

        String query = "SELECT * FROM users where role= '" + Role.NURSE.toString() + "'";
        Cursor c = db.rawQuery(query,null);

        while(c.moveToNext()){

            User user = new User();
            user.setUserId(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_USERID)));
            user.setName(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_NAME)));
            user.setSurname(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_SURNAME)));
            user.setPin(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_PIN)));
            user.setEmail(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_EMAIL)));
            user.setRole(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_ROLENAME)));
            users.add(user);
        }

        c.close();
        return users;
    }

    @SuppressLint("Range")
    public List<Leave> getAllLeaveByUserId(String userPin){
        List<Leave> leaveList = new ArrayList<>();
        db = getReadableDatabase();

        User user = getUserByPin(userPin);

        String query = "SELECT * FROM leave where userid=" + user.getUserId();
        Cursor c = db.rawQuery(query,null);

        while(c.moveToNext()){

            Leave leave = new Leave();
            leave.setId(c.getString(c.getColumnIndex(DBContract.LeaveTable.COLUMN_LEAVEID)));
            leave.setUserId(c.getString(c.getColumnIndex(DBContract.LeaveTable.COLUMN_USERID)));
            leave.setStartDate(c.getString(c.getColumnIndex(DBContract.LeaveTable.COLUMN_STARTDATETIME)));
            leave.setEndDate(c.getString(c.getColumnIndex(DBContract.LeaveTable.COLUMN_ENDDATETIME)));
            leave.setLeaveStatus(c.getString(c.getColumnIndex(DBContract.LeaveTable.COLUMN_STATUS)));
            leaveList.add(leave);
        }

        c.close();
        return leaveList;
    }

    @SuppressLint("Range")
    public List<Leave> getAllLeave(){
        List<Leave> leaveList = new ArrayList<>();
        db = getReadableDatabase();

        String query = "SELECT * FROM leave";
        Cursor c = db.rawQuery(query,null);

        while(c.moveToNext()){

            Leave leave = new Leave();
            leave.setId(c.getString(c.getColumnIndex(DBContract.LeaveTable.COLUMN_LEAVEID)));
            leave.setUserId(c.getString(c.getColumnIndex(DBContract.LeaveTable.COLUMN_USERID)));
            leave.setStartDate(c.getString(c.getColumnIndex(DBContract.LeaveTable.COLUMN_STARTDATETIME)));
            leave.setEndDate(c.getString(c.getColumnIndex(DBContract.LeaveTable.COLUMN_ENDDATETIME)));
            leave.setLeaveStatus(c.getString(c.getColumnIndex(DBContract.LeaveTable.COLUMN_STATUS)));
            leaveList.add(leave);
        }

        c.close();
        return leaveList;
    }

    @SuppressLint("Range")
    public List<Shift> getAllShiftByUserId(String userPin){
        List<Shift> shiftList = new ArrayList<>();
        db = getReadableDatabase();

        User user = getUserByPin(userPin);

        String query = "SELECT * FROM shift where userid=" + user.getUserId() + " order by shift_date";
        Cursor c = db.rawQuery(query,null);

        while(c.moveToNext()){

            Shift shift = new Shift();
            shift.setShiftId(c.getString(c.getColumnIndex(DBContract.ShiftsTable.COLUMN_SHIFTID)));
            shift.setStaffID(c.getString(c.getColumnIndex(DBContract.ShiftsTable.COLUMN_USERID)));
            shift.setPeriod(c.getString(c.getColumnIndex(DBContract.ShiftsTable.COLUMN_PERIOD)));
            shift.setDate(c.getString(c.getColumnIndex(DBContract.ShiftsTable.COLUMN_DATE)));
            shift.setClockOutTime(c.getString(c.getColumnIndex(DBContract.ShiftsTable.COLUMN_CLOCKOUT)));
            shift.setClockInTime(c.getString(c.getColumnIndex(DBContract.ShiftsTable.COLUMN_CLOCKIN)));
            shiftList.add(shift);
        }

        c.close();
        return shiftList;
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
    public Shift getShiftByUserIdByDate(String userId, String date){
        Shift shift = new Shift();
        db = getReadableDatabase();

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM shift where userid=").append(userId)
                .append(" and shift_date='").append(date).append("'").append(" and clockout IS NULL");

        Cursor c = db.rawQuery(query.toString(),null);

        while (c.moveToNext()){

            shift.setStaffID(c.getString(c.getColumnIndex(DBContract.ShiftsTable.COLUMN_USERID)));
            shift.setDate(c.getString(c.getColumnIndex(DBContract.ShiftsTable.COLUMN_DATE)));
            shift.setPeriod(c.getString(c.getColumnIndex(DBContract.ShiftsTable.COLUMN_PERIOD)));
            shift.setShiftId(c.getString(c.getColumnIndex(DBContract.ShiftsTable.COLUMN_SHIFTID)));
            shift.setClockInTime(c.getString(c.getColumnIndex(DBContract.ShiftsTable.COLUMN_CLOCKIN)));
            shift.setClockOutTime(c.getString(c.getColumnIndex(DBContract.ShiftsTable.COLUMN_CLOCKOUT)));
            break;
        }

        c.close();
        return shift;
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

    public void saveUser(User user){
        ContentValues cv = new ContentValues();
        cv.put(DBContract.UsersTable.COLUMN_USERID, user.getUserId());
        cv.put(DBContract.UsersTable.COLUMN_NAME, user.getName());
        cv.put(DBContract.UsersTable.COLUMN_SURNAME, user.getSurname());
        cv.put(DBContract.UsersTable.COLUMN_PIN, user.getPin());
        cv.put(DBContract.UsersTable.COLUMN_EMAIL, user.getEmail());
        cv.put(DBContract.UsersTable.COLUMN_ROLENAME, user.getRole());
        db.insert(DBContract.UsersTable.TABLE_NAME, null, cv);
    }

    public void saveShift(Shift shift){

        String selection = DBContract.ShiftsTable.COLUMN_SHIFTID + " = ?";
        String[] selectionArs = new String[]{shift.getShiftId()};

        if(shift.getShiftId() == null || shift.getShiftId().isEmpty()){
            ContentValues cv = new ContentValues();
            cv.put(DBContract.ShiftsTable.COLUMN_USERID, shift.getStaffID());
            cv.put(DBContract.ShiftsTable.COLUMN_DATE, shift.getDate());
            cv.put(DBContract.ShiftsTable.COLUMN_CLOCKIN, shift.getClockInTime());
            cv.put(DBContract.ShiftsTable.COLUMN_CLOCKOUT, shift.getClockOutTime());
            cv.put(DBContract.ShiftsTable.COLUMN_PERIOD, shift.getPeriod()); //
            db.insert(DBContract.ShiftsTable.TABLE_NAME, null, cv);
        }else{
            ContentValues cv = new ContentValues();
            cv.put(DBContract.ShiftsTable.COLUMN_DATE, shift.getDate());
            cv.put(DBContract.ShiftsTable.COLUMN_CLOCKIN, shift.getClockInTime());
            cv.put(DBContract.ShiftsTable.COLUMN_CLOCKOUT, shift.getClockOutTime());
            cv.put(DBContract.ShiftsTable.COLUMN_PERIOD, shift.getPeriod()); //
            db.update(DBContract.ShiftsTable.TABLE_NAME, cv, selection, selectionArs);
        }

    }

    public void saveLeave(Leave leave){
        ContentValues cv = new ContentValues();
        cv.put(DBContract.LeaveTable.COLUMN_USERID, leave.getUserId());
        cv.put(DBContract.LeaveTable.COLUMN_STARTDATETIME, leave.getStartDate()); //Date type
        cv.put(DBContract.LeaveTable.COLUMN_ENDDATETIME, leave.getEndDate()); //Date type
        cv.put(DBContract.LeaveTable.COLUMN_STATUS, leave.getLeaveStatus());
        db.insert(DBContract.LeaveTable.TABLE_NAME, null, cv);
    }

    private void fillUsers(){
        User u1 = new User("1", "Jordan", "Laing", "9876", "This is excluded for now", "MANAGER", "Jordy@mail", "0210000000");
        saveUser(u1);
    }

    private void fillShifts(){
        Shift s1 = new Shift("1", "24/11/21", "09:00", "12:00", "Dev");
        saveShift(s1);
    }

    private void fillLeave(){
        Leave l1 = new Leave("1", "29/11/21", "01/12/21", "APPROVED");
        saveLeave(l1);
    }
}
