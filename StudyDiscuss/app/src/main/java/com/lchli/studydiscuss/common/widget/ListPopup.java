package com.lchli.studydiscuss.common.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.lchli.studydiscuss.R;
import com.lchli.studydiscuss.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lichenghang on 2016/8/15.
 */

public class ListPopup extends PopupWindow {

    private final Context context;

    @BindView(R2.id.listView)
    ListView listView;
    @BindView(R2.id.mask)
    View mask;

    public ListPopup(Context context) {
        super(context);
        this.context = context;
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
        settingContent();

    }

    public void setListViewWidth(int w) {

    }

    public void setListViewHeight(int h) {

    }


    private void settingContent() {
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.list_popup_window, null);
        setContentView(contentView);
        ButterKnife.bind(this, contentView);
    }

    public void setAdapter(ListAdapter adapter) {
        listView.setAdapter(adapter);
    }

    public void setItemClickListener(AdapterView.OnItemClickListener lsn) {
        listView.setOnItemClickListener(lsn);
    }

    public void addHeader(View header) {
        listView.addHeaderView(header);
    }

    public void addFooter(View footer) {
        listView.addFooterView(footer);
    }

    public ListView getListView() {
        return listView;
    }


    @OnClick(R2.id.mask)
    public void onClick() {
        dismiss();
    }
}
