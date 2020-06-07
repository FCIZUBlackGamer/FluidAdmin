package com.thetatechno.fluidadmin.utils;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    private static String TAG = "Validation";
    private static final int PASSWORD_LENGTH = 6;


    public static boolean isValidEmail(String email) {
        return (TextUtils.isEmpty(email.trim()) || Patterns.EMAIL_ADDRESS.matcher(email).matches());

    }

    public static boolean isValidPassword(String password) {
        return password.length() >= PASSWORD_LENGTH;
    }
    public static boolean isValidPhoneNumber(String phone){
        Log.i(TAG,Patterns.PHONE.pattern());
        return (TextUtils.isEmpty(phone.trim()) || phone.matches("^[+]?[0-9]{10,15}$"));
    }
    public static boolean isValidForName(String word){

        return (!TextUtils.isEmpty(word.trim())&& word.matches("[a-zA-Z]+"));
    }
    public static boolean isValidForId(String id){

        return (!TextUtils.isEmpty(id.trim()));
    }
    public static boolean isValidForSpeciality(String speciality){

        return (!TextUtils.isEmpty(speciality.trim()));
    }
    public static boolean isValidForWord(String word){

        return (!TextUtils.isEmpty(word.trim()));
    }
    public static boolean isValidNumber(String number) {

        String regExpn = "^[0-9]{1,24}$";
        CharSequence inputStr = number;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();

    }

}

