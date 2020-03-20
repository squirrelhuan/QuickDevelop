package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

public class QDEditView extends View implements ViewTreeObserver.OnPreDrawListener {
    public QDEditView(Context context) {
        super(context);
        init();
    }

    public QDEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QDEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    int cursorState = 0;//游标状态
    float cursorWidth = 2;//光标宽度
    int cursorTime = 1000;//光标闪烁时间
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isPressed) {
                if (cursorState == 0) {
                    cursorState = 1;
                } else {
                    cursorState = 0;
                }
            }else {
                cursorState = 1;
            }
            postInvalidate();
            QDLogger.i("cursorState=" + cursorState);
            handler.postDelayed(runnable, cursorTime);
        }
    };

    private void init() {
        clickPoint = new PointF();
        cursorPoint = new PointF();
        cursorWidth = DisplayUtil.dip2px(getContext(), 1);

        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 0);
    }

    private String text = "When a rectangular clip operation (from clipRect) is not axis-aligned with the raster buffer, or when the clip operation is not rectalinear (e.g. because it is a rounded rectangle clip created by clipRRect or an arbitrarily complicated path clip created by clipPath), the edge of the clip needs to be anti-aliased.\n" +
            "\n" +
            "If two draw calls overlap at the edge of such a clipped region, without using saveLayer, the first drawing will be anti-aliased with the background first, and then the second will be anti-aliased with the result of blending the first drawing and the background. On the other hand, if saveLayer is used immediately after establishing the clip, the second drawing will cover the first in the layer, and thus the second alone will be anti-aliased with the background when the layer is clipped and composited (when restore is called).There are a several different hardware architectures for GPUs (graphics processing units, the hardware that handles graphics), but most of them involve batching commands and reordering them for performance. When layers are used, they cause the rendering pipeline to have to switch render target (from one layer to another). Render target switches can flush the GPU's command buffer, which typically means that optimizations that one could get with larger batching are lost. Render target switches also generate a lot of memory churn because the GPU needs to copy out the current frame buffer contents from the part of memory that's optimized for writing, and then needs to copy it back in once the previous render target (layer) is restored.";
    private int textSize = 60;
    private int textColor = Color.WHITE;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF();
        drawText(canvas, rectF);
    }

    private int lineCount;
    private int lineHeight;
    private long contentHeight;
    private long cursorIndex;//光标位置

    private void drawText(Canvas canvas, RectF rectF) {
        canvas.translate(getPaddingLeft(), positionY + getPaddingTop());
        Paint paint = new Paint();
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        int index = 0;
        lineHeight = textSize;
        int count = getContentWidth() / textSize;//每行显示文字数量
        lineCount = (int) Math.ceil(text.length() / (count * 1f));//行数
        QDLogger.i("lineCount=" + lineCount + ",count=" + count + ",text.length()=" + text.length());
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        StaticLayout layout = new StaticLayout(text, textPaint, getContentWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        layout.draw(canvas);
        lineCount = layout.getLineCount();
        contentHeight = layout.getHeight() + getPaddingBottom() + getPaddingTop();

        drawCursor(canvas);

        /*for(int i = 0;i<lineCount;i++){
            QDLogger.i("123");
            canvas.drawText(text.toCharArray(),i*count,Math.min(count,text.length()-count*i),0,textSize*i,paint);
        }*/
        // canvas.drawText("ok http",index*count,count,paint);

    }

    /**
     * 绘制光标
     *
     * @param canvas
     */
    private void drawCursor(Canvas canvas) {
        if (cursorState == 1) {
            RectF rectF = getCursorRectf();
            QDLogger.d("光标位置：" + rectF.left + "," + rectF.top + "," + rectF.right + "," + rectF.bottom);
            Paint paint = new Paint();
            paint.setColor(Color.YELLOW);
            canvas.drawRect(rectF, paint);
        }
    }

    //获取光标位置
    private RectF getCursorRectf() {
        long y = (long) (cursorPoint.y + positionY);
        long x = (long) (cursorPoint.x);
        return new RectF(x, y, x + cursorWidth, y + textSize);
        //cursorIndex
    }

    private int getContentWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    float positionY;
    float startTouchY;
    PointF clickPoint;
    PointF cursorPoint;//游标坐标
    boolean isPressed = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouchY = event.getRawY();
                isPressed = true;
                break;
            case MotionEvent.ACTION_MOVE:
                positionY -= startTouchY - event.getRawY();
                positionY = Math.min(positionY, 0);
                positionY = Math.max(positionY, -getContentHeight() + getHeight());
                startTouchY = event.getRawY();
                clickPoint = new PointF(event.getX(), event.getY());
                cursorPoint = new PointF(cursorPoint.x, -positionY + clickPoint.y);
                QDLogger.i("positionY=" + positionY + "，height=" + getHeight() + "，getContentHeight()=" + getContentHeight() + "，cursorPoint=" + cursorPoint);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                clickPoint = new PointF(event.getX(), event.getY());
                cursorPoint = new PointF(clickPoint.x, -positionY + clickPoint.y);
                isPressed = false;
                endTouch();
                break;
            case MotionEvent.ACTION_CANCEL:
                isPressed = false;
                break;
        }
        return true;
        // return super.onTouchEvent(event);
    }

    private void endTouch() {
        cursorState = 1;
        //clickPoint.y;
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 0);
    }

    private float getContentHeight() {
        return contentHeight; //lineCount*lineHeight;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    @Override
    public boolean onPreDraw() {
        return false;
    }

    @Override
    protected void onDetachedFromWindow() {
        handler.removeCallbacks(runnable);
        handler = null;
    }
}
