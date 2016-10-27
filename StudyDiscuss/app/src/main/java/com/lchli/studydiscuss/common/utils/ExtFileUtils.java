package com.lchli.studydiscuss.common.utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by lchli on 2016/8/27.
 */

public class ExtFileUtils {

    public static boolean makeFile(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            try {
                return f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }
}
