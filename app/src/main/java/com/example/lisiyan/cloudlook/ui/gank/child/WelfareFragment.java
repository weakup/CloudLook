package com.example.lisiyan.cloudlook.ui.gank.child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.http.HttpUtils;
import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.adapter.WelfareAdapter;
import com.example.lisiyan.cloudlook.app.Constants;
import com.example.lisiyan.cloudlook.base.BaseFragment;
import com.example.lisiyan.cloudlook.base.baseadapter.OnItemClickListener;
import com.example.lisiyan.cloudlook.bean.GankIoDataBean;
import com.example.lisiyan.cloudlook.databinding.FragmentWelfareBinding;
import com.example.lisiyan.cloudlook.http.RequestImpl;
import com.example.lisiyan.cloudlook.http.cache.ACache;
import com.example.lisiyan.cloudlook.model.GankOtherModel;
import com.example.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by lisiyan on 2017/11/20.
 */

public class WelfareFragment extends BaseFragment<FragmentWelfareBinding>{

    private GankOtherModel model;
    private GankIoDataBean meiziBean;
    private int mPage = 1;
    private ACache aCache;
    private WelfareAdapter mAdapter;
    private boolean isFirst = true;
    private boolean isPrepared = false;


    @Override
    public int setContent() {
        return R.layout.fragment_welfare;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new GankOtherModel();
        aCache = ACache.get(getContext());
        bindingView.xrvWelfare.setPullRefreshEnabled(false);
        bindingView.xrvWelfare.clearHeader();
        mAdapter = new WelfareAdapter();

        bindingView.xrvWelfare.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPage++;
                loadWelfareData();
            }
        });

        isPrepared = true;
    }

    List<String> imgList = new ArrayList<>();

    private void  loadWelfareData(){

        model.setData("福利",mPage, HttpUtils.per_page_more);
        model.getGankIoData(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                showContentView();
                GankIoDataBean gankIoDataBean = (GankIoDataBean) object;
                if (mPage == 1){
                    if (gankIoDataBean != null && gankIoDataBean.getResults() !=null &&
                            gankIoDataBean.getResults().size() > 0){
                       imgList.clear();
                       for (int i=0 ; i<gankIoDataBean.getResults().size();i++){
                           imgList.add(gankIoDataBean.getResults().get(i).getUrl());
                       }
                        setmAdapter(gankIoDataBean);
                        aCache.remove(Constants.GANK_MEIZI);
                        aCache.put(Constants.GANK_MEIZI, gankIoDataBean, 30000);
                    }
                }else {
                    if (gankIoDataBean != null && gankIoDataBean.getResults() != null &&
                            gankIoDataBean.getResults().size() > 0) {
                        bindingView.xrvWelfare.refreshComplete();
                        mAdapter.addAll(gankIoDataBean.getResults());
                        mAdapter.notifyDataSetChanged();

                        for (int i = 0; i < gankIoDataBean.getResults().size(); i++) {
                            imgList.add(gankIoDataBean.getResults().get(i).getUrl());
                        }

                    }else {

                        bindingView.xrvWelfare.noMoreLoading();
                    }
                }
            }

            @Override
            public void loadFailed() {

                bindingView.xrvWelfare.refreshComplete();
                if (mAdapter.getItemCount() == 0){

                    showError();
                }

                if (mPage > 1){
                    mPage--;
                }

            }

            @Override
            public void addSubscription(Disposable d) {

                WelfareFragment.this.addDisposable(d);
            }
        });

    }


    @Override
    protected void loadData() {
        if (!mIsVisible || !isPrepared || !isFirst){
            return;
        }
        if (meiziBean != null && meiziBean.getResults()!= null && meiziBean.getResults().size() > 0){
            showContentView();

            imgList.clear();
            for (int i = 0; i < meiziBean.getResults().size(); i++) {
                imgList.add(meiziBean.getResults().get(i).getUrl());
            }
            meiziBean = (GankIoDataBean) aCache.getAsObject(Constants.GANK_MEIZI);
            setmAdapter(meiziBean);
        }else {
            loadWelfareData();
        }
    }

    private void setmAdapter(GankIoDataBean gankIoDataBean){

        mAdapter.addAll(gankIoDataBean.getResults());
        bindingView.xrvWelfare.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        bindingView.xrvWelfare.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mAdapter.setOnItemClickListener(new OnItemClickListener<GankIoDataBean.ResultBean>() {
            @Override
            public void onClick(GankIoDataBean.ResultBean resultBean, int position) {

            }
        });

        // 显示成功后就不是第一次了，不再刷新
        isFirst = false;
    }

    @Override
    protected void onRefresh() {
        loadWelfareData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
