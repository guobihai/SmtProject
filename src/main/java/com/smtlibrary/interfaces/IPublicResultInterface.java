package com.smtlibrary.interfaces;

/**
 * Created by gbh on 16/7/4.
 */
public interface IPublicResultInterface<T> {
    void onSucess(T object);

    void onFailure(String msg, Exception e);
}
