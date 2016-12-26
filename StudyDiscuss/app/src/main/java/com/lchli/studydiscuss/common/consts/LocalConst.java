package com.lchli.studydiscuss.common.consts;

import android.os.Environment;

import com.lchli.studydiscuss.bm.user.entity.User;
import com.lchli.studydiscuss.bm.user.model.UserSessionManager;
import com.lchli.studydiscuss.common.utils.MapUtils;

import java.io.File;
import java.util.Map;

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

    public static Map<String, String> getNoteServerVerifyParams() {
        Map<String, String> params = MapUtils.stringMap();
        User sess = UserSessionManager.getSession();
        if (sess != null) {
            params.put("Token", sess.token);
        }
        return params;
    }


}
