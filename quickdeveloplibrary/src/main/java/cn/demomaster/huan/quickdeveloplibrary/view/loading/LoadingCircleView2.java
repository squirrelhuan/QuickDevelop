package cn.demomaster.huan.quickdeveloplibrary.view.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * @author squirrel桓
 * @date 2018/11/8.
 * description：加载动画
 */
public class LoadingCircleView2 extends View {

    public LoadingCircleView2(Context context) {
        super(context);
        init(null);
    }

    public LoadingCircleView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoadingCircleView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingView);
            pointColor = a.getColor(R.styleable.LoadingView_drawColor, pointColor);
            a.recycle();
        }
    }

    private int center_x, center_y, width, height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        center_x = width / 2;
        center_y = height / 2;
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

    private int pointColor = Color.WHITE;
    public void setPointColor(int pointColor) {
        this.pointColor = pointColor;
        postInvalidate();
    }

    private int lineWidth = 15;
    private void drawView(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(pointColor);
        mPaint.setStrokeWidth(lineWidth);
        mPaint.setStyle(Paint.Style.STROKE);

        float startAngle = progress;
        float sweepAngle = Math.abs(40*(progress-180)/180f)+25;
        int h = Math.min(width,height)-lineWidth*2;
        int left = lineWidth;
        int top = lineWidth;
        if(width>height){
            left = center_x-h/2;
        }else if(width<height){
            top = center_y-h/2;
        }
        //QDLogger.println("startAngle="+(startAngle)+",e="+(sweepAngle));
        RectF rectF = new RectF(left,top,left+h,top+h);
        mPaint.setColor(getResources().getColor(R.color.transparent_light_55));
        canvas.drawCircle(center_x, center_y, h/2, mPaint);

        mPaint.setColor(pointColor);
        canvas.drawArc(rectF, startAngle, sweepAngle, false, mPaint);//绘制圆弧，不含圆心

        mPaint.setStrokeWidth(lineWidth/2);
        mPaint.setStyle(Paint.Style.FILL);
        PointF p = getPointByAngle(center_x, center_y, h/2, startAngle);
        canvas.drawCircle(p.x, p.y, lineWidth/2, mPaint);
        PointF p2 = getPointByAngle(center_x, center_y, h/2, startAngle+sweepAngle);
        canvas.drawCircle(p2.x, p2.y, lineWidth/2, mPaint);
    }

    private PointF getPointByAngle(int x, int y, int r, float angle) {
        //(x-a)²+(y-b)²=r²中，有三个参数a、b、r，即圆心坐标为(a，b)，只要求出a、b、r
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
        animator.setDuration(3000);
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
        //animator.setInterpolator(new AccelerateInterpolator());
        animator.setInterpolator(new LinearInterpolator());
        //animator.setInterpolator(new CycleInterpolator());
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null) {
            animator.cancel();
        }
    }
}
