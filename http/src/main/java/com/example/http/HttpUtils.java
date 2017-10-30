package com.example.http;

import android.content.Context;

import com.example.http.utils.CheckNetwork;
import com.example.http.utils.ParamNames;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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
    private boolean debug;

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

    public void init(Context context ,boolean debug){

        this.context = context;
        this.debug = debug;

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

    class HttpHeadInterceptor implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            builder.addHeader("Accept","application/json;versions=1");
            if (CheckNetwork.isNetworkConnected(context)){

                int maxAge = 60;
                builder.addHeader("Cache-Control", "public, max-age=" + maxAge);
            }else {
                int maxStale = 60 * 60 * 24 * 28;
                builder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
            }

            return chain.proceed(builder.build());
        }

    }



    public <T> T getGankIOServer(Class<T> a){
        if (gankHttps ==null){

            synchronized (HttpUtils.class){

                if (gankHttps == null){
                    gankHttps =getBuilder("");
                }
            }
        }

        return (T) gankHttps;
    }
}
