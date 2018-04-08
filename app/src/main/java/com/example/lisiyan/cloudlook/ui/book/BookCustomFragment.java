package com.example.lisiyan.cloudlook.ui.book;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.lisiyan.cloudlook.MainActivity;
import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.adapter.BookAdapter;
import com.example.lisiyan.cloudlook.base.BaseFragment;
import com.example.lisiyan.cloudlook.bean.BookBean;
import com.example.lisiyan.cloudlook.databinding.FragmentBookCustomBinding;
import com.example.lisiyan.cloudlook.http.HttpClient;
import com.example.lisiyan.cloudlook.utils.CommonUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lisiyan on 2017/11/28.
 */

public class BookCustomFragment extends BaseFragment<FragmentBookCustomBinding> {


    private static final String TYPE = "param1";
    private String mType = "综合";
    private boolean mIsPrepared;
    private boolean mIsFirst = true;
    // 开始请求的角标
    private int mStart = 0;
    // 一次请求的数量
    private int mCount = 18;
    private MainActivity activity;
    private GridLayoutManager mLayoutManager;
    private BookAdapter mBookAdapter;


    @Override
    public int setContent() {
        return R.layout.fragment_book_custom;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static BookCustomFragment newInstance(String param1) {

        BookCustomFragment fragment = new BookCustomFragment();
        Bundle args = new Bundle();
        args.putString(TYPE,param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mType = getArguments().getString(TYPE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        bindingView.srlBook.setColorSchemeColors(CommonUtils.getColor(getActivity(),R.color.colorTheme));
        bindingView.srlBook.setOnRefreshListener(
                () -> bindingView.srlBook.postDelayed(
                () -> {
            mStart = 0;
            loadCustomData();
        },1000));

        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        bindingView.xrvBook.setLayoutManager(mLayoutManager);

        scrollRecycleView();

        mIsPrepared = true;

        loadData();
    }

    private void loadCustomData(){

        HttpClient.Builder.getDouBanService().getBook(mType,mStart,mCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BookBean bookBean) {

                        if (mStart == 0) {
                            if (bookBean != null && bookBean.getBooks() != null && bookBean.getBooks().size() > 0) {

                                if (mBookAdapter == null) {
                                    mBookAdapter = new BookAdapter(getActivity());
                                }
                                mBookAdapter.setList(bookBean.getBooks());
                                mBookAdapter.notifyDataSetChanged();
                                bindingView.xrvBook.setAdapter(mBookAdapter);

                            }
                            mIsFirst = false;
                        } else {
                            mBookAdapter.addAll(bookBean.getBooks());
                            mBookAdapter.notifyDataSetChanged();
                        }
                        if (mBookAdapter != null) {
                            mBookAdapter.updateLoadStatus(BookAdapter.LOAD_PULL_TO);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        showContentView();
                        if (bindingView.srlBook.isRefreshing()) {
                            bindingView.srlBook.setRefreshing(false);
                        }
                        if (mStart == 0) {
                            showError();
                        }

                    }

                    @Override
                    public void onComplete() {

                        showContentView();
                        if (bindingView.srlBook.isRefreshing()) {
                            bindingView.srlBook.setRefreshing(false);
                        }
                    }
                });
    }

    public void scrollRecycleView(){
        bindingView.xrvBook.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

                    if (mBookAdapter == null){
                        return;
                    }

                    if (mLayoutManager.getItemCount() == 0){
                        mBookAdapter.updateLoadStatus(BookAdapter.LOAD_NONE);
                        return;
                    }
                    if (lastVisibleItem + 1 ==mLayoutManager.getItemCount()
                            && mBookAdapter.getLoadStatus() != BookAdapter.LOAD_MORE){

                        mBookAdapter.updateLoadStatus(BookAdapter.LOAD_MORE);

                        new Handler().postDelayed(() -> {
                            mStart += mCount;
                            loadCustomData();
                        },1000);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    protected void loadData() {
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }

        bindingView.srlBook.setRefreshing(true);
        bindingView.srlBook.postDelayed(() -> loadCustomData(), 500);
    }

    @Override
    protected void onRefresh() {
        bindingView.srlBook.setRefreshing(true);
        loadCustomData();
    }
}
