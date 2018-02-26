package com.example.lisiyan.cloudlook.http;

import com.example.http.HttpUtils;
import com.example.lisiyan.cloudlook.bean.BookBean;
import com.example.lisiyan.cloudlook.bean.BookDetailBean;
import com.example.lisiyan.cloudlook.bean.FrontpageBean;
import com.example.lisiyan.cloudlook.bean.GankIoDataBean;
import com.example.lisiyan.cloudlook.bean.GankIoDayBean;
import com.example.lisiyan.cloudlook.bean.HotMovieBean;
import com.example.lisiyan.cloudlook.bean.MovieDetailBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lisiyan on 2017/10/30.
 */

public interface HttpClient {

    class Builder{

        public static HttpClient getGankIOServer() {
            return HttpUtils.getInstance().getGankIOServer(HttpClient.class);
        }

        public static HttpClient getTingServer(){
            return HttpUtils.getInstance().getTingServer(HttpClient.class);
        }

        public static HttpClient getDouBanService(){
            return HttpUtils.getInstance().getDouBanServer(HttpClient.class);
        }

        public static HttpClient getBackService(){
            return HttpUtils.getInstance().getBackServer(HttpClient.class);
        }

    }

    /**
     * 首页轮播图
     */
    @GET("ting?from=android&version=5.8.1.0&channel=ppzs&operator=3&method=baidu.ting.plaza.index&cuid=89CF1E1A06826F9AB95A34DC0F6AAA14")
    Observable<FrontpageBean> getFrontpage();

    /**
     * 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
     * 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * 请求个数： 数字，大于0
     * 第几页：数字，大于0
     * eg: http://gank.io/api/data/Android/10/1
     */
    @GET("data/{type}/{pre_page}/{page}")
    Observable<GankIoDataBean> getGankIoData(@Path("type") String id, @Path("page") int page, @Path("pre_page") int pre_page);

    /**
     * 每日数据： http://gank.io/api/day/年/月/日
     * eg:http://gank.io/api/day/2015/08/06
     */
    @GET("day/{year}/{month}/{day}")
    Observable<GankIoDayBean> getGankIoDay(@Path("year") String year, @Path("month") String month, @Path("day") String day);


    @GET("{page}.html")
    Observable<GankIoDayBean> getBackGankIoDay(@Path("page")String page);

    /**
     * 豆瓣热映电影，每日更新
     */
    @GET("v2/movie/in_theaters")
    Observable<HotMovieBean> getHotMovie();

    /**
     * 获取电影详情
     *
     * @param id 电影bean里的id
     */
    @GET("v2/movie/subject/{id}")
    Observable<MovieDetailBean> getMovieDetail(@Path("id") String id);

    /**
     * 获取豆瓣电影top250
     *
     * @param start 从多少开始，如从"0"开始
     * @param count 一次请求的数目，如"10"条，最多100
     */
    @GET("v2/movie/top250")
    Observable<HotMovieBean> getMovieTop250(@Query("start") int start, @Query("count") int count);

    /**
     * 根据tag获取图书
     *
     * @param tag   搜索关键字
     * @param count 一次请求的数目 最多100
     */

    @GET("v2/book/search")
    Observable<BookBean> getBook(@Query("tag") String tag, @Query("start") int start, @Query("count") int count);
    @GET("v2/book/{id}")
    Observable<BookDetailBean> getBookDetail(@Path("id") String id);


}
