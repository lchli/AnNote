package com.lchli.studydiscuss.bm.config;

import com.lchli.studydiscuss.BuildConfig;

/**
 * Created by lchli on 2016/10/26.
 */

public final class AppEnvironmentFactory {

    private static final AppEnvironment R = new ReleaseEnv();
    private static final AppEnvironment D = new DebugEnv();


    public static AppEnvironment getEnv() {
        switch (BuildConfig.BUILD_TYPE) {
            case "release":
                return R;
            case "debug":
                return D;
            default:
                throw new RuntimeException("unregonized build type!!");

        }
    }
}
