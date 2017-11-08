package com.example.lisiyan.cloudlook.app;

import android.app.Application;

import com.example.http.HttpUtils;

/**
 * Created by lisiyan on 2017/11/7.
 */

public class CloudLookApplication extends Application {

    private static CloudLookApplication cloudLookApplication;

    public static CloudLookApplication getInstance(){
        return cloudLookApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cloudLookApplication = this;
        HttpUtils.getInstance().init(this);
    }
}
