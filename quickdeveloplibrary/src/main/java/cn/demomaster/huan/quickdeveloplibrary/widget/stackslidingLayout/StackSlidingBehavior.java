package cn.demomaster.huan.quickdeveloplibrary.widget.stackslidingLayout;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

public class StackSlidingBehavior extends CoordinatorLayout.Behavior<StackSlidingLayout> {

    private int mInitialOffset;

    @Override
    public boolean onMeasureChild(@NonNull CoordinatorLayout parent, @NonNull StackSlidingLayout child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {

        QDLogger.d("onMeasureChild");
        int offset = getChildMeasureOffset(parent,child);
        int heightMeasureSpec = View.MeasureSpec.getSize(parentHeightMeasureSpec)-offset;
        child.measure(parentWidthMeasureSpec,View.MeasureSpec.makeMeasureSpec(heightMeasureSpec, View.MeasureSpec.EXACTLY));
        return true;
    }

    private int getChildMeasureOffset(CoordinatorLayout parent, StackSlidingLayout child) {
        QDLogger.d("getChildMeasureOffset");
        int offset =0;
        for (int i =0;i<parent.getChildCount();i++){
            View view = parent.getChildAt(i);
            if(view!=child&&view instanceof StackSlidingLayout){
                offset += ((StackSlidingLayout)view).getHeaderViewHeight();
            }
        }
        return offset;
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull StackSlidingLayout child, int layoutDirection) {
        QDLogger.d("onLayoutChild");
        parent.onLayoutChild(child,layoutDirection);
        StackSlidingLayout previous = getPreviousChild(parent,child);
        if(previous!=null){
            int offset = previous.getTop()+previous.getHeaderViewHeight();
            child.offsetTopAndBottom(offset);
        }
        mInitialOffset = child.getTop();
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    private StackSlidingLayout getPreviousChild(CoordinatorLayout parent, StackSlidingLayout child) {
       int cartindex = parent.indexOfChild(child);
        for(int i =cartindex-1;i>=0;i--){
            View v = parent.getChildAt(i);
            if(v instanceof StackSlidingLayout){
                return (StackSlidingLayout) v;
            }
        }
        return null;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull StackSlidingLayout child, @NonNull View directTargetChild, @NonNull View target, int axes) {

        QDLogger.d("onStartNestedScroll2");
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull StackSlidingLayout child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        QDLogger.d("onStartNestedScroll");
       boolean isVertical = (axes & View.SCROLL_AXIS_VERTICAL)!=0;
        return isVertical && child==directTargetChild;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull StackSlidingLayout child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
    }
}
