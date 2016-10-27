package com.lchli.studydiscuss.common.mvp;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}