package com.example.lisiyan.cloudlook;

import android.databinding.DataBindingUtil;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.lisiyan.cloudlook.databinding.ActivityMainBinding;
import com.example.lisiyan.cloudlook.databinding.NavHeaderMainBinding;
import com.example.lisiyan.cloudlook.ui.book.BookFragment;
import com.example.lisiyan.cloudlook.ui.gank.GankFragment;
import com.example.lisiyan.cloudlook.ui.one.OneFragment;
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
        ImgLoadUtil.getInstance().displayCirlce(mNavHeaderBinding.ivAvatar,null);
    }


    private void initContentFragment(){
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new GankFragment());
        fragmentList.add(new OneFragment());
        fragmentList.add(new BookFragment());
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        vpContent.setAdapter(myFragmentPagerAdapter);
        // 设置ViewPager最大缓存的页面个数(cpu消耗少)
        vpContent.setOffscreenPageLimit(2);
        vpContent.addOnPageChangeListener(this);
        llTitleGank.setSelected(true);
        vpContent.setCurrentItem(0);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar !=null){
            actionBar.setDisplayShowTitleEnabled(false);
        }


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        switch (position) {
            case 0:
                llTitleGank.setSelected(true);
                llTitleOne.setSelected(false);
                llTitleDou.setSelected(false);
                break;
            case 1:
                llTitleOne.setSelected(true);
                llTitleGank.setSelected(false);
                llTitleDou.setSelected(false);
                break;
            case 2:
                llTitleDou.setSelected(true);
                llTitleOne.setSelected(false);
                llTitleGank.setSelected(false);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mMainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                mMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                // 不退出程序，进入后台
                moveTaskToBack(true);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
