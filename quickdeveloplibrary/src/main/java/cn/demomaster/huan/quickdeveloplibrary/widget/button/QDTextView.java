package cn.demomaster.huan.quickdeveloplibrary.widget.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIViewHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDRoundButtonDrawable;

/**
 * @author squirrel桓
 * @date 2019/1/7.
 * description：
 */
public class QDTextView extends AppCompatTextView {
    public QDTextView(Context context) {
        super(context);
        init(context,null,0);
    }

    public QDTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public QDTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获取selectableItemBackground中对应的attrId
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);

            int[] attribute = new int[]{android.R.attr.selectableItemBackground};
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(typedValue.resourceId, attribute);
            setForeground(typedArray.getDrawable(0));
            typedArray.recycle();
        }
        StateListDrawable drawable = new StateListDrawable();
        //如果要设置莫项为false，在前面加负号 ，比如android.R.attr.state_focesed标志true，-android.R.attr.state_focesed就标志false

        QDRoundButtonDrawable bg_normal = fromAttributeSet(context, attrs, defStyleAttr);
        /*QDRoundButtonDrawable bg_focused = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
        QDRoundButtonDrawable bg_pressed = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
        QDRoundButtonDrawable bg_selected = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);*/
        QDRoundButtonDrawable bg_focused = bg_normal;
        QDRoundButtonDrawable bg_pressed = bg_normal;
        QDRoundButtonDrawable bg_selected = bg_normal;
        drawable.addState(new int[]{android.R.attr.state_focused}, bg_focused);
        drawable.addState(new int[]{android.R.attr.state_pressed}, bg_pressed);
        drawable.addState(new int[]{android.R.attr.state_selected}, bg_selected);
        drawable.addState(new int[]{}, bg_normal);//默认
        //btn.setBackgroundDrawable(drawable);

        //QDRoundButtonDrawable bg = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
        QMUIViewHelper.setBackgroundKeepingPadding(this, drawable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    ColorStateList colorBg;
    ColorStateList colorBorder;
    int borderWidth;
    boolean isRadiusAdjustBounds;
    int mRadius;
    int mRadiusTopLeft,mRadiusTopRight,mRadiusBottomLeft,mRadiusBottomRight;
    public QDRoundButtonDrawable fromAttributeSet(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QDButton, defStyleAttr, 0);
        colorBg = typedArray.getColorStateList(R.styleable.QDButton_qd_backgroundColor);
        colorBorder = typedArray.getColorStateList(R.styleable.QDButton_qd_borderColor);
        borderWidth = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_borderWidth, 0);
        isRadiusAdjustBounds = typedArray.getBoolean(R.styleable.QDButton_qd_isRadiusAdjustBounds, false);
        mRadius = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_radius, 0);
        mRadiusTopLeft = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_radiusTopLeft, 0);
        mRadiusTopRight = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_radiusTopRight, 0);
        mRadiusBottomLeft = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_radiusBottomLeft, 0);
        mRadiusBottomRight = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_radiusBottomRight, 0);
        typedArray.recycle();
        return generateDrawable();
    }

    public void setBackgroundColor2(int color) {
        int[] colors = new int[] {color,color,color,color,color,color};//{ pressed, focused, normal, focused, unable, normal };
        int[][] states = new int[6][];
        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
        states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
        states[2] = new int[] { android.R.attr.state_enabled };
        states[3] = new int[] { android.R.attr.state_focused };
        states[4] = new int[] { android.R.attr.state_window_focused };
        states[5] = new int[] {};
        colorBg = new ColorStateList(states,colors);
        setBackground(generateDrawable());
    }

    public void setBorderColor(int color) {
        int[] colors = new int[] {color,color,color,color,color,color};//{ pressed, focused, normal, focused, unable, normal };
        int[][] states = new int[6][];
        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
        states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
        states[2] = new int[] { android.R.attr.state_enabled };
        states[3] = new int[] { android.R.attr.state_focused };
        states[4] = new int[] { android.R.attr.state_window_focused };
        states[5] = new int[] {};
        colorBorder = new ColorStateList(states,colors);
        setBackground(generateDrawable());
    }

    private QDRoundButtonDrawable generateDrawable() {
        QDRoundButtonDrawable bg = new QDRoundButtonDrawable();
        bg.setBgData(colorBg);
        bg.setStrokeData(borderWidth, colorBorder);
        if (mRadiusTopLeft > 0 || mRadiusTopRight > 0 || mRadiusBottomLeft > 0 || mRadiusBottomRight > 0) {
            float[] radii = new float[]{
                    mRadiusTopLeft, mRadiusTopLeft,
                    mRadiusTopRight, mRadiusTopRight,
                    mRadiusBottomRight, mRadiusBottomRight,
                    mRadiusBottomLeft, mRadiusBottomLeft
            };
            bg.setCornerRadii(radii);
            isRadiusAdjustBounds = false;
        } else {
            bg.setCornerRadius(mRadius);
            if (mRadius > 0) {
                isRadiusAdjustBounds = false;
            }
        }
        bg.setIsRadiusAdjustBounds(isRadiusAdjustBounds);
        return bg;
    }
}
