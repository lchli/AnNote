package com.lchli.studydiscuss.common.networkLib;

import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.lchli.studydiscuss.common.utils.JsonLogger;
import com.lchli.studydiscuss.common.utils.UiHandler;
import okhttp3.Call;
import okhttp3.Response;

public abstract class OkUiCallback<T> extends Callback<T> {

    private String originalBody;

    /**
     * run in work Thread.
     *
     * @param response
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        originalBody = response.body().string();
        String url = response.request().url().uri().toString();
        LogUtils.i("ok http response string body[%s]:\n%s", url, originalBody);

        LogUtils.e("ok http response json body[%s]:\n", url);
        JsonLogger.json(originalBody);

        JSONObject jsonObject = new JSONObject(originalBody);
        final int errorCode = jsonObject.getInt("Code");
        final String msg = jsonObject.getString("ErrorMsg");
        switch (errorCode) {
            case OkErrorCode.SUCCESS:
                final T t = new Gson().fromJson(originalBody, getSuperclassTypeParameter(getClass()));
                return t;
            default:
                onFailInUiThread(new OkError(msg, errorCode));
                break;
        }
        return null;
    }

    private void onFailInUiThread(final OkError okError) {
        UiHandler.post(new Runnable() {
            @Override
            public void run() {
                onFail(okError);
            }
        });
    }

    private Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameter = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);
    }

    @Override
    public final void onResponse(T resp, int id) {
        if (resp == null) {
            return;
        }
        onSuccess(resp);
    }

    @Override
    public final void onError(Call call, Exception e, int id) {
        e.printStackTrace();
        onFail(new OkError(e.getMessage(), OkErrorCode.EXCEPTION));
    }

    /**
     * @param response Assert response is not null here.
     */
    public abstract void onSuccess(T response);

    public abstract void onFail(OkError error);

    public String getOkResponseBody() {
        return originalBody;
    }

}
