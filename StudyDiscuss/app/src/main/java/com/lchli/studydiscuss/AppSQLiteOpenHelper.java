package com.lchli.studydiscuss;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.apkfuns.logutils.LogUtils;
import com.lchli.studydiscuss.bm.note.entity.DaoMaster;
import com.lchli.studydiscuss.common.consts.LocalConst;
import com.lchli.studydiscuss.common.utils.ExtFileUtils;

import org.greenrobot.greendao.database.Database;

import java.io.File;

import cn.finalteam.toolsfinal.io.FileUtils;

public class AppSQLiteOpenHelper extends DaoMaster.DevOpenHelper {

    private static final String DB_DIR = String.format("%s/%s", LocalConst.STUDY_APP_ROOT_DIR, "database");

    static {
        FileUtils.mkdirs(new File(DB_DIR));
    }

    public AppSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, boolean isSdcardDatabase) {
        super(chooseContext(context, isSdcardDatabase), name, factory);
    }

    private static Context chooseContext(Context context, boolean isSdcardDatabase) {
        if (!isSdcardDatabase) {
            return context;
        }
        return new ContextWrapper(context) {
            @Override
            public File getDatabasePath(String name) {
                File noteDb = new File(DB_DIR, name);
                LogUtils.e("DB_DIR exists:" + new File(DB_DIR).exists());
                ExtFileUtils.makeFile(noteDb.getAbsolutePath());
                LogUtils.e("db:" + noteDb.getAbsolutePath());
                return noteDb;
            }

            @Override
            public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
                return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
            }

            @Override
            public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
                return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name).getPath(), factory, errorHandler);
            }

        };
    }


    /**
     * in production environment,you can Override this to impl your needs.
     * note:when upgrade you must modify DaoMaster#SCHEMA_VERSION.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}