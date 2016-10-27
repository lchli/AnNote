package com.lchli.studydiscuss.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;

public class PreferenceUtil {
    private static Context sContext= ContextProvider.context();
    private static final String PREF_NAME = "prefer.xml";

    private static SharedPreferences getPref(){
        int mode = Build.VERSION.SDK_INT < 11 ? Context.MODE_PRIVATE : Context.MODE_MULTI_PROCESS;
        return sContext.getSharedPreferences(PREF_NAME, mode);
    }

    public static void putInt(String key, int value) {
        SharedPreferences pref = getPref();
        Editor ed = pref.edit();
        ed.putInt(key, value);
        ed.commit();
    }

    public static int getInt(String key, int defValue) {
        SharedPreferences pref = getPref();
        return pref.getInt(key, defValue);
    }

    public static void putString(String key, String value) {
        SharedPreferences pref = getPref();
        Editor ed = pref.edit();
        ed.putString(key, value);
        ed.commit();
    }

    public static String getString(String key, String defValue) {
        SharedPreferences pref = getPref();
        return pref.getString(key, defValue);
    }

    public static void putBool(String key, boolean value) {
        SharedPreferences pref = getPref();
        Editor ed = pref.edit();
        ed.putBoolean(key, value);
        ed.commit();
    }

    public static boolean getBool(String key, boolean defValue) {
        SharedPreferences pref = getPref();
        return pref.getBoolean(key, defValue);
    }

    public static void putLong(String key, long value) {
        SharedPreferences pref = getPref();
        Editor ed = pref.edit();
        ed.putLong(key, value);
        ed.commit();
    }

    public static long getLong(String key, long defValue) {
        SharedPreferences pref = getPref();
        return pref.getLong(key, defValue);
    }

}
