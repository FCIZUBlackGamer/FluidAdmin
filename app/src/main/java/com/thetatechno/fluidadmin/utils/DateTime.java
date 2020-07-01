package com.thetatechno.fluidadmin.utils;


import java.text.ParseException;
import java.util.Calendar;

public class DateTime {

    public static final String DATE_FORMAT = "yyyy-MM-dd-HH:mm:ss";
    public static final String DATE_INPUT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String mDateTimeString = "2018-12-05 10:37:43";
    public static final String DATE_OUTPUT_FORMAT = "dd-MMM-yyyy";
    String mDateTime = null;
    static long mTimestamp;

    public static String getTime() {
        /**
         * Date Format example 4
         */
        try {
             mTimestamp = DateUtils.getTimeStampFromDateTime(mDateTimeString, DATE_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /**
         * Get current datetime example 5
         */
        String mDateTime = DateUtils.formatDateTimeFromDate(DATE_FORMAT, Calendar.getInstance().getTime());
        /**
         * DateTime Output Format
         */
//        try {
//            mDateTime = DateUtils.formatDateFromDateString(DATE_INPUT_FORMAT, DATE_INPUT_FORMAT, mDateTimeString);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        return String.valueOf(mDateTime);

    }
}
