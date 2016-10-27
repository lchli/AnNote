package com.lchli.studydiscuss.common.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ToastUtils {

    private static Context context = ContextProvider.context();

    public static void systemToast(int id) {
        systemToast(ResUtils.parseString(id));
    }

    public static void systemToast(String format, Object... args) {
        systemToast(buildMsg(format, args));
    }

    public static void systemToast(final String msg) {
        if(TextUtils.isEmpty(msg)){
            return;
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            UiHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

    private static String buildMsg(String format, Object... args) {
        String msg = (args == null) ? format : String.format(Locale.US, format,
                args);
        return msg;
    }

    public static void popupToast(String format, Object... args) {
        popupToast(buildMsg(format, args));
    }

    private static LinkedBlockingQueue<String> msgQ = new LinkedBlockingQueue<String>(
            100);

    /**
     * show toast on floatwindow.
     *
     * @param msg
     */
    public static void popupToast(String msg) {
        msgQ.offer(msg);
        if (msgQ.size() > 1)
            return;
        doTopToast(msg);
    }

    private static void doTopToast(String msg) {
        final WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        final TextView tv = new TextView(context);
        tv.setBackgroundColor(Color.BLACK);
        tv.setTextColor(Color.WHITE);
        tv.setPadding(5, 5, 5, 5);
        tv.setText(msg);
        wm.addView(tv, getLayoutParams());
        tv.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (tv.getParent() != null) {
                    wm.removeView(tv);
                    msgQ.poll();
                    String headmsg = msgQ.peek();
                    if (headmsg != null)
                        doTopToast(headmsg);

                }
            }
        }, 2000);

    }

    private static TextView quickenTextView;

    private static volatile AtomicInteger timeCount = new AtomicInteger(2);

    /**
     * quicken switch toast.
     *
     * @param msg
     */
    public static void quickenToast(String msg) {
        if (quickenTextView != null) {
            quickenTextView.setText(msg);
            timeCount.set(2);
            return;
        }
        timeCount.set(2);
        final WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        quickenTextView = new TextView(context);
        quickenTextView.setBackgroundColor(Color.BLACK);
        quickenTextView.setTextColor(Color.WHITE);
        quickenTextView.setPadding(5, 5, 5, 5);
        quickenTextView.setText(msg);
        wm.addView(quickenTextView, getLayoutParams());
        decideDismissToast(wm);
    }

    private static void decideDismissToast(final WindowManager wm) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (timeCount.get() > 0) {
                    SystemClock.sleep(1000);
                    timeCount.decrementAndGet();
                }
                quickenTextView.post(new Runnable() {

                    @Override
                    public void run() {
                        if (quickenTextView.getParent() != null) {
                            wm.removeView(quickenTextView);
                            quickenTextView = null;

                        }
                    }
                });

            }
        }).start();

    }

    private static final WindowManager.LayoutParams getLayoutParams() {
        WindowManager.LayoutParams mLayoutparams = new WindowManager.LayoutParams();
        mLayoutparams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mLayoutparams.gravity = Gravity.CENTER;
        mLayoutparams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutparams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutparams.format = PixelFormat.RGBA_8888;
        mLayoutparams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        return mLayoutparams;
    }
}
