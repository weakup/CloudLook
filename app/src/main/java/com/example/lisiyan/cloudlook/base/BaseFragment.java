package com.example.lisiyan.cloudlook.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.lisiyan.cloudlook.R;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.subscriptions.ArrayCompositeSubscription;

/**
 * Created by lisiyan on 2017/10/25.
 */

public abstract class BaseFragment<SV extends ViewDataBinding> extends Fragment {

    // 布局view
    protected SV bindingView;
    // fragment是否显示了
    protected boolean mIsVisible = false;
    // 加载中
    private LinearLayout mLlProgressBar;
    // 加载失败
    private LinearLayout mRefresh;
    // 内容布局
    protected RelativeLayout mContainer;
    // 动画
    private AnimationDrawable mAnimationDrawable;

    private CompositeDisposable mCompositeDisposable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base,null);
        bindingView = DataBindingUtil.inflate(getActivity().getLayoutInflater(),setContent(),null,false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        mContainer = view.findViewById(R.id.container);
        mContainer.addView(bindingView.getRoot());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLlProgressBar = getView(R.id.ll_progress_bar);
        ImageView img = getView(R.id.img_progress);

        // 加载动画
        mAnimationDrawable = (AnimationDrawable) img.getDrawable();
        // 默认进入页面就开启动画
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }

        mRefresh = getView(R.id.ll_error_refresh);
        RxView.clicks(mRefresh)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                        showLoading();
                        onRefresh();
                    }
                });

        bindingView.getRoot().setVisibility(View.GONE);
    }

    protected <T extends View> T getView(int id){

        return (T)getView().findViewById(id);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            mIsVisible = true;
            onVisible();
        }else {
            mIsVisible = false;
            onInvisible();
        }
    }

    // 努力加载中~
    protected void showLoading(){
        if (mLlProgressBar.getVisibility() != View.VISIBLE){
            mLlProgressBar.setVisibility(View.VISIBLE);
        }
        //开始动画
        if (!mAnimationDrawable.isRunning()){
            mAnimationDrawable.start();
        }
        if (bindingView.getRoot().getVisibility() != View.GONE){
            bindingView.getRoot().setVisibility(View.GONE);
        }
        if (mRefresh.getVisibility()!=View.GONE){
            mRefresh.setVisibility(View.GONE);
        }
    }

    protected void showContentView(){
        if (mLlProgressBar.getVisibility() != View.GONE){
            mLlProgressBar.setVisibility(View.GONE);
        }
        //停止动画
        if (mAnimationDrawable.isRunning()){
            mAnimationDrawable.stop();
        }
        if (mRefresh.getVisibility() != View.GONE){
            mRefresh.setVisibility(View.GONE);
        }
        if (bindingView.getRoot().getVisibility() != View.VISIBLE){
            bindingView.getRoot().setVisibility(View.VISIBLE);
        }
    }

    protected void showError(){

        if (mLlProgressBar.getVisibility() != View.GONE){
            mLlProgressBar.setVisibility(View.GONE);
        }

        if (mAnimationDrawable.isRunning()){
            mAnimationDrawable.stop();
        }

        if (mRefresh.getVisibility() != View.VISIBLE){
            mRefresh.setVisibility(View.VISIBLE);
        }

        if (bindingView.getRoot().getVisibility() != View.GONE){
            bindingView.getRoot().setVisibility(View.GONE);
        }

    }

    public void addDisposable(Disposable d){
        if (this.mCompositeDisposable ==null){
            this.mCompositeDisposable = new CompositeDisposable();
        }
        this.mCompositeDisposable.add(d);
    }

    public void removeDisposable(Disposable d){
        //不确定对不对
        if (this.mCompositeDisposable !=null && this.mCompositeDisposable.size()>0){
            this.mCompositeDisposable.remove(d);
        }
    }

    protected void onInvisible() {
    }

    /**
     * 显示时加载数据,需要这样的使用
     * 注意声明 isPrepared，先初始化
     * 生命周期会先执行 setUserVisibleHint 再执行onActivityCreated
     * 在 onActivityCreated 之后第一次显示加载数据，只加载一次
     */
    protected void loadData() {
    }

    protected void onVisible() {
        loadData();
    }

    public abstract int setContent();

    /**
     * 加载失败后点击后的操作
     */
    protected void onRefresh() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mCompositeDisposable !=null && mCompositeDisposable.size()>0){
            this.mCompositeDisposable.clear();
        }
    }
}
