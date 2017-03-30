package com.lchli.studydiscuss.bm.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lchli.studydiscuss.R;
import com.lchli.studydiscuss.R2;
import com.lchli.studydiscuss.bm.user.model.UserSessionManager;
import com.lchli.studydiscuss.common.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lchli on 2016/8/10.
 */
public class UserFragmentContainer extends BaseFragment {


    @BindView(R2.id.user_fragment_container)
    FrameLayout userFragmentContainer;

    public static UserFragmentContainer newInstance() {

        Bundle args = new Bundle();
        UserFragmentContainer fragment = new UserFragmentContainer();
        fragment.setArguments(args);
        return fragment;
    }

    public void toRegister() {
        FragmentTransaction trans = getChildFragmentManager().beginTransaction();
        trans.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        trans.replace(R.id.user_fragment_container, RegisterFragment.newInstance());
        trans.commitAllowingStateLoss();
    }

    public void toLogin(boolean isNeedAnim) {
        FragmentTransaction trans = getChildFragmentManager().beginTransaction();
        if (isNeedAnim) {
            trans.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
        trans.replace(R.id.user_fragment_container, LoginFragment.newInstance());
        trans.commitAllowingStateLoss();
    }

    public void toUserCenter(boolean isNeedAnim) {
        FragmentTransaction trans = getChildFragmentManager().beginTransaction();
        if (isNeedAnim) {
            trans.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
        trans.replace(R.id.user_fragment_container, UserCenterFragment.newInstance());
        trans.commitAllowingStateLoss();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_container, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void initLoadData() {
        if (UserSessionManager.getSession() == null) {
            toLogin(false);
        } else {
            toUserCenter(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Override and do not save.
    }
}
