package com.lchli.studydiscuss.common.networkLib;

import java.io.Serializable;

/**
 * Created by lichenghang on 2016/4/26.
 */
public class OkError implements Serializable {


    public String errMsg;
    public int errCode;

    public OkError(String errMsg, int errCode) {
        this.errMsg = errMsg;
        this.errCode = errCode;
    }
}
