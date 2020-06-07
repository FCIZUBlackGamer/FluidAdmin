package com.thetatechno.fluidadmin.utils;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {

    public static String toCamelCase(final String words) {
        if (words == null)
            return null;

        final StringBuilder builder = new StringBuilder(words.length());

        for (final String word : words.split(" ")) {
            if (!word.isEmpty()) {
                builder.append(Character.toUpperCase(word.charAt(0)));
                builder.append(word.substring(1).toLowerCase());
            }
            if (!(builder.length() == words.length()))
                builder.append(" ");
        }

        return builder.toString();
    }

    public static String displayTime(String time)  {
        String amOrPmTxt = "";
        String hours="";
        String minutes="";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(time);

            Log.i("StringUtil",date.getHours()+" date hours");

            if(date.getHours()> 12) {
                amOrPmTxt = "PM";
                if((date.getHours() %12) >=1 && (date.getHours()%12) <10)
                    hours = 0 +String.valueOf(date.getHours() %12);
                else
                    hours = String.valueOf(date.getHours() %12);
            }

            else if(date.getHours() == 12)
            {
                amOrPmTxt = "PM";
                hours = String.valueOf(date.getHours());
            }
            else if(date.getHours()<12 && date.getHours()>=10){
                amOrPmTxt = "AM";

                hours = String.valueOf(date.getHours());
            }
            else if (date.getMinutes() >=0 && date.getHours()<10)
            {
                amOrPmTxt = "AM";

                hours = 0+ String.valueOf(date.getHours());
            }

            if(date.getMinutes() >=0 && date.getMinutes() <10)
                minutes = 0+ String.valueOf(date.getMinutes());
            else
                minutes = String.valueOf(date.getMinutes());
            return hours + ":" + minutes +" "+amOrPmTxt;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";

    }
    public static String getDay(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dayTxt = "";
        try {
            Date mDate = simpleDateFormat.parse(date);
            dayTxt  = (String) DateFormat.format("dd", mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayTxt;

    }
    public static String getMonth(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String monthTxt = "";
        try {
            Date mDate = simpleDateFormat.parse(date);
            monthTxt = (String) DateFormat.format("MMM",  mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return monthTxt;

    }

    public static String getYear(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String yearTxt = "";
        try {
            Date mDate = simpleDateFormat.parse(date);
            yearTxt = (String) DateFormat.format("yyyy",  mDate); // Jun
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return yearTxt;

    }
}
