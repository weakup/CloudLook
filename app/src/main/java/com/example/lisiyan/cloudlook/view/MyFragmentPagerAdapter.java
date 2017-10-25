package com.example.lisiyan.cloudlook.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by lisiyan on 2017/10/25.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragment;
    private List<String> mTitleList;

    public MyFragmentPagerAdapter(FragmentManager fm,List<Fragment> Fragment) {
        super(fm);
        this.mFragment = Fragment;
    }

    public MyFragmentPagerAdapter(FragmentManager fm,List<Fragment> fragmnet,List<String> titleList){

        super(fm);
        this.mFragment = fragmnet;
        this.mTitleList = titleList;

    }


    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitleList != null) {
            return mTitleList.get(position);
        } else {
            return "";
        }
    }
}
