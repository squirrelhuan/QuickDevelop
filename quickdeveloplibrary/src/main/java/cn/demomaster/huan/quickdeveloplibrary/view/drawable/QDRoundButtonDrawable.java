package cn.demomaster.huan.quickdeveloplibrary.view.drawable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import cn.demomaster.huan.quickdeveloplibrary.R;

/**
 * 可以方便地生成圆角矩形/圆形 {@link android.graphics.drawable.Drawable}。
 * <p>
 * <ul>
 * <li>使用 {@link #setBgData(ColorStateList)} 设置背景色。</li>
 * <li>使用 {@link #setStrokeData(int, ColorStateList)} 设置描边大小、描边颜色。</li>
 * <li>使用 {@link #setIsRadiusAdjustBounds(boolean)} 设置圆角大小是否自动适应为 {@link android.view.View} 的高度的一半, 默认为 true。</li>
 * </ul>
 */
public class QDRoundButtonDrawable extends GradientDrawable  {

    /**
     * 圆角大小是否自适应为 View 的高度的一般
     */
    private boolean mRadiusAdjustBounds = true;
    private ColorStateList mFillColors;
    private int mStrokeWidth = 0;
    private ColorStateList mStrokeColors;

    private int colorState;

    /**
     * 设置按钮的背景色(只支持纯色,不支持 Bitmap 或 Drawable)
     */
    public void setBgData(@Nullable ColorStateList colors,int state) {
        colorState = state;
        setBgData(colors);
    }

    /**
     * 设置按钮的背景色(只支持纯色,不支持 Bitmap 或 Drawable)
     */
    public void setBgData(@Nullable ColorStateList colors) {
        if (hasNativeStateListAPI()) {
            super.setColor(colors);
        } else {
            mFillColors = colors;
            final int currentColor;
            if (colors == null) {
                currentColor = Color.TRANSPARENT;
            } else {
                currentColor = colors.getColorForState(getState(), colorState);
            }
            setColor(currentColor);
        }
    }

    /**
     * 设置按钮的描边粗细和颜色
     */
    public void setStrokeData(int width, @Nullable ColorStateList colors) {
        if (hasNativeStateListAPI()) {
            super.setStroke(width, colors);
        } else {
            mStrokeWidth = width;
            mStrokeColors = colors;
            final int currentColor;
            if (colors == null) {
                currentColor = Color.TRANSPARENT;
            } else {
                currentColor = colors.getColorForState(getState(), 0);
            }
            setStroke(width, currentColor);
        }
    }

    private boolean hasNativeStateListAPI() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 设置圆角大小是否自动适应为 View 的高度的一半
     */
    public void setIsRadiusAdjustBounds(boolean isRadiusAdjustBounds) {
        mRadiusAdjustBounds = isRadiusAdjustBounds;
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {
        boolean superRet = super.onStateChange(stateSet);
        if (mFillColors != null) {
            int color = mFillColors.getColorForState(stateSet, 0);
            setColor(color);
            superRet = true;
        }
        if (mStrokeColors != null) {
            int color = mStrokeColors.getColorForState(stateSet, 0);
            setStroke(mStrokeWidth, color);
            superRet = true;
        }
        return superRet;
    }

    @Override
    public boolean isStateful() {
        return (mFillColors != null && mFillColors.isStateful())
                || (mStrokeColors != null && mStrokeColors.isStateful())
                || super.isStateful();
    }

    int padding_internal =10;
    @Override
    protected void onBoundsChange(Rect r) {

        r.left=r.left+padding_internal;
        r.top=r.top+padding_internal;
        r.right=r.right-padding_internal;
        r.bottom=r.bottom-padding_internal;
        super.onBoundsChange(r);
        if (mRadiusAdjustBounds) {
            // 修改圆角为短边的一半
            setCornerRadius(Math.min(r.width(), r.height()) / 2);
        }
    }

    public static QDRoundButtonDrawable fromAttributeSet(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QDButton, defStyleAttr, 0);
        ColorStateList colorBg = typedArray.getColorStateList(R.styleable.QDButton_qd_backgroundColor);
        ColorStateList colorBorder = typedArray.getColorStateList(R.styleable.QDButton_qd_borderColor);
        int borderWidth = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_borderWidth, 0);
        boolean isRadiusAdjustBounds = typedArray.getBoolean(R.styleable.QDButton_qd_isRadiusAdjustBounds, false);
        int mRadius = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_radius, 0);
        int mRadiusTopLeft = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_radiusTopLeft, 0);
        int mRadiusTopRight = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_radiusTopRight, 0);
        int mRadiusBottomLeft = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_radiusBottomLeft, 0);
        int mRadiusBottomRight = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_radiusBottomRight, 0);
        typedArray.recycle();

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
            if(mRadius > 0){
                isRadiusAdjustBounds = false;
            }
        }
        bg.setIsRadiusAdjustBounds(isRadiusAdjustBounds);
        return bg;
    }

    /**
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @return
     */
    public static QDRoundButtonDrawable fromAttributeStateSet(Context context, AttributeSet attrs, int defStyleAttr,int state) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QDButton, defStyleAttr, 0);
        ColorStateList colorBg = typedArray.getColorStateList(R.styleable.QDButton_qd_backgroundColor);
        ColorStateList colorBorder = typedArray.getColorStateList(R.styleable.QDButton_qd_borderColor);
        int borderWidth = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_borderWidth, 0);
        boolean isRadiusAdjustBounds = typedArray.getBoolean(R.styleable.QDButton_qd_isRadiusAdjustBounds, false);
        int mRadius = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_radius, 0);
        int mRadiusTopLeft = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_radiusTopLeft, 0);
        int mRadiusTopRight = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_radiusTopRight, 0);
        int mRadiusBottomLeft = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_radiusBottomLeft, 0);
        int mRadiusBottomRight = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_radiusBottomRight, 0);
        typedArray.recycle();

        QDRoundButtonDrawable bg = new QDRoundButtonDrawable();
        bg.setBgData(colorBg,state);
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
            if(mRadius > 0){
                isRadiusAdjustBounds = false;
            }
        }
        bg.setIsRadiusAdjustBounds(isRadiusAdjustBounds);
        return bg;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

    }
}
