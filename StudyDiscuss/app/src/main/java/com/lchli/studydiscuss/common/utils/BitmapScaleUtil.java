package com.lchli.studydiscuss.common.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class BitmapScaleUtil {

    private static final String tag = BitmapScaleUtil.class.getSimpleName();


    public static Bitmap decodeSampledBitmapFromResource(int resId, long allowedBmpMaxMemorySize) {
        try {
            Resources res = ContextProvider.context().getResources();
            final Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);
            options.inSampleSize = calculateInSampleSize(options,
                    allowedBmpMaxMemorySize);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        } catch (Error err) {// mem error.
            err.printStackTrace();

        }
        return null;
    }

    public static Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd, long allowedBmpMaxMemorySize) {
        try {
            final Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fd, null, options);
            options.inSampleSize = calculateInSampleSize(options,
                    allowedBmpMaxMemorySize);
            options.inJustDecodeBounds = false;
            Bitmap ret = BitmapFactory.decodeFileDescriptor(fd, null, options);

            return ret;
        } catch (Error err) {
            err.printStackTrace();

        }
        return null;
    }

    public static Bitmap decodeSampledBitmapFromUrl(String urlpath, long allowedBmpMaxMemorySize) {

        String tmpDir = System.getProperty("java.io.tmpdir", ".");
        File saveFile = new File(tmpDir, UUID.randomUUID().toString());
        if (saveFile.exists()) {
            saveFile.delete();
        }
        try {
            saveFile.createNewFile();
            File ret = downloadFile(urlpath, saveFile);
            if (ret == null) {// fail.
                return null;
            }
            Bitmap bmp = decodeSampledBitmapFromPath(
                    saveFile.getAbsolutePath(), allowedBmpMaxMemorySize);
            return bmp;
        } catch (Throwable err) {
            err.printStackTrace();

        } finally {
            if (saveFile.exists()) {
                saveFile.delete();// true.
            }
        }
        return null;
    }


    public static File downloadFile(String urlstr, File saveFile) {
        try {
            URL url = new URL(urlstr);// cause speed low.
            URLConnection con = url.openConnection();
            con.setDoInput(true);
            con.connect();
            InputStream ins = con.getInputStream();
            final int bufsize = 102400;

            byte[] buffer = new byte[bufsize];
            int len = -1;
            FileOutputStream bos = new FileOutputStream(saveFile);
            while ((len = ins.read(buffer)) != -1) {
                bos.write(buffer, 0, len);

            }
            ins.close();
            bos.close();
            return saveFile;
        } catch (Error e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * assume one pix use 4 bytes.Bitmap.Config.ARGB_8888.
     */
    private final static int calculateInSampleSize(
            Options options, long reqMemorySize) {
        final int onePixBytes = 4;
        int reqPixs = (int) (reqMemorySize / onePixBytes);
        final int height = options.outHeight;
        final int width = options.outWidth;
        int orgPixs = height * width;
        int inSampleSize = 1;
        while (orgPixs / Math.pow(inSampleSize, 2) > reqPixs) {
            inSampleSize *= 2;

        }
        return inSampleSize;

    }

    public static Bitmap decodeSampledBitmapFromPath(String localpath, long allowedBmpMaxMemorySize) {
        try {
            final Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(localpath, options);
            options.inSampleSize = calculateInSampleSize(options,
                    allowedBmpMaxMemorySize);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(localpath, options);
        } catch (Throwable err) {
            err.printStackTrace();

        }
        return null;
    }

    public static Bitmap decodeSampledBitmapFromByteArray(byte[] bytes, long allowedBmpMaxMemorySize) {
        try {
            final Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            options.inSampleSize = calculateInSampleSize(options,
                    allowedBmpMaxMemorySize);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        } catch (Throwable err) {
            err.printStackTrace();

        }
        return null;
    }

    public static Bitmap decodeSampledBitmapFromFile(String srcPath, int reqWidth, int reqHeight) {
        final Options options = new Options();
        options.inScaled = true;
        options.inPurgeable = true;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
            options.inPreferQualityOverSpeed = false;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            options.inMutable = false;
        }
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, options);
        options.inSampleSize = calculateSampleFactor(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(srcPath, options);

    }

    private static int calculateSampleFactor(Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap rotateImage(Bitmap bitmap, float degrees) {
        Bitmap resultBitmap = bitmap;
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(degrees);
            // Rotate the bitmap
            resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (resultBitmap != bitmap) {
            bitmap.recycle();
        }
        return resultBitmap;
    }

    public static Bitmap roundCorners(final Bitmap source, final float radius) {
        int width = source.getWidth();
        int height = source.getHeight();

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        Bitmap clipped = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(clipped);
        canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius,
                paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        Bitmap rounded = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        canvas = new Canvas(rounded);
        canvas.drawBitmap(source, 0, 0, null);
        canvas.drawBitmap(clipped, 0, 0, paint);

        source.recycle();
        clipped.recycle();

        return rounded;
    }


    public static Bitmap circleBitmap(final Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        Bitmap clipped = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(clipped);
        final float radius = width > height ? height / 2 : width / 2;
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        Bitmap rounded = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        canvas = new Canvas(rounded);
        canvas.drawBitmap(source, 0, 0, null);
        canvas.drawBitmap(clipped, 0, 0, paint);

        source.recycle();
        clipped.recycle();

        return rounded;
    }

    public static Bitmap rotateImage(Bitmap bitmap, String storagePath) {
        Bitmap resultBitmap = bitmap;
        try {
            ExifInterface exifInterface = new ExifInterface(storagePath);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 1);

            Matrix matrix = new Matrix();

            // 1: nothing to do

            // 2
            if (orientation == ExifInterface.ORIENTATION_FLIP_HORIZONTAL) {
                matrix.postScale(-1.0f, 1.0f);
            }
            // 3
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180);
            }
            // 4
            else if (orientation == ExifInterface.ORIENTATION_FLIP_VERTICAL) {
                matrix.postScale(1.0f, -1.0f);
            }
            // 5
            else if (orientation == ExifInterface.ORIENTATION_TRANSPOSE) {
                matrix.postRotate(-90);
                matrix.postScale(1.0f, -1.0f);
            }
            // 6
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
            }
            // 7
            else if (orientation == ExifInterface.ORIENTATION_TRANSVERSE) {
                matrix.postRotate(90);
                matrix.postScale(1.0f, -1.0f);
            }
            // 8
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
            }

            // Rotate the bitmap
            resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
            if (resultBitmap != bitmap) {
                bitmap.recycle();
            }
        } catch (Exception exception) {
            Log.e("BitmapUtil", "Could not rotate the image: " + storagePath);
        }
        return resultBitmap;
    }

    public static boolean saveBitmap(Bitmap bitmap, File file, int quality) {
        if (bitmap == null) return false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
