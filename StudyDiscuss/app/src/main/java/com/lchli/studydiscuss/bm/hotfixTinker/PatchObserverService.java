package com.lchli.studydiscuss.bm.hotfixTinker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lchli.studydiscuss.BuildConfig;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.lib.util.TinkerLog;

import java.io.File;

import com.lchli.studydiscuss.common.consts.LocalConst;
import com.lchli.studydiscuss.common.utils.ToastUtils;

/**
 * Created by lchli on 2016/10/15.
 */

public class PatchObserverService extends Service {

    private static final String TAG = "PatchObserverService";
    private static final long POLL_DELAY = 30 * 1000;
    private static final int POLL_PATCH_MSG = 1;

    private static final String PATCH_APK_PATH = LocalConst.STUDY_APP_ROOT_DIR + "/patch_signed_7zip.apk";

    private volatile Looper mServiceLooper;
    private volatile ServiceHandler mServiceHandler;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {//should use push.
            //server should push info like :clientVersion,patchVersion,patchUrl，patchEnable
            TinkerLog.e(TAG, "----poll patch running------");
            String newPathVersion = "1.9";
            String destClientVersion = "1.1";

            Log.e(TAG, "patchxxx>>:" + BuildConfig.PATCH_VERSION);
            ToastUtils.systemToast("patchxxxsss:" + BuildConfig.PATCH_VERSION);

            if (compareVersionString(newPathVersion, BuildConfig.PATCH_VERSION) > 0 &&
                    compareVersionString(destClientVersion, BuildConfig.CLIENT_VERSION) == 0) {

                File patch = new File(PATCH_APK_PATH);//服务器应该只针对特定的客户端版本号下发补丁patch.apk
                if (patch.exists()) {
                    TinkerLog.e(TAG, "start install patch:" + PATCH_APK_PATH);
                    TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), PATCH_APK_PATH);
                }
            }

            mServiceHandler.sendEmptyMessageDelayed(POLL_PATCH_MSG, POLL_DELAY);

        }
    }

    public static void startSelf(Context context) {
        Intent it = new Intent(context, PatchObserverService.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(it);

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("PatchObserverService");
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        mServiceHandler.sendEmptyMessageDelayed(POLL_PATCH_MSG, POLL_DELAY);
    }

    @Override
    public void onDestroy() {
        mServiceLooper.quit();
    }

    private static int compareVersionString(String v1, String v2) {
        return v1.compareTo(v2);
    }
}
