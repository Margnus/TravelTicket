package com.travel.ticket.http;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;

/**
 * Created by 李小凡 on 2018/3/14.
 */

public abstract class AuthObserver<T> implements Observer<T> {

    @Override
    public void onError(Throwable e) {
        if(e != null && e instanceof HttpException){
            HttpException exception = (HttpException) e;
            if(exception.code() == 401){
                reLogin();
                return;
            }
        }
        onFailed(e);
    }

    public abstract void onFailed(Throwable e);

    public abstract void reLogin();
}
