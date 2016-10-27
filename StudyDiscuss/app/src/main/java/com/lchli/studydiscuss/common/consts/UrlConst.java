package com.lchli.studydiscuss.common.consts;

import com.lchli.studydiscuss.bm.config.AppEnvironmentFactory;

/**
 * Created by lchli on 2016/8/13.
 */

public class UrlConst {

    public static  String DEFAULT_IP = AppEnvironmentFactory.getEnv().getIP();
    public static final String SERVER = DEFAULT_IP + ":9090";
    public static final String LOGIN_URL = SERVER + "/Login";
    public static final String REGISTER_URL = SERVER + "/Register";
    public static final String UPLOAD_NOTE_URL = SERVER + "/UploadNote";
    public static final String GET_ALL_NOTES_URL = SERVER + "/GetAllNotes";
    public static final String UPLOAD_USER_PORTRAIT_URL = SERVER + "/UploadUserPortrait";
    public static final String DELETE_NOTE_URL = SERVER + "/DeleteNote";
    public static final String GET_USER_INFO_URL = SERVER + "/GetUserInfo";


}
