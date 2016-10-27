package com.lchli.studydiscuss.common.widget;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;

/**
 * Created by lchli on 2016/4/16.
 */
public class LoadingDialog {

    private ProgressDialog mProgressDialog;

    public void show(String msg, Activity activity) {
        if (mProgressDialog != null) {
            return;
        }
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setMessage(msg);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mProgressDialog = null;
            }
        });
        mProgressDialog.show();
    }

    public void dismiss() {
        if (mProgressDialog == null) {
            return;
        }
        mProgressDialog.dismiss();
    }


}
