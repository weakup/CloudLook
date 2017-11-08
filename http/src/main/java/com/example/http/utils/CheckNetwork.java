package com.example.http.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by lisiyan on 2017/10/30.
 */

public class CheckNetwork {

    /**
     * 判断网络是否连通
     */
    public static boolean isNetworkConnected(Context context){

        if (context != null){
            @SuppressWarnings("static-access")
            ConnectivityManager cm = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();

            return info !=null && info.isConnected();
        }else {

            return false;
        }

    }
}
