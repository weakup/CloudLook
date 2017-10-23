package com.example.lisiyan.cloudlook.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.example.lisiyan.cloudlook.MainActivity;
import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.databinding.ActivityTransitionBinding;
import com.example.lisiyan.cloudlook.utils.CommonUtils;
import com.example.lisiyan.cloudlook.utils.GlideOptionsUtils;
import com.jakewharton.rxbinding2.view.RxView;

import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lisiyan on 2017/10/23.
 */

public class TransitionActivity extends AppCompatActivity {

    private ActivityTransitionBinding mTransitionBinding;
    private boolean isIn;
    private boolean animationEnd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTransitionBinding = DataBindingUtil.setContentView(this, R.layout.activity_transition);
        mTransitionBinding.ivDefaultPic.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_transition_default));
        Glide.with(this)
                .load("")
                .apply(GlideOptionsUtils.getGlideOption())
                .into(mTransitionBinding.ivPic);


        Observable.timer(1500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mTransitionBinding.ivDefaultPic.setVisibility(View.GONE);
                    }
                });

        Observable.timer(3500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        toMainActivity();
                    }
                });

        RxView.clicks(mTransitionBinding.tvJump)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                        toMainActivity();
                    }
                });
        }

        private void toMainActivity(){

            if (isIn)
                return;

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.screen_zoom_in,R.anim.screen_zoom_out);
            finish();
            isIn = true;


    }

    private Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            animationEnd();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private void animationEnd(){

        synchronized (TransitionActivity.this){

            synchronized (TransitionActivity.this) {
                if (!animationEnd) {
                    animationEnd = true;
                    mTransitionBinding.ivPic.clearAnimation();
                    toMainActivity();
                }
            }


        }

    }

}

