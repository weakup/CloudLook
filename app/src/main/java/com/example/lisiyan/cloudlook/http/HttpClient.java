package com.example.lisiyan.cloudlook.http;

import com.example.http.HttpUtils;
import com.example.lisiyan.cloudlook.bean.GankIoDataBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by lisiyan on 2017/10/30.
 */

public interface HttpClient {

    class Builder{

        public static HttpClient getDouBanService(){

            return HttpUtils.getInstance().getGankIOServer(HttpClient.class);
        }

    }

    /**
     * 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
     * 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * 请求个数： 数字，大于0
     * 第几页：数字，大于0
     * eg: http://gank.io/api/data/Android/10/1
     */
    @GET("data/{type}/{req_number}/{page}")
    Observable<GankIoDataBean> mGankIoBean(@Path("type") String id,@Path("req_number") int req_number,@Path("page") int page);

}
