package com.lchli.studydiscuss.bm.note.entity;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by lchli on 2016/8/12.
 */
@Entity
public class Note implements Serializable{
    @SerializedName("ImagesDir")
    public String imagesDir;
    @SerializedName("Content")
    public String content;
    @SerializedName("LastModifyTime")
    public String lastModifyTime;
    @SerializedName("Title")
    public String title;
    @SerializedName("Type")
    public String type;
    @SerializedName("ThumbNail")
    public String thumbNail;
    @SerializedName("Uid")
    @Id
    public String uid;

    @SerializedName("UserId")
    public String userId;

    public String getUid() {
        return this.uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLastModifyTime() {
        return this.lastModifyTime;
    }
    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getImagesDir() {
        return this.imagesDir;
    }
    public void setImagesDir(String imagesDir) {
        this.imagesDir = imagesDir;
    }
    public String getThumbNail() {
        return this.thumbNail;
    }
    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Generated(hash = 2133484501)
    public Note(String imagesDir, String content, String lastModifyTime,
            String title, String type, String thumbNail, String uid, String userId) {
        this.imagesDir = imagesDir;
        this.content = content;
        this.lastModifyTime = lastModifyTime;
        this.title = title;
        this.type = type;
        this.thumbNail = thumbNail;
        this.uid = uid;
        this.userId = userId;
    }
    @Generated(hash = 1272611929)
    public Note() {
    }

}
