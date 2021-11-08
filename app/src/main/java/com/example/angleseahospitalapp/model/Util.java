package com.example.angleseahospitalapp.model;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

}
