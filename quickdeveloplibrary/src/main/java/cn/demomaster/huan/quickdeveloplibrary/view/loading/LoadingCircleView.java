package cn.demomaster.huan.quickdeveloplibrary.view.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import cn.demomaster.huan.quickdeveloplibrary.R;

/**
 * @author squirrel桓
 * @date 2018/11/8.
 * description：加载动画
 */
public class LoadingCircleView extends View {

    public LoadingCircleView(Context context) {
        super(context);
    }

    public LoadingCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int center_x, center_y, mwidth, width, height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        center_x = width / 2;
    }

    private boolean isPlaying = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);
        if (!isPlaying) {
            startAnimation();
        }
    }

    private int pointCount =4;
    private void drawView(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));

        //(x-a)²+(y-b)²=r²中，有三个参数a、b、r，即圆心坐标为(a，b)，只要求出a、b、r
        int a = width / 2;
        int b = height / 2;
        int maxRadius = Math.min(width, height) / 14;
        int r = Math.min(width, height) / 2 - maxRadius;
        //centerX centerY 圆的中心点
        float angle = progress/2;
        for (int i = 0; i < pointCount; i++) {
            float c = (angle-i*12);
            if(c<0||c>360){
                c=0;
            }else {
                c = (float) (Math.sin(Math.toRadians(c/2))*360);
            }
            mPaint.setAlpha((int)(255*(.75f-(i+1)/pointCount*.3f)));
            PointF p = getPointByAngle(a, b, r, c);
            canvas.drawCircle(p.x, p.y, (float) Math.pow(.85f,i)*maxRadius, mPaint);
        }

    }

    private PointF getPointByAngle(int x, int y, int r, float angle) {
        int a = (int) (x + (float) r * Math.cos(angle * 3.14 / 180));
        int b = (int) (y + (float) r * Math.sin(angle * 3.14 / 180));
        return new PointF(a, b);
    }

    private float progress;
    private boolean isForward = true;
    ValueAnimator animator;
    public void startAnimation() {
        isPlaying = true;
        final int end = 360;
        animator = ValueAnimator.ofInt(0, end);
        animator.setDuration(1600);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                progress = value;
                //Log.d(TAG, "progress=" + progress);
                if (progress >= end) {
                    isForward = !isForward;
                    //Log.d(TAG, "isForward=" + isForward);
                } else {
                    //postInvalidate();
                    invalidate();
                }
            }
        });
        //animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        //new AccelerateInterpolator()
        animator.setInterpolator(new AccelerateInterpolator());
        //animator.setInterpolator(new CycleInterpolator());
        animator.start();
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(animator!=null)
            animator.cancel();
    }
}
