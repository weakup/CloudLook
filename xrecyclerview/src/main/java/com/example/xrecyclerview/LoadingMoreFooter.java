package com.example.xrecyclerview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by lisiyan on 2017/11/1.
 */

public class LoadingMoreFooter extends LinearLayout {

    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;
    private TextView mTextView;
    private AnimationDrawable mAnimationDrawable;
    private ImageView mIvProgress;


    public LoadingMoreFooter(Context context) {
        super(context);
        initView(context);
    }

    public LoadingMoreFooter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadingMoreFooter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView(Context context){

        LayoutInflater.from(context).inflate(R.layout.yun_refresh_footer,this);
        mTextView = findViewById(R.id.msg);
        mIvProgress = findViewById(R.id.iv_progress);
        mAnimationDrawable = (AnimationDrawable) mIvProgress.getDrawable();
        if (!mAnimationDrawable.isRunning()){
            mAnimationDrawable.start();;
        }

        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


    }

    public void setState(int state){

        switch (state){

            case STATE_LOADING:
                if (!mAnimationDrawable.isRunning()){
                    mAnimationDrawable.start();
                }
            mIvProgress.setVisibility(VISIBLE);
            mTextView.setText(getContext().getText(R.string.listview_loading));
            this.setVisibility(VISIBLE);
            break;

            case STATE_COMPLETE:
                if (mAnimationDrawable.isRunning()){
                    mAnimationDrawable.stop();
                }
                mTextView.setText(getContext().getText(R.string.listview_loading));
                this.setVisibility(View.GONE);
                break;

            case STATE_NOMORE:
                if (mAnimationDrawable.isRunning()){
                    mAnimationDrawable.stop();
                }
                mTextView.setText(getContext().getText(R.string.nomore_loading));
                mIvProgress.setVisibility(GONE);
                this.setVisibility(VISIBLE);
                break;
        }

    }

    public void reSet(){

        this.setVisibility(GONE);
    }
}
