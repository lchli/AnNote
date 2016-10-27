package com.lchli.studydiscuss.common.utils;

import android.content.Context;

/**
 * Created by lchli on 2016/4/2.
 */
public final class ContextProvider {

    private static Context sContext;

    public static Context context() {
        if (sContext == null) {
            throw new IllegalStateException("context is not init,you must call initContext first!");
        }
        return sContext;
    }

    public static void initContext(Context context) {
        sContext = context.getApplicationContext();
    }
}
