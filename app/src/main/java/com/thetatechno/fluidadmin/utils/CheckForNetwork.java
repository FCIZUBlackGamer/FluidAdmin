package com.thetatechno.fluidadmin.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class CheckForNetwork {
    /*
     * Check if there is Internet connection or not
     *
     * @param activity parent activity
     * @return true if the internet connection is one
     * false if the internet connection is off
     */
    public static boolean isConnectionOn(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
