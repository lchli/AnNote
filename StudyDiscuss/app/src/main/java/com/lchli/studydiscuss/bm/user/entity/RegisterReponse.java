package com.lchli.studydiscuss.bm.user.entity;

import com.google.gson.annotations.SerializedName;
import com.lchli.studydiscuss.common.base.BaseResponse;

/**
 * Created by lchli on 2016/8/14.
 */

public class RegisterReponse extends BaseResponse {

    @SerializedName("User")
    public User user;
}
