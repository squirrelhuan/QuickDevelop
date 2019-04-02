package cn.demomaster.huan.quickdeveloplibrary.widget.stackslidingLayout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
public class StackSlidingBehavior extends CoordinatorLayout.Behavior<StackSlidingLayout> {


    private int mInitialOffset;

   /* @Override
    public boolean onMeasureChild(@NonNull CoordinatorLayout parent, @NonNull StackSlidingLayout child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        QDLogger.d("onMeasureChild");
        int offset = getChildMeasureOffset(parent, child);
        int height = View.MeasureSpec.getSize(parentHeightMeasureSpec) - offset;
        child.measure(parentWidthMeasureSpec, View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        return true;
    }*/

    private int getChildMeasureOffset(CoordinatorLayout parent, StackSlidingLayout child) {
        QDLogger.d("getChildMeasureOffset");
        int offset = 0;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view != child && view instanceof StackSlidingLayout) {
                offset += ((StackSlidingLayout) view).getHeaderViewHeight();
            }
        }
        return offset;
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull StackSlidingLayout child, int layoutDirection) {
        QDLogger.d("onLayoutChild");
        parent.onLayoutChild(child, layoutDirection);
        StackSlidingLayout previous = getPreviousChild(parent, child);
        if (previous != null) {
            int offset = previous.getTop() + previous.getHeaderViewHeight();
            child.offsetTopAndBottom(offset);
            //CoordinatorLayout.LayoutParams layoutParams_c = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            //layoutParams_c.topMargin = offset;
            //child.setLayoutParams(layoutParams_c);
            //child.setY();
            QDLogger.d("SlidingBehavior", child.getId() + "offsetTopAndBottom=" + offset+", top="+ child.getTop()+",y="+child.getY());
        }/*else {
            int offset = previous.getTop() + previous.getHeaderViewHeight();
            child.offsetTopAndBottom(offset);
        }*/
        mInitialOffset = child.getTop();
        QDLogger.d("SlidingBehavior", child.getId() + "mInitialOffset=" + mInitialOffset);
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    private StackSlidingLayout getPreviousChild(CoordinatorLayout parent, StackSlidingLayout child) {
        int cartindex = parent.indexOfChild(child);
        for (int i = cartindex - 1; i >= 0; i--) {
            View v = parent.getChildAt(i);
            if (v instanceof StackSlidingLayout) {
                return (StackSlidingLayout) v;
            }
        }
        return null;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull StackSlidingLayout child, @NonNull View directTargetChild, @NonNull View target, int axes) {
        QDLogger.d("onStartNestedScroll2");
        boolean isVertical = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        return isVertical && child == directTargetChild;
        // return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull StackSlidingLayout child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        QDLogger.d("onStartNestedScroll");
        boolean isVertical = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        return isVertical && child == directTargetChild;
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout parent, @NonNull StackSlidingLayout child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        //return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
        int shift = scroll(parent,child, consumed?0:(int) velocityY, mInitialOffset, mInitialOffset + child.getHeight() - child.getHeaderViewHeight());
        //shiftSlidings(shift, parent, child);
        return false;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout parent, @NonNull StackSlidingLayout child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        consumed[1] = scroll(parent,child, dy, mInitialOffset, mInitialOffset + child.getHeight() - child.getHeaderViewHeight());
        //shiftSlidings(consumed[1], parent, child);
         //super.onNestedPreScroll(parent, child, target, dx, dy, consumed, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout parent, @NonNull StackSlidingLayout child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        int shift = scroll(parent,child, dyUnconsumed, mInitialOffset, mInitialOffset + child.getHeight() - child.getHeaderViewHeight());
        //shiftSlidings(shift, parent, child);
    }

    private StackSlidingLayout getNextChild(CoordinatorLayout parent, StackSlidingLayout child) {
        int cartindex = parent.indexOfChild(child);
        for (int i = cartindex + 1; i < parent.getChildCount(); i++) {
            View v = parent.getChildAt(i);
            if (v instanceof StackSlidingLayout) {
                return (StackSlidingLayout) v;
            }
        }
        return null;
    }

    private int scroll(CoordinatorLayout parent,StackSlidingLayout child, int dy, int minOffset, int maxOffset) {
        //1.k控制自己的移动
        int initialOffset = child.getTop();
        View view = getPreviousChild(parent,child);
        int heightAll = getAllChildHeight(parent);
        int top = getChildTop(parent);
        int bottom = getChildBottom(parent);
        int offset = clamp(initialOffset - dy, -bottom,-top)-initialOffset;
        child.offsetTopAndBottom(offset);


        moveOther(parent,child,offset);

        return -offset;//滑动方向
    }

    private int getChildBottom(CoordinatorLayout parent) {
        int bottom =0;
        if(parent.getChildCount()>0){
            View lastChild = parent.getChildAt(parent.getChildCount()-1);
            bottom = lastChild.getTop()+lastChild.getHeight()-parent.getHeight();
        }
        return bottom;
    }

    private int getChildTop(CoordinatorLayout parent) {
        int top =0;
        if(parent.getChildCount()>0){
            top = parent.getChildAt(0).getTop();
        }
        return top;
    }

    private int getAllChildHeight(CoordinatorLayout parent) {
        int height=0;
        for(int i=0;i<parent.getChildCount();i++){
            height += parent.getChildAt(i).getHeight();
        }
        return height;
    }

    private void moveOther(CoordinatorLayout parent, StackSlidingLayout child,int offset2) {
        StackSlidingLayout current = child;
        StackSlidingLayout above = getPreviousChild(parent, current);
        while (above != null) {
            int offset = (-above.getHeight()+current.getTop())-above.getTop();
            above.offsetTopAndBottom(offset);
            current = above;
            above = getPreviousChild(parent, current);
        }

        current = child;
        StackSlidingLayout below = getNextChild(parent, current);
        while(below!=null){
            int offset = (current.getHeight()+current.getTop())-below.getTop();
            below.offsetTopAndBottom(offset);
            current = below;
            below = getNextChild(parent, current);
        }
    }

    private int clamp(int i, int minOffset, int maxOffset) {
        if (i > maxOffset) {
            return maxOffset;
        } else if (i < minOffset) {
            return minOffset;
        } else {
            return i;
        }
    }
}
