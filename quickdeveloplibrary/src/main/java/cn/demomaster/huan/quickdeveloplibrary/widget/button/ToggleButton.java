package cn.demomaster.huan.quickdeveloplibrary.widget.button;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

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
        setOnClickListener(view -> setChecked(!checked));
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        startAnimation(checked);
        if (onToggleChangeListener != null) {
            onToggleChangeListener.onToggle(this, checked);
        }
    }

    private int width, height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private boolean checked = true;
    private float circle_padding = 3;

    //private int color_01;
    //private int color_02;
    private int backColor = Color.WHITE;
    private int lineColor = Color.parseColor("#dadbda");//描边颜色
    private int toogleColor = Color.parseColor("#4ebb7f");//选中颜色
    /**
     * 手柄颜色
     */
    private int spotColor = Color.parseColor("#ffffff");
    private int lineWidth = 4;

    public int getToogleColor() {
        return toogleColor;
    }

    public void setToogleColor(int toogleColor) {
        this.toogleColor = toogleColor;
        postInvalidate();
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
        postInvalidate();
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        postInvalidate();
    }

    private void drawView(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        int w = height / 2 - lineWidth / 2;
        float lineWidth2 = lineWidth * (progress);
        float r = w - lineWidth2;

        //绘制底色
        circle_padding = r / 8;
        RectF mRecF = new RectF(lineWidth / 2f, height / 2f - r, width - lineWidth / 2f, height / 2f + r);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(backColor);
        canvas.drawRoundRect(mRecF, w, w, mPaint);
        mPaint.setStrokeWidth(lineWidth2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(lineColor);
        canvas.drawRoundRect(mRecF, w, w, mPaint);

        PointF pointF = new PointF(r + (width - 2 * r) * (1 - progress), height / 2f);
        RectF mRecF_left = new RectF(lineWidth2, height / 2f - r + lineWidth2, pointF.x + r, height / 2f + r - lineWidth2);
        mPaint.setStyle(Paint.Style.FILL);
        //绘制动态区域
        mPaint.setColor(toogleColor);
        canvas.drawRoundRect(mRecF_left, Math.min(r, mRecF_left.right), Math.min(r, mRecF_left.right), mPaint);
        //画圆
        mPaint.setColor(spotColor);
        canvas.drawCircle((1 - progress) * (width - 2 * w) + w, height / 2f, r - circle_padding * (1 - progress), mPaint);
        mPaint.setColor(lineColor);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.save();
        mPaint.setAlpha((int) ((progress) * 255));
        //描边w
        canvas.drawCircle((1 - progress) * (width - 2 * w) + r + lineWidth / 2f, height / 2f, r - (circle_padding) * (1 - progress), mPaint);
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
        //animator.setInterpolator(new OvershootInterpolator());
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            progress = value;
            postInvalidate();
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
        void onToggle(View view, boolean on);
    }

    private OnToggleChangeListener onToggleChangeListener;

    public void setOnToggleChanged(OnToggleChangeListener onToggleChanged) {
        onToggleChangeListener = onToggleChanged;
    }

}
