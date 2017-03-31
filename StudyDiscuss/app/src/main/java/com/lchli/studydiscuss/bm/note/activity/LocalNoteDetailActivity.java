package com.lchli.studydiscuss.bm.note.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.lchli.studydiscuss.R;
import com.lchli.studydiscuss.R2;
import com.lchli.studydiscuss.bm.note.NoteUtils;
import com.lchli.studydiscuss.bm.note.entity.Note;
import com.lchli.studydiscuss.bm.note.widget.LinkMovementMethodExt;
import com.lchli.studydiscuss.bm.note.widget.URLImageGetter;
import com.lchli.studydiscuss.common.base.BaseAppCompatActivity;
import com.lchli.studydiscuss.common.consts.LocalConst;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lchli on 2016/8/12.
 */

public class LocalNoteDetailActivity extends BaseAppCompatActivity {


    @BindView(R2.id.imageEditText_content)
    TextView imageEditTextContent;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R2.id.appbar)
    AppBarLayout appbar;
    @BindView(R2.id.main_content)
    CoordinatorLayout mainContent;

    private Note note;


    public static void startSelf(Context context, Note note) {
        Intent it = new Intent(context, LocalNoteDetailActivity.class);
        if (!(context instanceof Activity)) {
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        it.putExtra("note", note);
        context.startActivity(it);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        note = (Note) getIntent().getSerializableExtra("note");
        setContentView(R.layout.activity_local_note_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar.setTitle(note.title);

        imageEditTextContent.setMovementMethod(new LinkMovementMethodExt(handler, ImageSpan.class));
        String content= note.content;


        imageEditTextContent.setText(Html.fromHtml(content, new URLImageGetter(imageEditTextContent), null));
        LogUtils.e("formated note.content:" + content);



    }


    // make links and image work
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == LinkMovementMethodExt.Msg_Image_Clicked) {
                LinkMovementMethodExt.MessageSpan ms = (LinkMovementMethodExt.MessageSpan) msg.obj;
                Object[] spans = (Object[]) ms.getObj();
                for (Object span : spans) {
                    if (span instanceof ImageSpan) {
                        String imagePath = String.format("%s/%s", note.imagesDir, ((ImageSpan) span).getSource());
                        LogUtils.e("clicked img:" + imagePath);
                        ArrayList<String> imgSrcs = NoteUtils.parseImageSpanSrc(imageEditTextContent, LocalConst.STUDY_APP_ROOT_DIR);
                        int current = imgSrcs.indexOf(imagePath);
                        if (current == -1) {
                            current = 0;
                        }
                        ImageGalleryActivity.startSelf(getApplicationContext(), imgSrcs, current);
                    }
                }
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.local_note_detail_toolbar_actions, menu);
        String type = note.type;
        final int maxTypeLen = 5;
        if (type.length() >= maxTypeLen) {
            type = type.substring(0, maxTypeLen) + "...";
        }
        menu.findItem(R.id.action_note_type).setTitle(String.format("[%s]", type));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_edit_note) {
            EditNoteActivity.startSelf(this, note);
            finish();

        } else if (i == android.R.id.home) {
            finish();

        }
        return super.onOptionsItemSelected(item);
    }
}
