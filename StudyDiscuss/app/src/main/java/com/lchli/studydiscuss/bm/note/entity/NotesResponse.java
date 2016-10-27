package com.lchli.studydiscuss.bm.note.entity;

import com.google.gson.annotations.SerializedName;
import com.lchli.studydiscuss.common.base.BaseResponse;

import java.util.List;

/**
 * Created by lchli on 2016/8/13.
 */

public class NotesResponse extends BaseResponse {

    @SerializedName("Notes")
    public List<Note> notes;

}
