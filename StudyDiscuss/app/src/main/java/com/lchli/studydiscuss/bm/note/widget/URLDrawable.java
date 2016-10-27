package com.lchli.studydiscuss.bm.note.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.lchli.studydiscuss.bm.note.NoteUtils;

import com.lchli.studydiscuss.common.utils.ScreenHelper;

public class URLDrawable extends BitmapDrawable {

    private final Rect bound;
    private Drawable drawable;
    private String imageName;

    public URLDrawable(Context context, String imageName) {
        this.imageName = imageName;
        this.bound = getDefaultImageBounds(context);
        this.setBounds(bound);
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
        drawable.setBounds(bound);
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        if (drawable != null) {
            drawable.draw(canvas);
        }
    }

    private Rect getDefaultImageBounds(Context context) {
        int width = ScreenHelper.getScreenWidth(context);
        int[] size = NoteUtils.parseNoteImageSize(imageName);
        int w = size[0];
        int h = size[1];
        return new Rect((width - w) / 2, 0, (width - w) / 2 + w, h);

    }
}