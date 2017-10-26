package com.example.lisiyan.cloudlook;

import android.databinding.DataBindingUtil;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.lisiyan.cloudlook.databinding.ActivityMainBinding;
import com.example.lisiyan.cloudlook.databinding.NavHeaderMainBinding;
import com.example.lisiyan.cloudlook.utils.CommonUtils;
import com.example.lisiyan.cloudlook.utils.ImgLoadUtil;
import com.example.lisiyan.cloudlook.view.MyFragmentPagerAdapter;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {

    private FrameLayout llTitleMenu;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private ViewPager vpContent;

    private ActivityMainBinding mMainBinding;
    private ImageView llTitleGank;
    private ImageView llTitleOne;
    private ImageView llTitleDou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        initId();


//        StatusBarUtil.setColorNoTranslucentForDrawerLayout(this,drawerLayout,
//                ContextCompat.getColor(this,R.color.colorTheme));
        initContentFragment();
        initDrawerLayout();
        initListener();
    }

    private void initId(){

        drawerLayout = mMainBinding.drawerLayout;
        navView = mMainBinding.navView;
        fab = mMainBinding.include.fab;
        toolbar = mMainBinding.include.toolbar;
        llTitleMenu = mMainBinding.include.llTitleMenu;
        vpContent = mMainBinding.include.vpContent;
        fab.setVisibility(View.GONE);

        llTitleGank = mMainBinding.include.ivTitleGank;
        llTitleOne = mMainBinding.include.ivTitleOne;
        llTitleDou = mMainBinding.include.ivTitleDou;

    }

    private void initListener(){

        llTitleMenu.setOnClickListener(this);
        llTitleGank.setOnClickListener(this);
        llTitleOne.setOnClickListener(this);
        llTitleDou.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    NavHeaderMainBinding mNavHeaderBinding;

    private void initDrawerLayout(){
        navView.inflateHeaderView(R.layout.nav_header_main);
        View headerView = navView.getHeaderView(0);
        mNavHeaderBinding = DataBindingUtil.bind(headerView);
        ImgLoadUtil.displayCirlce(mNavHeaderBinding.ivAvatar,null);
    }


    private void initContentFragment(){
        List<Fragment> fragmentList = new ArrayList<>();
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        vpContent.setAdapter(myFragmentPagerAdapter);
        vpContent.setCurrentItem(0);
        llTitleGank.setSelected(true);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case  R.id.ll_title_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.iv_title_gank:
                if (vpContent.getCurrentItem()!=0){
                    llTitleGank.setSelected(true);
                    llTitleOne.setSelected(false);
                    llTitleDou.setSelected(false);
                    vpContent.setCurrentItem(0);
                }
                break;
            case R.id.iv_title_one:// 电影栏
                if (vpContent.getCurrentItem() != 1) {
                    Log.d("Main", String.valueOf(vpContent.getCurrentItem()));
                    llTitleOne.setSelected(true);
                    llTitleGank.setSelected(false);
                    llTitleDou.setSelected(false);
                    vpContent.setCurrentItem(1);

                }
                break;
            case R.id.iv_title_dou:// 书籍栏
                if (vpContent.getCurrentItem() != 2) {
                    llTitleDou.setSelected(true);
                    llTitleOne.setSelected(false);
                    llTitleGank.setSelected(false);
                    vpContent.setCurrentItem(2);
                }
                break;

            default:
                break;

        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
