package com.example.lisiyan.cloudlook.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.lisiyan.cloudlook.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by lisiyan on 2017/11/13.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object url, ImageView imageView) {

        RequestOptions myOptions = new RequestOptions()
                .error(R.drawable.img_two_bi_one)
                .placeholder(R.drawable.img_two_bi_one);

        Glide.with(context).load(url)
                .apply(myOptions)
                .transition(new DrawableTransitionOptions().crossFade(1000))
                .into(imageView);


    }
}
