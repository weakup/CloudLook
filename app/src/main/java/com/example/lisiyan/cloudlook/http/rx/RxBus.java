package com.example.lisiyan.cloudlook.http.rx;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by lisiyan on 2017/11/6.
 */

public class RxBus {

    private static volatile  RxBus mInstance;
    private  Subject<Object> bus;

    private RxBus(){

        bus = PublishSubject.create().toSerialized();

    }

    public static RxBus getDefault(){

        if (mInstance ==null){

            synchronized (RxBus.class){

                if (mInstance == null){

                    mInstance = new RxBus();
                }
            }
        }

        return mInstance;
    }


    /**提供事件 根据code 分发
     *
     * @param code 事件代码
     * @param o    事件
     */
    public void post(int code , Object o){

        bus.onNext(new RxBusBaseMessage(code,o));
    }

    public boolean hasObservable() {
        return bus.hasObservers();
    }

    /**
     * 解析传递成指定的被观察着
     * @param code  只能接收到指定code
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable toObservable(final int code, final Class<T> eventType){

        return bus.ofType(RxBusBaseMessage.class)
                .filter(o ->
                        o.getCode() == code && eventType.isInstance(o.getObject())).map(
                                rxBusBaseMessage -> rxBusBaseMessage.getObject()).cast(eventType);
    }
}
