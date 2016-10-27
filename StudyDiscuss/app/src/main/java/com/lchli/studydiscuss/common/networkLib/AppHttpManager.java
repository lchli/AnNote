package com.lchli.studydiscuss.common.networkLib;

import com.apkfuns.logutils.LogUtils;
import com.lchli.studydiscuss.common.utils.MapUtils;
import com.lchli.studydiscuss.bm.user.model.UserSessionManager;
import com.lchli.studydiscuss.bm.user.entity.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lchli on 2016/4/25.
 */
public class AppHttpManager {

    static {
        OkHttpUtils.initClient(OKClientProvider.getSSLClient());
    }

    private static Map<String, String> commonHeaders = Collections.unmodifiableMap(getCommonHeader());


    private static Map<String, String> getCommonParams() {
        Map<String, String> commonParams = new HashMap<>();
        User se = UserSessionManager.getSession();
        if (se == null) {
            return commonParams;
        }
        commonParams.put("Token", se.token != null ? se.token : "");
        commonParams.put("UserId", se.uid != null ? se.uid : "");

        return commonParams;
    }

    private static Map<String, String> getCommonHeader() {
        Map<String, String> commonHeaders = new HashMap<>();
        LogUtils.e("http commonHeaders:\n");
        LogUtils.e(commonHeaders);
        return commonHeaders;
    }


    public static RequestCall get(String url, Map<String, String> params, OkUiCallback callback) {
        params = appendCommonParams(params, url);
        GetBuilder builder = OkHttpUtils.get().url(url).headers(commonHeaders);
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            builder.addParams(entry.getKey(), entry.getValue());
        }
        RequestCall call = builder.build();
        call.execute(callback);
        return call;
    }

    public static RequestCall post(String url, Map<String, String> params, OkUiCallback callback) {
        params = appendCommonParams(params, url);
        PostFormBuilder builder = OkHttpUtils.post().url(url).headers(commonHeaders);
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            builder.addParams(entry.getKey(), entry.getValue());
        }
        RequestCall call = builder.build();
        call.execute(callback);
        return call;
    }


    public static RequestCall downloadFile(String url, FileCallBack callback) {
        RequestCall call = OkHttpUtils
                .get()
                .url(url)
                .build();
        call.execute(callback);
        return call;
    }

    public static RequestCall uploadFile(final String url, Map<String, String> textParams, Map<String, File> fileParams, OkUiCallback callback) {
        textParams = appendCommonParams(textParams, url);
        PostFormBuilder builder = OkHttpUtils.post().headers(commonHeaders).url(url);
        if (!isNullOrEmpty(fileParams)) {
            Set<Map.Entry<String, File>> entrySet = fileParams.entrySet();
            for (Map.Entry<String, File> entry : entrySet) {
                builder.addFile(entry.getKey(), entry.getValue().getName(), entry.getValue());
            }
        }

        RequestCall call = builder.params(textParams).build();
        call.execute(callback);
        return call;
    }

    public static RequestCall uploadFile(final String url, Map<String, String> textParams, List<File> fileParams, String filesParamName, OkUiCallback callback) {
        textParams = appendCommonParams(textParams, url);
        PostFormBuilder builder = OkHttpUtils.post().headers(commonHeaders).url(url);
        if (!isEmptyList(fileParams)) {
            for (File entry : fileParams) {
                builder.addFile(filesParamName, entry.getName(), entry);
            }
        }
        RequestCall call = builder.params(textParams).build();
        call.execute(callback);
        return call;
    }


    private static Map<String, String> appendCommonParams(Map<String, String> params, String url) {
        if (!isNullOrEmpty(params)) {
//            if (UserSessionHelper.instance().isLogined()) {
//                String _r = encryptParams(params, url);
//                params.clear();
//                params.putAll(getCommonParams());
//                params.put("_r", _r);
//            } else {
//                params.putAll(getCommonParams());
//            }
            params.putAll(getCommonParams());
        } else {
            params = getCommonParams();
        }

        LogUtils.e("[%s]http totalParams:\n", url);
        LogUtils.e(params);

        return params;
    }

    public static String buildUrlCacheKey(Map<String, String> params, String url) {
        params = appendCommonParams(params, url);
        return url + "&" + MapUtils.join(params, "&");
    }


//    private static String encryptParams(Map<String, String> requestParams, String url) {
//        url = buildWholeUrl(url);
//        StringBuilder sb = new StringBuilder();
//        Set<String> keys = requestParams.keySet();
//        for (String key : keys) {
//            if (sb.length() > 0)
//                sb.append("&");
//            sb.append(key).append("=").append(requestParams.get(key));
//        }
//        sb.append("&t=t");
//
//        final String aeskey = UserSessionHelper.instance().getSession().token;
//        String secret = AesUtil.encrypt(sb.toString(), aeskey);
//        LogUtils.e("[%s]ok http  _r:\n%s", url, sb.toString());
//        LogUtils.e("[%s]ok http  _r crypted:\n%s", url, secret);
//
//        return secret;
//    }

    private static boolean isNullOrEmpty(Map map) {
        return map == null || map.isEmpty();
    }


    private static boolean isEmptyList(List list) {
        return list == null || list.isEmpty();
    }


}
