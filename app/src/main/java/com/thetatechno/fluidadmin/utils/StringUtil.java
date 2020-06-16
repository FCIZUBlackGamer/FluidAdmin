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

    public static String displayTime(String time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm aa");
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm a");
        try {
            Date date = simpleDateFormat.parse(time);

            Log.i("StringUtil", date.getHours() + " date hours");
            String dateString = simpleTimeFormat.format(date).toString();
            return dateString;

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String getDay(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm aa");
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm aa");
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm aa");
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
