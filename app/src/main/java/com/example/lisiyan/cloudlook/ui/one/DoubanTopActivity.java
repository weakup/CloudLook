package com.example.lisiyan.cloudlook.ui.one;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.adapter.DouBanTopAdapter;
import com.example.lisiyan.cloudlook.base.BaseActivity;
import com.example.lisiyan.cloudlook.bean.HotMovieBean;
import com.example.lisiyan.cloudlook.databinding.ActivityDoubanTopBinding;
import com.example.lisiyan.cloudlook.http.HttpClient;
import com.example.xrecyclerview.XRecyclerView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lisiyan on 2017/11/27.
 */

public class DoubanTopActivity extends BaseActivity<ActivityDoubanTopBinding> {

    private DouBanTopAdapter mDouBanTopAdapter;
    private int mStart = 0;
    private int mCount = 21;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_douban_top);
        setTitle("豆瓣电影Top250");
        mDouBanTopAdapter = new DouBanTopAdapter(DoubanTopActivity.this);
        loadDouBanTop250();
        bindingView.xrvTop.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mStart += mCount;
                loadDouBanTop250();
            }
        });

    }

    private void loadDouBanTop250(){

        HttpClient.Builder.getDouBanService().getMovieTop250(mStart,mCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotMovieBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        addDisposable(d);
                    }

                    @Override
                    public void onNext(HotMovieBean hotMovieBean) {
                        if (mStart == 0) {
                            if (hotMovieBean != null && hotMovieBean.getSubjects() != null && hotMovieBean.getSubjects().size() > 0){

                                mDouBanTopAdapter.clear();
                                mDouBanTopAdapter.addAll(hotMovieBean.getSubjects());
                                //构造器中，第一个参数表示列数或者行数，第二个参数表示滑动方向,瀑布流
                                bindingView.xrvTop.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                                bindingView.xrvTop.setAdapter(mDouBanTopAdapter);
                                bindingView.xrvTop.setPullRefreshEnabled(false);
                                bindingView.xrvTop.clearHeader();
                                bindingView.xrvTop.setLoadingMoreEnabled(true);
                                mDouBanTopAdapter.notifyDataSetChanged();

                            }else {
                                bindingView.xrvTop.setVisibility(View.GONE);
                            }
                        }else {
                            if (hotMovieBean != null && hotMovieBean.getSubjects() != null && hotMovieBean.getSubjects().size() > 0){
                                bindingView.xrvTop.refreshComplete();
                                mDouBanTopAdapter.addAll(hotMovieBean.getSubjects());
                                mDouBanTopAdapter.notifyDataSetChanged();
                            }else {
                                bindingView.xrvTop.noMoreLoading();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        bindingView.xrvTop.refreshComplete();
                        if (mDouBanTopAdapter.getItemCount() == 0) {
                            showError();
                        }

                    }

                    @Override
                    public void onComplete() {

                        showContentView();
                    }
                });
    }


    @Override
    protected void onRefresh() {
        loadDouBanTop250();
    }

    public static void start(Context mContext) {
        Intent intent = new Intent(mContext, DoubanTopActivity.class);
        mContext.startActivity(intent);
    }


}
