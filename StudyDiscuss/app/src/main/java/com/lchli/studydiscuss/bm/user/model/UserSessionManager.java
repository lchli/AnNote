package com.lchli.studydiscuss.bm.user.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.lchli.studydiscuss.bm.note.busEvent.CloudNoteListChangedEvent;
import com.lchli.studydiscuss.bm.user.entity.User;

import com.lchli.studydiscuss.common.utils.EventBusUtils;
import com.lchli.studydiscuss.common.utils.PreferenceUtil;

/**
 * Created by lchli on 2016/8/14.
 */

public final class UserSessionManager {

    private static final String User_Session = "User_Session";
    private static final String Last_User_Portrait = "Last_User_Portrait";
    private static final String Last_User_Account = "Last_User_Account";
    private static final int USER_VERSION = 1;

    public static User getSession() {
        String json = PreferenceUtil.getString(User_Session, null);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        User user = new Gson().fromJson(json, User.class);
        if (user.userVersion < USER_VERSION) {
            saveSession(null);
            return null;
        } else {
            return user;
        }
    }

    public static void saveSession(User user) {
        if (user == null) {
            PreferenceUtil.putString(User_Session, "");
        } else {
            user.userVersion = USER_VERSION;
            String json = new Gson().toJson(user);
            PreferenceUtil.putString(User_Session, json);

            PreferenceUtil.putString(Last_User_Portrait, user.portraitUrl);
            PreferenceUtil.putString(Last_User_Account, user.account);
        }
    }

    public static void logout() {
        saveSession(null);
        EventBusUtils.post(new CloudNoteListChangedEvent());
    }

    public static void login(User session) {
        saveSession(session);
        EventBusUtils.post(new CloudNoteListChangedEvent());
    }

    public static String getLastUserPortrait() {
        return PreferenceUtil.getString(Last_User_Portrait, "");
    }

    public static String getLastUserAccount() {
        return PreferenceUtil.getString(Last_User_Account, "");
    }

}
