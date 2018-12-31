package cn.demomaster.huan.quickdeveloplibrary.view.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarTip;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;

import static cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityRoot.TAG;

/**
 * @author squirrel桓
 * @date 2018/11/8.
 * description：加载动画
 */
public class StateView extends ImageTextView {

    public StateView(Context context) {
        super(context);
        init();
    }

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        //animator.setRepeatMode(ValueAnimator.REVERSE);
        //animator.setRepeatCount(ValueAnimator.INFINITE);
        // accelerate_decelerate_interpolator
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
    }



    private int center_x, center_y, mwidth, width, height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        center_x = width / 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawView(canvas);
        super.onDraw(canvas);
    }

    public static enum StateType {
        COMPLETE, WARNING, ERROR,LOADING
    }
    private StateType stateType = StateType.LOADING;
    private StateType stateType_target = StateType.LOADING;

    private int targetColor;
    private int warningColor = Color.YELLOW;
    private int errorColor = Color.RED;
    private int completeColor = Color.GREEN;
    private int loadingColor = Color.GRAY;
    private int mainColor = Color.WHITE;
    private int lineWidth = 4;

    private void drawView(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));
        int a = width / 2;
        int b = height / 2;
        //canvas.drawCircle(a,b, Math.min(a,b), mPaint);
        mPaint.setColor(getResources().getColor(R.color.white));
        int linewidth = DisplayUtil.dip2px(getContext(), lineWidth);
        mPaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), lineWidth));


        int r0 = Math.min(a, b);
        int r = (int) (progress * r0);
        warningColor = getContext().getResources().getColor(R.color.orange);
        switch (stateType_target) {
            case COMPLETE://完成
                targetColor = completeColor;
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
                if (completeColor == targetColor) {
                    color = completeColor;
                } else {
                    color = getCurrentColor(progress, completeColor, targetColor);
                }
                mPaint.setColor(color);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(a, b, r0, mPaint);

                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(getResources().getColor(R.color.white));

                int w1 = r * 3 / 5;
                int w2 = r;
                PointF pointF = new PointF(width / 2 - r / 12, height / 2 + r * 2 / 5);
                double degrees_start = 315.0;
                double radians1 = Math.toRadians(degrees_start);
                PointF pointF1 = new PointF((float) (pointF.x + Math.sin(radians1) * w1), (float) (pointF.y - Math.cos(radians1) * w1));

                double degrees_end = degrees_start + 90;
                double radians2 = Math.toRadians(degrees_end);
                PointF pointF2 = new PointF((float) (pointF.x + Math.sin(radians2) * w2), (float) (pointF.y - Math.cos(radians2) * w2));

                double degrees_end2 = degrees_end + 180;
                double radians3 = Math.toRadians(degrees_end2);

                //Log.i(TAG,pointF1.x+","+pointF1.y);
                mPaint.setColor(mainColor);
                canvas.drawLine(pointF1.x, pointF1.y, pointF.x, pointF.y, mPaint);
                canvas.drawLine((float) (pointF.x + Math.sin(radians3) * (linewidth / 2)*progress), (float) (pointF.y + Math.cos(radians3) * (linewidth / 2)*progress), pointF2.x, pointF2.y, mPaint);
                break;
            case ERROR://异常
                if (errorColor == targetColor) {
                    color = errorColor;
                } else {
                    color = getCurrentColor(progress, errorColor, targetColor);
                }
                mPaint.setColor(color);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(a, b, r0, mPaint);

                int width_error = r * 5 / 7;
                PointF pointF_error = new PointF(width / 2, height / 2);
                double degrees_error_01 = 315.0;
                double radians_error_01 = Math.toRadians(degrees_error_01);
                PointF pointF_error_01 = new PointF((float) (pointF_error.x - Math.sin(radians_error_01) * width_error), (float) (pointF_error.y + Math.cos(radians_error_01) * width_error));
                PointF pointF_error_02 = new PointF((float) (pointF_error.x + Math.sin(radians_error_01) * width_error), (float) (pointF_error.y - Math.cos(radians_error_01) * width_error));
                PointF pointF_error_03 = new PointF((float) (pointF_error.x - Math.sin(radians_error_01) * width_error), (float) (pointF_error.y - Math.cos(radians_error_01) * width_error));
                PointF pointF_error_04 = new PointF((float) (pointF_error.x + Math.sin(radians_error_01) * width_error), (float) (pointF_error.y + Math.cos(radians_error_01) * width_error));


                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(mainColor);
                canvas.drawLine(pointF_error_01.x, pointF_error_01.y, pointF_error_02.x, pointF_error_02.y, mPaint);
                canvas.drawLine(pointF_error_03.x, pointF_error_03.y, pointF_error_04.x, pointF_error_04.y, mPaint);
                break;
            case WARNING://警告
                if (warningColor == targetColor) {
                    color = warningColor;
                } else {
                    color = getCurrentColor(progress, warningColor, targetColor);
                }
                mPaint.setColor(color);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(a, b, r0, mPaint);

                int height_warning = r * 9 / 7;
                float weight1 = .6f;
                float weight2 = .8f;
                PointF pointF_warning = new PointF(width / 2, height / 2);

                PointF pointF_warning_01 = new PointF(pointF_warning.x, (float) (pointF_warning.y - 0.5 * height_warning));
                PointF pointF_warning_02 = new PointF(pointF_warning.x, (float) (pointF_warning.y + (weight1 - .5) * height_warning));
                PointF pointF_warning_03 = new PointF(pointF_warning.x, (float) (pointF_warning.y + (weight2 - .5) * height_warning));
                PointF pointF_warning_04 = new PointF(pointF_warning.x, (float) (pointF_warning.y + 0.5 * height_warning));

                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(mainColor);
                canvas.drawLine(pointF_warning_01.x, pointF_warning_01.y, pointF_warning_02.x, pointF_warning_02.y, mPaint);
                canvas.drawLine(pointF_warning_03.x, pointF_warning_03.y, pointF_warning_04.x, pointF_warning_04.y, mPaint);
                break;


            case LOADING://加载
                if (loadingColor == targetColor) {
                    color = loadingColor;
                } else {
                    color = getCurrentColor(progress, loadingColor, targetColor);
                }
                mPaint.setColor(color);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(a, b, r0, mPaint);

                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(mainColor);
                RectF oval = new RectF((width-r0)/2,(height-r0)/2,(width-r0)/2+r0,(height-r0)/2+r0);
                canvas.rotate((float) progress*360,width/2,height/2);
                canvas.drawArc(oval, 0, 90+Math.abs(progress-0.5f)*140, false, mPaint);
                //canvas.save();
                //canvas.translate(0,0);
                //canvas.rotate((float) (90 - Math.toDegrees(60)));
                break;
        }
    }

    private float progress;
    public void setStateType(StateType stateType) {
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
    public void show() {
       if(animator==null){
            init();
        }else {
            if(animator.isStarted()||animator.isRunning()){
                animator.cancel();
                init();
            }
        }
        //animator = MyValueAnimator.ofFloat(0, 1);
       // animator.setFloatValues(0, 1);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        if(stateType==StateType.LOADING){
            animator.setRepeatCount(ValueAnimator.INFINITE);
        }else {
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

    //隐藏后显示
    public void hideAndShow(final StateType stateType1) {
        if (animator != null) {
            if(animator.isStarted()||animator.isRunning()){
                animator.cancel();
            }
            animator = ValueAnimator.ofFloat(0, progress);
            //animator.setFloatValues(0, progress);
            animator.setRepeatCount(0);
            animator.setDuration((int) (duration * progress));
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
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
            });
            animator.reverse();
        }
    }


    public void setWarningColor(int warningColor) {
        this.warningColor = warningColor;
    }

    public void setErrorColor(int errorColor) {
        this.errorColor = errorColor;
    }

    public void setCompleteColor(int completeColor) {
        this.completeColor = completeColor;
    }

    public void setMainColor(int mainColor) {
        this.mainColor = mainColor;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * 根据fraction值来计算当前的颜色。
     */
    private int getCurrentColor(float fraction, int startColor, int endColor) {
        int redCurrent;
        int blueCurrent;
        int greenCurrent;
        int alphaCurrent;

        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaStart = Color.alpha(startColor);

        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        int alphaEnd = Color.alpha(endColor);

        int redDifference = redEnd - redStart;
        int blueDifference = blueEnd - blueStart;
        int greenDifference = greenEnd - greenStart;
        int alphaDifference = alphaEnd - alphaStart;

        redCurrent = (int) (redStart + fraction * redDifference);
        blueCurrent = (int) (blueStart + fraction * blueDifference);
        greenCurrent = (int) (greenStart + fraction * greenDifference);
        alphaCurrent = (int) (alphaStart + fraction * alphaDifference);

        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
    }


}
