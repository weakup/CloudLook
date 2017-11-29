package com.example.lisiyan.cloudlook.ui.book;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.widget.ImageView;

import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.base.BaseHeaderActivity;
import com.example.lisiyan.cloudlook.bean.BookDetailBean;
import com.example.lisiyan.cloudlook.bean.BooksBean;
import com.example.lisiyan.cloudlook.databinding.ActivityBookDetailBinding;
import com.example.lisiyan.cloudlook.databinding.HeaderBookDetailBinding;
import com.example.lisiyan.cloudlook.http.HttpClient;
import com.example.lisiyan.cloudlook.utils.CommonUtils;
import com.example.lisiyan.cloudlook.view.webview.WebViewActivity;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lisiyan on 2017/11/29.
 */

public class BookDetailActivity extends BaseHeaderActivity<HeaderBookDetailBinding,ActivityBookDetailBinding>{

    private BooksBean booksBean;
    private String mBookDetailUrl;
    private String mBookDetailName;
    public final static String EXTRA_PARAM = "bookBean";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        if (getIntent() != null){

            booksBean = (BooksBean) getIntent().getSerializableExtra(EXTRA_PARAM);

        }

        setMotion(setHeaderPicView(),true);
        initSlideShapeTheme(setHeaderImgUrl(),setHeaderImageView());

        setTitle(booksBean.getTitle());
        setSubTitle("作者：" + booksBean.getAuthor());
        bindingHeaderView.setBooksBean(booksBean);
        bindingHeaderView.executePendingBindings();

        loadBookDetail();

    }


    private void loadBookDetail(){

        HttpClient.Builder.getDouBanService().getBookDetail(booksBean.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        addSubscription(d);
                    }

                    @Override
                    public void onNext(BookDetailBean bookDetailBean) {
                        mBookDetailUrl = bookDetailBean.getAlt();
                        mBookDetailName = bookDetailBean.getTitle();
                        bindingContentView.setBookDetailBean(bookDetailBean);
                        bindingContentView.executePendingBindings();
                    }

                    @Override
                    public void onError(Throwable e) {

                        showError();
                    }

                    @Override
                    public void onComplete() {
                        showContentView();
                    }
                });
    }

    @Override
    protected void setTitleClickMore() {
        WebViewActivity.loadUrl(this, mBookDetailUrl, mBookDetailName);
    }

    @Override
    protected int setHeaderLayout() {
        return R.layout.header_book_detail;
    }

    @Override
    protected String setHeaderImgUrl() {
        if (booksBean == null){
            return "";
        }
        return booksBean.getImages().getMedium();
    }

    @Override
    protected ImageView setHeaderImageView() {
        return bindingHeaderView.imgItemBg;
    }

    @Override
    protected ImageView setHeaderPicView() {
        return bindingHeaderView.ivOnePhoto;
    }

    @Override
    protected void onRefresh() {
        loadBookDetail();
    }

    /**
     * @param context      activity
     * @param positionData bean
     * @param imageView    imageView
     */
    public static void start(Activity context, BooksBean positionData, ImageView imageView) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(EXTRA_PARAM, positionData);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                        imageView, CommonUtils.getString(R.string.transition_book_img));//与xml文件对应
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }

}
