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
import com.example.lisiyan.cloudlook.bean.FrontpageBean;
import com.example.lisiyan.cloudlook.databinding.FragmentEverydayBinding;
import com.example.lisiyan.cloudlook.databinding.HeaderItemEverydayBinding;
import com.example.lisiyan.cloudlook.http.RequestImpl;
import com.example.lisiyan.cloudlook.http.cache.ACache;
import com.example.lisiyan.cloudlook.http.rx.RxBus;
import com.example.lisiyan.cloudlook.http.rx.RxBusBaseMessage;
import com.example.lisiyan.cloudlook.http.rx.RxCodeConstants;
import com.example.lisiyan.cloudlook.model.EverydayModel;
import com.example.lisiyan.cloudlook.utils.GlideImageLoader;
import com.example.lisiyan.cloudlook.utils.SPUtils;
import com.example.lisiyan.cloudlook.utils.TimeUtil;
import com.example.lisiyan.cloudlook.view.webview.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by lisiyan on 2017/10/30.
 *  每日推荐
 */

public class EverydayFragment extends BaseFragment<FragmentEverydayBinding> {

    private ACache maCache;
    private RotateAnimation animation;
    private HeaderItemEverydayBinding mHeaderBinding;
    private ArrayList<String> mBannerImages;
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
        mBannerImages = (ArrayList<String>) maCache.getAsObject(Constants.BANNER_PIC);


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

        mHeaderBinding.includeEveryday.ibXiandu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.loadUrl(v.getContext(), "https://gank.io/xiandu", "加载中...");
            }
        });

        mHeaderBinding.includeEveryday.ibMovieHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post(RxCodeConstants.JUMP_TYPE_TO_ONE, new RxBusBaseMessage());
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

    private void loadBannerPicture(){

        mEverydayModel.showBanncerPage(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                if (mBannerImages == null){
                    mBannerImages = new ArrayList<>();
                }else {

                    mBannerImages.clear();
                }

                FrontpageBean bean = (FrontpageBean) object;
                if (bean != null && bean.getResult() != null && bean.getResult().getFocus() != null && bean.getResult().getFocus().getResult() != null){
                    final List<FrontpageBean.ResultBeanXXXXXXXXXXXXXXX.FocusBean.ResultBeanXXXXXXXXXXX> result = bean.getResult().getFocus().getResult();
                    if (result !=null && result.size() > 0){
                        for (int i=0 ; i< result.size(); i++){
                            //获取所有图片
                            mBannerImages.add(result.get(i).getRandpic());
                        }

                        mHeaderBinding.banner.setImages(mBannerImages).setImageLoader(new GlideImageLoader()).start();
                        maCache.remove(Constants.BANNER_PIC);
                        maCache.put(Constants.BANNER_PIC, mBannerImages, 30000);
                    }
                }
            }

            @Override
            public void loadFailed() {

            }

            @Override
            public void addSubscription(Disposable d) {

                EverydayFragment.this.addDisposable(d);
            }
        });

    }


    @Override
    protected void loadData() {

        // 显示时轮播图滚动
        if (mHeaderBinding != null && mHeaderBinding.banner != null) {
            mHeaderBinding.banner.startAutoPlay();
            mHeaderBinding.banner.setDelayTime(4000);
        }

        if (!mIsVisible || !mIsPrepared) {
            return;
        }
        String oneData = SPUtils.getString("everyday_data", "2016-11-26");
        if (!oneData.equals(TimeUtil.getData())){// 是第二天
            if (TimeUtil.isRightTime()){//大于12：30,请求

                isOldDayRequest = false;
                mEverydayModel.setData(getTodayTime().get(0),getTodayTime().get(1),getTodayTime().get(2));
                showRotaLoading(true);
                loadBannerPicture();
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

    /**
     * 加载正文内容
     */
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
//丑到爆的备份访问
                mEverydayModel.showBackRecyclerViewData(new RequestImpl() {
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

                        showError();

                    }

                    @Override
                    public void addSubscription(Disposable d) {

                        EverydayFragment.this.addDisposable(d);
                    }
                });

//                showError();

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

        if (mBannerImages != null && mBannerImages.size() > 0) {
            mHeaderBinding.banner.setImages(mBannerImages).setImageLoader(new GlideImageLoader()).start();
        } else {
            loadBannerPicture();
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
    protected void onInvisible() {
        // 不可见时轮播图停止滚动
        if (mHeaderBinding != null && mHeaderBinding.banner != null) {
            mHeaderBinding.banner.stopAutoPlay();
        }
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
