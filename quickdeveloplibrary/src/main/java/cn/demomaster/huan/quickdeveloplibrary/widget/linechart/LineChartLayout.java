package cn.demomaster.huan.quickdeveloplibrary.widget.linechart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.google.android.material.appbar.AppBarLayout;

public class LineChartLayout extends RelativeLayout {

    public LineChartLayout(Context context) {
        super(context);
    }

    public LineChartLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChartLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LineChartLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    ViewDragHelper mViewDragHelper;
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //中间参数表示灵敏度,比如滑动了多少像素才视为触发了滑动.值越大越灵敏.
        mViewDragHelper = ViewDragHelper.create(this, 1f, new ViewDragCallback());
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //固定写法
        int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_UP) {
            mViewDragHelper.cancel();
            return false;
        }
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //固定写法
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        //固定写法
        //此方法用于自动滚动,比如自动回滚到默认位置.
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    //这个类的回调方法,才是ViewDragHelper的重点
    private class ViewDragCallback extends ViewDragHelper.Callback{

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //child 表示想要滑动的view
            //pointerId 表示触摸点的id, 比如多点按压的那个id
            //返回值表示,是否可以capture,也就是是否可以滑动.可以根据不同的child决定是否可以滑动
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //child 表示当前正在移动的view
            //left 表示当前的view正要移动到左边距为left的地方
            //dx 表示和上一次滑动的距离间隔
            //返回值就是child要移动的目标位置.可以通过控制返回值,从而控制child只能在ViewGroup的范围中移动.
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //child 表示当前正在移动的view
            //top 表示当前的view正要移动到上边距为top的地方
            //dx 表示和上一次滑动的距离间隔
            return top;
        }
    }
}
