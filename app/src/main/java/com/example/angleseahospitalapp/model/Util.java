package com.example.angleseahospitalapp.model;

import java.text.Format;

public class Util {

    public static String formatDayDate(int day){
        String dayFormmat;
        if(day <= 9){
            return dayFormmat = "0"+day;
        }else{
           return dayFormmat = ""+day;
        }
    }
}
