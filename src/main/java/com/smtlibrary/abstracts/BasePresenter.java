package com.smtlibrary.abstracts;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by gbh on 16/9/8.
 *  MVP BasePresenter
 */
public abstract class BasePresenter<T> {
    protected Reference<T> mViewRef;//view 接口类型的弱引用

    public void attachView(T view) {
        mViewRef = new WeakReference<T>(view);//建立关联mViewRef
    }

    protected T getView() {
        return mViewRef.get();
    }

    /**
     * 判断view 是否还处于活动状态
     * @return
     */
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() {
        if (null != mViewRef) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

}
