package com.travel.ticket.http;

import com.example.http.utils.CheckNetwork;
import com.travel.ticket.TravelApplication;
import com.travel.ticket.entity.TokenBean;
import com.travel.ticket.util.PreferenceUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lixiaofan on 2018/2/3.
 */

public class HttpHeadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder.addHeader("Accept", "application/json;versions=1");
        if (CheckNetwork.isNetworkConnected(TravelApplication.getInstance())) {
            int maxAge = 60;
            builder.addHeader("Cache-Control", "public, max-age=" + maxAge);
        } else {
            int maxStale = 60 * 60 * 24 * 28;
            builder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
        }
        builder.addHeader("authorization", "Bearer " + PreferenceUtil.getInstance().getString(TokenBean.ACCESS_TOKEN, ""));
        return chain.proceed(builder.build());
    }
}
