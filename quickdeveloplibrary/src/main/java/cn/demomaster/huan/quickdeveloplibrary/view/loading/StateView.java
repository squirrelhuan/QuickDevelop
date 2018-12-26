package cn.demomaster.huan.quickdeveloplibrary.view.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarTip;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;

/**
 * @author squirrel桓
 * @date 2018/11/8.
 * description：加载动画
 */
public class StateView extends ImageTextView {

    public StateView(Context context) {
        super(context);
    }
    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public StateView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    @Override
    protected void onDraw(Canvas canvas) {
        drawView(canvas);
        super.onDraw(canvas);
    }


    private ActionBarTip.StateType stateType = ActionBarTip.StateType.COMPLETE;
    private int warningColor = Color.YELLOW;
    private int errorColor = Color.RED;
    private int completeColor = Color.GREEN;
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

        switch (stateType) {
            case COMPLETE://完成
                mPaint.setColor(completeColor);
                mPaint.setStyle(Paint.Style.FILL);
                int r = Math.min(a, b);
                canvas.drawCircle(a, b, r, mPaint);

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
                mPaint.setColor(getResources().getColor(R.color.white));
                canvas.drawLine(pointF1.x, pointF1.y, pointF.x, pointF.y, mPaint);
                canvas.drawLine((float) (pointF.x + Math.sin(radians3) * (linewidth / 2)), (float) (pointF.y + Math.cos(radians3) * (linewidth / 2)), pointF2.x, pointF2.y, mPaint);
                break;
            case ERROR://异常
                mPaint.setColor(errorColor);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(a, b, Math.min(a, b), mPaint);

                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(getResources().getColor(R.color.white));
                canvas.drawLine(width / 4, height / 4, width * 3 / 4, height * 3 / 4, mPaint);
                canvas.drawLine(width * 3 / 4, height / 4, width / 4, height * 3 / 4, mPaint);
                break;
            case WARNING://警告
                mPaint.setColor(warningColor);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(a, b, Math.min(a, b), mPaint);

                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(getResources().getColor(R.color.white));
                canvas.drawLine(width / 2, height / 4, width / 2, height * 5 / 9, mPaint);
                canvas.drawLine(width / 2, height * 6 / 9, width / 2, height * 3 / 4, mPaint);
                break;
        }
    }

    private float progress;
    private boolean isForward = true;
    public void setStateType(ActionBarTip.StateType stateType) {
        this.stateType = stateType;
        if (animator!=null&&animator.isRunning()) {
            hide();
        }else {
            show();
        }
    }

    private ValueAnimator animator;
    private int duration = 400;

    public void show() {
        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                if (progress == 0) {
                    setVisibility(GONE);
                } else {
                    setVisibility(VISIBLE);
                }
                invalidate();
            }
        });
        //animator.setRepeatMode(ValueAnimator.REVERSE);
        //animator.setRepeatCount(ValueAnimator.INFINITE);//accelerate_decelerate_interpolator
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    public void hide() {
        if (animator != null) {
            animator.setFloatValues(0, progress);
            animator.setDuration((int) (duration * progress));
            animator.reverse();
        }
    }

}
