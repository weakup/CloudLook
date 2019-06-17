package com.example.lisiyan.cloudlook.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;


import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.base.BaseActivity;
import com.example.lisiyan.cloudlook.databinding.ActivityNavDownloadBinding;
import com.example.lisiyan.cloudlook.utils.QRCodeUtil;


public class NavDownloadActivity extends BaseActivity<ActivityNavDownloadBinding>{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_download);
        showContentView();

        setTitle("扫码下载");
        //        https://github.com/weakup/LookRespository/raw/master/apk/app-debug.apk
        String url = "https://github.com/weakup/LookRespository/raw/master/apk";
        QRCodeUtil.showThreadImage(this, url, bindingView.ivErweima, R.mipmap.my_ic_launcher);
    }

    public static void start(Context mContext) {
        Intent intent = new Intent(mContext, NavDownloadActivity.class);
        mContext.startActivity(intent);
    }
}
