package com.lchli.studydiscuss.bm.hotfixTinker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lchli.studydiscuss.R;

import com.lchli.studydiscuss.common.base.BaseAppCompatActivity;

import com.lchli.studydiscuss.common.utils.ResUtils;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by lchli on 2016/10/23.
 */

public class TinkerPatchActivity extends BaseAppCompatActivity {

    public static void startSelf(Context context) {
        Intent it = new Intent(context, TinkerPatchActivity.class);
        if (!(context instanceof Activity)) {
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(it);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("提示")
                .setContentText(ResUtils.parseString(R.string.patch_restart_process_dialog))
                .setConfirmText("重启")
                .setCancelText("推迟")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        System.exit(0);
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        finish();
                    }
                })
                .show();
    }
}
