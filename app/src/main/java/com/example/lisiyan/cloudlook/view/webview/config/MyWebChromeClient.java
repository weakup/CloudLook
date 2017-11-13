package com.example.lisiyan.cloudlook.view.webview.config;

import android.content.pm.ActivityInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.view.webview.WebViewActivity;

/**
 * Created by lisiyan on 2017/11/10.
 */

public class MyWebChromeClient extends WebChromeClient {

    private WebViewActivity mActivity;
    private IWebPageView mIWebPageView;
    private View mXProgressVideo;
    private View mXCustomView;
    private CustomViewCallback mXCustomViewCallback;

    public MyWebChromeClient(IWebPageView mIWebPageView){

        this.mIWebPageView = mIWebPageView;
        this.mActivity = (WebViewActivity) mIWebPageView;
    }

//    加载html的title
    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        mActivity.setTitle(title);

    }

    // 播放网络视频时全屏会被调用的方法
    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mIWebPageView.hindWebView();
        // 如果一个视图已经存在，那么立刻终止并新建一个
        if (mXCustomView != null) {
            callback.onCustomViewHidden();
            return;
        }

        mActivity.fullViewAddView(view);
        mXCustomView = view;
        mXCustomViewCallback = callback;
        mIWebPageView.showVideoFullView();
    }

    // 视频播放退出全屏会被调用的
    @Override
    public void onHideCustomView() {
        if (mXCustomView == null)// 不是全屏播放状态
            return;
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mXCustomView.setVisibility(View.GONE);
        if (mActivity.getVideoFullView() != null) {
            mActivity.getVideoFullView().removeView(mXCustomView);
        }
        mXCustomView = null;
        mIWebPageView.hindVideoFullView();
        mXCustomViewCallback.onCustomViewHidden();
        mIWebPageView.showWebView();
    }




//    加载的页面改变
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        mIWebPageView.progressChanged(newProgress);
    }

    /**
     * 判断是否是全屏
     */
    public boolean inCustomView() {
        return (mXCustomView != null);
    }


}
