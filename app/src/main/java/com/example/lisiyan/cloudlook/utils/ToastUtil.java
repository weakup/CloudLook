package com.example.lisiyan.cloudlook.utils;

import android.widget.Toast;

import com.example.lisiyan.cloudlook.app.CloudLookApplication;

/**
 * Created by lisiyan on 2017/11/22.
 */

public class ToastUtil {

    private static Toast mToast;

    public static void showToast(String text){

        if (mToast == null){

            mToast = Toast.makeText(CloudLookApplication.getInstance(),text,Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.show();

    }
}
