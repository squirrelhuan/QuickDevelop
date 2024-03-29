package cn.demomaster.huan.quickdeveloplibrary.view.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;

/**
 * @author squirrel桓
 * @date 2018/11/8.
 * description：加载动画
 */
public class LoadingCircleBallView extends View {

    public LoadingCircleBallView(Context context) {
        super(context);
    }

    public LoadingCircleBallView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingCircleBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int width, height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
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

    private void drawView(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));

        //(x-a)²+(y-b)²=r²中，有三个参数a、b、r，即圆心坐标为(a，b)，只要求出a、b、r
        int a = width / 2;
        int b = height / 2;
        int maxRadius = Math.min(width, height) / 14;
        int r = Math.min(width, height) / 2 - maxRadius;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 2));
        mPaint.setAlpha((int) (255 * .4f));
        canvas.drawCircle(a, b, r, mPaint);
        //centerX centerY 圆的中心点
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAlpha((int) (255 * .7f));
        PointF p = getPointByAngle(a, b, r, progress);
        canvas.drawCircle(p.x, p.y, maxRadius, mPaint);

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
        animator.addUpdateListener(animation -> {
            progress = (int) animation.getAnimatedValue();
            //Log.d(TAG, "progress=" + progress);
            if (progress >= end) {
                isForward = !isForward;
                //Log.d(TAG, "isForward=" + isForward);
            } else {
                //postInvalidate();
                invalidate();
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
        if (animator != null) {
            animator.removeAllUpdateListeners();
            animator.cancel();
        }
    }
}
