package com.example.lisiyan.cloudlook.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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

    private static int getDefaultPic(int type){

        switch (type) {
            case 0:// 电影
                return R.drawable.img_default_movie;
            case 1:// 妹子
                return R.drawable.img_default_meizi;
            case 2:// 书籍
                return R.drawable.img_default_book;
        }
        return R.drawable.img_default_meizi;
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

    /**
     * 用于干货item，将gif图转换为静态图
     */
    public static void displayGif(String url, ImageView imageView) {

        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.img_one_bi_one)
                .placeholder(R.drawable.img_one_bi_one);

        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }

    /**
     * 书籍、妹子图、电影列表图
     * 默认图区别
     */
    public static void displayEspImage(String url, ImageView imageView, int type){

        RequestOptions requestOptions = new RequestOptions()
                .error(getDefaultPic(type))
                .placeholder(getDefaultPic(type));

        Glide.with(imageView.getContext())
                .load(url)
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

    /**
     * 妹子，电影列表图
     *
     * @param defaultPicType 电影：0；妹子：1； 书籍：2
     * 1. 方法名可与注解名一样，也可不一样
     * 2. 第一个参数必须是View，就是自定义属性所在的View
     * 3. 第二个参数就是自定义属性的值，与注解值对应。这是数组，可多个
     * displayFadeImage -> url   defaultPictype -> de...
     */

    @BindingAdapter({"android:displayFadeImage", "android:defaultPicType"})
    public static void displayFadeImage(ImageView imageView,String url,int defaultPicType){
        displayEspImage(url,imageView,defaultPicType);
    }

    /**
     * 电影列表图片
     */
    @BindingAdapter("android:showMovieImg")
    public static void showMovieImg(ImageView imageView, String url) {

        RequestOptions requestOptions = new RequestOptions()
                .override((int)CommonUtils.getDimens(R.dimen.movie_detail_width), (int) CommonUtils.getDimens(R.dimen.movie_detail_height))
                .error(getDefaultPic(0))
                .placeholder(getDefaultPic(0));


        Glide.with(imageView.getContext())
                .load(url)
                .transition(new DrawableTransitionOptions().crossFade(1500))
                .apply(requestOptions)
                .into(imageView);
    }



}
