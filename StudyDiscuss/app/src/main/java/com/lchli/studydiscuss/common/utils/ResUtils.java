package com.lchli.studydiscuss.common.utils;

/**
 * Created by lchli on 2016/4/2.
 */
public class ResUtils {

    public static String parseString(int resId, Object... args) {
        return ContextProvider.context().getString(resId, args);
    }

    public static String[] parseStringArray(int resId) {
        return ContextProvider.context().getResources().getStringArray(resId);
    }

    public static int parseDimenPix(int resId) {
        return ContextProvider.context().getResources().getDimensionPixelSize(resId);
    }

    public static int parseColor(int resId) {
        return ContextProvider.context().getResources().getColor(resId);
    }
}
