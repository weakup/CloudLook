package com.example.lisiyan.cloudlook.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.example.lisiyan.cloudlook.R;

/**
 * Created by lisiyan on 2017/10/23.
 */

public class GlideOptionsUtils{

    public static RequestOptions getGlideOption(){

        RequestOptions myOptions = new RequestOptions()
                .error(R.drawable.img_transition_default)
                .placeholder(R.drawable.img_transition_default);

        return myOptions;
    }

    public static RequestOptions getCircleOption(Context context){

        RequestOptions myOptions = new RequestOptions()
                .error(R.drawable.ic_avatar_default)
                .transform(new GlideCircleTransform(context));

        return myOptions;
    }

}
