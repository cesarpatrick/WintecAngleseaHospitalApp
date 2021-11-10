package com.example.angleseahospitalapp.model;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static String formatDayDate(int day){
        String dayFormmat;
        if(day <= 9){
            return "0"+day;
        }else{
           return ""+day;
        }
    }

    public static String getTimeFromStringDate(String date){

        if(!date.isEmpty()){
            String[] dayFormmat = date.split(" ");
            return dayFormmat[1];
        }

        return null;
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
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        return format1.format(date);
    }

    public static String convertDateTimeToString(Date date){
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
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

    public static String shiftDuration(String date1, String date2){
        // SimpleDateFormat converts the
        // string format to date object
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        // Try Block
        try {

            // parse method is used to parse
            // the text from a string to
            // produce the date
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);

            // Calucalte time difference
            // in milliseconds
            long difference_In_Time
                    = d2.getTime() - d1.getTime();



            long difference_In_Minutes
                    = (difference_In_Time
                    / (1000 * 60))
                    % 60;

            long difference_In_Hours
                    = (difference_In_Time
                    / (1000 * 60 * 60))
                    % 24;


           return Util.formatDayDate(Integer.parseInt(difference_In_Hours+"")) + ":" + Util.formatDayDate(Integer.parseInt(difference_In_Minutes+""));

        }

        // Catch the Exception
        catch (ParseException e) {
            e.printStackTrace();
        }

     return null;
    }
}
