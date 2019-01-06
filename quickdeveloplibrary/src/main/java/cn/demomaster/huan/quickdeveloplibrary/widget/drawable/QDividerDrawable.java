package cn.demomaster.huan.quickdeveloplibrary.widget.drawable;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.Gravity;

/**
 * Created by Squirrel桓 on 2019/1/6.
 */
public class QDividerDrawable extends GradientDrawable {

    private int mStrokeWidth = 1;
    private int mStrokeColors = Color.GRAY;
    private int mBackGroundColor = Color.TRANSPARENT;
    private int gravity;

    public QDividerDrawable() {
        init();
    }

    public QDividerDrawable(int gravity) {
        this.gravity = gravity;
        init();
    }

    public QDividerDrawable(int strokeWidth, int color) {
        this.mStrokeColors = color;
        this.mStrokeWidth = strokeWidth;
        init();
    }

    private void init() {
        //设置背景色
        setColor(mBackGroundColor);
        //设置边框的厚度以及边框的颜色
        setStroke(mStrokeWidth, mStrokeColors);
        //设置圆角的半径  当然也是可以一个个设置圆角的半径
        setCornerRadius(0);
        //设置背景的形状，默认就是矩形，跟xml文件中类型android:shape的值保持一致，具体有：GradientDrawable.LINE  GradientDrawable.OVAL GradientDrawable.RECTANGLE  GradientDrawable.RING
        setShape(GradientDrawable.RECTANGLE);
    }

    @Override
    protected void onBoundsChange(Rect r) {

        r.left = r.left - mStrokeWidth;
        r.top = r.top - mStrokeWidth;
        r.right = r.right + mStrokeWidth;
        r.bottom = r.bottom + mStrokeWidth;
        switch (gravity) {
            case Gravity.LEFT:
                r.left = r.left + mStrokeWidth;
                break;
            case Gravity.TOP:
                r.top = r.top + mStrokeWidth;
                break;
            case Gravity.RIGHT:
                r.right = r.right - mStrokeWidth;
                break;
            case Gravity.BOTTOM:
                r.bottom = r.bottom - mStrokeWidth;
                break;
            default:
                r.left = r.left + mStrokeWidth;
                r.top = r.top + mStrokeWidth;
                r.right = r.right - mStrokeWidth;
                r.bottom = r.bottom - mStrokeWidth;
                break;
        }
        super.onBoundsChange(r);
    }

    public int getmStrokeWidth() {
        return mStrokeWidth;
    }

    public void setmStrokeWidth(int mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
    }

    public int getmStrokeColors() {
        return mStrokeColors;
    }

    public void setmStrokeColors(int mStrokeColors) {
        this.mStrokeColors = mStrokeColors;
    }

    public int getmBackGroundColor() {
        return mBackGroundColor;
    }

    public void setmBackGroundColor(int mBackGroundColor) {
        this.mBackGroundColor = mBackGroundColor;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }
}
