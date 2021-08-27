package cn.demomaster.huan.quickdeveloplibrary.view.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Shader;
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
public class LoadingCircleBallInnerView extends View {

    public LoadingCircleBallInnerView(Context context) {
        super(context);
    }

    public LoadingCircleBallInnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingCircleBallInnerView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    private void drawView(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.red));

        //(x-a)²+(y-b)²=r²中，有三个参数a、b、r，即圆心坐标为(a，b)，只要求出a、b、r
        int a = width / 2;
        int b = height / 2;
        int maxRadius = Math.min(width, height) / 14;
        int r = Math.min(width, height) / 2 - maxRadius;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 2));
        mPaint.setAlpha((int) (255 * .7f));
        canvas.drawCircle(a, b, r, mPaint);


        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAlpha((int) (255 * .7f));
        PointF p = getPointByAngle(a, b, (int) (r * 2 / 3f), progress);
        Shader mShader = new LinearGradient(p.x, p.y, p.x + r / 2f, p.y + r / 2f, new int[]{0xaaffffff, 0x11ffffff}, null, Shader.TileMode.REPEAT);
//新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
        mShader = new RadialGradient(p.x, p.y, maxRadius, new int[]{0xccffffff, 0x11ffffff}, new float[]{0.3f, 1f}, Shader.TileMode.REPEAT);
        mPaint.setShader(mShader);
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
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (int) animation.getAnimatedValue();
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
        if (animator != null)
            animator.cancel();
    }
}
