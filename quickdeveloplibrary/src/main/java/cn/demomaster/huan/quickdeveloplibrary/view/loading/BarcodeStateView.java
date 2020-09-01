package cn.demomaster.huan.quickdeveloplibrary.view.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import cn.demomaster.huan.quickdeveloplibrary.animation.QDValueAnimator;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;

/**
 * 汇入动画
 * @author squirrel桓
 * @date 2018/11/8.
 * description：加载动画
 */
public class BarcodeStateView extends View {

    public BarcodeStateView(Context context) {
        super(context);
        //init();
    }

    public BarcodeStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //init();
    }

    public BarcodeStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //init();
    }

    private int centerX, centerY, width, height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        centerX = width / 2;
        centerY = height/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        drawView(canvas);
        if (animator==null||(!animator.isRunning()&&!animator.isStarted())) {
            startAnimation();
        }
    }

    int color0 = Color.parseColor("#77DEDEDE");
    int color1 = Color.parseColor("#6495ED");
    int color2 = Color.parseColor("#00CD00");
    private void drawView(Canvas canvas) {
        //QDLogger.e("progress=" + progress + "");
        //canvas.rotate((progress / 360) * 90 + 45 + (index) * 90, getMeasuredWidth() / 2, getMeasuredHeight() / 2);

        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //canvas.drawColor(Color.YELLOW);

        int padding = DisplayUtil.dip2px(getContext(),5);
        int radius1 = DisplayUtil.dip2px(getContext(),10);
        RectF rectF = new RectF(padding,padding,width-padding,height-padding);
        mPaint.setColor(color0);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(rectF, radius1, radius1, mPaint);

        int layerId= canvas.saveLayer(0,0,width,height,null,Canvas.ALL_SAVE_FLAG);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color1);
        canvas.drawRoundRect(rectF, radius1, radius1, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        RectF rectF3 = new RectF(-width/2,-height/2,width+width/2,height+height/2);
        mPaint.setColor(color1);
        canvas.drawArc(rectF3, 0, (1-progress)*360, true, mPaint);//以斜上45度为起点，顺时针扫过135度

        mPaint.setXfermode(null);

        canvas.restoreToCount(layerId);

        int padding2 = padding+DisplayUtil.dip2px(getContext(),5);
        int radius2 = DisplayUtil.dip2px(getContext(),5);
        mPaint.setColor(color2);
        RectF rectF2 = new RectF(padding2,padding2,width-padding2,height-padding2);
        canvas.drawRoundRect(rectF2, radius2, radius2, mPaint);


/*
        if (isForward) {
            mPaint.setColor(getCurrentColor(1-progress,Color.parseColor("#4876FF"),Color.parseColor("#483D8B")));
            Path path = new Path();
            path.close();
            canvas.drawPath(path,mPaint);

        }*/
    }

    private float progress;
    private boolean isForward = true;
    QDValueAnimator animator;
    public void startAnimation() {
        final float start = 0f;
        final float end = 2f;
        animator = QDValueAnimator.ofFloat(start, end);
        animator.setDuration(5000);
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

            }

            @Override
            public void onStartClose(Object value) {
                isForward = false;
            }

            @Override
            public void onClosing(Object value) {
                if (getVisibility() == VISIBLE) {
                    progress = Float.valueOf((int) value);
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
        if(animator!=null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                animator.cancel();
            }
        }
    }

}
