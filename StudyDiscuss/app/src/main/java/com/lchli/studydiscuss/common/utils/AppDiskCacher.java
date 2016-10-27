package com.lchli.studydiscuss.common.utils;

import com.lchli.studydiscuss.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cn.finalteam.toolsfinal.io.FileUtils;
import com.lchli.studydiscuss.common.consts.LocalConst;

/**
 * Created by lchli on 2016/9/16.
 */

public class AppDiskCacher {

    private static final long DISK_CACHE_SIZE = 10 * 1024 * 1024;
    private static final String DISK_CACHE_DIR = String.format("%s/%s", LocalConst.STUDY_APP_ROOT_DIR, "OkHttpCache");

    static {
        FileUtils.mkdirs(new File(DISK_CACHE_DIR));
    }


    private SimpleDiskCache diskCache;

    private AppDiskCacher() {
        try {
            diskCache = SimpleDiskCache.open(new File(DISK_CACHE_DIR), BuildConfig.VERSION_CODE, DISK_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AppDiskCacher getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void putCache(String key, String value, long durationMillsecs) {
        if (diskCache == null) {
            return;
        }
        key = SecureHashUtil.makeMD5Hash(key);

        Map<String, Serializable> meta = new HashMap<>();
        meta.put("expiredTime", durationMillsecs + System.currentTimeMillis());
        meta.put("appVersionCode", BuildConfig.VERSION_CODE);
        try {
            diskCache.put(key, value, meta);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCache(String key) {
        if (diskCache == null) {
            return null;
        }
        key = SecureHashUtil.makeMD5Hash(key);

        try {
            SimpleDiskCache.StringEntry cache = diskCache.getString(key);
            if (cache != null) {
                int appVersionCode = (int) cache.getMetadata().get("appVersionCode");
                if (appVersionCode != BuildConfig.VERSION_CODE) {
                    diskCache.put(key, "");//delete invalid.
                    return null;
                }
                long expiredTime = (long) cache.getMetadata().get("expiredTime");
                if (!NetConnectionUtils.isConnected() || System.currentTimeMillis() < expiredTime) {
                    return cache.getString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class InstanceHolder {
        private static AppDiskCacher INSTANCE = new AppDiskCacher();
    }
}
