package com.example.lisiyan.cloudlook.http;

import org.reactivestreams.Subscription;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by lisiyan on 2017/10/31.
 */

public interface RequestImpl {

    void loadSuccess(Object object);

    void loadFailed();

    void addSubscription(Disposable d);

}
