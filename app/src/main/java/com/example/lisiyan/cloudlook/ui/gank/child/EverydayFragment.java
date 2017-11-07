package com.example.lisiyan.cloudlook.ui.gank.child;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.adapter.EverydayAdapter;
import com.example.lisiyan.cloudlook.base.BaseFragment;
import com.example.lisiyan.cloudlook.bean.AndroidBean;
import com.example.lisiyan.cloudlook.databinding.FragmentEverydayBinding;
import com.example.lisiyan.cloudlook.databinding.HeaderItemEverydayBinding;
import com.example.lisiyan.cloudlook.http.RequestImpl;
import com.example.lisiyan.cloudlook.model.EverydayModel;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by lisiyan on 2017/10/30.
 */

public class EverydayFragment extends BaseFragment<FragmentEverydayBinding> {

    private RotateAnimation animation;
    private HeaderItemEverydayBinding mHeaderBinding;
    private ArrayList<List<AndroidBean>> mLists;
    private EverydayModel mEverydayModel;
    private View mHeaderView;
    private View mFooterView;
    private EverydayAdapter mEverydayAdapter;
    private boolean mIsPrepared = false;


    @Override
    public int setContent() {
        return R.layout.fragment_everyday;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        bindingView.llLoading.setVisibility(View.VISIBLE);
        //fromDegrees和toDegrees：这两个分别是旋转的起始角度和结束角度。
        //pivotXType和pivotYType：X,Y轴的伸缩模式，定义了pivotXValue和pivotYValue怎么被使用
        //pivotXValue和pivotYValue：在X,Y方向的位置,自身的x的一般，y的一半
        animation = new RotateAnimation(0f,360f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(3000);//持续时间
        animation.setInterpolator(new LinearInterpolator());//不停顿
        animation.setRepeatCount(10);
        bindingView.ivLoading.setAnimation(animation);
        animation.startNow();
        mEverydayModel = new EverydayModel();
        mHeaderBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.header_item_everyday,null,false);

        initRecycleView();
        mIsPrepared = true;
        /**
         * 因为启动时先走loadData()再走onActivityCreated，
         * 所以此处要额外调用load(),不然最初不会加载内容
         */
        loadData();


    }

    private void initRecycleView(){

        bindingView.xrvEveryday.setPullRefreshEnabled(false);
        bindingView.xrvEveryday.setLoadingMoreEnabled(false);

        if (mHeaderView == null){

            mHeaderView = mHeaderBinding.getRoot();
            bindingView.xrvEveryday.addHeaderView(mHeaderView);
        }


        bindingView.xrvEveryday.setLayoutManager(new LinearLayoutManager(getContext()));

        //设置嵌套滑动
        bindingView.xrvEveryday.setNestedScrollingEnabled(false);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        bindingView.xrvEveryday.setHasFixedSize(false);
        bindingView.xrvEveryday.setItemAnimator(new DefaultItemAnimator());


    }


    @Override
    protected void loadData() {

        if (!mIsVisible || !mIsPrepared) {
            return;
        }

//        showRotaLoading(true);
        showContentData();

    }

    private void showContentData() {

        mEverydayModel.showRecyclerViewData(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {

                if (mLists != null){
                    mLists.clear();
                }

                mLists = (ArrayList<List<AndroidBean>>) object;

                if (mLists.size() > 0 && mLists.get(0).size() > 0){

                    setAdapter(mLists);
                }else {

                }

            }

            @Override
            public void loadFailed() {

                if (mLists !=null && mLists.size() > 0){

                    return;
                }

                showError();

            }

            @Override
            public void addSubscription(Disposable d) {

                EverydayFragment.this.addDisposable(d);
            }
        });
    }

    private void showRotaLoading(boolean isLoading) {
        if (isLoading) {
            bindingView.llLoading.setVisibility(View.VISIBLE);
            bindingView.xrvEveryday.setVisibility(View.GONE);
            animation.startNow();
        } else {
            bindingView.llLoading.setVisibility(View.GONE);
            bindingView.xrvEveryday.setVisibility(View.VISIBLE);
            animation.cancel();
        }
    }

    private void setAdapter(List<List<AndroidBean>> lists){

      showRotaLoading(false);
      if (mEverydayAdapter == null){
          mEverydayAdapter = new EverydayAdapter(getContext());
      }else {
          mEverydayAdapter.clear();
      }

      mEverydayAdapter.addAll(lists);
      bindingView.xrvEveryday.setAdapter(mEverydayAdapter);
      mEverydayAdapter.notifyDataSetChanged();

    }



}
