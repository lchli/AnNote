package com.lchli.studydiscuss.bm.user.view;

import com.lchli.studydiscuss.common.mvp.MvpView;

/**
 * Created by lchli on 2016/10/25.
 */

public interface LoginMvpView extends MvpView {

    void showMessage(String msg);

    void showProgressIndicator();

    void dismissProgressIndicator();

    void showSuccess();
}
