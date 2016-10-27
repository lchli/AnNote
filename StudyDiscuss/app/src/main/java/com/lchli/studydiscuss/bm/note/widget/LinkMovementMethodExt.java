package com.lchli.studydiscuss.bm.note.widget;

import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by lchli on 2016/8/13.
 */

public class LinkMovementMethodExt extends LinkMovementMethod {

    private Class spanClass = null;
    private Handler handler;
    public static final int Msg_Image_Clicked = 200;


    public LinkMovementMethodExt(Handler _handler, Class _spanClass) {
        handler = _handler;
        spanClass = _spanClass;
    }

    int x1;
    int x2;
    int y1;
    int y2;

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = (int) event.getX();
            y1 = (int) event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            x2 = (int) event.getX();
            y2 = (int) event.getY();
            if (Math.abs(x1 - x2) < 10 && Math.abs(y1 - y2) < 10) {
                x2 -= widget.getTotalPaddingLeft();
                y2 -= widget.getTotalPaddingTop();
                x2 += widget.getScrollX();
                y2 += widget.getScrollY();
                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y2);
                int off = layout.getOffsetForHorizontal(line, x2);
                /**             * get you interest span             */
                Object[] spans = buffer.getSpans(off, off, spanClass);
                if (spans.length != 0) {
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(spans[0]),
                            buffer.getSpanEnd(spans[0]));
                    MessageSpan obj = new MessageSpan();
                    obj.setObj(spans);
                    obj.setView(widget);
                    Message message = handler.obtainMessage();
                    message.obj = obj;
                    message.what = Msg_Image_Clicked;
                    message.sendToTarget();
                    return true;
                }
            }
        }
        return super.onTouchEvent(widget, buffer, event);
    }


    public class MessageSpan {
        private Object obj;
        private TextView view;

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public TextView getView() {
            return view;
        }

        public void setView(TextView view) {
            this.view = view;
        }
    }
}
