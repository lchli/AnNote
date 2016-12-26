package com.lchli.studydiscuss.bm.user.presenter;

import android.text.TextUtils;

import com.lchli.studydiscuss.R;
import com.lchli.studydiscuss.bm.user.entity.RegisterReponse;
import com.lchli.studydiscuss.bm.user.model.UserSessionManager;
import com.lchli.studydiscuss.bm.user.view.LoginMvpView;
import com.lchli.studydiscuss.common.consts.UrlConst;
import com.lchli.studydiscuss.common.mvp.Presenter;
import com.lchli.studydiscuss.common.networkLib.AppHttpManager;
import com.lchli.studydiscuss.common.networkLib.OkError;
import com.lchli.studydiscuss.common.networkLib.OkErrorCode;
import com.lchli.studydiscuss.common.networkLib.OkUiCallback;
import com.lchli.studydiscuss.common.utils.MapUtils;
import com.lchli.studydiscuss.common.utils.ResUtils;

import java.util.Map;

/**
 * Created by lchli on 2016/10/25.
 */

public class LoginPresenter implements Presenter<LoginMvpView> {

    private LoginMvpView mLoginMvpView;

    @Override
    public void attachView(LoginMvpView view) {
        mLoginMvpView = view;
    }

    @Override
    public void detachView() {
        mLoginMvpView = null;
    }

    public void login(String userAccount, String userPwd) {
        if (TextUtils.isEmpty(userAccount)) {
            mLoginMvpView.showMessage(ResUtils.parseString(R.string.account_cannot_null));
            return;
        }
        if (TextUtils.isEmpty(userPwd)) {
            mLoginMvpView.showMessage(ResUtils.parseString(R.string.pwd_cannot_null));
            return;
        }
        mLoginMvpView.showProgressIndicator();

        Map<String, String> params = MapUtils.stringMap();
        params.put("userName", userAccount);
        params.put("pwd", userPwd);

        AppHttpManager.get(UrlConst.LOGIN_URL, params, new OkUiCallback<RegisterReponse>() {
            @Override
            public void onSuccess(RegisterReponse response) {
                mLoginMvpView.dismissProgressIndicator();

                if (response.code == OkErrorCode.SUCCESS) {
                    UserSessionManager.login(response.user);

                    mLoginMvpView.showSuccess();
                } else {
                    mLoginMvpView.showMessage(response.errorMsg);
                }
            }

            @Override
            public void onFail(OkError error) {
                mLoginMvpView.dismissProgressIndicator();
                mLoginMvpView.showMessage(error.errMsg);
            }
        });

    }
}
