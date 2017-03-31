package com.lchli.studydiscuss;

import android.app.Application;

import com.lchli.studydiscuss.bm.note.entity.DaoMaster;
import com.lchli.studydiscuss.bm.note.entity.DaoSession;
import com.lchli.studydiscuss.common.utils.ContextProvider;
import com.lchli.studydiscuss.common.utils.ProcessUtil;

/**
 * Created by lchli on 2016/10/19.
 */

public class StudyApp {

    private static final String NOTE_DATA_BASE = "notes.db";

    private DaoSession daoSession;

    private Application mApplication;

    private static class InstanceHolder {
        private static StudyApp app = new StudyApp();
    }

    public static StudyApp instance() {
        return InstanceHolder.app;
    }

    public Application getApplication() {
        return mApplication;
    }

    public void onCreate(Application application) {

        //LogUtils.configAllowLog = AppEnvironmentFactory.getEnv().logFlag();

        mApplication = application;
        ContextProvider.initContext(application);

        if (!ProcessUtil.isMainProcess()) {
            Thread.setDefaultUncaughtExceptionHandler(new AppExceptionHandler());
        }

    }


    /**
     * lazy init.
     *
     * @return
     */
    public DaoSession getDaoSession() {
        if (daoSession == null) {
            AppSQLiteOpenHelper helper = new AppSQLiteOpenHelper(mApplication, NOTE_DATA_BASE, null, true);
            daoSession = new DaoMaster(helper.getWritableDb()).newSession();
        }
        return daoSession;
    }
}
