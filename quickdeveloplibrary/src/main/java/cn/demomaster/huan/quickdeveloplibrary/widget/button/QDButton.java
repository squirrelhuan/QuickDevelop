package cn.demomaster.huan.quickdeveloplibrary.widget.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.QDViewUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIViewHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDRoundButtonDrawable;

/**
 * Created by Squirrel桓 on 2019/1/5.
 */
public class QDButton extends AppCompatButton {

    public QDButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public QDButton(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.QDButtonStyle);
        init(context, attrs, R.attr.QDButtonStyle);
    }

    public QDButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        StateListDrawable drawable = new StateListDrawable();
        //如果要设置莫项为false，在前面加负号 ，比如android.R.attr.state_focesed标志true，-android.R.attr.state_focesed就标志false

        QDRoundButtonDrawable bg_normal = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
        /*QDRoundButtonDrawable bg_focused = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
        QDRoundButtonDrawable bg_pressed = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
        QDRoundButtonDrawable bg_selected = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);*/

        QDRoundButtonDrawable bg_enabled = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = getResources().getColor(R.color.gray);
            int color_normal = color;// bg_normal.backgroundColors.getColorForState(bg_normal.getState(),Color.RED);
            int[] colors = new int[]{color_normal, color_normal, color, color_normal, color_normal, color_normal};//{ pressed, focused, normal, focused, unable, normal };
            ColorStateList colorBg = QDViewUtil.getColorStateList(colors);
            bg_enabled.setColor(colorBg);
        }

        //TODO 处理 描边颜色

        drawable.addState(new int[]{android.R.attr.state_focused}, bg_normal);
        drawable.addState(new int[]{android.R.attr.state_pressed}, bg_normal);
        drawable.addState(new int[]{android.R.attr.state_selected}, bg_normal);
        drawable.addState(new int[]{-android.R.attr.state_enabled}, bg_enabled);
        drawable.addState(new int[]{}, bg_normal);//默认
        //btn.setBackgroundDrawable(drawable);

        //QDRoundButtonDrawable bg = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
        QMUIViewHelper.setBackgroundKeepingPadding(this, drawable);

        fromAttributeSet(context, attrs, defStyleAttr);
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

    public void setBackgroundColor(int color){
           /* QDRoundButtonDrawable qdRoundButtonDrawable = (QDRoundButtonDrawable) getBackground();
            qdRoundButtonDrawable.setColor(color);
*/
            colorBg = QDViewUtil.getColorStateList(color);
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
