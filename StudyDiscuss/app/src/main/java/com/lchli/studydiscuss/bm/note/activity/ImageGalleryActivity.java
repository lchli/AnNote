package com.lchli.studydiscuss.bm.note.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.lchli.studydiscuss.R;
import com.lchli.studydiscuss.R2;
import com.lchli.studydiscuss.common.base.BaseAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lchli on 2016/8/13.
 */

public class ImageGalleryActivity extends BaseAppCompatActivity {

    @BindView(R2.id.convenientBanner)
    ConvenientBanner convenientBanner;

    public static void startSelf(Context context, ArrayList<String> images, int current) {
        Intent it = new Intent(context, ImageGalleryActivity.class);
        if (!(context instanceof Activity)) {
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        it.putStringArrayListExtra("images", images);
        it.putExtra("current", current);
        context.startActivity(it);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        ButterKnife.bind(this);

        List<String> images = getIntent().getStringArrayListExtra("images");
        int current = getIntent().getIntExtra("current", 0);

        convenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, images)
                // .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        convenientBanner.setcurrentitem(current);


    }

    private class LocalImageHolderView implements Holder<String> {

        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String path) {
            Glide.with(context).load(path).into(imageView);
        }
    }
}
