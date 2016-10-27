package com.lchli.studydiscuss.common.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lchli.studydiscuss.R;

import com.lchli.studydiscuss.common.utils.ResUtils;

import static android.widget.RelativeLayout.LayoutParams.MATCH_PARENT;
import static android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT;

/**
 * Created by lchli on 2016/4/3.
 */
public class CommonTitleView extends FrameLayout {


    private RelativeLayout rootView;
    private int leftId = 0;
    private int rightId = 100;
    private static final int PADDING = ResUtils.parseDimenPix(R.dimen.title_view_padding);
    private static final int TEXT_SIZE_PIX = ResUtils.parseDimenPix(R.dimen.title_view_textsize);
    private static final int TEXT_COLOR = ResUtils.parseColor(R.color.titleView_textcolor);
    private static final int ICON_SIZE = ResUtils.parseDimenPix(R.dimen.title_view_iconsize);

    public CommonTitleView(Context context) {
        super(context);
        init();
    }

    public CommonTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public CommonTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.common_title_view, this);
        rootView = (RelativeLayout) findViewById(R.id.rootView);

    }

    public void addLeftText(String text, OnClickListener lsn) {
        addLeftTexttView(lsn, text, -1);
    }

    public void addLeftIcon(int iconResId, OnClickListener lsn) {
        addLeftTexttView(lsn, null, iconResId);
    }

    public void addRightText(String text, OnClickListener lsn) {
        addRightTextView(lsn, text, -1);
    }

    public void addRightIcon(int iconResId, OnClickListener lsn) {
        addRightTextView(lsn, null, iconResId);
    }

    public void setCenterText(String text, OnClickListener lsn) {
        addCenterTextView(lsn, text, -1);
    }

    public void setCenterIcon(int iconResId, OnClickListener lsn) {
        addCenterTextView(lsn, null, iconResId);
    }

    private void addCenterTextView(OnClickListener lsn, String text, int iconResId) {
        View tv = newTextView(lsn, text, iconResId);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootView.addView(tv, lp);
    }


    private void addRightTextView(OnClickListener lsn, String text, int iconResId) {
        View tv = newTextView(lsn, text, iconResId);
        tv.setId(++rightId);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        if (rightId == 101) {
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else {
            lp.addRule(RelativeLayout.LEFT_OF, rightId - 1);
        }
        rootView.addView(tv, lp);
    }

    private void addLeftTexttView(OnClickListener lsn, String text, int iconResId) {
        View tv = newTextView(lsn, text, iconResId);
        tv.setId(++leftId);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);

        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        if (leftId == 1) {
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else {
            lp.addRule(RelativeLayout.RIGHT_OF, leftId - 1);
        }
        rootView.addView(tv, lp);
    }

    private View newTextView(OnClickListener lsn, String text, int iconResId) {

        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, TEXT_SIZE_PIX);
        textView.setTextColor(TEXT_COLOR);
        textView.setGravity(Gravity.CENTER);

        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
        }
        if (iconResId >= 0) {
            textView.setBackgroundResource(iconResId);
        }
        LinearLayout item = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp;
        if (TextUtils.isEmpty(text)) {
            lp = new LinearLayout.LayoutParams(ICON_SIZE, ICON_SIZE);
        } else {
            lp = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        }
        lp.gravity=Gravity.CENTER;
        item.addView(textView, lp);
        item.setOnClickListener(lsn);
        item.setPadding(PADDING, PADDING, PADDING, PADDING);
        if (lsn != null) {
           // item.setBackgroundResource(R.drawable.selector_list_item);
        }

        return item;
    }

}
