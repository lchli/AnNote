package com.lchli.studydiscuss.bm.note.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by lchli on 2016/8/15.
 */
@Entity
public class NoteType implements Serializable {

    public String name;

    @Id(autoincrement = true)
    public Long _id;

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Generated(hash = 1736730102)
    public NoteType(String name, Long _id) {
        this.name = name;
        this._id = _id;
    }

    @Generated(hash = 1549737010)
    public NoteType() {
    }

    public NoteType(String name) {
        this.name = name;
    }
}
