package cn.demomaster.huan.quickdeveloplibrary.view.banner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

/**
 * 广告轮播指示器
 */
public class BannerCursorView extends View implements Banner.BannerIndicator {
    public BannerCursorView(Context context) {
        super(context);
        init();
    }

    public BannerCursorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BannerCursorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BannerCursorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    float radius = 7;
    float radius_actived = 7;
    float w =30;
    Paint mPaint;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //QDLogger.e("indicatorCount="+indicatorCount+",tabIndex="+tabIndex);
        float x1 = getWidth()/2-(indicatorCount+1)*w/2;
        for(int i=0;i<indicatorCount;i++){
            float x = w*(i+1);
            float y = getHeight()/2;
            float radius_c = radius;
            if(tabIndex==i){
                radius_c = radius_actived;
                mPaint.setColor(Color.GREEN);
            }else {
                mPaint.setColor(Color.WHITE);
            }
            canvas.drawCircle(x1+x, y, radius_c, mPaint);
        }
    }

    int indicatorCount;

    int tabIndex;//当前所属index
    @Override
    public void selecte(int position) {
        //确定当前位置和目标位置
        tabIndex = position;
        startTransPositonAnimation(currentX, getWidth()/(indicatorCount+1)*position);
    }

    public void setIndicatorCount(int indicatorCount) {
        this.indicatorCount = indicatorCount;
        postInvalidate();
    }


    int currentX;
    ValueAnimator animator;
    private Interpolator mInterpolator = new FastOutSlowInInterpolator();
    private int mDuration = 300;
    private void startTransPositonAnimation(int current, int target) {
        animator = ValueAnimator.ofInt((int) current, (int) target);
        animator.setInterpolator(mInterpolator);
        animator.setDuration(mDuration).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentX = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });
    }

}
