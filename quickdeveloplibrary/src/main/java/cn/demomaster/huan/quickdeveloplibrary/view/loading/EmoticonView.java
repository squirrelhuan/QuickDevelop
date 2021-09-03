package cn.demomaster.huan.quickdeveloplibrary.view.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.view.ImageTextView;

/**
 * @author squirrel桓
 * @date 2018/11/8.
 * description：加载动画
 */
public class EmoticonView extends ImageTextView {

    public EmoticonView(Context context) {
        super(context);
        init();
    }

    public EmoticonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmoticonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

  /*  ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            progress = (float) animation.getAnimatedValue();
            invalidate();
        }
    };*/

    void init() {
        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(duration);
        //animator.addUpdateListener(updateListener);
        //animator.setRepeatMode(ValueAnimator.REVERSE);
        //animator.setRepeatCount(ValueAnimator.INFINITE);
        // accelerate_decelerate_interpolator
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
    }


    private int width;
    private int height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawView(canvas);
        super.onDraw(canvas);
    }

   /* public static enum LoadStateType {
        Sad, Halo, Sluggish, Smile, NONE
    }*/

    private LoadStateType stateType = LoadStateType.LOADING;
    private LoadStateType stateType_target = LoadStateType.LOADING;

    private int warningColor = Color.YELLOW;
    private int errorColor = Color.RED;
    private int completeColor = Color.GREEN;
    private int loadingColor = Color.GRAY;
    private int mainColor = Color.WHITE;
    private int lineWidth = 4;
    private boolean drawCricleBackground = true;

    private void drawView(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));
        //canvas.drawCircle(a,b, Math.min(a,b), mPaint);
        mPaint.setColor(getResources().getColor(R.color.white));
        int linewidth = DisplayUtil.dip2px(getContext(), lineWidth);
        mPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), lineWidth));
        float a = width / 2f;
        float b = height / 2f;

        float r0 = Math.min(a, b);
        float r = (int) (progress * r0);
        warningColor = getContext().getResources().getColor(R.color.orange);
        switch (stateType_target) {
            case COMPLETE://完成
                int targetColor = completeColor;
                break;
            case ERROR://完成
                targetColor = errorColor;
                break;
            case WARNING://警告
                targetColor = warningColor;
                break;
            case LOADING://加载
                targetColor = loadingColor;
                break;
        }
        int color;
        switch (stateType) {
            case COMPLETE://完成
                mPaint.setColor(mainColor);
                mPaint.setStyle(Paint.Style.STROKE);
                if (drawCricleBackground)
                    canvas.drawCircle(a, b, r0 - mPaint.getStrokeWidth(), mPaint);
                mPaint.setColor(getResources().getColor(R.color.white));
                mPaint.setColor(mainColor);
                mPaint.setStyle(Paint.Style.FILL);
                float w1 = r / 7 * progress;
                float h1 = r / 5;
                PointF pointF1 = new PointF(width / 4f + w1 / 2f, height / 3f);
                //Path path1 = new Path();
                //path1.moveTo(pointF1.x - w1, pointF1.y);
                //path1.quadTo(pointF1.x, pointF1.y - h1 * progress, pointF1.x + w1, pointF1.y);
                //canvas.drawPath(path1, mPaint);
                canvas.drawCircle(pointF1.x, pointF1.y, w1, mPaint);

                PointF pointF2 = new PointF(width * 3 / 4f - w1 / 2, height / 3f);
                canvas.drawCircle(pointF2.x, pointF2.y, w1, mPaint);
                /*Path path2 = new Path();
                path2.moveTo(pointF2.x - w1, pointF2.y);
                path2.quadTo(pointF2.x, pointF2.y - h1 * progress, pointF2.x + w1, pointF2.y);
                canvas.drawPath(path2, mPaint);*/

                mPaint.setStyle(Paint.Style.STROKE);
                PointF pointF3 = new PointF(width / 2f, height * 3 / 4f);
                Path path = new Path();
                path.moveTo(pointF3.x - r / 2 * progress, pointF3.y);
                path.quadTo(pointF3.x, pointF3.y + r * 2 / 5 * progress, pointF3.x + r / 2 * progress, pointF3.y);
                canvas.drawPath(path, mPaint);

                break;
            case ERROR://异常
                mPaint.setColor(mainColor);
                mPaint.setStyle(Paint.Style.STROKE);
                if (drawCricleBackground)
                    canvas.drawCircle(a, b, r0 - mPaint.getStrokeWidth(), mPaint);

                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(mainColor);

                float B_w1 = r / 7;
                float B_h1 = r / 5;
                PointF B_pointF1 = new PointF(width / 4f + B_w1 / 2f, height / 3f);
                Path B_path1 = new Path();
                B_path1.moveTo(B_pointF1.x - B_w1, B_pointF1.y);
                B_path1.quadTo(B_pointF1.x, B_pointF1.y - B_h1 * progress, B_pointF1.x + B_w1, B_pointF1.y);
                canvas.drawPath(B_path1, mPaint);

                PointF B_pointF2 = new PointF(width * 3 / 4f - B_w1 / 2f, height / 3f);
                Path B_path2 = new Path();
                B_path2.moveTo(B_pointF2.x - B_w1, B_pointF2.y);
                B_path2.quadTo(B_pointF2.x, B_pointF2.y - B_h1 * progress, B_pointF2.x + B_w1, B_pointF2.y);
                canvas.drawPath(B_path2, mPaint);

                PointF B_pointF3 = new PointF(width / 2f, height * 3 / 4f);
                Path B_path = new Path();
                B_path.moveTo(B_pointF3.x - r / 2f, B_pointF3.y);
                B_path.quadTo(B_pointF3.x, B_pointF3.y - r * 2 / 5f * progress, B_pointF3.x + r / 2f, B_pointF3.y);
                canvas.drawPath(B_path, mPaint);
                break;
            case WARNING://警告
                mPaint.setColor(mainColor);
                mPaint.setStyle(Paint.Style.STROKE);
                if (drawCricleBackground)
                    canvas.drawCircle(a, b, r0 - mPaint.getStrokeWidth(), mPaint);

                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(mainColor);
                float C_w1 = r / 7;
                float C_h1 = 0;
                PointF C_pointF1 = new PointF(width / 4f + C_w1 / 2f, height / 3f);
                Path C_path1 = new Path();
                C_path1.moveTo(C_pointF1.x - C_w1 * progress, C_pointF1.y);
                C_path1.quadTo(C_pointF1.x, C_pointF1.y - C_h1 * progress, C_pointF1.x + C_w1 * progress, C_pointF1.y);
                canvas.drawPath(C_path1, mPaint);

                PointF C_pointF2 = new PointF(width * 3 / 4f - C_w1 / 2f, height / 3f);
                Path C_path2 = new Path();
                C_path2.moveTo(C_pointF2.x - C_w1 * progress, C_pointF2.y);
                C_path2.quadTo(C_pointF2.x, C_pointF2.y - C_h1 * progress, C_pointF2.x + C_w1 * progress, C_pointF2.y);
                canvas.drawPath(C_path2, mPaint);

                PointF C_pointF3 = new PointF(width / 2f, height * 3 / 4f);
                Path C_path = new Path();
                C_path.moveTo(C_pointF3.x - r / 3 * progress, C_pointF3.y);
                C_path.quadTo(C_pointF3.x, C_pointF3.y, C_pointF3.x + r / 3 * progress, C_pointF3.y);
                canvas.drawPath(C_path, mPaint);
                break;
            case LOADING://加载
                mPaint.setColor(mainColor);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setStyle(Paint.Style.STROKE);
                //if (drawCricleBackground)
                //    canvas.drawCircle(a, b, r0-mPaint.getStrokeWidth(), mPaint);

                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(mainColor);
                RectF oval = new RectF((width - r0) / 2, (height - r0) / 2, (width - r0) / 2 + r0, (height - r0) / 2 + r0);
                canvas.rotate(progress * 360, width / 2f, height / 2f);
                canvas.drawArc(oval, 0, 90 + Math.abs(progress - 0.5f) * 140, false, mPaint);
                //canvas.save();
                //canvas.translate(0,0);
                //canvas.rotate((float) (90 - Math.toDegrees(60)));
                break;
        }
    }

    private float progress;
    public void setStateType(LoadStateType stateType) {
        stateType_target = stateType;
        if (this.stateType != stateType) {
            hideAndShow(stateType);
        } else {
            this.stateType = stateType;
            show();
        }
    }

    private ValueAnimator animator;
    private int duration = 500;
    ValueAnimator.AnimatorUpdateListener showUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            progress = (float) animation.getAnimatedValue();
            invalidate();
        }
    };

    public void show() {
        if (animator == null) {
            init();
        } else {
            if (animator.isStarted() || animator.isRunning()) {
                animator.cancel();
                init();
            }
        }
        //animator = MyValueAnimator.ofFloat(0, 1);
        // animator.setFloatValues(0, 1);
        animator.setDuration(duration);
        animator.addUpdateListener(showUpdateListener);

        if (stateType == LoadStateType.LOADING) {
            animator.setRepeatCount(ValueAnimator.INFINITE);
        } else {
            animator.setRepeatCount(0);
        }
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    public void hide() {
        if (animator != null) {
            animator.setFloatValues(0, progress);
            animator.setRepeatCount(0);
            animator.addUpdateListener(null);
            animator.setDuration((int) (duration * progress));
            animator.reverse();
        }
    }

    private LoadStateType stateType1 = LoadStateType.LOADING;
    ValueAnimator.AnimatorUpdateListener hideUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            progress = (float) animation.getAnimatedValue();
            if (progress == 0) {
                stateType = stateType1;
                show();
            } else {
                invalidate();
            }
        }
    };

    //隐藏后显示
    public void hideAndShow(final LoadStateType stateType1) {
        this.stateType1 = stateType1;
        if (animator != null) {
            if (animator.isStarted() || animator.isRunning()) {
                animator.cancel();
            }
            animator = ValueAnimator.ofFloat(0, progress);
            //animator.setFloatValues(0, progress);
            animator.setRepeatCount(0);
            animator.setDuration((int) (duration * progress));
            animator.addUpdateListener(hideUpdateListener);
            animator.reverse();
        }
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        postInvalidate();
    }

    public void setDuration(int duration) {
        this.duration = duration;
        postInvalidate();
    }

    public void setDrawCricleBackground(boolean drawCricleBackground) {
        this.drawCricleBackground = drawCricleBackground;
        postInvalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null) {
            animator.cancel();
        }
        if (showUpdateListener != null) {
            showUpdateListener = null;
        }
        if (hideUpdateListener != null) {
            hideUpdateListener = null;
        }
        /*if (updateListener != null) {
            updateListener = null;
        }*/
    }
}
