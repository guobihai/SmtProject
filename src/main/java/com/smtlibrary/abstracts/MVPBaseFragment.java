package com.smtlibrary.abstracts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by gbh on 16/9/8.
 * MVPBaseActivity<V, T extends BasePresenter<V>>
 * MVP 设计模式MVPBaseFragment
 */
public abstract class MVPBaseFragment<V, T extends BasePresenter<V>> extends Fragment {

    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }


    protected abstract T createPresenter();
}
