package com.lchli.studydiscuss.common.utils;

import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lichenghang on 2016/6/23.
 */
public final class JsonLogger {


    public static void json(String json) {
        int indent = 4;
        if (TextUtils.isEmpty(json)) {
            LogUtils.e("JSON{json is empty}");
            return;
        }
        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String msg = jsonObject.toString(indent);
                LogUtils.e(msg);
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String msg = jsonArray.toString(indent);
                LogUtils.e(msg);
            }
        } catch (JSONException e) {
            LogUtils.e(e.toString() + "\n\njson = " + json);
        }
    }

}
