package cn.demomaster.huan.quickdeveloplibrary.view.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * @author squirrel桓
 * @date 2018/11/8.
 * description：加载动画
 */
public class LoadingCubeView extends View {

    public LoadingCubeView(Context context) {
        super(context);
    }

    public LoadingCubeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingCubeView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    boolean isDrawed;
    private int[] colors = {Color.BLUE, Color.YELLOW, Color.GREEN, Color.RED};
    private int spacing = 6;

    private void drawView(Canvas canvas) {

        //QDLogger.e("progress="+progress+"");
        canvas.rotate(progress, getMeasuredWidth() / 2f, getMeasuredHeight() / 2f);
        //canvas.drawColor(Color.BLACK);
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        int r = (int) (Math.min(width, height) / 1.44);

        int r2 = (Math.min(width, height));
        canvas.translate((r2 - r) / 2f, (r2 - r) / 2f);
        int margin_left = width > height ? (width - height) / 2 : 0;
        int margin_top = width < height ? (height - width) / 2 : 0;
        RectF mRecF1 = new RectF(margin_left, margin_top, margin_left + r / 2f - spacing / 2f, margin_top + r / 2f - spacing / 2f);
        RectF mRecF2 = new RectF(margin_left + r / 2f + spacing / 2f, margin_top, margin_left + r, margin_top + r / 2f - spacing / 2f);
        RectF mRecF3 = new RectF(margin_left, margin_top + r / 2f + spacing / 2f, margin_left + r / 2f - spacing / 2f, margin_top + r);
        RectF mRecF4 = new RectF(margin_left + r / 2f + spacing / 2f, margin_top + r / 2f + spacing / 2f, margin_left + r, margin_top + r);
        if (!isDrawed) {
            //isDrawed = true;
                mPaint.setColor(colors[0]);
                canvas.drawRoundRect(mRecF1, 5, 5, mPaint);
                mPaint.setColor(colors[1]);
                canvas.drawRoundRect(mRecF2, 5, 5, mPaint);
                mPaint.setColor(colors[2]);
                canvas.drawRoundRect(mRecF3, 5, 5, mPaint);
                mPaint.setColor(colors[3]);
                canvas.drawRoundRect(mRecF4, 5, 5, mPaint);
                //canvas.drawArc(mRecF, 0, progress, true, mPaint);//以斜上45度为起点，顺时针扫过135度
        }

     /*   Path path = new Path();
        path.moveTo(0,0);
        path.quadTo(0,200,200,200);
        path.quadTo(400,200,400,400);
       Paint paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
        paint.setColor(Color.GREEN);
        canvas.drawPath(path,paint);*/
    }

    private float progress;
    private boolean isForward = true;
    ValueAnimator animator;

    public void startAnimation() {
        isPlaying = true;
        final int end = 360;
        animator = ValueAnimator.ofInt(0, end);
        animator.setDuration(1200);
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
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null){
            animator.removeAllUpdateListeners();
            animator.cancel();
        }
    }
}
