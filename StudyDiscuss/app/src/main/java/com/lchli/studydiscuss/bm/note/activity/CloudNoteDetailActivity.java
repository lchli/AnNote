package com.lchli.studydiscuss.bm.note.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.lchli.studydiscuss.R;
import com.lchli.studydiscuss.bm.note.entity.Note;
import com.lchli.studydiscuss.bm.user.entity.RegisterReponse;
import com.lchli.studydiscuss.common.base.BaseAppCompatActivity;
import com.lchli.studydiscuss.common.consts.UrlConst;
import com.lchli.studydiscuss.common.networkLib.AppHttpManager;
import com.lchli.studydiscuss.common.networkLib.OkError;
import com.lchli.studydiscuss.common.networkLib.OkErrorCode;
import com.lchli.studydiscuss.common.networkLib.OkUiCallback;
import com.lchli.studydiscuss.common.utils.MapUtils;
import com.lchli.studydiscuss.common.utils.ToastUtils;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lchli on 2016/8/12.
 */

public class CloudNoteDetailActivity extends BaseAppCompatActivity {


  @Bind(R.id.user_portrait)
  CircleImageView userPortrait;
  @Bind(R.id.userNick)
  TextView userNick;
  @Bind(R.id.toolbar)
  Toolbar toolbar;
  @Bind(R.id.collapsing_toolbar)
  CollapsingToolbarLayout collapsingToolbar;
  @Bind(R.id.appbar)
  AppBarLayout appbar;

  @Bind(R.id.main_content)
  CoordinatorLayout mainContent;
  @Bind(R.id.web)
  WebView web;


  private Note note;


  public static void startSelf(Context context, Note note) {
    Intent it = new Intent(context, CloudNoteDetailActivity.class);
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
    setContentView(R.layout.activity_cloud_note_detail);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    collapsingToolbar.setTitle(note.title);

    LogUtils.e("note.content:" + note.content);

    Map<String, String> params = MapUtils.stringMap();
    params.put("uid", note.userId);
    AppHttpManager.get(UrlConst.GET_USER_INFO_URL, params, new OkUiCallback<RegisterReponse>() {
      @Override
      public void onSuccess(RegisterReponse response) {
        if (response.code == OkErrorCode.SUCCESS && response.user != null) {
          userNick.setText(response.user.nick);
          Glide.with(activity()).load(response.user.portraitUrl)
              .placeholder(R.drawable.default_head).into(userPortrait);
        } else {
          ToastUtils.systemToast(response.errorMsg);
        }
      }

      @Override
      public void onFail(OkError error) {
        ToastUtils.systemToast(error.errMsg);
      }
    });

    web.getSettings().setSupportZoom(false);
    web.getSettings().setBuiltInZoomControls(false);
    web.setWebViewClient(new WebClient());

    web.loadUrl(note.ShareUrl);
  }



  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.cloud_note_detail_toolbar_actions, menu);
    String type = note.type;

    final int maxTypeLen = 5;
    if (type != null && type.length() >= maxTypeLen) {
      type = type.substring(0, maxTypeLen) + "...";
    }
    menu.findItem(R.id.action_note_type).setTitle(String.format("[%s]", type));
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    if (web.canGoBack()) {
      web.goBack();
      return;
    }
    super.onBackPressed();
  }

  private static class WebClient extends WebViewClient {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
      view.loadUrl(request.getUrl().toString());
      return true;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      view.loadUrl(url);
      return true;
    }
  }

}
