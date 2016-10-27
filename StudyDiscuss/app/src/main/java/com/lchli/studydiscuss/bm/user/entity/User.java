package com.lchli.studydiscuss.bm.user.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lchli on 2016/8/14.
 */

public class User implements Serializable {

    @SerializedName("Name")
    public String account;
    @SerializedName("Pwd")
    public String pwd;
    @SerializedName("Uid")
    public String uid;
    @SerializedName("PortraitUrl")
    public String portraitUrl;
    @SerializedName("Nick")
    public String nick;
    @SerializedName("Token")
    public String token;

    public int userVersion;
}
