package cn.demomaster.huan.quickdeveloplibrary.view.timeline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TimeLineView extends View {
    public TimeLineView(Context context) {
        super(context);
        init();
    }

    public TimeLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TimeLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    Paint mPaint;
    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
    }

    int lineColorActive = Color.GREEN;
    int lineColor = Color.GRAY;
    int pointColorActive = Color.GREEN;
    int pointColor = Color.GRAY;

    List<TimePoint> points = new ArrayList<>();
    int pointRadius = 20;
    int lineWidth = 5;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(points.size()<1){
            return;
        }

        //画线
        mPaint.setStrokeWidth(lineWidth);
        TimePoint lastPoint = points.get(0);
        for(int i=1;i<points.size();i++) {
            if(points.get(i).isActive&&lastPoint.isActive){
                mPaint.setColor(lineColorActive);
            }else {
                mPaint.setColor(lineColor);
            }
            canvas.drawLine(lastPoint.x,lastPoint.y,points.get(i).x,points.get(i).y,mPaint);
            lastPoint = points.get(i);
        }

        //画点
        for(int i=0;i<points.size();i++) {
            if(points.get(i).isActive){
                mPaint.setColor(pointColorActive);
            }else {
                mPaint.setColor(pointColor);
            }
            canvas.drawCircle(points.get(i).x,points.get(i).y,pointRadius,mPaint);
        }
    }

    View targetView;
    int targetId;

    /**
     * 针对滚动式布局
     * @param viewGroup
     * @param id
     */
    public void setTargetViewById(ViewGroup viewGroup,int id){
        isScrollView = true;
        targetView = viewGroup;
        targetId = id;
        if(viewGroup instanceof RecyclerView)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewGroup.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    updateState();
                }
            });
        }
        updateState();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(targetView!=null){
            return targetView.dispatchTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    Map<Integer,View> targetViewsMap = new LinkedHashMap<>();
    ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
            updateState();
        }
    };
    ViewTreeObserver.OnDrawListener onDrawListener = new ViewTreeObserver.OnDrawListener() {
        @Override
        public void onDraw() {
            updateState();
        }
    };
    /**
     * 非滚动式布局
     * @param view
     */
    public void addTargetView(View view){
        /*if(targetViewsMap.size()>0){
            view.setActivated(true);
        }*/
        isScrollView = false;
        targetViewsMap.put(view.hashCode(),view);
        view.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        view.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        view.getViewTreeObserver().removeOnDrawListener(onDrawListener);
        view.getViewTreeObserver().addOnDrawListener(onDrawListener);
    }
    boolean isScrollView;
    private void updateState() {
        List<View> views;
        if(!isScrollView){
            views = new ArrayList<>();
            if(targetViewsMap!=null) {
                for (Map.Entry entry : targetViewsMap.entrySet()) {
                    views.add((View) entry.getValue());
                }
            }
        }else {
            views = findViewByid(targetView, targetId);
        }
        if(views!=null){
            points = new ArrayList<>();
            for (View view:views){
                int[] position = new int[2];
                view.getLocationInWindow(position);

                int[] positionSelf = new int[2];
                getLocationInWindow(positionSelf);

                TimePoint point = new TimePoint(getWidth()/2,position[1]+view.getHeight()/2-positionSelf[1]);
                point.setActive(view.isActivated());
                points.add(point);
            }
            invalidate();
        }
    }

    public List<View> findViewByid(View view,int id){
        List<View> views = new ArrayList<>();
        if(view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup)view).getChildCount(); i++) {
                List<View> views2 =  findViewByid(((ViewGroup) view).getChildAt(i),id);
                views.addAll(views2);
            }
        }else {
            if(view.getId()==id){
                views.add(view);
            }
        }
        return views;
    }
}
