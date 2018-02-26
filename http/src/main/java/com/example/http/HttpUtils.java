package com.example.http;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lisiyan on 2017/10/30.
 */

public class HttpUtils {

    private static HttpUtils instance;
    private Gson gson;
    private Context context;
    private Object gankHttps;
    private Object doubanHttps;
    private Object dongtingHttps;
    private boolean debug;

    private Object backHttps;

    private final static String API_GANKIO = "https://gank.io/api/";
    private final static String API_TING = "https://tingapi.ting.baidu.com/v1/restserver/";
    private final static String API_DOUBAN = "https://api.douban.com/";

    private final static String API_BACK = "https://raw.githubusercontent.com/weakup/LookRespository/master/everyfragment/";

    /**
     * 分页数据，每页的数量
     */
    public static int per_page = 10;
    public static int per_page_more = 20;

    public static HttpUtils getInstance(){

        if (instance == null){
            synchronized (HttpUtils.class){
                if (instance == null){
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }

    public void init(Context context){

        this.context = context;

    }


    private Retrofit.Builder getBuilder(String apiUrl){

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(getOkhttpClient())
                .baseUrl(apiUrl)
                //判空
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        return builder;
    }

    private Gson getGson(){

        if (gson ==null){

            GsonBuilder builder = new GsonBuilder();
            builder.setLenient()
                    .setFieldNamingStrategy(new AnnotateNaming())
                    .serializeNulls();
            gson = builder.create();
        }

        return gson;
    }

    private static class AnnotateNaming implements FieldNamingStrategy {
        @Override
        public String translateName(Field field) {
            ParamNames a = field.getAnnotation(ParamNames.class);
            return a != null ? a.value() : FieldNamingPolicy.IDENTITY.translateName(field);
        }
    }

    public OkHttpClient getOkhttpClient(){

        OkHttpClient client1;
        client1 = getUnsafeOkHttpClinet();
        return client1;

    }

    //HTTPS 请求设置
    //默认信任所有的证书
    public OkHttpClient getUnsafeOkHttpClinet(){
        try {

            X509TrustManager x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };

            final TrustManager[] trustAllcCerts = new TrustManager[]{

                    x509TrustManager
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null,trustAllcCerts,new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
            okBuilder.readTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(10,TimeUnit.SECONDS)
                    .writeTimeout(20,TimeUnit.SECONDS)
                    .addInterceptor(new HttpHeadInterceptor())
                    .sslSocketFactory(sslSocketFactory,x509TrustManager)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });

            return okBuilder.build();

        }catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    class HttpHeadInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            builder.addHeader("Accept", "application/json;versions=1");
            if (true) {
                int maxAge = 60;
                builder.addHeader("Cache-Control", "public, max-age=" + maxAge);
            } else {
                int maxStale = 60 * 60 * 24 * 28;
                builder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
            }

            return chain.proceed(builder.build());
        }
    }

    public <T> T getBackServer(Class<T> a){
        if (backHttps == null){
            synchronized (HttpUtils.class){
                if (backHttps == null){
                    backHttps = getBuilder(API_BACK).build().create(a);
                }
            }
        }

        return (T) backHttps;
    }

    public <T> T getDouBanServer(Class<T> a) {
        if (doubanHttps == null) {
            synchronized (HttpUtils.class) {
                if (doubanHttps == null) {
                    doubanHttps = getBuilder(API_DOUBAN).build().create(a);
                }
            }
        }
        return (T) doubanHttps;
    }


    public <T> T getGankIOServer(Class<T> a){
        if (gankHttps ==null){

            synchronized (HttpUtils.class){

                if (gankHttps == null){
                    gankHttps =getBuilder(API_GANKIO).build().create(a);
                }
            }
        }

        return (T) gankHttps;
    }

    public <T> T getTingServer(Class<T> a) {
        if (dongtingHttps == null) {
            synchronized (HttpUtils.class) {
                if (dongtingHttps == null) {
                    dongtingHttps = getBuilder(API_TING).build().create(a);
                }
            }
        }
        return (T) dongtingHttps;
    }
}
