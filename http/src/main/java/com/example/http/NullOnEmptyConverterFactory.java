package com.example.http;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by lisiyan on 2017/10/30.
 */

public class NullOnEmptyConverterFactory extends Converter.Factory{


    //如果为空返回null 不是调用下一个转换器

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {

        final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);

        return new Converter<ResponseBody, Object>() {
            @Override
            public Object convert(ResponseBody value) throws IOException {

                if (value.contentLength() == 0) return null;

                return delegate.convert(value);
            }
        };
    }
}
