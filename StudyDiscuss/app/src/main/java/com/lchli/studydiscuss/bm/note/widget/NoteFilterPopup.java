package com.lchli.studydiscuss.bm.note.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.lchli.studydiscuss.R;
import com.lchli.studydiscuss.bm.note.entity.NoteFilterData;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lichenghang on 2016/4/27.
 */
public class NoteFilterPopup extends PopupWindow {


    private final Context mContext;
    @Bind(R.id.title_edit)
    EditText titleEdit;
    @Bind(R.id.tag_edit)
    EditText tagEdit;
    @Bind(R.id.btFilter)
    Button btFilter;
    @Bind(R.id.maskView)
    View maskView;

    private NoteFilterData postFilterEntity = new NoteFilterData("","");
    private Callback mCallback;


    public NoteFilterPopup(final Context context) {
        super(context);
        mContext = context;
        this.setTouchable(true);
        this.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);

        View contentView = LayoutInflater.from(context).inflate(
                R.layout.popup_note_filter, null);
        setContentView(contentView);
        ButterKnife.bind(this, contentView);


    }

    public void setCallback(Callback cb) {
        mCallback = cb;
    }


    @OnClick({R.id.btFilter, R.id.maskView})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btFilter:
                if (mCallback != null) {
                    postFilterEntity.title = titleEdit.getText().toString();
                    postFilterEntity.tag = tagEdit.getText().toString();
                    mCallback.confirm(postFilterEntity);
                }
                dismiss();
                break;
            case R.id.maskView:
                dismiss();
                break;
        }
    }


    public interface Callback {

        void confirm(NoteFilterData data);
    }


}
