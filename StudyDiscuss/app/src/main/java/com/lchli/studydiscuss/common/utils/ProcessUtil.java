package com.lchli.studydiscuss.common.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.Iterator;
import java.util.List;


public class ProcessUtil {

    private static String sCurrentProcessName;

    public static String getCurrentProcessName(Context context) {

        if (sCurrentProcessName == null) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List infos = activityManager.getRunningAppProcesses();
            Iterator iter = infos.iterator();

            while (iter.hasNext()) {
                ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) iter.next();
                if (info.pid == android.os.Process.myPid()) {
                    sCurrentProcessName = info.processName;
                    return sCurrentProcessName;
                }
            }
        }
        return sCurrentProcessName;
    }

    public static boolean isMainProcess() {
        return ContextProvider.context().getPackageName().equals(getCurrentProcessName(ContextProvider.context()));
    }
}
