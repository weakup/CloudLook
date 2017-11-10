package com.example.lisiyan.cloudlook.view.webview.config;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.lisiyan.cloudlook.view.webview.WebViewActivity;

/**
 * Created by lisiyan on 2017/11/10.
 */

public class MyWebChromeClient extends WebChromeClient {

    private WebViewActivity mActivity;
    private IWebPageView mIWebPageView;

    public MyWebChromeClient(IWebPageView mIWebPageView){

        this.mIWebPageView = mIWebPageView;
        this.mActivity = (WebViewActivity) mIWebPageView;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        mActivity.setTitle(title);

    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        mIWebPageView.progressChanged(newProgress);
    }
}
