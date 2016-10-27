package com.lchli.studydiscuss.bm.config;

/**
 * Created by lchli on 2016/10/26.
 */

public class DebugEnv implements AppEnvironment {
    @Override
    public String getIP() {
        return "http://192.168.1.7";
    }

    @Override
    public boolean logFlag() {
        return true;
    }
}
