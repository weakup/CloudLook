package com.example.lisiyan.cloudlook.ui.one;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.example.lisiyan.cloudlook.MainActivity;
import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.adapter.OneAdapter;
import com.example.lisiyan.cloudlook.app.Constants;
import com.example.lisiyan.cloudlook.app.ConstantsImageUrl;
import com.example.lisiyan.cloudlook.base.BaseFragment;
import com.example.lisiyan.cloudlook.bean.HotMovieBean;
import com.example.lisiyan.cloudlook.databinding.FragmentOneBinding;
import com.example.lisiyan.cloudlook.http.HttpClient;
import com.example.lisiyan.cloudlook.http.cache.ACache;
import com.example.lisiyan.cloudlook.utils.ImgLoadUtil;
import com.example.lisiyan.cloudlook.utils.SPUtils;
import com.example.lisiyan.cloudlook.utils.TimeUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lisiyan on 2017/10/26.
 */

public class OneFragment extends BaseFragment<FragmentOneBinding>{

    // 初始化完成后加载数据
    private boolean isPrepared = false;
    // 是否正在刷新（用于刷新数据时返回页面不再刷新）
    private boolean mIsLoading = false;
    // 第一次显示时加载数据，第二次不显示
    private boolean isFirst = true;
    private MainActivity activity;
    private ACache aCache;
    private View mHeaderView = null;
    private OneAdapter oneAdapter;
    private HotMovieBean mHotMovieBean;


    @Override
    public int setContent() {
        return R.layout.fragment_one;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        aCache = ACache.get(getActivity());
        oneAdapter = new OneAdapter(activity);
        mHotMovieBean = (HotMovieBean) aCache.getAsObject(Constants.ONE_HOT_MOVIE);
        isPrepared = true;

    }

    /**
     * 懒加载
     * 从此页面新开activity界面返回此页面 不会走这里
     */

    @Override
    protected void loadData() {
        if (!isPrepared || !mIsVisible) {
            return;
        }

        // 显示，准备完毕，不是当天，则请求数据（正在请求时避免再次请求）
        String oneData = SPUtils.getString("one_data", "2016-11-26");
        if (!oneData.equals(TimeUtil.getData()) && !mIsLoading){
            showLoading();
            /**延迟执行防止卡顿*/
            postDelayLoad();
        }else{
            if(mIsLoading){
                return;
            }
            if (!isFirst){
                return;
            }
            showLoading();
            if (mHotMovieBean == null && !mIsLoading){
                postDelayLoad();
            }else {

                bindingView.listOne.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this){
                            setAdapter(mHotMovieBean);
                            showContentView();
                        }
                    }
                },150);
            }

        }
    }

    private void loadHotMovie(){
        HttpClient.Builder.getDouBanService().getHotMovie()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotMovieBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(HotMovieBean hotMovieBean) {

                        if (hotMovieBean != null){

                            aCache.remove(Constants.ONE_HOT_MOVIE);
                            // 保存12个小时
                            aCache.put(Constants.ONE_HOT_MOVIE,hotMovieBean,43200);
                            setAdapter(hotMovieBean);
                            // 保存请求的日期
                            SPUtils.putString("one_data", TimeUtil.getData());
                            // 刷新结束
                            mIsLoading = false;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showContentView();
                        if (oneAdapter != null && oneAdapter.getItemCount() == 0){
                            showError();
                        }

                    }

                    @Override
                    public void onComplete() {

                        showContentView();
                    }
                });
    }

    private void postDelayLoad(){
        synchronized (this){
            if (!mIsLoading){
                mIsLoading = true;
                bindingView.listOne.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadHotMovie();
                    }
                },150);
            }
        }
    }

    private void setAdapter(HotMovieBean hotMovieBean){

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        bindingView.listOne.setLayoutManager(manager);
        // 加上这两行代码，下拉出提示才不会产生出现刷新头的bug，不加拉不下来
        bindingView.listOne.setPullRefreshEnabled(false);
        bindingView.listOne.clearHeader();
        bindingView.listOne.setLoadingMoreEnabled(false);

        // 需加，不然滑动不流畅
        bindingView.listOne.setNestedScrollingEnabled(false);
        bindingView.listOne.setHasFixedSize(false);

        if (mHeaderView == null){

            mHeaderView = View.inflate(getContext(),R.layout.header_item_one,null);
            View llMovieTop = mHeaderView.findViewById(R.id.ll_moview_top);
            ImageView ivImg = mHeaderView.findViewById(R.id.iv_img);
            ImgLoadUtil.displayRandom(3, ConstantsImageUrl.ONE_URL_01,ivImg);

            RxView.clicks(llMovieTop)
                    .throttleFirst(1000, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {

                        }
                    });
        }


        bindingView.listOne.addHeaderView(mHeaderView);
        oneAdapter.clear();
        oneAdapter.addAll(hotMovieBean.getSubjects());
        bindingView.listOne.setAdapter(oneAdapter);
        oneAdapter.notifyDataSetChanged();

        isFirst = false;

        }


    @Override
    protected void onRefresh() {
        loadHotMovie();
    }
}

