package com.example.lisiyan.cloudlook.ui.gank.child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.adapter.WelfareAdapter;
import com.example.lisiyan.cloudlook.app.Constants;
import com.example.lisiyan.cloudlook.base.BaseFragment;
import com.example.lisiyan.cloudlook.bean.GankIoDataBean;
import com.example.lisiyan.cloudlook.databinding.FragmentWelfareBinding;
import com.example.lisiyan.cloudlook.http.cache.ACache;
import com.example.lisiyan.cloudlook.model.GankOtherModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lisiyan on 2017/11/20.
 * 每日x图
 */

public class WelfareFragment extends BaseFragment<FragmentWelfareBinding>{

    private GankOtherModel model;
    private GankIoDataBean meiziBean;
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

//        bindingView.xrvWelfare.setLoadingListener(new XRecyclerView.LoadingListener() {
//            @Override
//            public void onRefresh() {
//
//            }
//
//            @Override
//            public void onLoadMore() {
////                loadWelfareData();
//            }
//        });

        isPrepared = true;
    }

    List<String> imgList = new ArrayList<>();

    private void  loadWelfareData(){

        showContentView();
        meiziBean = new GankIoDataBean();
        List<GankIoDataBean.ResultBean> resultBeanList = new ArrayList<>();
        for (int i=1;i<=6;i++){
            GankIoDataBean.ResultBean resultBean = new GankIoDataBean.ResultBean();
            resultBean.setUrl("https://raw.githubusercontent.com/weakup/LookRespository/master/dailypicture/"+i+".jpg");
            resultBeanList.add(resultBean);
        }
        meiziBean.setResults(resultBeanList);
        setmAdapter(meiziBean);

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

        mAdapter.setOnItemClickListener((resultBean, position) -> {

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
