package com.lchli.studydiscuss.common.utils;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by lchli on 2016/9/18.
 */

public class AppListItemAnimatorUtils {

    public static void startAnim(View view) {
        view.clearAnimation();
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.2f, 1f);
        scaleY.setDuration(300);
        scaleY.start();
    }
}
