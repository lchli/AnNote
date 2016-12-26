package com.lchli.studydiscuss.common.utils;

import java.util.Map;

/**
 * Created by lichenghang on 2016/12/26.
 */

public class HttpHelper {

    public static String addExtraParamsToUrl(String url,Map<String,String> params){

        if(MapUtils.isEmpty(params)){
            return url;
        }

        return url+"?"+MapUtils.join(params,"=");


    }
}
