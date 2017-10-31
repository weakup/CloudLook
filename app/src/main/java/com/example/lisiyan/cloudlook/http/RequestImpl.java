package com.example.lisiyan.cloudlook.http;

import org.reactivestreams.Subscription;

/**
 * Created by lisiyan on 2017/10/31.
 */

public interface RequestImpl {

    void loadSuccess(Object object);

    void loadFailed();

    void addSubscription(Subscription subscription);

}
