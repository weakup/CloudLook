package com.example.lisiyan.cloudlook.ui.gank.child;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.bumptech.glide.Glide;
import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.adapter.EverydayAdapter;
import com.example.lisiyan.cloudlook.app.Constants;
import com.example.lisiyan.cloudlook.base.BaseFragment;
import com.example.lisiyan.cloudlook.bean.AndroidBean;
import com.example.lisiyan.cloudlook.databinding.FragmentEverydayBinding;
import com.example.lisiyan.cloudlook.databinding.HeaderItemEverydayBinding;
import com.example.lisiyan.cloudlook.http.RequestImpl;
import com.example.lisiyan.cloudlook.http.cache.ACache;
import com.example.lisiyan.cloudlook.model.EverydayModel;
import com.example.lisiyan.cloudlook.utils.SPUtils;
import com.example.lisiyan.cloudlook.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by lisiyan on 2017/10/30.
 */

public class EverydayFragment extends BaseFragment<FragmentEverydayBinding> {

    private ACache maCache;
    private RotateAnimation animation;
    private HeaderItemEverydayBinding mHeaderBinding;
    private ArrayList<List<AndroidBean>> mLists;
    private EverydayModel mEverydayModel;
    private View mHeaderView;
    private View mFooterView;
    private EverydayAdapter mEverydayAdapter;
    private boolean mIsPrepared = false;
    private boolean mIsFirst = true;
    // 是否是上一天的请求
    private boolean isOldDayRequest;
    // 记录请求的日期
    String year = getTodayTime().get(0);
    String month = getTodayTime().get(1);
    String day = getTodayTime().get(2);


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

        maCache = ACache.get(getContext());
        mEverydayModel = new EverydayModel();



        mHeaderBinding = DataBindingUtil
                .inflate(LayoutInflater.from(getContext()),R.layout.header_item_everyday,null,false);
        initLocalSetting();
        initRecycleView();
        mIsPrepared = true;
        /**
         * 因为启动时先走loadData()再走onActivityCreated，
         * 所以此处要额外调用load(),不然最初不会加载内容
         */
        loadData();


    }

    private void initLocalSetting(){

        mEverydayModel.setData(getTodayTime().get(0), getTodayTime().get(1), getTodayTime().get(2));
//去掉日前面的0
        mHeaderBinding.includeEveryday.tvDailyText.setText(getTodayTime().get(2).indexOf("0") == 0?
                getTodayTime().get(2).replace("0", "") : getTodayTime().get(2));
        mHeaderBinding.includeEveryday.ibXiadu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
        String oneData = SPUtils.getString("everyday_data", "2016-11-26");
        if (!oneData.equals(TimeUtil.getData())){// 是第二天
            if (TimeUtil.isRightTime()){//大于12：30,请求

                isOldDayRequest = false;
                mEverydayModel.setData(getTodayTime().get(0),getTodayTime().get(1),getTodayTime().get(2));
                showRotaLoading(true);
                showContentData();
            } else {// 小于12:30，取缓存没有请求前一天

                List<String> lastTime = TimeUtil.getLastTime(getTodayTime().get(0),getTodayTime().get(1),getTodayTime().get(2));
                mEverydayModel.setData(lastTime.get(0),lastTime.get(1),lastTime.get(2));
                year = lastTime.get(0);
                month = lastTime.get(1);
                day = lastTime.get(2);

                isOldDayRequest = true;// 是昨天
                getACacheData();

            }
        }else {// 当天，取缓存

            isOldDayRequest = false;
            getACacheData();
        }

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

                    requestBeforeData();
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

    //网络请求没取到 先取缓存，再取前一天数据

    private void requestBeforeData(){

        mLists = (ArrayList<List<AndroidBean>>) maCache.getAsObject(Constants.EVERYDAY_CONTENT);
        if (mLists != null && mLists.size() >0){
            setAdapter(mLists);
        }else {
            // 一直请求，知道请求到数据为止
            ArrayList<String> lastTime = TimeUtil.getLastTime(year, month, day);
            mEverydayModel.setData(lastTime.get(0), lastTime.get(1), lastTime.get(2));
            year = lastTime.get(0);
            month = lastTime.get(1);
            day = lastTime.get(2);
            showContentData();
        }
    }

    private void getACacheData(){

        if (!mIsFirst){
            return;
        }

        mLists = (ArrayList<List<AndroidBean>>) maCache.getAsObject(Constants.EVERYDAY_CONTENT);
        if (mLists != null && mLists.size() >0){

            setAdapter(mLists);
        }else {

            showRotaLoading(true);
            showContentData();
        }

    }
//正在为你开启干货
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

    private void setAdapter(ArrayList<List<AndroidBean>> lists){

      showRotaLoading(false);
      if (mEverydayAdapter == null){
          mEverydayAdapter = new EverydayAdapter(getContext());
      }else {
          mEverydayAdapter.clear();
      }

      mEverydayAdapter.addAll(lists);
      //只缓存一天的数据，更新，缓存3天
      maCache.remove(Constants.EVERYDAY_CONTENT);
      maCache.put(Constants.EVERYDAY_CONTENT,lists,259200);
      if (isOldDayRequest){
          List<String> lastTime = TimeUtil.getLastTime(getTodayTime().get(0), getTodayTime().get(1), getTodayTime().get(2));
          SPUtils.putString("everyday_data", lastTime.get(0) + "-" + lastTime.get(1) + "-" + lastTime.get(2));
      }else {
        // 保存请求的日期
          SPUtils.putString("everyday_data", TimeUtil.getData());
      }
      mIsFirst = false;
      bindingView.xrvEveryday.setAdapter(mEverydayAdapter);
      mEverydayAdapter.notifyDataSetChanged();

    }

    private List<String> getTodayTime(){

        String data = TimeUtil.getData();
        String[] split = data.split("-");
        String year = split[0];
        String month = split[1];
        String day = split[2];
        List<String> list = new ArrayList<>();
        list.add(year);
        list.add(month);
        list.add(day);
        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        bindingView.xrvEveryday.setFocusable(false);
        Glide.with(getActivity()).resumeRequests();
    }

    @Override
    public void onPause() {
        super.onPause();
        Glide.with(getActivity()).pauseRequests();
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
        showContentView();
        showRotaLoading(true);
        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
