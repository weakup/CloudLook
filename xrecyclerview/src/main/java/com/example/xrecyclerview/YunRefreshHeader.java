package com.example.xrecyclerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by lisiyan on 2017/10/25.
 */

public class YunRefreshHeader extends LinearLayout implements BaseRefreshHeader{

    private Context mContext;
    private AnimationDrawable animationDrawable;
    private TextView msg;
    private int mMeasuredHeight;
    private int mState = STATE_NORMAL;
    private LinearLayout mContainer;


    public YunRefreshHeader(Context context) {
        this(context,null);
    }

    public YunRefreshHeader(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public YunRefreshHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView(){

        LayoutInflater.from(mContext).inflate(R.layout.kaws_refresh_header,this);
        ImageView imageView = findViewById(R.id.img);

        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        if (animationDrawable.isRunning()){
            animationDrawable.stop();
        }

        msg = findViewById(R.id.msg);
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();
        setGravity(Gravity.CENTER_HORIZONTAL);
        mContainer = findViewById(R.id.container);
        mContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0));
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onMove(float delta) {
        if (getVisiableHeight() > 0 || delta >0){
            setVisiableHeight((int) (delta + getVisiableHeight()));
            if (mState <= STATE_RELEASE_TO_REFRESH){ // 未处于刷新状态，更新箭头
                if (getVisiableHeight() > mMeasuredHeight){
                    setState(STATE_RELEASE_TO_REFRESH);
                }else {
                    setState(STATE_NORMAL);
                }
            }
        }

    }

    @Override
    public boolean releaseAction() {
        boolean isOnRefresh = false;
        int height = getVisiableHeight();
        if (height == 0) //not visible.
            isOnRefresh = false;

        if (getVisiableHeight() > mMeasuredHeight && mState < STATE_REFRESHING){
            setState(STATE_REFRESHING);
            isOnRefresh = true;
        }

        int destHeight = 0;// default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mState == STATE_REFRESHING){
            destHeight = mMeasuredHeight;
        }

        smoothScrollTo(destHeight);

        return isOnRefresh;
    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisiableHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisiableHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }


    @Override
    public void refreshComplate() {

        setState(STATE_DONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               reset();
            }
        },500);
    }

    @Override
    public int getVisiableHeight() {
        return mContainer.getHeight();
    }

    public int getState(){
        return mState;
    }

    private void  setState(int state){

        if (state ==mState) return;
        switch (state){
            case STATE_NORMAL:
                if (animationDrawable.isRunning()){
                    animationDrawable.stop();
                }
                msg.setText(R.string.listview_header_hint_normal);
                break;
            case STATE_RELEASE_TO_REFRESH:
                if (mState != STATE_RELEASE_TO_REFRESH){
                    if (!animationDrawable.isRunning()){
                        animationDrawable.start();
                    }
                    msg.setText(R.string.listview_header_hint_release);
                }
                break;
            case STATE_REFRESHING:
                msg.setText(R.string.refreshing);
                break;
            case STATE_DONE:
                msg.setText(R.string.refresh_done);
                break;
            default:
                break;

        }

        mState =state;
    }

    private void setVisiableHeight(int height){
        if (height < 0) height = 0;

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        lp.height = height;
        mContainer.setLayoutParams(lp);

    }


    public void reset() {
        smoothScrollTo(0);
        setState(STATE_NORMAL);
    }
}
