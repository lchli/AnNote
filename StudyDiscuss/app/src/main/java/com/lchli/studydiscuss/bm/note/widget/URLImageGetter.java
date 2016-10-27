package com.lchli.studydiscuss.bm.note.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import com.lchli.studydiscuss.common.consts.LocalConst;

import com.lchli.studydiscuss.common.utils.BitmapScaleUtil;
import com.lchli.studydiscuss.common.utils.UiHandler;

public class URLImageGetter implements Html.ImageGetter {

    private TextView textView;
    private Context context;
    private String imageDir;

    public URLImageGetter(String imageDir, TextView textView) {
        this.context = textView.getContext();
        this.textView = textView;
        this.imageDir = imageDir;
    }

    @Override
    public Drawable getDrawable(String src) {
        URLDrawable urlDrawable = new URLDrawable(context, src);

        ImageGetterAsyncTask getterTask = new ImageGetterAsyncTask(urlDrawable);
        getterTask.execute(src);
        return urlDrawable;
    }

    private class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {

        URLDrawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable drawable) {
            this.urlDrawable = drawable;
        }

        @Override
        protected void onPostExecute(final Drawable result) {
            if (result != null) {
                urlDrawable.setDrawable(result);
                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.invalidate();
                    }
                });

            }
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            Bitmap bmp;
            if (isNetImagePath(imageDir)) {
                bmp = BitmapScaleUtil.decodeSampledBitmapFromUrl(imageDir + source, LocalConst.BITMAP_MAX_MEMORY);
            } else {
                bmp = BitmapScaleUtil.decodeSampledBitmapFromPath(imageDir + "/" + source, LocalConst.BITMAP_MAX_MEMORY);
            }
            if (bmp == null) {
                return null;
            }
            return new BitmapDrawable(bmp);
        }


    }

    private static boolean isNetImagePath(String path) {
        if (path == null) {
            return false;
        }
        return path.startsWith("http://") || path.startsWith("https://");
    }

}