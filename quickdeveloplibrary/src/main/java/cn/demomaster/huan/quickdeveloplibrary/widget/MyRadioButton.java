package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.QDBitmapUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDViewUtil;

public class MyRadioButton extends androidx.appcompat.widget.AppCompatRadioButton {
    public MyRadioButton(Context context) {
        super(context);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        QDViewUtil.handleCustomAttrs(this,attrs);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        QDViewUtil.handleCustomAttrs(this,attrs);
    }
}
