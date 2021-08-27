package cn.demomaster.huan.quickdeveloplibrary.view.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * @author squirrel桓
 * @date 2018/11/8.
 * description：加载动画
 */
public class LoadingDownLoadView extends View {

    public LoadingDownLoadView(Context context) {
        super(context);
    }

    public LoadingDownLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingDownLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    boolean isDrawed;
    private int lineWidth = 30;

    private void drawView(Canvas canvas) {
        //QDLogger.e("progress="+progress+"");
        // canvas.rotate(progress, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        //canvas.drawColor(Color.BLACK);
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        float currentY = progress * height * 2 - height;
        canvas.translate(0, currentY);

        if (!isDrawed) {
            mPaint.setColor(Color.BLACK);
            canvas.drawCircle(center_x, -currentY + height / 2f, width / 2f, mPaint);
            Path path1 = new Path();
            path1.addRoundRect(new RectF(0, 0, width, height), center_x, center_y, Path.Direction.CCW);
            canvas.clipPath(path1);
            mPaint.setColor(Color.RED);
            Path path = new Path();
            path.moveTo(center_x - lineWidth / 2f, 0);
            path.lineTo(center_x + lineWidth / 2f, 0);
            path.lineTo(center_x + lineWidth / 2f, height - lineWidth * 3 / 2f);
            path.lineTo(center_x + lineWidth * 4 / 3f, height - lineWidth * 3 / 2f);
            path.lineTo(center_x, height);
            path.lineTo(center_x - lineWidth * 4 / 3f, height - lineWidth * 3 / 2f);
            path.lineTo(center_x - lineWidth / 2f, height - lineWidth * 3 / 2f);
            path.lineTo(center_x - lineWidth / 2f, 0);
            canvas.drawPath(path, mPaint);
        }

    }

    private float progress;
    private boolean isForward = true;
    ValueAnimator animator;

    public void startAnimation() {
        isPlaying = true;
        final float end = 1f;
        animator = ValueAnimator.ofFloat(0f, end);
        animator.setDuration(1200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
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
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null)
            animator.cancel();
    }
}
