package cn.demomaster.huan.quickdeveloplibrary.widget.popup;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

public class QDPopup extends PopupWindow {
    public QDPopup(Context context) {
        super(context);
    }

    public QDPopup(View contentView, int width, int height) {
        super(contentView, width, height);
    }
}
