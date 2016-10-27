package com.lchli.studydiscuss.common.base;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lchli on 2016/8/13.
 */

public class BaseResponse {

    @SerializedName("Code")
    public int code;

    @SerializedName("ErrorMsg")
    public String errorMsg;
}
