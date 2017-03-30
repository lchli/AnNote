package com.lchli.studydiscuss.bm.user.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lchli.studydiscuss.R;
import com.lchli.studydiscuss.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lchli on 2016/8/14.
 */

public class UserCenterListItem extends FrameLayout {


    @BindView(R2.id.icon_widget)
    public ImageView iconWidget;
    @BindView(R2.id.text_widget)
    public TextView textWidget;

    public UserCenterListItem(Context context) {
        super(context);
        init(null);
    }

    public UserCenterListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public UserCenterListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.list_item_user_center, this);
        ButterKnife.bind(this);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UserCenterListItem);
            String text = a.getString(R.styleable.UserCenterListItem_UserCenterListItem_text);
            Drawable icon = a.getDrawable(R.styleable.UserCenterListItem_UserCenterListItem_icon);
            a.recycle();
            if (text != null) {
                textWidget.setText(text);
            }
            if (icon != null) {
                iconWidget.setImageDrawable(icon);
            }
        }
    }
}
