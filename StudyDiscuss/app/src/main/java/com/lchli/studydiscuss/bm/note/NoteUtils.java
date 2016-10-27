package com.lchli.studydiscuss.bm.note;

import android.text.Spannable;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.lchli.studydiscuss.common.utils.UUIDUtils;

import java.util.ArrayList;

/**
 * Created by lchli on 2016/8/13.
 */

public class NoteUtils {

    private static final String IMAGE_WIDTH_HEIGHT_DEVIDER = "x";
    private static final String IMAGE_NAME_DEVIDER = "_";
    private static final String IMAGE_NAME_PATTERN = "%s" + IMAGE_WIDTH_HEIGHT_DEVIDER + "%s" + IMAGE_NAME_DEVIDER + "%s.jpg";//300x200_test.jpg

    /**
     * 从TextView的Html内容里面查找img标签的src属性值。例如：<img src=... />
     *
     * @param textView
     * @param srcPrefix 需要向src值添加的前缀路径。
     * @return
     */
    public static ArrayList<String> parseImageSpanSrc(TextView textView, String srcPrefix) {
        Spannable sp = (Spannable) textView.getText();
        Object[] spans = sp.getSpans(0, sp.length(), ImageSpan.class);
        ArrayList<String> imgSrcs = new ArrayList<>();
        for (int i = 0; i < spans.length; i++) {
            Object span = spans[i];
            if (span instanceof ImageSpan) {
                String imgSrc = ((ImageSpan) span).getSource();
                imgSrcs.add(srcPrefix + imgSrc);
                LogUtils.e("imgsrc:" + imgSrc);
            }
        }
        return imgSrcs;

    }

    /**
     * 从图片的名字里面解析出图片的宽高信息。如：300x200_test.jpg
     *
     * @param imageName
     * @return 包含宽、高的数组。
     */
    public static int[] parseNoteImageSize(String imageName) {
        try {
            int index = imageName.indexOf(IMAGE_NAME_DEVIDER);
            String[] wh = imageName.substring(0, index).split(IMAGE_WIDTH_HEIGHT_DEVIDER);
            int w = Integer.parseInt(wh[0]);
            int h = Integer.parseInt(wh[1]);
            return new int[]{w, h};
        } catch (Exception e) {
            return new int[]{200, 200};
        }
    }

    /**
     * 生成图片的名字，其中包含宽、高信息。如：300x200_test.jpg
     *
     * @param width
     * @param height
     * @return
     */
    public static String buildNoteImageName(int width, int height) {
        return String.format(IMAGE_NAME_PATTERN, width, height, UUIDUtils.uuid());
    }

}
