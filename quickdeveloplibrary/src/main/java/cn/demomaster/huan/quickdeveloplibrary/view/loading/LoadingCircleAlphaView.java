package cn.demomaster.huan.quickdeveloplibrary.view.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import java.util.LinkedHashMap;

import cn.demomaster.huan.quickdeveloplibrary.R;

/**
 * @author squirrel桓
 * @date 2018/11/8.
 * description：加载动画
 */
public class LoadingCircleAlphaView extends View {

    public LoadingCircleAlphaView(Context context) {
        super(context);
    }

    public LoadingCircleAlphaView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingCircleAlphaView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    private int pointCount = 3;

    private void drawView(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));


        //(x-a)²+(y-b)²=r²中，有三个参数a、b、r，即圆心坐标为(a，b)，只要求出a、b、r
        int a = width / 2;
        int b = height / 2;
        int maxRadius = Math.min(width, height) / 14;
        int r = Math.min(width, height) / 2 - maxRadius;
        int index = (int) progress / 360 / pointCount;
        int yu = (int) (progress % 360 / pointCount);
        if (map.size() < pointCount) {
            int x = (int) (Math.random() * 10 % 3*width);
            int y = (int) (Math.random() * 10 % 3*height);
            map.put(index, new PointF(x, y));
        } else  {
            int x = (int) (Math.random() * 10 % 3*width);
            int y = (int) (Math.random() * 10 % 3*height);
            map.put(index, new PointF(x, y));
        }
        //centerX centerY 圆的中心点
        float angle = progress / 2;
        for (int i = 0; i < map.size(); i++) {
            float c = (angle - i * (360 / pointCount));

            //mPaint.setAlpha((int)(255*(.75f-(i+1)/pointCount*.3f)));
            PointF p = map.get(i);
            canvas.drawCircle(p.x, p.y, r / 2, mPaint);
        }

    }

    private PointF getPointByAngle(int x, int y, float r, float angle) {
        int a = (int) (x + (float) r * Math.cos(angle * 3.14 / 180));
        int b = (int) (y + (float) r * Math.sin(angle * 3.14 / 180));
        return new PointF(a, b);
    }

    private float progress;
    private boolean isForward = true;

    private LinkedHashMap<Integer, PointF> map = new LinkedHashMap<Integer, PointF>();

    public void startAnimation() {
        isPlaying = true;
        final int end = 360;
        final ValueAnimator animator = ValueAnimator.ofInt(0, end);
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

}
