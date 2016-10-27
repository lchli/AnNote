package com.lchli.studydiscuss.bm.user.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lchli.studydiscuss.R;
import com.lchli.studydiscuss.common.base.BaseFragment;
import com.lchli.studydiscuss.common.widget.CommonTitleView;
import com.lchli.studydiscuss.common.widget.LoadingDialog;
import com.lchli.studydiscuss.bm.user.model.UserSessionManager;
import com.lchli.studydiscuss.bm.user.presenter.LoginPresenter;
import com.lchli.studydiscuss.bm.user.view.LoginMvpView;

import com.lchli.studydiscuss.common.utils.ResUtils;
import com.lchli.studydiscuss.common.utils.ToastUtils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lchli on 2016/8/10.
 */
public class LoginFragment extends BaseFragment implements LoginMvpView {


    @Bind(R.id.common_title)
    CommonTitleView commonTitle;
    @Bind(R.id.user_portrait)
    ImageView userPortrait;
    @Bind(R.id.user_account_edit)
    EditText userAccountEdit;
    @Bind(R.id.user_pwd_edit)
    EditText userPwdEdit;

    private LoadingDialog mLoadingDialog = new LoadingDialog();
    private LoginPresenter presenter;

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter = new LoginPresenter();
        presenter.attachView(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        commonTitle.addRightText(ResUtils.parseString(R.string.register), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFragmentContainer userFragmentContainer = (UserFragmentContainer) getParentFragment();
                userFragmentContainer.toRegister();
            }
        });
        commonTitle.setCenterText(ResUtils.parseString(R.string.login), null);
        Glide.with(getActivity()).load(UserSessionManager.getLastUserPortrait())
                .placeholder(R.drawable.default_head).into(userPortrait);
        userAccountEdit.setText(UserSessionManager.getLastUserAccount());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.login_widget)
    public void onClick() {
        presenter.login(userAccountEdit.getText().toString(), userPwdEdit.getText().toString());
    }

    @Override
    public void showMessage(String msg) {
        ToastUtils.systemToast(msg);
    }

    @Override
    public void showProgressIndicator() {
        mLoadingDialog.show(getString(R.string.loading_msg), getActivity());
    }

    @Override
    public void dismissProgressIndicator() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showSuccess() {
        UserFragmentContainer userFragmentContainer = (UserFragmentContainer) getParentFragment();
        userFragmentContainer.toUserCenter(true);
    }
}
