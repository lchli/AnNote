package com.lchli.studydiscuss.common.consts;

import android.os.Environment;

import java.io.File;

import cn.finalteam.toolsfinal.io.FileUtils;

/**
 * Created by lchli on 2016/8/12.
 */

public final class LocalConst {

    public static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String STUDY_APP_ROOT_DIR = String.format("%s/%s", SD_PATH, "StudyApp");

    static {
        FileUtils.mkdirs(new File(STUDY_APP_ROOT_DIR));
    }

    public static final long BITMAP_MAX_MEMORY = 5 * 1024 * 1024;

}
