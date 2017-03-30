package com.lchli.studydiscuss.bm.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lchli.studydiscuss.BuildConfig;
import com.lchli.studydiscuss.R;
import com.lchli.studydiscuss.R2;
import com.lchli.studydiscuss.bm.user.entity.UploadPortraitReponse;
import com.lchli.studydiscuss.bm.user.entity.User;
import com.lchli.studydiscuss.bm.user.model.UserSessionManager;
import com.lchli.studydiscuss.bm.user.widget.UserCenterListItem;
import com.lchli.studydiscuss.common.base.BaseFragment;
import com.lchli.studydiscuss.common.consts.UrlConst;
import com.lchli.studydiscuss.common.networkLib.AppHttpManager;
import com.lchli.studydiscuss.common.networkLib.OkError;
import com.lchli.studydiscuss.common.networkLib.OkErrorCode;
import com.lchli.studydiscuss.common.networkLib.OkUiCallback;
import com.lchli.studydiscuss.common.utils.ListUtils;
import com.lchli.studydiscuss.common.utils.MapUtils;
import com.lchli.studydiscuss.common.utils.ToastUtils;

import junit.framework.Assert;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

import static com.lchli.studydiscuss.bm.user.model.UserSessionManager.getSession;

/**
 * Created by lchli on 2016/8/10.
 */
public class UserCenterFragment extends BaseFragment {

    private static final int REQUEST_CODE_GALLERY = 1;

    @BindView(R2.id.user_portrait)
    ImageView userPortrait;
    @BindView(R2.id.user_nick)
    TextView userNick;
    @BindView(R2.id.about_app_widget)
    UserCenterListItem aboutAppWidget;
    @BindView(R2.id.check_update_widget)
    UserCenterListItem checkUpdateWidget;

    public static UserCenterFragment newInstance() {

        Bundle args = new Bundle();
        UserCenterFragment fragment = new UserCenterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_center, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        aboutAppWidget.textWidget.setText(getString(R.string.app_version_pattern, BuildConfig.VERSION_NAME, "老李"));
        User user = getSession();
        Assert.assertNotNull(user);
        Glide.with(getActivity()).load(user.portraitUrl).placeholder(R.drawable.add_portrait).into(userPortrait);
        userNick.setText(TextUtils.isEmpty(user.nick) ? user.account : user.nick);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @OnClick({R2.id.user_portrait, R2.id.check_update_widget, R2.id.logout_widget})
    public void onClick(View view) {
        switch (view.getId()) {
            case R2.id.user_portrait:
                GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (!ListUtils.isEmpty(resultList)) {
                            String portraitPath = resultList.get(0).getPhotoPath();
                            Glide.with(getActivity()).load(portraitPath).into(userPortrait);
                            final User session = UserSessionManager.getSession();
                            if (session == null) {
                                return;
                            }
                            File portraitJpg = new File(portraitPath);

                            Map<String, String> params = MapUtils.stringMap();
                            params.put("uid", session.uid);

                            AppHttpManager.uploadFile(UrlConst.UPLOAD_USER_PORTRAIT_URL, params, Collections.singletonList(portraitJpg), "UserPortrait", new OkUiCallback<UploadPortraitReponse>() {
                                @Override
                                public void onSuccess(UploadPortraitReponse response) {
                                    if (response.code == OkErrorCode.SUCCESS) {
                                        session.portraitUrl = response.PortraitUrl;
                                        UserSessionManager.saveSession(session);

                                        ToastUtils.systemToast(R.string.update_portrait_success);
                                    } else {
                                        ToastUtils.systemToast(response.errorMsg);
                                    }

                                }

                                @Override
                                public void onFail(OkError error) {
                                    ToastUtils.systemToast(error.errMsg);
                                }
                            });


                        }

                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {
                        ToastUtils.systemToast(errorMsg);
                    }
                });
                break;
            case R2.id.check_update_widget:
                ToastUtils.systemToast(R.string.app_no_update);
                break;
            case R2.id.logout_widget:
                UserSessionManager.logout();
                UserFragmentContainer userFragmentContainer = (UserFragmentContainer) getParentFragment();
                userFragmentContainer.toLogin(true);
                break;
        }
    }
}
