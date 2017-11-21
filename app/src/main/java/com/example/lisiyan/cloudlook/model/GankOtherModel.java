package com.example.lisiyan.cloudlook.model;

import com.example.lisiyan.cloudlook.bean.GankIoDataBean;
import com.example.lisiyan.cloudlook.http.HttpClient;
import com.example.lisiyan.cloudlook.http.RequestImpl;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lisiyan on 2017/11/20.
 */

public class GankOtherModel {

    private String id;
    private int page;
    private int per_page;


    public void setData(String id,int page,int per_page){
        this.id = id;
        this.page = page;
        this.per_page = per_page;
    }

    public void getGankIoData(final RequestImpl listener){

        HttpClient.Builder.getGankIOServer().getGankIoData(id,page,per_page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GankIoDataBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        listener.addSubscription(d);
                    }

                    @Override
                    public void onNext(GankIoDataBean gankIoDataBean) {

                        listener.loadSuccess(gankIoDataBean);
                    }

                    @Override
                    public void onError(Throwable e) {

                        listener.loadFailed();

                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }
}
