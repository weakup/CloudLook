package com.example.lisiyan.cloudlook.utils;

import android.content.res.Resources;
import android.graphics.Color;

import com.example.lisiyan.cloudlook.app.CloudLookApplication;

import java.util.Random;

/**
 * Created by lisiyan on 2017/10/23.
 */

public class CommonUtils {

    public static Resources getResoure() {
        return CloudLookApplication.getInstance().getResources();
    }

    public static float getDimens(int resId) {
        return getResoure().getDimension(resId);
    }

    /**
     * 随机颜色
     */
    public static int randomColor() {
        Random random = new Random();
        int red = random.nextInt(150) + 50;//50-199
        int green = random.nextInt(150) + 50;//50-199
        int blue = random.nextInt(150) + 50;//50-199
        return Color.rgb(red, green, blue);
    }


}
