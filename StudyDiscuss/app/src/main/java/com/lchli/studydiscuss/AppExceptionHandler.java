package com.lchli.studydiscuss;

import com.apkfuns.logutils.LogUtils;
import com.lchli.studydiscuss.common.consts.LocalConst;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import cn.finalteam.toolsfinal.io.FileUtils;
import com.lchli.studydiscuss.common.utils.ExtFileUtils;

/**
 * Created by lchli on 2016/8/14.
 */

public class AppExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final String EXCEPTION_DIR = String.format("%s/%s", LocalConst.STUDY_APP_ROOT_DIR, "Exception");
    private static final String RECENT_EXCEPTION_FILE = String.format("%s/%s", EXCEPTION_DIR, "RecentException.txt");

    static {
        FileUtils.mkdirs(new File(EXCEPTION_DIR));
        ExtFileUtils.makeFile(RECENT_EXCEPTION_FILE);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        logException(ex);
    }

    public static void logException(Throwable ex) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        printWriter.close();
        LogUtils.e(stringWriter.toString());

        try {
            PrintWriter printWriterFile = new PrintWriter(new File(RECENT_EXCEPTION_FILE));
            ex.printStackTrace(printWriterFile);
            printWriterFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e("app exception log save fail.");
        }
    }
}
