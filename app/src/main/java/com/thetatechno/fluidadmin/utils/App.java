package com.thetatechno.fluidadmin.utils;

import android.app.Application;
import android.content.Context;

import com.yariksoffice.lingver.Lingver;

import java.util.Locale;

import static com.thetatechno.fluidadmin.utils.Constants.ARABIC;
import static com.thetatechno.fluidadmin.utils.Constants.ENGLISH;



public class App extends Application {
    private static App mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
//        if (!PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(ENGLISH)) {
//            LanguageUtil.changeLanguageType(mContext, new Locale(ARABIC));
//        } else {
//            LanguageUtil.changeLanguageType(mContext, Locale.ENGLISH);
//        }
        Lingver.init(mContext, Locale.ENGLISH);

    }

    public static Context getContext() {
        return mContext;
    }
}
