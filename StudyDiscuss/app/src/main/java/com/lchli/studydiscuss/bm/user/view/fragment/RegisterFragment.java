package com.lchli.studydiscuss.bm.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lchli.studydiscuss.R;
import com.lchli.studydiscuss.common.base.BaseFragment;
import com.lchli.studydiscuss.common.networkLib.AppHttpManager;
import com.lchli.studydiscuss.common.networkLib.OkError;
import com.lchli.studydiscuss.common.networkLib.OkErrorCode;
import com.lchli.studydiscuss.common.networkLib.OkUiCallback;
import com.lchli.studydiscuss.common.consts.UrlConst;
import com.lchli.studydiscuss.common.utils.MapUtils;
import com.lchli.studydiscuss.common.widget.CommonTitleView;
import com.lchli.studydiscuss.common.widget.LoadingDialog;
import com.lchli.studydiscuss.bm.user.model.UserSessionManager;
import com.lchli.studydiscuss.bm.user.entity.RegisterReponse;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.lchli.studydiscuss.common.utils.ResUtils;
import com.lchli.studydiscuss.common.utils.ToastUtils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import com.lchli.studydiscuss.common.utils.ListUtils;

/**
 * Created by lchli on 2016/8/10.
 */
public class RegisterFragment extends BaseFragment {

    private static final int REQUEST_CODE_GALLERY = 1;

    @Bind(R.id.common_title)
    CommonTitleView commonTitle;
    @Bind(R.id.user_portrait)
    ImageView userPortrait;
    @Bind(R.id.user_account_edit)
    EditText userAccountEdit;
    @Bind(R.id.user_pwd_edit)
    EditText userPwdEdit;
    @Bind(R.id.user_nick)
    EditText userNick;

    private String portraitPath;
    private LoadingDialog mLoadingDialog = new LoadingDialog();

    public static RegisterFragment newInstance() {

        Bundle args = new Bundle();
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        commonTitle.addRightText(ResUtils.parseString(R.string.login), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFragmentContainer userFragmentContainer = (UserFragmentContainer) getParentFragment();
                userFragmentContainer.toLogin(true);
            }
        });
        commonTitle.setCenterText(ResUtils.parseString(R.string.register), null);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick({R.id.user_portrait, R.id.register_widget})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_portrait:
                GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (!ListUtils.isEmpty(resultList)) {
                            portraitPath = resultList.get(0).getPhotoPath();
                            Glide.with(getActivity()).load(portraitPath)
                                    .placeholder(R.drawable.add_portrait).into(userPortrait);
                        }

                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {
                        ToastUtils.systemToast(errorMsg);
                    }
                });
                break;
            case R.id.register_widget:
                String userAccount = userAccountEdit.getText().toString();
                if (TextUtils.isEmpty(userAccount)) {
                    ToastUtils.systemToast(R.string.account_cannot_null);
                    return;
                }
                String userPwd = userPwdEdit.getText().toString();
                if (TextUtils.isEmpty(userPwd)) {
                    ToastUtils.systemToast(R.string.pwd_cannot_null);
                    return;
                }

                List<File> portrait = null;
                if (!TextUtils.isEmpty(portraitPath)) {
                    portrait = Collections.singletonList(new File(portraitPath));
                }

                Map<String, String> params = MapUtils.stringMap();
                params.put("userName", userAccount);
                params.put("pwd", userPwd);
                params.put("nick", userNick.getText().toString());

                mLoadingDialog.show(getString(R.string.loading_msg), getActivity());
                AppHttpManager.uploadFile(UrlConst.REGISTER_URL, params, portrait, "UserPortrait", new OkUiCallback<RegisterReponse>() {
                    @Override
                    public void onSuccess(RegisterReponse response) {
                        mLoadingDialog.dismiss();
                        if (response.code == OkErrorCode.SUCCESS) {
                            UserSessionManager.login(response.user);

                            UserFragmentContainer userFragmentContainer = (UserFragmentContainer) getParentFragment();
                            userFragmentContainer.toUserCenter(true);

                        } else {
                            ToastUtils.systemToast(response.errorMsg);
                        }

                    }

                    @Override
                    public void onFail(OkError error) {
                        mLoadingDialog.dismiss();
                        ToastUtils.systemToast(error.errMsg);
                    }
                });

                break;
        }
    }
}
