package cn.demomaster.huan.quickdeveloplibrary.widget.button;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by Squirrel桓 on 2018/12/17.
 */
public class ToggleButton extends View {

    public ToggleButton(Context context) {
        super(context);
        init();
    }

    public ToggleButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ToggleButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private boolean isCheckedDef = false;

    public void init() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setChecked(!checked);
            }
        });
        setChecked(isCheckedDef);
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        startAnimation(checked);
        if (onToggleChangeListener != null) {
            onToggleChangeListener.onToggle(checked);
        }
    }

    private int center_x, center_y, mwidth, width, height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        center_x = width / 2;
    }

    private boolean checked = true;
    private int circle_padding = 3;
    private int color_01;
    private int color_02;

    private void drawView(Canvas canvas) {

        if (checked) {
            color_01 = Color.RED;
            color_02 = Color.WHITE;
        } else {
            color_01 = Color.WHITE;
            color_02 = Color.RED;
        }

        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color_01);
        int r = Math.min(width / 2, height);

        circle_padding = r / 20;
        RectF mRecF = new RectF((width / 2 - r), height / 2 - r / 2, (width / 2 - r) + 2 * r, height / 2 + r / 2);
        mPaint.setColor(color_01);
        canvas.drawRoundRect(mRecF, r / 2, r / 2, mPaint);

        float p = Math.min(3 / 2 * progress, 1);
        RectF mRecF2;
        if (checked) {
            mRecF2 = new RectF((width / 2 - r) + r * (1 - p), height / 2 - r / 2 * (p), (width / 2 - r) + 2 * r - r * (1 - p), height / 2 + r / 2 * (p));
        } else {
            mRecF2 = new RectF((width / 2 - r) + r * p, height / 2 - r / 2 * (1 - p), (width / 2 - r) + 2 * r - r * p, height / 2 + r / 2 * (1 - p));
        }
        mPaint.setColor(color_02);
        canvas.drawRoundRect(mRecF2, r / 2, r / 2, mPaint);

        mPaint.setColor(Color.WHITE);
        canvas.drawCircle((width / 2 - r) + 2 * r - r / 2 - (progress) * r, height / 2, r / 2, mPaint);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.save();
        mPaint.setAlpha((int) (progress * 255 * 0.7));
        canvas.drawRoundRect(mRecF, r / 2, r / 2, mPaint);
        canvas.drawCircle((width / 2 - r) + 2 * r - r / 2 - (progress) * r, height / 2, r / 2, mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);
    }

    private float progress;

    public void startAnimation(boolean checked) {
        int start = 0;
        int end = 1;
        if (checked) {
            start = 1;
            end = 0;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                progress = value;
                postInvalidate();
            }
        });
        //animator.setRepeatMode(ValueAnimator.RESTART);
        //animator.setRepeatCount(ValueAnimator.INFINITE);
        //animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    /**
     * @author ThinkPad
     */
    public interface OnToggleChangeListener {
        /**
         * @param on = =
         */
        public void onToggle(boolean on);
    }

    private OnToggleChangeListener onToggleChangeListener;

    public void setOnToggleChanged(OnToggleChangeListener onToggleChanged) {
        onToggleChangeListener = onToggleChanged;
    }

}
