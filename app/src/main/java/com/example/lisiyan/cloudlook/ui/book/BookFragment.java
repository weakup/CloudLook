package com.example.lisiyan.cloudlook.ui.book;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.base.BaseFragment;
import com.example.lisiyan.cloudlook.databinding.FragmentBookBinding;
import com.example.lisiyan.cloudlook.view.MyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lisiyan on 2017/10/26.
 */

public class BookFragment extends BaseFragment<FragmentBookBinding>{

    private List<String> mTitleList = new ArrayList<>(3);
    private List<Fragment> mFragments = new ArrayList<>(3);


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showLoading();

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(),mFragments,mTitleList);
        bindingView.vpBook.setAdapter(myFragmentPagerAdapter);
        bindingView.vpBook.setOffscreenPageLimit(2);
        myFragmentPagerAdapter.notifyDataSetChanged();
        bindingView.tabBook.setTabMode(TabLayout.MODE_FIXED);
        bindingView.tabBook.setupWithViewPager(bindingView.vpBook);
        showContentView();
    }

    @Override
    public int setContent() {
        return R.layout.fragment_book;
    }
}
