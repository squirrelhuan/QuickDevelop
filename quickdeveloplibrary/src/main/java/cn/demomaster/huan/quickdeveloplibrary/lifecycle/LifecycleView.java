package cn.demomaster.huan.quickdeveloplibrary.lifecycle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.customview.widget.ViewDragHelper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * 时域图
 */
public class LifecycleView extends View {

    public LifecycleView(Context context) {
        super(context);
        initView(context);
    }

    public LifecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LifecycleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LifecycleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private boolean isLoadComplete;//加载完成
    private ViewDragHelper mDragHelper;

    public void initView(final Context context) {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                isLoadComplete = true;
                mScaleGestureDetector = new ScaleGestureDetector(context,
                        new ScaleGestureListener());
                mDragHelper = ViewDragHelper.create((ViewGroup) getParent(), 1.0f, new DragHelperCallback());
            }
        });
    }

    LinkedHashMap<LifeCycleClassInfo, List<LifeCycleEvent>> linePoints;

    public LinkedHashMap<LifeCycleClassInfo, List<LifeCycleEvent>> getLinePoints() {
        return linePoints;
    }

    private float centerY;
    private float baselineY;//y轴基线
    private float axisScaleX = 1f;//默认坐标轴最大显示区域比例
    private float axisScaleY = 0.5f;//默认坐标轴最大显示区域比例
    private boolean showLable = true;

    public void setShowLable(boolean showLable) {
        this.showLable = showLable;
        postInvalidate();
    }

    public void setBaselineY(float baselineY) {
        this.baselineY = baselineY;
        postInvalidate();
    }

    public void setLinePoints(LinkedHashMap<LifeCycleClassInfo, List<LifeCycleEvent>> linePoints) {
        this.linePoints = linePoints;
        /*for(Map.Entry entry : linePoints.entrySet()) {

            for (LifeCycleEvent lifeCycleEvent : (List<LifeCycleEvent>)entry.getValue()) {
                QDLogger.i("clazz:"+((LifeCycleClassInfo)entry.getKey()).getClazz().getName()+",lifeCycleEvent=" + lifeCycleEvent.lifecycleType+",time="+lifeCycleEvent.getTime());
            }
        }*/
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
        if (linePoints != null && linePoints.size() > 0) {
            drawLine(canvas);
        }
    }

    private float granularity = 20;//刻度间隔
    private int columnNum;//纵向刻度数量
    private float offsetX;//x轴偏移量
    private float offsetY;//y轴偏移量

    /**
     * 绘制坐标轴
     *
     * @param canvas
     */
    private void drawAxis(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(axisColor);
        paint.setStrokeWidth(2);
        int i = 0;
        for (Map.Entry entry : linePoints.entrySet()) {
            Path pathX = new Path();
            float h = (i + 1) / (linePoints.size() + 1f) * getHeight();
            pathX.moveTo(0, h);
            pathX.lineTo(getWidth(), h);
            canvas.drawPath(pathX, paint);
            i++;
        }

    }

    private int columnColor = 0x55888888;//纵向标尺颜色
    private int axisColor = 0x77000000;//坐标系颜色
    private int lineWidth = 5;
    private int lineColor = 0x901B88EE;
    private int lineColorActive = 0xffFFFF00;
    private int pointColor = Color.GREEN;
    private int textColor = 0xffFA8072;
    private int textSize = 26;
    private int pointRadius = 8;

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        postInvalidate();
    }

    private void drawLine(Canvas canvas) {
        long leftX = 0;
        long rightX = System.currentTimeMillis();
        for (Map.Entry entry : linePoints.entrySet()) {
            for (LifeCycleEvent lifeCycleEvent : (List<LifeCycleEvent>) entry.getValue()) {
                if (leftX == 0) {
                    leftX = lifeCycleEvent.time;
                }
                if (leftX > lifeCycleEvent.time) {
                    leftX = lifeCycleEvent.time;
                }
                if (rightX < lifeCycleEvent.time) {
                    rightX = lifeCycleEvent.time;
                }
            }
        }

        //System.out.println("leftX=" + leftX + ",rightX=" + rightX);
        float widthX = rightX - leftX;
        int i = 0;
        for (Map.Entry entry : linePoints.entrySet()) {
            //drawLine(canvas, linePoints.get(i));
            List<LifeCycleEvent> lifeCycleEvents = (List<LifeCycleEvent>) entry.getValue();
            centerY = getHeight() / 2f;
            Paint paint = new Paint();
            paint.setColor(lineColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(lineWidth);
            paint.setAntiAlias(true);
            Path path = null;
            int y = getHeight() / (linePoints.size() + 1) * (i + 1);

            LifeCycleEvent lastLifeCycleEvent = lifeCycleEvents.get(lifeCycleEvents.size() - 1);//最後一個狀態值

            if (lastLifeCycleEvent.lifecycleType == LifecycleType.onActivityResumed || lastLifeCycleEvent.lifecycleType == LifecycleType.onFragmentResumed) {
                paint.setColor(lineColorActive);
            }

            for (LifeCycleEvent lifeCycleEvent : lifeCycleEvents) {
                if (path == null) {
                    path = new Path();
                    path.moveTo((lifeCycleEvent.time - leftX) / widthX * getWidth(), y);
                } else {
                    path.lineTo((lifeCycleEvent.time - leftX) / widthX * getWidth(), y);
                }
            }
            if (lastLifeCycleEvent.lifecycleType != LifecycleType.onActivityDestroyed && lastLifeCycleEvent.lifecycleType != LifecycleType.onFragmentDestroyed) {
                path.lineTo(getWidth(), y);
            }

            //path.close();
            canvas.drawPath(path, paint);

            int textSize_current = Math.min(textSize, y);
            //画点
            Paint pointPaint = new Paint();
            pointPaint.setColor(pointColor);
            for (LifeCycleEvent lifeCycleEvent : lifeCycleEvents) {
                float x = (lifeCycleEvent.time - leftX) / widthX * getWidth();
                //System.out.println("time="+lifeCycleEvent.time+",widthX="+widthX+",x=" + x);
                canvas.drawCircle(x, y, pointRadius, pointPaint);
                if (lifeCycleEvent.lifecycleType == LifecycleType.onActivityCreated || lifeCycleEvent.lifecycleType == LifecycleType.onFragmentCreated) {
                    Paint textPaint = new Paint();
                    textPaint.setTextSize(textSize_current);
                    textPaint.setColor(textColor);
                    float textLeft = Math.min(x + pointRadius, getWidth() - 200);
                    canvas.drawText(((LifeCycleClassInfo) entry.getKey()).getClazz().getSimpleName() + "(" + ((LifeCycleClassInfo) entry.getKey()).getClazzHashCode() + ")", textLeft, y - pointRadius, textPaint);
                }
            }
            i++;
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
                case MotionEvent.ACTION_POINTER_UP://第二个手指抬起的时候
                    //needToHandle=true;
                    break;
                default:
                    break;
            }
            lastMultiTouchTime = System.currentTimeMillis();// 记录双指缩放后的时间
            return mScaleGestureDetector.onTouchEvent(event);//让mScaleGestureDetector处理触摸事件
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastMultiTouchTime > 200) {
                try {
                    mDragHelper.processTouchEvent(event);
                } catch (Exception e) {
                    QDLogger.e(e);
                }
            }
            return true;
        }
        // return super.onTouchEvent(event);
    }

    /**
     * 控制位移
     */
    public class DragHelperCallback extends ViewDragHelper.Callback {
        //这个地方实际上函数返回值为true就代表可以滑动 为false 则不能滑动
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == LifecycleView.this;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            QDLogger.i("onViewPositionChanged");
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            QDLogger.i("onViewReleased,xvel=" + xvel + ",yvel=" + yvel);
            //int top = getPaddingTop();
            // mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
            //mScroller.startScroll;
            if (transitionType != TransitionType.none) {
                fling((int) xvel, (int) yvel);
            }
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            QDLogger.i("clampViewPositionHorizontal,left=" + left + ",dx=" + dx);
            if (transitionType != TransitionType.vertical && transitionType != TransitionType.none) {
                offsetX = offsetX + dx;
                postInvalidate();
            }
            return 0;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            QDLogger.i("clampViewPositionVertical,top=" + top + ",dy=" + dy);
            if (transitionType != TransitionType.horizontal && transitionType != TransitionType.none) {
                offsetY = offsetY + dy;
                postInvalidate();
            }
            return getTop();
        }
    }

    int dx;
    int dy;

    private void fling(int xvel, int yvel) {
        dx = (int) Math.sqrt(xvel);
        dy = (int) Math.sqrt(yvel);
        if (transitionType == TransitionType.horizontal || transitionType == TransitionType.transitionXY) {
            if (dx < 0) {
                offsetX += Math.max(dx, -200);
            } else {
                offsetX += Math.min(dx, 200);
            }
        }
        if (transitionType == TransitionType.vertical || transitionType == TransitionType.transitionXY) {
            if (dy < 0) {
                offsetY += Math.max(dy, -200);
            } else {
                offsetY += Math.min(dy, 200);
            }
        }
        postInvalidate();
        int t = (transitionType == TransitionType.horizontal ? dx : dy);
        if (Math.abs(t) > 2) {
            handler.postDelayed(() -> fling(dx, dy), 10);
        }
    }

    private Handler handler = new Handler();

    private long lastMultiTouchTime;// 记录多点触控缩放后的时间
    private float preScale = 1;// 默认前一次缩放比例为1
    private float scale = 1;

    /**
     * 控制缩放
     */
    public class ScaleGestureListener implements
            ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float previousSpan = Math.max(1, detector.getPreviousSpan());// 前一次双指间距
            float currentSpan = detector.getCurrentSpan();// 本次双指间距
            if (currentSpan < previousSpan) {
                // 缩小
                scale = Math.max(preScale * Math.abs(currentSpan / previousSpan), 0.01f);//最小0.1倍数
                QDLogger.println("缩小:" + scale);
            } else {
                // 放大
                scale = Math.min(preScale * Math.abs(currentSpan / previousSpan), 50);//最大50倍数
                QDLogger.println("放大:" + scale);
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

    public void setScaleType(ScaleType scaleType) {
        this.scaleType = scaleType;
        postInvalidate();
    }

    public void setTransitionType(TransitionType transitionType) {
        this.transitionType = transitionType;
        offsetX = 0;
        offsetY = 0;
        postInvalidate();
    }

    public enum ScaleType {
        scaleX//x轴缩放
        , scaleY//y轴缩放
        , scaleXY//xy轴都缩放
        , none//不允许缩放
    }

    /**
     * 移动类型
     */
    public enum TransitionType {
        horizontal//横向滚动
        , vertical//纵向滚动
        , transitionXY//任意移动
        , none//不允许移动
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (linePoints != null) {
            linePoints = null;
        }
    }
}
