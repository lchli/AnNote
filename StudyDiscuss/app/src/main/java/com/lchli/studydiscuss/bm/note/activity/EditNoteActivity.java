package com.lchli.studydiscuss.bm.note.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.lchli.studydiscuss.R;
import com.lchli.studydiscuss.R2;
import com.lchli.studydiscuss.StudyApp;
import com.lchli.studydiscuss.bm.note.NoteUtils;
import com.lchli.studydiscuss.bm.note.busEvent.LocalNoteListChangedEvent;
import com.lchli.studydiscuss.bm.note.entity.Note;
import com.lchli.studydiscuss.bm.note.entity.NoteType;
import com.lchli.studydiscuss.bm.note.entity.NoteTypeDao;
import com.lchli.studydiscuss.bm.note.widget.ImageEditText;
import com.lchli.studydiscuss.common.base.AbsAdapter;
import com.lchli.studydiscuss.common.base.BaseAppCompatActivity;
import com.lchli.studydiscuss.common.consts.LocalConst;
import com.lchli.studydiscuss.common.utils.BitmapScaleUtil;
import com.lchli.studydiscuss.common.utils.ContextProvider;
import com.lchli.studydiscuss.common.utils.EventBusUtils;
import com.lchli.studydiscuss.common.utils.ListUtils;
import com.lchli.studydiscuss.common.utils.ScreenHelper;
import com.lchli.studydiscuss.common.utils.TimeUtils;
import com.lchli.studydiscuss.common.utils.ToastUtils;
import com.lchli.studydiscuss.common.utils.UUIDUtils;
import com.lchli.studydiscuss.common.widget.ListPopup;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.toolsfinal.io.FileUtils;

import static android.view.View.inflate;

/**
 * Created by lchli on 2016/8/12.
 */

public class EditNoteActivity extends BaseAppCompatActivity {


  private static final int REQUEST_CODE_GALLERY = 1;
  private static final int REQUEST_CODE_CAMERA = 2;



  @BindView(R2.id.tv_note_category)
  TextView tvNoteCategory;
  @BindView(R2.id.et_note_title)
  EditText etNoteTitle;
  @BindView(R2.id.imageEditText_content)
  ImageEditText imageEditTextContent;
  @BindView(R2.id.bt_save)
  Button btSave;
  @BindView(R2.id.bt_more)
  Button btMore;
  @BindView(R2.id.title_bar)
  View title_bar;

  private String courseUUID;
  private String courseDir;
  private Note oldNote;
  private ListPopup noteTypePop;
  private NoteTypeAdapter mNoteTypeAdapter;

  public static void startSelf(Context context) {
    Intent it = new Intent(context, EditNoteActivity.class);
    if (!(context instanceof Activity)) {
      it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
    context.startActivity(it);
  }

  public static void startSelf(Context context, Note note) {
    Intent it = new Intent(context, EditNoteActivity.class);
    if (!(context instanceof Activity)) {
      it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
    it.putExtra("note", note);
    context.startActivity(it);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    oldNote = (Note) getIntent().getSerializableExtra("note");
    setContentView(R.layout.activity_edit_note);
    ButterKnife.bind(this);

    settingNotePopup();
    imageEditTextContent.setMovementMethod(ScrollingMovementMethod.getInstance());
    if (oldNote != null) {
      courseUUID = oldNote.uid;
      courseDir = oldNote.imagesDir;

      tvNoteCategory.setText(oldNote.type);
      etNoteTitle.setText(oldNote.title);
      imageEditTextContent.setText(Html.fromHtml(oldNote.content, localImageGetter, null));
    } else {
      courseUUID = UUIDUtils.uuid();
      courseDir = NoteUtils.buildNoteDir(courseUUID);
      FileUtils.mkdirs(new File(courseDir));
    }

  }

  private void settingNotePopup() {
    noteTypePop = new ListPopup(this);
    noteTypePop.setItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NoteType tag = (NoteType) parent.getAdapter().getItem(position);
        tvNoteCategory.setText(tag.name);
        noteTypePop.dismiss();
      }
    });
    View header = View.inflate(this, R.layout.list_header_note_type, null);
    final EditText tagWidget = (EditText) header.findViewById(R.id.tag_widget);
    ImageView delete_widget = (ImageView) header.findViewById(R.id.delete_widget);
    delete_widget.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String inputType = tagWidget.getText().toString();
        if (TextUtils.isEmpty(inputType)) {
          return;
        }
        QueryBuilder<NoteType> buider =
            StudyApp.instance().getDaoSession().getNoteTypeDao().queryBuilder();
        buider.where(NoteTypeDao.Properties.Name.eq(inputType));
        List<NoteType> old = buider.list();

        if (!ListUtils.isEmpty(old)) {
          ToastUtils.systemToast("标签已经存在!");
          return;
        }
        StudyApp.instance().getDaoSession().getNoteTypeDao().insert(new NoteType(inputType));
        loadNoteTypes();
      }
    });

    noteTypePop.getListView().addHeaderView(header);
    noteTypePop.getListView()
        .setDivider(new ColorDrawable(getResources().getColor(R.color.list_divider_color)));
    noteTypePop.getListView()
        .setDividerHeight(getResources().getDimensionPixelSize(R.dimen.list_divider_height));
    mNoteTypeAdapter = new NoteTypeAdapter();
    noteTypePop.setAdapter(mNoteTypeAdapter);
    loadNoteTypes();
  }

  private void loadNoteTypes() {
    new AsyncTask<Void, Void, List<NoteType>>() {
      @Override
      protected List<NoteType> doInBackground(Void... params) {
        return StudyApp.instance().getDaoSession().getNoteTypeDao().queryRaw("");
      }

      @Override
      protected void onPostExecute(List<NoteType> noteTypes) {
        LogUtils.e(noteTypes);
        mNoteTypeAdapter.refresh(noteTypes);

      }
    }.execute();
  }

  private Html.ImageGetter localImageGetter = new Html.ImageGetter() {
    @Override
    public Drawable getDrawable(String source) {
      LogUtils.e("ImageGetter thread:" + Thread.currentThread().getName());

      String imagePath =LocalConst.STUDY_APP_ROOT_DIR+ source;

      Bitmap bmp =
          BitmapScaleUtil.decodeSampledBitmapFromPath(imagePath, LocalConst.BITMAP_MAX_MEMORY);
      if (bmp == null) {
        return null;
      }

      int w = bmp.getWidth();
      int h = bmp.getHeight();

      Drawable drawable = new BitmapDrawable(bmp);
      int left = (ScreenHelper.getScreenWidth(getApplicationContext()) - w) / 2;
      drawable.setBounds(left, 0, w + left, h);
      return drawable;

    }
  };

  @OnClick({R2.id.bt_save, R2.id.bt_more, R2.id.tv_note_category})
  public void onClick(View view) {
    switch (view.getId()) {
      case R2.id.bt_save:
        String title = etNoteTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
          ToastUtils.systemToast(R.string.note_title_cannot_empty);
          return;
        }
        String htmlContent = Html.toHtml(imageEditTextContent.getText());
        if (TextUtils.isEmpty(htmlContent)) {
          ToastUtils.systemToast(R.string.note_content_cannot_empty);
          return;
        }
        final String thumbName = UUIDUtils.uuid() + ".jpg";
        final File destFile = new File(courseDir, thumbName);
        if (!destFile.exists()) {
          try {
            destFile.createNewFile();
          } catch (IOException e) {
            ToastUtils.systemToast(R.string.save_note_thumb_fail);
            e.printStackTrace();
            return;
          }
        }
        boolean isSuccess =
            BitmapScaleUtil.saveBitmap(getViewBitmap(imageEditTextContent), destFile, 100);
        if (!isSuccess) {
          ToastUtils.systemToast(R.string.save_note_thumb_fail);
          return;
        }
        Note note = new Note();
        note.type = tvNoteCategory.getText().toString();
        note.title = title;
        note.uid = courseUUID;
        note.lastModifyTime = TimeUtils.getTime(System.currentTimeMillis());
        note.content = htmlContent;
        note.imagesDir = courseDir;
        note.thumbNail = thumbName;

        StudyApp.instance().getDaoSession().getNoteDao().insertOrReplace(note);
        deleteUnusedImages(note);

        EventBusUtils.post(new LocalNoteListChangedEvent());
        finish();
        break;
      case R2.id.bt_more:
        DialogPlus dialog = DialogPlus.newDialog(this)
            .setAdapter(new InsertImageDialogAdapter(getApplicationContext()))
            .setOnItemClickListener(new OnItemClickListener() {
              @Override
              public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                switch (position) {
                  case 0:
                    dialog.dismiss();
                    openAlbum();
                    break;
                  case 1:
                    dialog.dismiss();
                    openCamera();
                    break;
                }

              }
            })
            .setExpanded(true) // This will enable the expand feature, (similar to android L share
                               // dialog)
            .create();
        dialog.show();

        break;
      case R2.id.tv_note_category:
        noteTypePop.showAsDropDown(title_bar);
        break;
    }
  }

  private static Bitmap getViewBitmap(View v) {
    v.setDrawingCacheEnabled(true);
    v.buildDrawingCache();
    return v.getDrawingCache();
  }


  private void openAlbum() {
    GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY,
        new GalleryFinal.OnHanlderResultCallback() {
          @Override
          public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (!ListUtils.isEmpty(resultList)) {
              String imagePath = resultList.get(0).getPhotoPath();
              imageEditTextContent.insertImage(imagePath, localImageGetter, courseDir,courseUUID);
            }

          }

          @Override
          public void onHanlderFailure(int requestCode, String errorMsg) {
            ToastUtils.systemToast(errorMsg);
          }
        });
  }

  private void openCamera() {
    GalleryFinal.openCamera(REQUEST_CODE_CAMERA, new GalleryFinal.OnHanlderResultCallback() {
      @Override
      public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
        if (!ListUtils.isEmpty(resultList)) {
          String imagePath = resultList.get(0).getPhotoPath();
          imageEditTextContent.insertImage(imagePath, localImageGetter, courseDir,courseUUID);
        }
      }

      @Override
      public void onHanlderFailure(int requestCode, String errorMsg) {
        ToastUtils.systemToast(errorMsg);
      }
    });

  }

  @Override
  public void onBackPressed() {
    if (oldNote != null) {
      deleteUnusedImages(oldNote);
    } else {
      FileUtils.deleteQuietly(new File(courseDir));
    }
    super.onBackPressed();
  }

  private void deleteUnusedImages(Note note) {
    File[] images = new File(courseDir).listFiles();
    if (!ArrayUtils.isEmpty(images)) {
      for (File img : images) {
        if (StringUtils.equals(img.getName(), note.thumbNail)) {
          continue;
        }
        if (note.content == null || !note.content.contains(img.getName())) {
          img.delete();
        }

      }
    }
  }

  class NoteTypeAdapter extends AbsAdapter<NoteType> {



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ViewHolder(viewType);
    }

    @Override
    public void onBindViewHolder(AbsViewHolder vh, int position) {
      final NoteType type = getItem(position);
      ViewHolder holder = (ViewHolder) vh;
      holder.tagWidget.setText(type.name);
      holder.deleteWidget.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          StudyApp.instance().getDaoSession().getNoteTypeDao().delete(type);
          loadNoteTypes();
        }
      });

    }

    class ViewHolder extends AbsViewHolder {
      @BindView(R2.id.tag_widget)
      TextView tagWidget;
      @BindView(R2.id.delete_widget)
      ImageView deleteWidget;
      View view;

      public ViewHolder(int viewType) {
        super(viewType);
        view = inflate(ContextProvider.context(), R.layout.list_item_note_type, null);
        ButterKnife.bind(this, view);
      }

      @Override
      protected View getItemView() {
        return view;
      }
    }
  }


  static class InsertImageDialogAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    public InsertImageDialogAdapter(Context context) {
      layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
      return 2;
    }

    @Override
    public Object getItem(int position) {
      return position;
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
      View view = convertView;

      if (view == null) {
        view = layoutInflater.inflate(R.layout.list_item_insert_image_dialog, parent, false);

        viewHolder = new ViewHolder();
        viewHolder.textView = (TextView) view.findViewById(R.id.text_view);
        viewHolder.imageView = (ImageView) view.findViewById(R.id.image_view);
        view.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) view.getTag();
      }

      Context context = parent.getContext();
      switch (position) {
        case 0:
          viewHolder.textView.setText(context.getString(R.string.photo_album));
          viewHolder.imageView.setImageResource(R.drawable.ic_album);
          break;
        case 1:
          viewHolder.textView.setText(context.getString(R.string.camera));
          viewHolder.imageView.setImageResource(R.drawable.ic_camera);
          break;
      }

      return view;
    }

    static class ViewHolder {
      TextView textView;
      ImageView imageView;
    }
  }
}
