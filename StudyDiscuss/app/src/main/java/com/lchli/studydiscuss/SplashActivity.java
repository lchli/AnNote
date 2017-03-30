package com.lchli.studydiscuss;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lchli.studydiscuss.bm.home.HomeActivity;
import com.lchli.studydiscuss.common.base.BaseAppCompatActivity;
import com.lchli.studydiscuss.common.utils.ToastUtils;

import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * Created by lchli on 2016/10/16.
 */

public class SplashActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dexter.initialize(getApplication());
        checkPermissions();

    }


    private void checkPermissions() {
        Dexter.checkPermissions(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    Thread.setDefaultUncaughtExceptionHandler(new AppExceptionHandler());
                    initGalleryFinal();

                    //
                    HomeActivity.startSelf(SplashActivity.this);
                } else {
                    ToastUtils.systemToast("访问存储卡权限被禁止！");
                }
                finish();

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }


    private void initGalleryFinal() {
        ThemeConfig themeConfig = ThemeConfig.DEFAULT;
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        cn.finalteam.galleryfinal.ImageLoader imageLoader;
        PauseOnScrollListener pauseOnScrollListener = null;
        imageLoader = new GlideImageLoader();
        functionConfigBuilder.setEnableEdit(true);
        functionConfigBuilder.setRotateReplaceSource(true);
        functionConfigBuilder.setEnableCrop(false);
        functionConfigBuilder.setCropReplaceSource(true);
        functionConfigBuilder.setEnableCamera(false);
        functionConfigBuilder.setEnablePreview(true);
        FunctionConfig functionConfig = functionConfigBuilder.build();
        CoreConfig coreConfig = new CoreConfig.Builder(getApplication(), imageLoader, themeConfig)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(pauseOnScrollListener)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
    }

}
