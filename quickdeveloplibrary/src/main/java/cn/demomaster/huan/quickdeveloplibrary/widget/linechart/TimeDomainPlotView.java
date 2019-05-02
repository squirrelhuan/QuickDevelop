package cn.demomaster.huan.quickdeveloplibrary.widget.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;

import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

/**
 * 时域图
 */
public class TimeDomainPlotView extends View {

    public TimeDomainPlotView(Context context) {
        super(context);
        initView(context);
    }

    public TimeDomainPlotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TimeDomainPlotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public TimeDomainPlotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private boolean isLoadComplete;//加载完成
    private ViewDragHelper mDragHelper;

    public void initView(final Context context) {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                isLoadComplete = true;
                mScaleGestureDetector = new ScaleGestureDetector(context,
                        new ScaleGestureListener());
                mDragHelper = ViewDragHelper.create((ViewGroup) getParent(), 1.0f, new DragHelperCallback());
            }
        });

    }

    List<LinePoint> linePoints;

    public List<LinePoint> getLinePoints() {
        return linePoints;
    }

    private float maxX;
    private float maxY;
    private float centerY;
    private float baselineY;//y轴基线
    private float axisScaleX = 1f;//默认坐标轴最大显示区域比例
    private float axisScaleY = 0.5f;//默认坐标轴最大显示区域比例

    public void setBaselineY(float baselineY) {
        this.baselineY = baselineY;
        postInvalidate();
    }

    public void setLinePoints(List<LinePoint> linePoints) {
        this.linePoints = linePoints;
        maxX = 0;
        maxY = 0;
        for (int i = 0; i < linePoints.size(); i++) {
            if (maxX < linePoints.get(i).getX() / axisScaleX) {
                maxX = linePoints.get(i).getX() / axisScaleX;
            }
            if (maxY < linePoints.get(i).getY() / axisScaleY) {
                maxY = linePoints.get(i).getY() / axisScaleY;
            }
        }
        postInvalidate();
    }

    @Override
    public void postInvalidate() {
        if (isLoadComplete) {
            super.postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAxis(canvas);
        if (linePoints != null) {
            drawLine(canvas);
        }
    }

    private float granularity = 50;//刻度间隔
    private int columnNum;//纵向刻度数量
    private float offsetX;//x轴偏移量
    private float offsetY;//y轴偏移量

    /**
     * 绘制坐标轴
     *
     * @param canvas
     */
    private void drawAxis(Canvas canvas) {
        Path pathX = new Path();
        pathX.moveTo(0, getHeight() / 2 + offsetY);
        pathX.lineTo(getWidth(), getHeight() / 2 + offsetY);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(axisColor);
        paint.setStrokeWidth(2);
        canvas.drawPath(pathX, paint);
        Path pathY = new Path();
        pathY.moveTo(offsetX,  0);
        pathY.lineTo(offsetX, getHeight());
        canvas.drawPath(pathY, paint);

        //绘制刻度
        Paint columnPaint = new Paint();
        columnPaint.setStyle(Paint.Style.STROKE);
        columnPaint.setColor(columnColor);
        columnPaint.setStrokeWidth(1);
        float g = granularity * scale;
        int c =1;
        if(scale<1){
           c = (int) (1/scale);
        }
        columnNum = (int) (getWidth() / g);
        W:
        for (int i = 0; i < columnNum; i+=c) {
            float startX = i * g + offsetX%g;
            canvas.drawLine(startX, 0, startX, getHeight(), columnPaint);
            if (startX > getWidth()) {
                break W;
            }
        }
    }

    private int columnColor = 0x55888888;//纵向标尺颜色
    private int axisColor = 0xcc888888;//坐标系颜色
    private int lineColor = Color.RED;
    private int pointColor = Color.GREEN;
    private int textColor = Color.BLACK;
    private int textSize = 32;
    private int pointRadius = 5;

    private void drawLine(Canvas canvas) {
        centerY = getHeight() / 2;
        int startP = 0;
        int endP = linePoints.size();
        Paint paint = new Paint();
        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        float g = granularity * scale;
        Path path = new Path();
        path.moveTo(0 + offsetX, -linePoints.get(0).getY() / maxY * getHeight() / 2 + centerY - baselineY + offsetY);
        for (int i = startP; i < endP; i++) {
            float startX = i * g + offsetX;
            float startY = -linePoints.get(i).getY() / maxY * getHeight() / 2 + centerY - baselineY + offsetY;
            path.lineTo(startX, startY);
        }
        //path.close();
        canvas.drawPath(path, paint);

        //画点
        Paint pointPaint = new Paint();
        pointPaint.setColor(pointColor);
        for (int i = startP; i < endP; i++) {
            float startX = i * g + offsetX;
            float startY = -linePoints.get(i).getY() / maxY * getHeight() / 2 + centerY - baselineY + offsetY;
            canvas.drawCircle(startX, startY, pointRadius, pointPaint);
        }

        //画值
        Paint textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        for (int i = startP; i < endP; i++) {
            float startX = i * g + offsetX;
            float startY = -linePoints.get(i).getY() / maxY * getHeight() / 2 + centerY - baselineY + offsetY+(linePoints.get(i).getY()>0?-textSize/2-pointRadius:textSize+pointRadius);
            String text = linePoints.get(i).getY()+"";
            // 文字宽
            float textWidth1 = textPaint.measureText(text);
            canvas.drawText(text,startX-textWidth1/2,startY,textPaint);
        }
    }

    private ScaleGestureDetector mScaleGestureDetector = null;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerCount = event.getPointerCount(); // 获得多少点
        //QDLogger.i("获得多少点:"+pointerCount);
        if (pointerCount > 1) {// 多点触控，
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //needToHandle=false;
                    break;
                case MotionEvent.ACTION_MOVE:

                    break;
                case MotionEvent.ACTION_POINTER_2_UP://第二个手指抬起的时候
                    //needToHandle=true;
                    break;
                default:
                    break;
            }
            return mScaleGestureDetector.onTouchEvent(event);//让mScaleGestureDetector处理触摸事件
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastMultiTouchTime > 200) {
                try {
                    mDragHelper.processTouchEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        // return super.onTouchEvent(event);
    }

    public class DragHelperCallback extends ViewDragHelper.Callback {
        //这个地方实际上函数返回值为true就代表可以滑动 为false 则不能滑动
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == TimeDomainPlotView.this;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            QDLogger.i("onViewPositionChanged");
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            //int top = getPaddingTop();
           // mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            QDLogger.i("clampViewPositionHorizontal,left=" + left + ",dx=" + dx);
            offsetX = offsetX + dx;
            postInvalidate();
            return 0;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            QDLogger.i("clampViewPositionVertical,top=" + top + ",dy=" + dy);
            offsetY = offsetY + dy;
            postInvalidate();
            return getTop();
        }
    }

    private long lastMultiTouchTime;// 记录多点触控缩放后的时间
    private float preScale = 1;// 默认前一次缩放比例为1
    private float scale = 1;

    public class ScaleGestureListener implements
            ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float previousSpan = Math.max(1, detector.getPreviousSpan());// 前一次双指间距
            float currentSpan = detector.getCurrentSpan();// 本次双指间距
            if (currentSpan < previousSpan) {
                // 缩小
                scale = Math.max(preScale * Math.abs(currentSpan / previousSpan), 0.1f);//最小0.1倍数
                QDLogger.i("缩小:" + scale);
            } else {
                // 放大
                scale = Math.min(preScale * Math.abs(currentSpan / previousSpan), 50);//最大50倍数
                QDLogger.i("放大:" + scale);
            }
            invalidate();
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            // 一定要返回true才会进入onScale()这个函数
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            preScale = scale;// 记录本次缩放比例
            lastMultiTouchTime = System.currentTimeMillis();// 记录双指缩放后的时间
        }
    }

    private ScaleType scaleType = ScaleType.scaleX;
    private TransitionType transitionType = TransitionType.horizontal;

    public static enum ScaleType {
        scaleX//x轴缩放
        , scaleY//y轴缩放
        , scaleXY//xy轴都缩放
        , none//不允许缩放
    }

    /**
     * 移动类型
     */
    public static enum TransitionType {
        horizontal//横向滚动
        , vertical//纵向滚动
        , transitionXY//任意移动
        , none//不允许移动
    }

}
