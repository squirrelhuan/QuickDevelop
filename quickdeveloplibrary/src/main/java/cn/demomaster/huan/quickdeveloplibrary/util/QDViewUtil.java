package cn.demomaster.huan.quickdeveloplibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.widget.MyRadioButton;

public class QDViewUtil {
    public static Activity getActivityFromView(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public static void setTintColor(ImageView imageView, int color) {
        Drawable drawable = imageView.getDrawable();
        if (drawable != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.setTint(color);
                imageView.setImageDrawable(drawable);
            }
        }
    }

    public static ColorStateList getColorStateList(int color) {
        int[] colors = new int[]{color, color, color, color, color, color};//{ pressed, focused, normal, focused, unable, normal };
        return getColorStateList(colors);
    }

    public static ColorStateList getColorStateList(int[] colors) {
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        return new ColorStateList(states, colors);
    }

    public static float getAttrFloatValue(Context context, int attrRes) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrRes, typedValue, true);
        return typedValue.getFloat();
    }

    public static int getAttrColor(Context context, int attrRes) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrRes, typedValue, true);
        return typedValue.data;
    }

    public static ColorStateList getAttrColorStateList(Context context, int attrRes) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrRes, typedValue, true);
        return ContextCompat.getColorStateList(context, typedValue.resourceId);
    }

    public static Drawable getAttrDrawable(Context context, int attrRes) {
        int[] attrs = new int[]{attrRes};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        Drawable drawable = getAttrDrawable(context, ta, 0);
        ta.recycle();
        return drawable;
    }

    public static Drawable getAttrDrawable(Context context, TypedArray typedArray, int index) {
        TypedValue value = typedArray.peekValue(index);
        if (value != null) {
            if (value.type != TypedValue.TYPE_ATTRIBUTE && value.resourceId != 0) {
                return QMUIDrawableHelper.getVectorDrawable(context, value.resourceId);
            }
        }
        return null;
    }

    public static int getAttrDimen(Context context, int attrRes) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrRes, typedValue, true);
        return TypedValue.complexToDimensionPixelSize(typedValue.data, QMUIDisplayHelper.getDisplayMetrics(context));

    }

    public static void handleCustomAttrs(TextView textView, AttributeSet attrs) {
        int drawableHeight = -1, drawableWidth = -1, drawableMargin;
        Context context = textView.getContext();
        if (attrs == null) {
            return;
        }

        //设置drawble的大小
        Drawable[] drawables = textView.getCompoundDrawables();
        if (drawables != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QDTextView);
            drawableWidth = typedArray.getDimensionPixelOffset(R.styleable.QDTextView_qd_drawable_width, drawableWidth);
            drawableHeight = typedArray.getDimensionPixelOffset(R.styleable.QDTextView_qd_drawable_height, drawableHeight);
            typedArray.recycle();
            for (Drawable drawable : drawables) {
                if (drawable != null) {
                    int w = drawableWidth;
                    int h = drawableHeight;
                    if (w == -1||h == -1) {//若未指定大小则使用bitmap的自身大小
                        Bitmap bitmap = QDBitmapUtil.getBitmapByDrawable(drawable);
                        if (w == -1) {
                            w = bitmap.getWidth();
                        }
                        if (h == -1) {
                            h = bitmap.getHeight();
                        }
                    }
                    drawable.setBounds(0, 0, w, h);
                }
            }
            textView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);//将改变了属性的drawable再重新设置回去
        }
    }
}
