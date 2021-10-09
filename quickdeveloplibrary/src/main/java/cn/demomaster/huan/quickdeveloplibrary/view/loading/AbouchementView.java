package cn.demomaster.huan.quickdeveloplibrary.view.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.demomaster.huan.quickdeveloplibrary.animation.QDValueAnimator;
import cn.demomaster.huan.quickdeveloplibrary.util.QDColorUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.modle.Baller;

/**
 * 汇入动画
 *
 * @author squirrel桓
 * @date 2018/11/8.
 * description：加载动画
 */
public class AbouchementView extends View {

    public AbouchementView(Context context) {
        super(context);
        //init();
    }

    public AbouchementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //init();
    }

    public AbouchementView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //init();
    }

    int radius;

    private void init() {
        centerY = (int) (getHeight() / 2f);
        radius = (int) (Math.min(getWidth(), getHeight()) / 2f);
        //int r2 = r*10/8;// (int) (r*progress/360f);
        ballers.clear();
        degrees = 0;
        do {
            double r1 = random.nextDouble() * radius * 2 / 10f + radius / 10f * 6f;
            generate(degrees, r1, 0, false);
            generate(degrees, radius, r1, true);
        } while (!(degrees > 360));
    }

    double degrees = 0;
    Random random = new Random();

    /**
     * 生成外围点
     *
     * @param degrees
     * @param r
     * @param refrenceMode 贝塞尔曲线的中间参考点
     */
    public void generate(double degrees, double r, double r1, boolean refrenceMode) {
        Baller baller = new Baller();
        double degreesX = random.nextDouble() * 30 + (refrenceMode ? 3 : 1);
        degrees = degrees + degreesX;

        double radians = Math.toRadians(degrees);
        int r0 = Math.min(getWidth(), getHeight()) / 2;
        if (refrenceMode) {
            r = random.nextDouble() * 2 / 10 * r0 + r1;
        }
        int x = (int) (centerX + r * Math.cos(radians));
        int y = (int) (centerY + r * Math.sin(radians));
        //QDLogger.e("degrees="+degrees+",x="+x+",y="+y+",r2="+r+",progress="+progress+",centerX="+centerX+",centerY="+centerY);
        baller.setX(x);
        baller.setY(y);
        baller.setRadiusLocal(r);
        baller.setDegreesLocal(degrees);
        baller.setRadius(5);
        if (degrees <= 360) {
            ballers.add(baller);
        }

        this.degrees = degrees;
    }

    private int centerX, centerY;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        init();
    }

    List<Baller> ballers = new ArrayList<>();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);
        if (animator == null || (!animator.isRunning() && !animator.isStarted())) {
            startAnimation();
        }
    }

    private void drawView(Canvas canvas) {
        //QDLogger.e("progress=" + progress + "");
        //canvas.rotate((progress / 360) * 90 + 45 + (index) * 90, getMeasuredWidth() / 2, getMeasuredHeight() / 2);

        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        canvas.drawColor(Color.YELLOW);
        mPaint.setColor(Color.RED);

        if (isForward) {
            for (int i = 0; i < ballers.size(); i++) {
                Baller baller = ballers.get(i);
                resetRadius(i, baller);
                // canvas.drawRoundRect(mRecF5, 5, 5, mPaint);
                /*int x1 = Math.abs(baller.x-centerX);
                int y1 = Math.abs(baller.y-centerY);
                int dx =0;
                int dy =0;
                if(x1>3&&y1>3){
                    dx = 2;
                    dy = y1/x1*dx;
                }else {
                   *//* if (x1 < 3) {
                        dx = x1;
                    } else {
                        dx = 3;
                    }

                    if (y1 < 3) {
                        dy = y1;
                    }else {
                        dy = 3;
                    }*//*
                }
                if(baller.x<centerX){
                    if(centerX-baller.x<dx){
                        baller.setX(centerX);
                    }else {
                        baller.setX(baller.x+dx);
                    }
                }else if(baller.x>centerX){
                    if(baller.x-centerX<dx){
                        baller.setX(centerX);
                    }else {
                        baller.setX(baller.x-dx);
                    }
                }
                if(baller.y<centerY){
                    if(centerY-baller.y<dy){
                        baller.setY(getHeight()/2);
                    }else {
                        baller.setY(baller.y+dy);
                    }
                }else if(baller.y>centerY){
                    if(baller.y-centerY<dy){
                        baller.setY(centerY);
                    }else {
                        baller.setY(baller.y-dy);
                    }
                }
                baller.setRadius(5);
                ballers.set(i,baller);*/
                RectF mRecF5 = new RectF(baller.getLeft(), baller.getTop(), baller.getRight(), baller.getBottom());
                canvas.drawOval(mRecF5, mPaint);
            }
            mPaint.setColor(QDColorUtil.getCurrentColor(1 - progress, Color.parseColor("#4876FF"), Color.parseColor("#483D8B")));
            Path path = new Path();
            for (int i = 0; i < ballers.size() / 2; i++) {
                if (i == 0) {
                    path.moveTo(ballers.get(0).x, ballers.get(0).y);
                } else if (i == ballers.size() / 2) {
                    int x = ballers.get(0).x;
                    int y = ballers.get(0).y;
                    int x1 = ballers.get(ballers.size() - 1).x;
                    int y1 = ballers.get(ballers.size() - 1).y;
                    path.quadTo(x1, y1, x, y);
                } else {
                    int x = ballers.get(i * 2).x;
                    int y = ballers.get(i * 2).y;
                    int x1 = ballers.get(i * 2 - 1).x;
                    int y1 = ballers.get(i * 2 - 1).y;
                    path.quadTo(x1, y1, x, y);
                    //path.lineTo(ballers.get(i*2).x, ballers.get(i*2).y);
                }
            }
            path.close();
            canvas.drawPath(path, mPaint);
            /*path = new Path();
            for(int i=0;i<ballers.size();i++){
                if(i==0) {
                    path.moveTo(ballers.get(0).x, ballers.get(0).y);
                }else {
                   path.lineTo(ballers.get(i).x, ballers.get(i).y);
                }
            }
            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            canvas.drawPath(path,paint);*/
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

    private void resetRadius(int i, Baller baller) {
        //在原点中的半径和角度
        double radians = Math.toRadians(baller.getDegreesLocal());
        double r1 = baller.getRadiusLocal() * progress;
        if (r1 < radius / 5f) {
            r1 = radius / 5f;
        }
        int x = (int) (centerX + r1 * Math.cos(radians));
        int y = (int) (centerY + r1 * Math.sin(radians));
        baller.setX(x);
        baller.setY(y);
        ballers.set(i, baller);
    }

    private float progress;
    private boolean isForward = true;
    QDValueAnimator animator;

    public void startAnimation() {
        final float start = 1f;
        final float end = 0f;
        animator = QDValueAnimator.ofFloat(start, end);
        animator.setDuration(2600);
        animator.setAnimationListener(new QDValueAnimator.AnimationListener() {
            @Override
            public void onStartOpen(Object value) {
                isForward = true;
            }

            @Override
            public void onOpening(Object value) {
                if (getVisibility() == VISIBLE) {
                    progress = (float) value;
                    //QDLogger.d( "progress=" + progress);
                    invalidate();
                }
            }

            @Override
            public void onEndOpen(Object value) {
                init();
            }

            @Override
            public void onStartClose(Object value) {
                isForward = false;
            }

            @Override
            public void onClosing(Object value) {
                if (getVisibility() == VISIBLE) {
                    progress = (float)value;
                    //QDLogger.d( "progress=" + progress+",value="+value);
                    invalidate();
                }
            }

            @Override
            public void onEndClose(Object value) {

            }
        });
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(-1);//ValueAnimator.INFINITE
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null) {
            animator.removeAllUpdateListeners();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                animator.cancel();
            }
        }
    }

}
