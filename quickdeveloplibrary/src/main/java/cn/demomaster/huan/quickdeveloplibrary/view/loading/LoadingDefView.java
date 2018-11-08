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
public class LoadingDefView extends View {

    public LoadingDefView(Context context) {
        super(context);
    }

    public LoadingDefView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingDefView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mPaint.setColor(Color.RED);
        int r = Math.min(width, height);
        int margin_left = width > height ? (width - height) / 2 : 0;
        int margin_top = width < height ? (height - width) / 2 : 0;
        RectF mRecF = new RectF(margin_left, margin_top, margin_left + r, margin_top + r);
        canvas.drawArc(mRecF, 0, progress, true, mPaint);//以斜上45度为起点，顺时针扫过135度
    }

    private float progress;

    public void startAnimation() {
        isPlaying = true;
        final ValueAnimator animator = ValueAnimator.ofFloat(0, 360);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                progress = value;
                //postInvalidate();
                invalidate();
            }
        });
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

}
