package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * @author squirrel桓
 * @date 2018/12/14.
 * description：
 */
public class AutoCenterHorizontalScrollView extends HorizontalScrollView  {
    public AutoCenterHorizontalScrollView(Context context) {
        super(context);
    }

    public AutoCenterHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoCenterHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        Log.i(tag,"onOverScrolled");
    }

    String tag ="AutoCenter";
    @Override
    public void computeScroll() {
        super.computeScroll();
        Log.i(tag,"computeScroll");
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.i(tag,"left="+l);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        Log.i(tag,"onNestedScroll。。。");
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
        Log.i(tag,"onNestedScrollAccepted。。。");
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.i(tag,"onStartNestedScroll。。。");
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
        Log.i(tag,"onStopNestedScroll。。。");
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);
        Log.i(tag,"onNestedPreScroll。。。");
    }

    @Override
    protected int computeHorizontalScrollExtent() {
        Log.i(tag,"computeHorizontalScrollExtent。。。"+super.computeHorizontalScrollExtent());//
        return super.computeHorizontalScrollExtent();
    }

    @Override
    protected int computeVerticalScrollRange() {
        Log.i(tag,"computeVerticalScrollRange。。。"+super.computeVerticalScrollRange());//
        return super.computeVerticalScrollRange();
    }

    @Override
    protected int computeVerticalScrollOffset() {
        Log.i(tag,"computeVerticalScrollOffset。。。"+super.computeVerticalScrollOffset());
        return super.computeVerticalScrollOffset();
    }

    @Override
    protected int computeVerticalScrollExtent() {
        Log.i(tag,"computeVerticalScrollExtent。。。"+super.computeVerticalScrollExtent());//
        return super.computeVerticalScrollExtent();
    }

    @Override
    protected boolean awakenScrollBars() {
        return super.awakenScrollBars();
    }
}
