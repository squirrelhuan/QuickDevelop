package cn.demomaster.huan.quickdeveloplibrary.view.banner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import cn.demomaster.huan.quickdeveloplibrary.R;

/**
 * 广告轮播指示器
 */
public class BannerCursorView2 extends View implements Banner.BannerIndicator {
    public BannerCursorView2(Context context) {
        super(context);
        init();
    }

    public BannerCursorView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BannerCursorView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BannerCursorView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    int cursorPointColor = Color.WHITE;

    public void setLineColor(int lineColor) {
        this.cursorPointColor = lineColor;
        mPaint.setColor(lineColor);
        postInvalidate();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(cursorPointColor);
    }

    float radius = 7;
    float radius_actived = 7;
    float w = 30;
    Paint mPaint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x1 = getWidth() / 2f - (indicatorCount + 1) * w;
        for (int i = 0; i < indicatorCount; i++) {
            float x = w * (i * 2 + 1);
            float y = getHeight() / 2f - w / 4;
            if (tabIndex == i) {
                mPaint.setColor(getResources().getColor(R.color.colorPrimary));
            } else {
                mPaint.setColor(Color.WHITE);
            }

            RectF rectF = new RectF(x1 + x, y, x1 + x + w, y + w / 4);
            canvas.drawRoundRect(rectF, 0, 0, mPaint);
        }
    }

    int indicatorCount;

    @Override
    public void selecte(int position) {
        tabIndex = position;
        startTransPositonAnimation(currentX, getWidth() / (indicatorCount + 1) * position);
    }

    public void setIndicatorCount(int indicatorCount) {
        this.indicatorCount = indicatorCount;
        postInvalidate();
    }

    int tabIndex;//当前所属index
    int currentX;
    ValueAnimator animator;
    private final Interpolator mInterpolator = new FastOutSlowInInterpolator();

    private void startTransPositonAnimation(int current, int target) {
        animator = ValueAnimator.ofInt(current, target);
        animator.setInterpolator(mInterpolator);
        int mDuration = 300;
        animator.setDuration(mDuration).start();
        animator.addUpdateListener(animation -> {
            currentX = (int) animation.getAnimatedValue();
            invalidate();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });
    }
}
