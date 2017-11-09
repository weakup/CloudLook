package com.example.lisiyan.cloudlook.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.lisiyan.cloudlook.R;

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


    /**
     *
     * @param imgNumber  多大尺寸的图
     * @param imageUrl
     * @param imageView
     */
    public static void displayRandom(int imgNumber , String imageUrl , ImageView imageView){

        RequestOptions requestOptions = new RequestOptions()
                .error(getMusicDefaultPic(imgNumber))
                .placeholder(getMusicDefaultPic(imgNumber));

        Glide.with(imageView.getContext())
                .load(imageUrl)
                .transition(new DrawableTransitionOptions().crossFade(1500))
                .apply(requestOptions)
                .into(imageView);


    }

    private static int getMusicDefaultPic(int imgNumber) {
        switch (imgNumber) {
            case 1:
                return R.drawable.img_two_bi_one;
            case 2:
                return R.drawable.img_four_bi_three;
            case 3:
                return R.drawable.img_one_bi_one;
        }
        return R.drawable.img_four_bi_three;
    }

}
