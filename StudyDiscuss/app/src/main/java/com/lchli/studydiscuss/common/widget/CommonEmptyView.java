package com.lchli.studydiscuss.common.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lchli.studydiscuss.R;


/**
 * 通用的空页面。
 * Created by lichenghang on 2016/4/5.
 */
public class CommonEmptyView extends FrameLayout {

    public static final int DEF_TEXT_SIZE_SP = 16;
    public static final int DEF_TEXT_COLOR = Color.parseColor("#646464");
    private LinearLayout rootView;

    public CommonEmptyView(Context context) {
        super(context);
        init();
    }

    public CommonEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CommonEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.common_empty_view, this);
        rootView = (LinearLayout) findViewById(R.id.rootView);
    }

    public TextView addEmptyText(String text) {
       return addEmptyText(text, null);
    }

    public TextView addEmptyText(String text, OnClickListener lsn) {
        TextView tv = new TextView(getContext());
        tv.setText(text);
        tv.setTextSize(DEF_TEXT_SIZE_SP);
        tv.setTextColor(DEF_TEXT_COLOR);
        tv.setOnClickListener(lsn);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        rootView.addView(tv, lp);
        return tv;

    }

    public void addEmptyImage(int imageResId) {
        addEmptyImage(imageResId, null);
    }

    public void addEmptyImage(int imageResId, OnClickListener lsn) {
        ImageView iv = new ImageView(getContext());
        iv.setImageResource(imageResId);
        iv.setOnClickListener(lsn);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        rootView.addView(iv, lp);
    }

    public void show() {
        rootView.setVisibility(View.VISIBLE);
    }

    public void hide() {
        rootView.setVisibility(View.GONE);
    }
}
