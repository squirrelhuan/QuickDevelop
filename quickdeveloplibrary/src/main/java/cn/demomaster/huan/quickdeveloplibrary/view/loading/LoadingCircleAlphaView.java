package cn.demomaster.huan.quickdeveloplibrary.view.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    private float  progress_old;
    private void drawView(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));


        //(x-a)²+(y-b)²=r²中，有三个参数a、b、r，即圆心坐标为(a，b)，只要求出a、b、r
        int maxRadius = Math.min(width, height) ;
        int index = (int) progress / (360 / pointCount);

        if (circlePoints.size() < pointCount) {
            int x = (int) (Math.random() * maxRadius*3/5)+maxRadius/5+(width-maxRadius)/2;
            int y = (int) (Math.random() * maxRadius*3/5)+maxRadius/5+(height-maxRadius)/2;
            circlePoints.add(index, new CirclePoint(x, y, 255, 0));
        }

        for (int i =0;i< circlePoints.size();i++) {
            CirclePoint circlePoint = circlePoints.get(i);
            if (circlePoint.angle >= 360) {
                int x = (int) (Math.random() * maxRadius*3/5)+maxRadius/5+(width-maxRadius)/2;
                int y = (int) (Math.random() * maxRadius*3/5)+maxRadius/5+(height-maxRadius)/2;
                circlePoints.set(i, new CirclePoint(x, y, 255, 0));
            } else {
                float t = 0;
                if(progress_old<progress){
                    t = progress-progress_old;
                }else {
                    t= 360-progress_old+progress;
                }

                circlePoint.setAngle(circlePoint.angle+t);
                circlePoint.setRadius(circlePoint.radius+maxRadius/3 * t / 360);
                circlePoint.setAlpha(circlePoint.alpha - (int)(t/360f*255f));
                progress_old = progress;
            }
        }

        //centerX centerY 圆的中心点
        for (int i = 0; i < circlePoints.size(); i++) {
            //mPaint.setAlpha((int)(255*(.75f-(i+1)/pointCount*.3f)));
            CirclePoint p = circlePoints.get(i);
            mPaint.setAlpha(p.alpha);
            canvas.drawCircle(p.x, p.y, p.radius, mPaint);
        }

    }

    private PointF getPointByAngle(int x, int y, float r, float angle) {
        int a = (int) (x + (float) r * Math.cos(angle * 3.14 / 180));
        int b = (int) (y + (float) r * Math.sin(angle * 3.14 / 180));
        return new PointF(a, b);
    }

    private float progress;
    private boolean isForward = true;

    private List<CirclePoint> circlePoints = new ArrayList<>();

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

    public static class CirclePoint {
        private int x;
        private int y;
        private int alpha;
        private float radius;
        private float angle = 0;

        public CirclePoint(int x, int y, int alpha, float radius) {
            this.x = x;
            this.y = y;
            this.alpha = alpha;
            this.radius = radius;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getAlpha() {
            return alpha;
        }

        public void setAlpha(int alpha) {
            this.alpha = alpha;
        }

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public float getAngle() {
            return angle;
        }

        public void setAngle(float angle) {
            this.angle = angle;
        }
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(animator!=null)
            animator.cancel();
    }
}
