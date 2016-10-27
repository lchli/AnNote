package com.lchli.studydiscuss.bm.note.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.EditText;

import com.lchli.studydiscuss.R;
import com.lchli.studydiscuss.common.consts.LocalConst;
import com.lchli.studydiscuss.bm.note.NoteUtils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import com.lchli.studydiscuss.common.utils.BitmapScaleUtil;
import com.lchli.studydiscuss.common.utils.ToastUtils;

/**
 * Created by lchli on 2016/8/12.
 */

public class ImageEditText extends EditText {

    private static final String HTML_IMG_PATTERN = "<br><img src=\"%s\" /><br>";

    public ImageEditText(Context context) {
        super(context);
    }

    public ImageEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        setSelection(getText().length());
    }

    public void insertImage(final String imagePath, final Html.ImageGetter imageGetter, final String courseDir) {
        final File sourceImage = new File(imagePath);
        if (!sourceImage.exists()) {
            ToastUtils.systemToast(R.string.insert_image_fail);
            return;
        }
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    Bitmap bmp = BitmapScaleUtil.decodeSampledBitmapFromPath(imagePath, LocalConst.BITMAP_MAX_MEMORY);
                    final String imageName = NoteUtils.buildNoteImageName(bmp.getWidth(), bmp.getHeight());
                    final File destFile = new File(courseDir, imageName);
                    if (!destFile.exists()) {
                        try {
                            destFile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                            ToastUtils.systemToast(R.string.insert_image_fail);
                            return null;
                        }
                    }
                    BitmapScaleUtil.saveBitmap(bmp, destFile, 100);
                    bmp.recycle();
                    return imageName;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String imageName) {
                if (imageName != null) {
                    String imageTag = String.format(Locale.ENGLISH, HTML_IMG_PATTERN, imageName);
                    int selection = getSelectionStart();
                    if (selection == -1) {
                        selection = 0;
                    }
                    getText().insert(selection, Html.fromHtml(imageTag, imageGetter, null));
                } else {
                    ToastUtils.systemToast(R.string.insert_image_fail);
                }
            }
        }.execute();

    }


}
