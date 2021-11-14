package com.example.angleseahospitalapp.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.angleseahospitalapp.model.Leave;
import com.example.angleseahospitalapp.model.Notification;
import com.example.angleseahospitalapp.model.Role;
import com.example.angleseahospitalapp.model.Shift;
import com.example.angleseahospitalapp.model.User;
import com.example.angleseahospitalapp.model.UserGroup;

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
                DBContract.UsersTable.COLUMN_GROUP + " TEXT, " +
                DBContract.UsersTable.COLUMN_PHONE + " TEXT, " +
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
                DBContract.ShiftsTable.COLUMN_WEEKEND + " TEXT, " +
                DBContract.ShiftsTable.COLUMN_PUBLIC_HOLIDAY + " TEXT, " +
                "FOREIGN KEY(" + DBContract.ShiftsTable.COLUMN_USERID + ") REFERENCES " +
                DBContract.UsersTable.TABLE_NAME + "(" + DBContract.UsersTable.COLUMN_USERID + ")" + "ON DELETE CASCADE" +
                ")";

        final String SQL_CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " +
                DBContract.NotificationsTable.TABLE_NAME + "( " +
                DBContract.NotificationsTable.COLUMN_NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBContract.NotificationsTable.COLUMN_USER_ID + " INTEGER, " +
                DBContract.NotificationsTable.COLUMN_DESCRIPTION + " TEXT, " +
                DBContract.NotificationsTable.COLUMN_DATE + " DATE, " +
                "FOREIGN KEY(" + DBContract.NotificationsTable.COLUMN_USER_ID + ") REFERENCES " +
                DBContract.UsersTable.TABLE_NAME + "(" + DBContract.UsersTable.COLUMN_USERID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_SHIFTS_TABLE);
        db.execSQL(SQL_CREATE_LEAVE_TABLE);
        db.execSQL(SQL_CREATE_NOTIFICATIONS_TABLE);
        fillUsers();
        fillShifts();
        fillLeave();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.LeaveTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.ShiftsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.UsersTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.NotificationsTable.TABLE_NAME);
        onCreate(db);
    }


    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @SuppressLint("Range")
    public List<Notification> getAllNotifications(){
        List<Notification> notifications = new ArrayList<>();
        db = getReadableDatabase();

        String query = "SELECT * FROM notifications order by id DESC";
        Cursor c = db.rawQuery(query,null);

        while(c.moveToNext()){
            Notification notification = new Notification();
            notification.setUserId(c.getString(c.getColumnIndex(DBContract.NotificationsTable.COLUMN_NOTIFICATION_ID)));
            notification.setUserId(c.getString(c.getColumnIndex(DBContract.NotificationsTable.COLUMN_USER_ID)));
            notification.setDescription(c.getString(c.getColumnIndex(DBContract.NotificationsTable.COLUMN_DESCRIPTION)));
            notification.setDate(c.getString(c.getColumnIndex(DBContract.NotificationsTable.COLUMN_DATE)));
            notifications.add(notification);
        }

        c.close();
        return notifications;
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
            user.setPhoneNumber(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_ROLENAME)));
            user.setPhoto(c.getBlob(c.getColumnIndex(DBContract.UsersTable.COLUMN_PHOTO)));
            user.setGroup(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_GROUP)));
            users.add(user);
        }

        c.close();
        return users;
    }

    @SuppressLint("Range")
    public List<User> getAllUsersByGroup(UserGroup group){
        List<User> users = new ArrayList<>();
        db = getReadableDatabase();

        String query = "SELECT * FROM users where user_group= '" + group.toString() + "'";
        Cursor c = db.rawQuery(query,null);

        while(c.moveToNext()){

            User user = new User();
            user.setUserId(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_USERID)));
            user.setName(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_NAME)));
            user.setSurname(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_SURNAME)));
            user.setPin(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_PIN)));
            user.setEmail(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_EMAIL)));
            user.setRole(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_ROLENAME)));
            user.setPhoneNumber(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_ROLENAME)));
            user.setPhoto(c.getBlob(c.getColumnIndex(DBContract.UsersTable.COLUMN_PHOTO)));
            user.setGroup(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_GROUP)));
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

        String query = "SELECT * FROM leave where userid=" + user.getUserId() + " order by leaveid desc";
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

        String query = "SELECT * FROM leave order by leaveid desc";
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
            shift.setWeekend(Boolean.parseBoolean(c.getString(c.getColumnIndex(DBContract.ShiftsTable.COLUMN_WEEKEND))));
            shift.setPublicHoliday(Boolean.parseBoolean(c.getString(c.getColumnIndex(DBContract.ShiftsTable.COLUMN_PUBLIC_HOLIDAY))));
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
                usr.setPhoneNumber(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_PHONE)));
                usr.setPhoto(c.getBlob(c.getColumnIndex(DBContract.UsersTable.COLUMN_PHOTO)));
                usr.setGroup(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_GROUP)));

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
            shift.setWeekend(Boolean.parseBoolean(c.getString(c.getColumnIndex(DBContract.ShiftsTable.COLUMN_WEEKEND))));
            shift.setPublicHoliday(Boolean.parseBoolean(c.getString(c.getColumnIndex(DBContract.ShiftsTable.COLUMN_PUBLIC_HOLIDAY))));
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
                usr.setPhoneNumber(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_PHONE)));
                usr.setGroup(c.getString(c.getColumnIndex(DBContract.UsersTable.COLUMN_GROUP)));
            } while (c.moveToNext());
        }

        c.close();
        return usr;
    }

    public void saveUser(User user){
        String selection = DBContract.UsersTable.COLUMN_USERID + " = ?";
        String[] selectionArs = new String[]{user.getUserId()};

        if(user.getUserId() == null || user.getUserId().isEmpty()){
            ContentValues cv = new ContentValues();
            cv.put(DBContract.UsersTable.COLUMN_USERID, user.getUserId());
            cv.put(DBContract.UsersTable.COLUMN_NAME, user.getName());
            cv.put(DBContract.UsersTable.COLUMN_SURNAME, user.getSurname());
            cv.put(DBContract.UsersTable.COLUMN_PIN, user.getPin());
            cv.put(DBContract.UsersTable.COLUMN_EMAIL, user.getEmail());
            cv.put(DBContract.UsersTable.COLUMN_ROLENAME, user.getRole());
            cv.put(DBContract.UsersTable.COLUMN_PHOTO, user.getPhoto());
            cv.put(DBContract.UsersTable.COLUMN_PHONE, user.getPhoneNumber());
            cv.put(DBContract.UsersTable.COLUMN_GROUP, user.getGroup());
            db.insert(DBContract.UsersTable.TABLE_NAME, null, cv);

        }else{
            ContentValues cv = new ContentValues();
            cv.put(DBContract.UsersTable.COLUMN_USERID, user.getUserId());
            cv.put(DBContract.UsersTable.COLUMN_NAME, user.getName());
            cv.put(DBContract.UsersTable.COLUMN_SURNAME, user.getSurname());
            cv.put(DBContract.UsersTable.COLUMN_PIN, user.getPin());
            cv.put(DBContract.UsersTable.COLUMN_EMAIL, user.getEmail());
            cv.put(DBContract.UsersTable.COLUMN_ROLENAME, user.getRole());
            cv.put(DBContract.UsersTable.COLUMN_PHOTO, user.getPhoto());
            cv.put(DBContract.UsersTable.COLUMN_PHONE, user.getPhoneNumber());
            cv.put(DBContract.UsersTable.COLUMN_GROUP, user.getGroup());
            db.update(DBContract.UsersTable.TABLE_NAME, cv, selection, selectionArs);
        }

    }

    public void saveNotification(Notification notification){
        ContentValues cv = new ContentValues();
        cv.put(DBContract.NotificationsTable.COLUMN_NOTIFICATION_ID, notification.getId());
        cv.put(DBContract.NotificationsTable.COLUMN_USER_ID, notification.getUserId());
        cv.put(DBContract.NotificationsTable.COLUMN_DESCRIPTION, notification.getDescription());
        cv.put(DBContract.NotificationsTable.COLUMN_DATE, notification.getDate());
        db.insert(DBContract.NotificationsTable.TABLE_NAME, null, cv);
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
            cv.put(DBContract.ShiftsTable.COLUMN_WEEKEND, shift.getWeekend());
            cv.put(DBContract.ShiftsTable.COLUMN_PUBLIC_HOLIDAY, shift.getPublicHoliday()); //

            db.insert(DBContract.ShiftsTable.TABLE_NAME, null, cv);

        }else{
            ContentValues cv = new ContentValues();
            cv.put(DBContract.ShiftsTable.COLUMN_DATE, shift.getDate());
            cv.put(DBContract.ShiftsTable.COLUMN_CLOCKIN, shift.getClockInTime());
            cv.put(DBContract.ShiftsTable.COLUMN_CLOCKOUT, shift.getClockOutTime());
            cv.put(DBContract.ShiftsTable.COLUMN_PERIOD, shift.getPeriod()); //
            cv.put(DBContract.ShiftsTable.COLUMN_WEEKEND, shift.getWeekend());
            cv.put(DBContract.ShiftsTable.COLUMN_PUBLIC_HOLIDAY, shift.getPublicHoliday());
            db.update(DBContract.ShiftsTable.TABLE_NAME, cv, selection, selectionArs);
        }

    }

    public void saveLeave(Leave leave){
        String selection = DBContract.LeaveTable.COLUMN_LEAVEID + " = ?";
        String[] selectionArs = new String[]{leave.getId()};

        if(leave.getId() == null || leave.getId().isEmpty()) {
            ContentValues cv = new ContentValues();
            cv.put(DBContract.LeaveTable.COLUMN_USERID, leave.getUserId());
            cv.put(DBContract.LeaveTable.COLUMN_STARTDATETIME, leave.getStartDate()); //Date type
            cv.put(DBContract.LeaveTable.COLUMN_ENDDATETIME, leave.getEndDate()); //Date type
            cv.put(DBContract.LeaveTable.COLUMN_STATUS, leave.getLeaveStatus());
            db.insert(DBContract.LeaveTable.TABLE_NAME, null, cv);
        }else{
            ContentValues cv = new ContentValues();
            cv.put(DBContract.LeaveTable.COLUMN_USERID, leave.getUserId());
            cv.put(DBContract.LeaveTable.COLUMN_STARTDATETIME, leave.getStartDate()); //Date type
            cv.put(DBContract.LeaveTable.COLUMN_ENDDATETIME, leave.getEndDate()); //Date type
            cv.put(DBContract.LeaveTable.COLUMN_STATUS, leave.getLeaveStatus());
            db.update(DBContract.LeaveTable.TABLE_NAME, cv, selection, selectionArs);
        }
    }

    private void fillUsers(){
        User u1 = new User("1", " Mandy", "", "9876", null, "MANAGER", "cesarawswintec@gmail.com", "XXX XXX XXXX","");
        saveUser(u1);
    }

    private void fillShifts(){
//        Shift s1 = new Shift("1", "24/11/21", "09:00", "12:00", "Dev", false, false);
//        saveShift(s1);
    }

    private void fillLeave(){
//        Leave l1 = new Leave("1", "29/11/21", "01/12/21", "APPROVED");
//        saveLeave(l1);
    }
}
