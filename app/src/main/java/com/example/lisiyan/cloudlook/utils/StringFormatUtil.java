package com.example.lisiyan.cloudlook.utils;

import com.example.lisiyan.cloudlook.bean.moviechild.PersonBean;

import java.util.List;

/**
 * Created by lisiyan on 2017/11/23.
 */

public class StringFormatUtil {

    /**
     * 格式化导演、主演名字
     */

    public static String formatName(List<PersonBean> casts){

        if (casts != null && casts.size() >0){

            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < casts.size(); i++) {
                if (i < casts.size() - 1) {
                    stringBuilder.append(casts.get(i).getName()).append(" / ");
                } else {
                    stringBuilder.append(casts.get(i).getName());
                }
            }

            return stringBuilder.toString();
        }else {

            return "无名";
        }
    }

    /**
     * 格式化电影类型
     */
    public static String formatGenres(List<String> genres) {
        if (genres != null && genres.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < genres.size(); i++) {
                if (i < genres.size() - 1) {
                    stringBuilder.append(genres.get(i)).append(" / ");
                } else {
                    stringBuilder.append(genres.get(i));
                }
            }
            return stringBuilder.toString();

        } else {
            return "不知名类型";
        }
    }

}
