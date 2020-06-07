package com.thetatechno.fluidadmin.utils;

public class WeekDays {

    public static String getDay(String dayCharacters) {
        String day = null;
        switch (dayCharacters) {
            case "SAT":
                day = "Saturday";
                break;
            case "SUN":
                day = "Sunday";
                break;
            case "MON":
                day = "Monday";
                break;
            case "TUE":
                day = "Tuesday";
                break;
            case "WED":
                day = " Wednesday";
                break;
            case "THU":
                day = "Thursday";
                break;
            case "FRI":
                day = " Friday";
                break;
            default:
                day = "Day Not Found";

        }
        return day;
    }
}
