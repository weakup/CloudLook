package com.example.lisiyan.cloudlook.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.databinding.ActivityNavProblemBackBinding;
import com.example.lisiyan.cloudlook.utils.CommonUtils;
import com.example.lisiyan.cloudlook.view.webview.WebViewActivity;

/**
 * Created by lisiyan on 2018/5/21.
 */

public class NavProblemActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityNavProblemBackBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_nav_problem_back);
        mBinding.tvIssues.setOnClickListener(this);
        mBinding.tvJianshu.setOnClickListener(this);
        mBinding.tvQq.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.tv_issues:
                WebViewActivity.loadUrl(v.getContext(), CommonUtils.getString(R.string.string_url_issues),"Issues");
                break;
            case R.id.tv_jianshu:
                WebViewActivity.loadUrl(v.getContext(),CommonUtils.getString(R.string.string_url_jianshu),"加载中...");
                break;
            case R.id.tv_qq:
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=2442216102";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;
        }
    }

    public static void start(Context mContext) {
        Intent intent = new Intent(mContext, NavProblemActivity.class);
        mContext.startActivity(intent);
    }


}


