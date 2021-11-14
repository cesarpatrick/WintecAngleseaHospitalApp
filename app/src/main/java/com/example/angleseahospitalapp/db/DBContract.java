package com.example.angleseahospitalapp.db;

import android.provider.BaseColumns;

public final class DBContract {

    private DBContract(){}

    //constants for the naming convention for the users table
    public static class UsersTable implements BaseColumns{
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USERID = "userid"; //primary key
        public static final String COLUMN_NAME = "firstname";
        public static final String COLUMN_SURNAME = "surname";
        public static final String COLUMN_PIN = "pin";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_ROLENAME = "role";
        public static final String COLUMN_FINGERPRINT = "fingerprint"; //not sure if this is still needed
        public static final String COLUMN_PHOTO = "photo";
    }

    //constants for the naming convention for the Shifts table
    public static class ShiftsTable implements BaseColumns{
        public static final String TABLE_NAME = "shift";
        public static final String COLUMN_SHIFTID = "shiftid"; //primary key
        public static final String COLUMN_USERID = "userid"; //foreign key
        public static final String COLUMN_DATE = "shift_date";
        public static final String COLUMN_CLOCKIN = "clockin";
        public static final String COLUMN_CLOCKOUT = "clockout";
        public static final String COLUMN_PERIOD = "period";
        public static final String COLUMN_WEEKEND = "weekend";
        public static final String COLUMN_PUBLIC_HOLIDAY = "public_holiday";
    }

    //constants for the naming convention for the Shifts table
    public static class LeaveTable implements BaseColumns{
        public static final String TABLE_NAME = "leave";
        public static final String COLUMN_LEAVEID = "leaveid"; //primary key
        public static final String COLUMN_USERID = "userid"; //foreign key
        public static final String COLUMN_STARTDATETIME = "startdatetime";
        public static final String COLUMN_ENDDATETIME= "enddatetime";
        public static final String COLUMN_STATUS = "status";
    }

    public static class NotificationsTable implements BaseColumns{
        public static final String TABLE_NAME = "notifications";
        public static final String COLUMN_NOTIFICATION_ID = "id"; //primary key
        public static final String COLUMN_USER_ID = "userid"; //foreign key
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DATE = "date";
    }
}
