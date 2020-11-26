package cn.demomaster.huan.quickdeveloplibrary.view.keybored;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.util.AttributeSet;

import java.lang.reflect.Field;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * Created by Administrator on 2018/3/7 0007.
 */

public class QDKeyboardView extends KeyboardView {

    private static final String TAG = "QDKeyboardView";
    private boolean isCap;
    private Drawable delDrawable;
    private Drawable lowDrawable;
    private Drawable upDrawable;
    private Drawable hideDrawable;
    /**
     * 按键的宽高至少是图标宽高的倍数
     */
    private static final int ICON2KEY = 2;

    public QDKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QDKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.isCap = false;
        this.delDrawable = null;
        this.lowDrawable = null;
        this.upDrawable = null;
        this.hideDrawable = null;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            List<Keyboard.Key> keys = getKeyboard().getKeys();
            for (Keyboard.Key key : keys) {
                if (key.codes[0] == -5 || key.codes[0] == -2 || key.codes[0] == 100860 || key.codes[0] == -1|| key.codes[0] == -3)
                    drawSpecialKey(canvas, key);
            }
        } catch (Exception e) {
            QDLogger.e(e);
        }
    }

    private void drawSpecialKey(Canvas canvas, Keyboard.Key key) {
        if (key.codes[0] == -5) {
            drawKeyBackground(R.drawable.keyboard_change, canvas, key);
            drawTextAndIcon(canvas, key, delDrawable);
        } else if (key.codes[0] == -2 || key.codes[0] == 100860) {
            drawKeyBackground(R.drawable.keyboard_change, canvas, key);
            drawTextAndIcon(canvas, key, null);
        } else if (key.codes[0] == -1) {
            if (isCap) {
                drawKeyBackground(R.drawable.keyboard_change, canvas, key);
                drawTextAndIcon(canvas, key, upDrawable);
            } else {
                drawKeyBackground(R.drawable.keyboard_change, canvas, key);
                drawTextAndIcon(canvas, key, lowDrawable);
            }
        }else if (key.codes[0] == -3) {
            hideDrawable = getResources().getDrawable(R.drawable.ic_keyboard_hide_black_24dp);
            if (isCap) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    hideDrawable.setTint(Color.WHITE);
                }
                drawKeyBackground(R.drawable.keyboard_change, canvas, key);
                drawTextAndIcon(canvas, key, hideDrawable);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    hideDrawable.setTint(Color.BLACK);
                }
                drawKeyBackground(R.drawable.keyboard_change, canvas, key);
                drawTextAndIcon(canvas, key, hideDrawable);
            }
        }
    }

    private void drawKeyBackground(int id, Canvas canvas, Keyboard.Key key) {
        Drawable drawable = getContext().getResources().getDrawable(id);
        int[] state = key.getCurrentDrawableState();
        if (key.codes[0] != 0) {
            drawable.setState(state);
        }
        drawable.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        drawable.draw(canvas);
    }

    private void drawTextAndIcon(Canvas canvas, Keyboard.Key key, Drawable drawable) {
        try {
            Rect bounds = new Rect();
            Paint paint = new Paint();
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            if (key.label != null) {
                String label = key.label.toString();
                Field field;
                if (label.length() > 1 && key.codes.length < 2) {
                    int labelTextSize = 0;
                    try {
                        field = KeyboardView.class.getDeclaredField(getContext().getString(R.string.mLabelTextSize));
                        field.setAccessible(true);
                        labelTextSize = (int) field.get(this);
                    } catch (Exception e) {
                        QDLogger.e(e);
                        QDLogger.e(e==null?"e=null":e.toString());
                    }
                    paint.setTextSize(labelTextSize);
                    paint.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    int keyTextSize = 0;
                    try {
                        field = KeyboardView.class.getDeclaredField(getContext().getString(R.string.mLabelTextSize));
                        field.setAccessible(true);
                        keyTextSize = (int) field.get(this);
                    } catch (Exception e) {
                        QDLogger.e(e);
                        QDLogger.e(e==null?"e=null":e.toString());
                    }
                    paint.setTextSize(keyTextSize);
                    paint.setTypeface(Typeface.DEFAULT);
                }
                paint.getTextBounds(key.label.toString(), 0, key.label.toString().length(), bounds);
                canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                        (key.y + key.height / 2) + bounds.height() / 2, paint);
            }
            if (drawable == null) return;
            // 约定: 最终图标的宽度和高度都需要在按键的宽度和高度的二分之一以内
            // 如果: 图标的实际宽度和高度都在按键的宽度和高度的二分之一以内, 那就不需要变换, 否则就需要等比例缩小
            int iconSizeWidth, iconSizeHeight;
            key.icon = drawable;
            int iconH = DisplayUtil.px2dip(getContext(), key.icon.getIntrinsicHeight());
            int iconW = DisplayUtil.px2dip(getContext(), key.icon.getIntrinsicWidth());
            if (key.width >= (ICON2KEY * iconW) && key.height >= (ICON2KEY * iconH)) {
                //图标的实际宽度和高度都在按键的宽度和高度的二分之一以内, 不需要缩放, 因为图片已经够小或者按键够大
                setIconSize(canvas, key, iconW, iconH);
            } else {
                //图标的实际宽度和高度至少有一个不在按键的宽度或高度的二分之一以内, 需要等比例缩放, 因为此时图标的宽或者高已经超过按键的二分之一
                //需要把超过的那个值设置为按键的二分之一, 另一个等比例缩放
                //不管图标大小是多少, 都以宽度width为标准, 把图标的宽度缩放到和按键一样大, 并同比例缩放高度
                double multi = 1.0 * iconW / key.width;
                int tempIconH = (int) (iconH / multi);
                if (tempIconH <= key.height) {
                    //宽度相等时, 图标的高度小于等于按键的高度, 按照现在的宽度和高度设置图标的最终宽度和高度
                    iconSizeHeight = tempIconH / ICON2KEY;
                    iconSizeWidth = key.width / ICON2KEY;
                } else {
                    //宽度相等时, 图标的高度大于按键的高度, 这时按键放不下图标, 需要重新按照高度缩放
                    double mul = 1.0 * iconH / key.height;
                    int tempIconW = (int) (iconW / mul);
                    iconSizeHeight = key.height / ICON2KEY;
                    iconSizeWidth = tempIconW / ICON2KEY;
                }
                setIconSize(canvas, key, iconSizeWidth, iconSizeHeight);
            }
        } catch (Exception e) {
            QDLogger.e(e);
            QDLogger.e(e==null?"e=null":e.toString());
        }
    }

    private void setIconSize(Canvas canvas, Keyboard.Key key, int iconSizeWidth, int iconSizeHeight) {
        int left = key.x + (key.width - iconSizeWidth) / 2;
        int top = key.y + (key.height - iconSizeHeight) / 2;
        int right = key.x + (key.width + iconSizeWidth) / 2;
        int bottom = key.y + (key.height + iconSizeHeight) / 2;
        key.icon.setBounds(left, top, right, bottom);
        key.icon.draw(canvas);
        key.icon = null;
    }

    public void setCap(boolean cap) {
        isCap = cap;
    }

    public void setDelDrawable(Drawable delDrawable) {
        this.delDrawable = delDrawable;
    }

    public void setLowDrawable(Drawable lowDrawable) {
        this.lowDrawable = lowDrawable;
    }

    public void setUpDrawable(Drawable upDrawable) {
        this.upDrawable = upDrawable;
    }

}
