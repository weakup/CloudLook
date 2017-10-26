package com.example.lisiyan.cloudlook.ui.gank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.base.BaseFragment;
import com.example.lisiyan.cloudlook.databinding.FragmentGankBinding;
import com.example.lisiyan.cloudlook.view.MyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lisiyan on 2017/10/26.
 */

public class GankFragment extends BaseFragment<FragmentGankBinding>{


    private List<String> mTitleList = new ArrayList<>(4);
    private List<Fragment> mFragments = new ArrayList<>(4);


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       showLoading();
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(),mFragments,mTitleList);
        bindingView.vpGank.setAdapter(myFragmentPagerAdapter);
        // 左右预加载页面的个数
        //viewpage 每次切換的時候都會重新Oncreate 用這個不會 内存可能溢出
        bindingView.vpGank.setOffscreenPageLimit(3);
        myFragmentPagerAdapter.notifyDataSetChanged();
        bindingView.tabGank.setTabMode(TabLayout.MODE_FIXED);
        bindingView.tabGank.setupWithViewPager(bindingView.vpGank);
        showContentView();

    }


    @Override
    protected void showContentView() {
        super.showContentView();
    }

    @Override
    public int setContent() {
        return R.layout.fragment_gank;
    }

    @Override
    protected void showError() {
        super.showError();
    }

    private void initFragmentList(){

        mTitleList.add("每日推荐");
        mTitleList.add("福利");
        mTitleList.add("干货订制");
        mTitleList.add("大安卓");
        //

    }


}
