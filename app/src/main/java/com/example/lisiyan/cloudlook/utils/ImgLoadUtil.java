package com.example.lisiyan.cloudlook.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by lisiyan on 2017/10/24.
 */

public class ImgLoadUtil {

    private static ImgLoadUtil instance;

    private ImgLoadUtil(){

    }

    public static ImgLoadUtil getInstance(){
        if (instance ==null){
            synchronized (ImgLoadUtil.class){
                if (instance==null){
                    instance = new ImgLoadUtil();
                }
            }
        }
        return instance;
    }

    public static void displayCirlce(ImageView imageView,String imageUrl){
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .apply(GlideOptionsUtils.getCircleOption(imageView.getContext()))
                .into(imageView);

    }
}
