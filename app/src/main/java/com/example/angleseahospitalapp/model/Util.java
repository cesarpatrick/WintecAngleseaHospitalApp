package com.example.angleseahospitalapp.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Util {

    public static String formatDayDate(int day){
        String dayFormmat;
        if(day <= 9){
            return dayFormmat = "0"+day;
        }else{
           return dayFormmat = ""+day;
        }
    }


    public static String getFirstDayOfMonth(Date date){
//        String dayFormmat;
        return "";
    }

    public static String getLastDayOfMonth(Date date){
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(date);
        return str;
    }

    public static String getDayNameText(Date date){
        Format f = new SimpleDateFormat("EEE");
        String str = f.format(date);
        return str;
    }

    public static Date convertStringToDate(String stringDate){
        try {

            if(stringDate != null && !stringDate.isEmpty()){
               return new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String convertDateToString(Date date){
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        return format1.format(date);
    }

    public static String convertDateTimeToString(Date date){
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format1.format(date);
    }

    public static String getTimeByShiftPeriod(ShiftPeriod period){

        if (period.equals(period.MORNING)){
          return "08:00 am - 04:00 pm";
        }else if(period.equals(period.AFTERNOON)){
          return "04:00 pm - 00:00 am";
        }else if(period.equals(period.NIGHT)){
            return "00:00 am - 08:00 am";
        }else{
            return null;
        }
    }

    public static String dateQuery(String stringDate){

        SimpleDateFormat inSDF = new SimpleDateFormat("mm/dd/yyyy");
        SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-mm-dd");

        String outDate = "";

        if(stringDate != null && !stringDate.isEmpty()){
            try {
                Date date = inSDF.parse(stringDate);
                outDate = outSDF.format(date);
                return outDate;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
