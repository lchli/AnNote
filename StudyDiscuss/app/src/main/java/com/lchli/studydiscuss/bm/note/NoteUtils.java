package com.lchli.studydiscuss.bm.note;

import android.text.Spannable;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.lchli.studydiscuss.common.consts.LocalConst;
import com.lchli.studydiscuss.common.utils.UUIDUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import cn.finalteam.toolsfinal.io.FileUtils;

/**
 * Created by lchli on 2016/8/13.
 */

public class NoteUtils {

  private static final String IMAGE_WIDTH_HEIGHT_DEVIDER = "x";
  private static final String IMAGE_NAME_DEVIDER = "_";
  private static final String IMAGE_NAME_PATTERN =
      "%s" + IMAGE_WIDTH_HEIGHT_DEVIDER + "%s" + IMAGE_NAME_DEVIDER + "%s.jpg";// 300x200_test.jpg


  private static final String HTML_IMG_PATTERN = "<br><img src=\"/resources/%s/%s\" /><br>";

  private static final String NOTE_IMAGES_DIR =
      String.format("%s/%s", LocalConst.STUDY_APP_ROOT_DIR, "resources");

  static {
    FileUtils.mkdirs(new File(NOTE_IMAGES_DIR));
  }


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
      int start = imageName.lastIndexOf("/");
      int index = imageName.lastIndexOf(IMAGE_NAME_DEVIDER);
      String[] wh = imageName.substring(start + 1, index).split(IMAGE_WIDTH_HEIGHT_DEVIDER);
      int w = Integer.parseInt(wh[0]);
      int h = Integer.parseInt(wh[1]);
      return new int[] {w, h};
    } catch (Exception e) {
      e.printStackTrace();
      return new int[] {200, 200};
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



  public static String buildImgLabel(String imgName,String noteUid) {
    if (imgName == null) {
      return null;
    }
    return String.format(Locale.ENGLISH, HTML_IMG_PATTERN, noteUid, imgName);
  }

  public static String buildNoteDir(String courseUUID) {
    return String.format("%s/%s/", NOTE_IMAGES_DIR, courseUUID);
  }
}
