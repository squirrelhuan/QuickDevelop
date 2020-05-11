package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cn.demomaster.huan.quickdeveloplibrary.util.QDBitmapUtil;

/**
 * @author squirrel桓
 * @date 2018/11/20.
 * description：
 */
public class RatingBar extends View {


    public RatingBar(Context context) {
        super(context);
        init();
    }

    public RatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private int backResourceId;//背景色
    private int frontResourceId;//前景色
    private boolean useCustomDrable = false;//0,颜色，true.图片资源
    private boolean isFloat = true;//0,float，1.int

    /**
     * 设置背景资源
     *
     * @param backResourceId 0为空
     */
    public void setBackResourceId(int backResourceId) {
        this.backResourceId = backResourceId;
        setCustomDrawable();
    }

    /**
     * 设置前景资源
     *
     * @param frontResourceId
     */
    public void setFrontResourceId(int frontResourceId) {
        this.frontResourceId = frontResourceId;
        setCustomDrawable();
    }

    private void setCustomDrawable() {
        setUseCustomDrable(backResourceId != 0 || frontResourceId != 0);
    }

    /**
     * 设置资源类型
     *
     * @param useCustomDrable
     */
    public void setUseCustomDrable(boolean useCustomDrable) {
        this.useCustomDrable = useCustomDrable;
    }

    /**
     * 设置数据类型默认为float
     *
     * @param isFloat true ?float:int
     */
    public void setFloat(boolean isFloat) {
        this.isFloat = isFloat;
    }

    private void init() {
        setTouch();
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
        super.onDraw(canvas);
        drawView(canvas);
    }

    private int count = 5;
    private int countMni = 0;//最小取值
    private int countMax = 5;//最大取值
    private int foregroundColor = Color.RED, backgroundColor = Color.LTGRAY;
    private int activateCount = 0;
    private float progress = .8f;
    private float progressMin = 0f;
    private int progressInteger = 0;

    public int getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setColor(int activateColor, int darkColor) {
        this.foregroundColor = activateColor;
        this.backgroundColor = darkColor;
        postInvalidate();
    }

    public void setActivateCount(int activateCount) {
        this.activateCount = activateCount;
        this.progress = (float) activateCount / count;
        postInvalidate();
    }

    public void setProgress(float progress) {
        this.progress = progress;
        postInvalidate();
    }

    /**
     * 设置最小取值 float类型时
     *
     * @param progressMin
     */
    public void setProgressMin(float progressMin) {
        this.progressMin = progressMin;
    }

    /**
     * 设置最小取值 int类型时
     *
     * @param countMni
     */
    public void setCountMni(int countMni) {
        this.countMni = countMni;
    }

    /**
     * int类型的时候取值
     *
     * @return
     */
    public int getProgressInteger() {
        progressInteger = (int) Math.max(progress * count, countMni);
        return progressInteger;
    }

    public float getProgress() {
        return progress;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private float getProgressByState() {
        if (!isFloat) {//int
            float a = (float) Math.ceil(progress * count);
            return Math.max(a, countMni) / count;
        } else {
            return Math.max(progress, progressMin);
        }
    }

    private void drawView(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);

        float progress_c = getProgressByState();
        //禁用硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //使用离屏绘制
        int layerID = canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);

        //横向排列的五个星所在的坐标位置gravity：left
        int cell = Math.min(height, width / count);//最大正方形
        Bitmap backbitmap = null;
        Bitmap frontBitmap = null;
        if (useCustomDrable) {
            // Bitmap backbitmap = QDBitmapUtil.drawable2Bitmap(backDrawable);
            backbitmap = QDBitmapUtil.drawable2Bitmap(getContext(), backResourceId);
            backbitmap = QDBitmapUtil.zoomImage(backbitmap, cell, cell);
            frontBitmap = QDBitmapUtil.drawable2Bitmap(getContext(), frontResourceId);
            frontBitmap = QDBitmapUtil.zoomImage(frontBitmap, cell, cell);
        }

        //遍历绘制五角星
        for (int i = 0; i < count; i++) {
            int x1 = i * width / count;
            int y1 = height / 2 - cell / 2;
            if (!useCustomDrable) {
                //一个五角星需要10个定点
                //最上面的顶点开始
                PointF p1 = new PointF(x1 + cell / 2, y1);
                PointF p2 = new PointF(x1 + cell / 2 + cell * .14f, y1 + cell * .35f);
                PointF p3 = new PointF(x1 + cell / 2 + cell * .5f, y1 + cell * .35f);
                PointF p4 = new PointF(x1 + cell / 2 + cell * .22f, y1 + cell * .6f);
                PointF p5 = new PointF(x1 + cell / 2 + cell * .35f, y1 + cell * 1f);
                PointF p6 = new PointF(x1 + cell / 2, y1 + cell * .72f);
                PointF p7 = new PointF(x1 + cell / 2 - cell * .35f, y1 + cell * 1f);
                PointF p8 = new PointF(x1 + cell / 2 - cell * .22f, y1 + cell * .6f);
                PointF p9 = new PointF(x1 + cell / 2 - cell * .5f, y1 + cell * .35f);
                PointF p10 = new PointF(x1 + cell / 2 - cell * .14f, y1 + cell * .35f);

                Path path = null;
                PointF[] points = {p1, p2, p3, p4, p5, p6, p7, p8, p9, p10};
                for (PointF pointF : points) {
                    if (path == null) {
                        path = new Path();
                        path.moveTo(pointF.x, pointF.y);
                    } else {
                        path.lineTo(pointF.x, pointF.y);
                    }
                }
                path.close();
                mPaint.setColor(backgroundColor);
                canvas.drawPath(path, mPaint);

                //使用CLEAR作为PorterDuffXfermode绘制的矩形
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                //设置为在区域内重叠部分绘制
                RectF rectF = new RectF(0, 0, width * progress_c, height);
                mPaint.setColor(foregroundColor);
                canvas.drawRect(rectF, mPaint);
                //canvas.drawPath(path,mPaint);
                //最后将画笔去除Xfermode
                mPaint.setXfermode(null);
                //还原图层
                canvas.restoreToCount(layerID);
            } else {
                if (backbitmap != null) {
                    //绘制背景
                    Rect rect = new Rect(x1, y1, (int) (width), y1 + cell);
                    canvas.clipRect(rect);
                    canvas.drawBitmap(backbitmap, x1, y1, mPaint);
                    canvas.save();
                }
                if (frontBitmap != null) {
                    //绘制前景
                    Rect rect2 = new Rect(x1, y1, (int) (width * progress_c), y1 + cell);
                    canvas.clipRect(rect2);
                    canvas.drawBitmap(frontBitmap, x1, y1, mPaint);
                    canvas.restore();
                }
            }
        }
    }

    private boolean canTouch;//是否可触摸改变进度
    public void setCanTouch(boolean canTouch) {
        this.canTouch = canTouch;
    }

    private void setTouch() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (canTouch) {
                    progress = (float) motionEvent.getX() / width;
                }
                return true;
            }
        });
    }
}
