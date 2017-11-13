package com.example.lisiyan.cloudlook.utils;

import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by lisiyan on 2017/11/13.
 */

public class TimeUtil {

    /**
     * 获取当前日期
     */
    public static String getData() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new Date());
        return date;
    }

     /**
     * 获取当前时间是否大于12：30
     */
    public static boolean isRightTime() {

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return hour > 12 || (hour == 12 && minute >= 30);
    }

    /**
     * 得到上一天的时间
     */
    public static ArrayList<String> getLastTime(String year, String month, String day) {
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.set(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));//月份是从0开始的，所以11表示12月

        //使用roll方法进行向前回滚
        //cl.roll(Calendar.DATE, -1);
        //使用set方法直接进行设置
        int inDay = ca.get(Calendar.DATE);
        ca.set(Calendar.DATE, inDay - 1);

        ArrayList<String> list = new ArrayList<>();
        list.add(String.valueOf(ca.get(Calendar.YEAR)));
        list.add(String.valueOf(ca.get(Calendar.MONTH) + 1));
        list.add(String.valueOf(ca.get(Calendar.DATE)));
        return list;
    }
}
