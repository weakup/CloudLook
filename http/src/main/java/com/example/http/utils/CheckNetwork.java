package com.example.http.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by lisiyan on 2017/10/30.
 */

public class CheckNetwork {

    public static boolean isNetworkConnected(Context context){

        if (context != null){

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();

            return info !=null && info.isConnected();
        }else {

            return false;
        }

    }
}
