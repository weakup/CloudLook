package com.example.lisiyan.cloudlook.model;

import com.example.lisiyan.cloudlook.app.ConstantsImageUrl;
import com.example.lisiyan.cloudlook.bean.AndroidBean;
import com.example.lisiyan.cloudlook.bean.FrontpageBean;
import com.example.lisiyan.cloudlook.bean.GankIoDayBean;
import com.example.lisiyan.cloudlook.http.HttpClient;
import com.example.lisiyan.cloudlook.http.RequestImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lisiyan on 2017/11/6.
 */

public class EverydayModel {


    private String year = "2016";
    private String month = "11";
    private String day = "24";
    private static final String HOME_ONE = "home_one";
    private static final String HOME_TWO = "home_two";
    private static final String HOME_SIX = "home_six";

    public void setData(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * 轮播图
     */
    public void showBanncerPage(final RequestImpl listener){

        HttpClient.Builder.getTingServer().getFrontpage()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<FrontpageBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.addSubscription(d);
                    }

                    @Override
                    public void onNext(FrontpageBean frontpageBean) {

                        listener.loadSuccess(frontpageBean);
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


    public void showRecyclerViewData(final RequestImpl listener){


        Function<GankIoDayBean,Observable<List<List<AndroidBean>>>> function = gankIoDayBean -> {

            List<List<AndroidBean>> lists = new ArrayList<>();
            GankIoDayBean.ResultsBean results =gankIoDayBean.getResults();

            if (results.getAndroid() != null && results.getAndroid().size() > 0) {
                addUrlList(lists, results.getAndroid(), "Android");
            }
            if (results.getWelfare() != null && results.getWelfare().size() > 0) {
                addUrlList(lists, results.getWelfare(), "福利");
            }
            if (results.getiOS() != null && results.getiOS().size() > 0) {
                addUrlList(lists, results.getiOS(), "IOS");
            }
            if (results.getRestMovie() != null && results.getRestMovie().size() > 0) {
                addUrlList(lists, results.getRestMovie(), "休息视频");
            }
            if (results.getResource() != null && results.getResource().size() > 0) {
                addUrlList(lists, results.getResource(), "拓展资源");
            }
            if (results.getRecommend() != null && results.getRecommend().size() > 0) {
                addUrlList(lists, results.getRecommend(), "瞎推荐");
            }
            if (results.getFront() != null && results.getFront().size() > 0) {
                addUrlList(lists, results.getFront(), "前端");
            }
            if (results.getApp() != null && results.getApp().size() > 0) {
                addUrlList(lists, results.getApp(), "App");
            }

            return Observable.just(lists);
        };



        Observer<List<List<AndroidBean>>> observer = new Observer<List<List<AndroidBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {

                listener.addSubscription(d);

            }

            @Override
            public void onNext(List<List<AndroidBean>> lists) {

                listener.loadSuccess(lists);
            }

            @Override
            public void onError(Throwable e) {

                listener.loadFailed();
            }

            @Override
            public void onComplete() {

            }
        };

        HttpClient.Builder.getGankIOServer().getGankIoToDay()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(function)
                .subscribe(observer);


    }

    public void showBackRecyclerViewData(final RequestImpl listener){


        Function<GankIoDayBean,Observable<List<List<AndroidBean>>>> function = gankIoDayBean -> {

            List<List<AndroidBean>> lists = new ArrayList<>();
            GankIoDayBean.ResultsBean results =gankIoDayBean.getResults();

            if (results.getAndroid() != null && results.getAndroid().size() > 0) {
                addUrlList(lists, results.getAndroid(), "Android");
            }
            if (results.getWelfare() != null && results.getWelfare().size() > 0) {
                addUrlList(lists, results.getWelfare(), "福利");
            }
            if (results.getiOS() != null && results.getiOS().size() > 0) {
                addUrlList(lists, results.getiOS(), "IOS");
            }
            if (results.getRestMovie() != null && results.getRestMovie().size() > 0) {
                addUrlList(lists, results.getRestMovie(), "休息视频");
            }
            if (results.getResource() != null && results.getResource().size() > 0) {
                addUrlList(lists, results.getResource(), "拓展资源");
            }
            if (results.getRecommend() != null && results.getRecommend().size() > 0) {
                addUrlList(lists, results.getRecommend(), "瞎推荐");
            }
            if (results.getFront() != null && results.getFront().size() > 0) {
                addUrlList(lists, results.getFront(), "前端");
            }
            if (results.getApp() != null && results.getApp().size() > 0) {
                addUrlList(lists, results.getApp(), "App");
            }

            return Observable.just(lists);
        };



        Observer<List<List<AndroidBean>>> observer = new Observer<List<List<AndroidBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {

                listener.addSubscription(d);

            }

            @Override
            public void onNext(List<List<AndroidBean>> lists) {

                listener.loadSuccess(lists);
            }

            @Override
            public void onError(Throwable e) {

                listener.loadFailed();
            }

            @Override
            public void onComplete() {

            }
        };

        HttpClient.Builder.getBackService().getBackGankIoDay("1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(function)
                .subscribe(observer);


    }

    /**
     *这个bean 第一个位置 只有title 这个属性 有值 其余  desc type url 有值
     * @param lists  用来存储
     * @param arrayList 这个list是复合的 他们Json数据格式一样 统一叫做AndroidBean（包含ios 福利啥的~）
     * @param typeTitle
     */

    public void addUrlList(List<List<AndroidBean>> lists, List<AndroidBean> arrayList, String typeTitle){

        AndroidBean bean = new AndroidBean();
        bean.setType_title(typeTitle);
        List<AndroidBean> androidBean = new ArrayList();
        androidBean.add(bean);
        lists.add(androidBean);

        int androidSize = arrayList.size();

        if (androidSize > 0 && androidSize < 4){

            lists.add(addUrlList(arrayList,androidSize));

        }else if (androidSize >= 4){

            List<AndroidBean> list1 = new ArrayList<>();
            List<AndroidBean> list2 = new ArrayList<>();

            for (int i = 0; i < androidSize; i++) {
                if (i < 3) {
                    list1.add(getAndroidBean(arrayList, i, androidSize));
                } else if (i < 6) {
                    list2.add(getAndroidBean(arrayList, i, androidSize));
                }
            }
            lists.add(list1);
            lists.add(list2);

        }

    }

    private AndroidBean getAndroidBean(List<AndroidBean> arrayList,int i,int androidSize){

        AndroidBean androidBean =new AndroidBean();
        // 标题
        androidBean.setDesc(arrayList.get(i).getDesc());
        // 类型
        androidBean.setType(arrayList.get(i).getType());
        // 跳转链接
        androidBean.setUrl(arrayList.get(i).getUrl());

//        if (i < 3) {
//            androidBean.setImage_url(ConstantsImageUrl.getRandomImageUrl());//三小图
//        } else if (androidSize == 4) {
//            androidBean.setImage_url(ConstantsImageUrl.HOME_ONE_URLS[getRandom(1)]);//一图
//        } else if (androidSize == 5) {
//            androidBean.setImage_url(ConstantsImageUrl.HOME_TWO_URLS[getRandom(2)]);//两图
//        } else if (androidSize >= 6) {
//            androidBean.setImage_url(ConstantsImageUrl.getRandomImageUrl());//三小图
//        }

        androidBean.setImage_url(ConstantsImageUrl.getRandomImageUrl());

        return androidBean;

    }

    private List<AndroidBean> addUrlList(List<AndroidBean> arrayList, int androidSize) {
        List<AndroidBean> tempList = new ArrayList<>();
        for (int i = 0; i < androidSize; i++) {
            AndroidBean androidBean = new AndroidBean();
            // 标题
            androidBean.setDesc(arrayList.get(i).getDesc());
            // 类型
            androidBean.setType(arrayList.get(i).getType());
            // 跳转链接
            androidBean.setUrl(arrayList.get(i).getUrl());
//            DebugUtil.error("---androidSize:  " + androidSize);
            // 随机图的url

//            if (androidSize == 1) {
//                androidBean.setImage_url(ConstantsImageUrl.HOME_ONE_URLS[getRandom(1)]);//一图
//            } else if (androidSize == 2) {
//                androidBean.setImage_url(ConstantsImageUrl.HOME_TWO_URLS[getRandom(2)]);//两图
//            } else if (androidSize == 3) {
//                androidBean.setImage_url(ConstantsImageUrl.getRandomImageUrl());//三图
//            }

            androidBean.setImage_url(ConstantsImageUrl.getRandomImageUrl());

            tempList.add(androidBean);
        }
        return tempList;
    }

    private int getRandom(int type) {

        String saveWhere = null;
        int urlLength = 0;
        if (type == 1) {
            saveWhere = HOME_ONE;
            urlLength = ConstantsImageUrl.HOME_ONE_URLS.length;
        } else if (type == 2) {
            saveWhere = HOME_TWO;
            urlLength = ConstantsImageUrl.HOME_TWO_URLS.length;
        } else if (type == 3) {
            saveWhere = HOME_SIX;
            urlLength = ConstantsImageUrl.HOME_SIX_URLS.length;
        }

        Random random = new Random();

        int randomInt = random.nextInt(urlLength);
        return randomInt;
    }
}
