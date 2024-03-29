package cn.demomaster.huan.quickdeveloplibrary.view.banner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


public class BannerFrameLayout extends FrameLayout {
    public BannerFrameLayout(@NonNull Context context) {
        super(context);
    }

    public BannerFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BannerFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BannerFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    float[] mBannerRadius = new float[]{25f, 25f, 0f, 0f, 25, 25, 25, 25};

    /**
     * 设置矩形圆角
     *
     * @param mBannerRadius
     */
    public void setBannerRadius(float[] mBannerRadius) {
        this.mBannerRadius = mBannerRadius;
        postInvalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        //if (mBannerRadius[0]!=0||mBannerRadius[1]!=0||mBannerRadius[2]!=0||mBannerRadius[3]!=0) {
        Path path = new Path();
           /* path.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()),
                    mBannerRadius[0], mBannerRadius[1], Path.Direction.CW);*/
        path.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()),
                mBannerRadius, Path.Direction.CW);
        canvas.clipPath(path);
        //}
        super.dispatchDraw(canvas);
    }
}
