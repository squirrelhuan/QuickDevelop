package cn.demomaster.huan.quickdeveloplibrary.widget.stackslidingLayout;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

public class StackSlidingBehavior extends CoordinatorLayout.Behavior<StackSlidingLayout> {


    private int mInitialOffset;

 /*   @Override
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
        if (previous == null) {
            int offset = child.getTop();
            QDLogger.d("SlidingBehavior", child.getId() + "offsetTopAndBottom=" + offset + ", top=" + child.getTop() + ",y=" + child.getY());
        } else {
            int offset = previous.getTop() + previous.getHeight();
            child.offsetTopAndBottom(offset);
        }
        mInitialOffset = child.getTop();
        return true;
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

    boolean isVerticalScroll;

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull StackSlidingLayout child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        QDLogger.d("onStartNestedScroll axes=" + axes + ",target=" + target.getClass().getName());
        boolean isVertical = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        isVerticalScroll = isVertical && child == directTargetChild;//返回true则子view不在进行滚动
        return isVerticalScroll;
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout parent, @NonNull StackSlidingLayout child, @NonNull View target, float velocityX, float velocityY) {

        //int shift = scroll(parent, child, (int) velocityY);
        return false;
       // return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout parent, @NonNull StackSlidingLayout child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        QDLogger.d("onNestedFling...");
        //return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
        int shift = scroll(parent, child, consumed ? 0 : (int) velocityY);
        //shiftSlidings(shift, parent, child);
        return false;
    }


    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout parent, @NonNull StackSlidingLayout child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        QDLogger.d("dy=" + dy + ",consumed x=" + consumed[0] + ",y=" + consumed[1]);

        //dy>0上推，dy<0下拉
        boolean isToped = !target.canScrollVertically(-1);//的值表示是否能向下滚动，false表示已经滚动到顶部
        boolean isBottomed = !ViewCompat.canScrollVertically(target, 1);//的值表示是否能向上滚动，false表示已经滚动到底部
        if (child.getTop() == 0 && dy > 0 && !isBottomed) {
            consumed[1] = -scroll(parent, child, dy);
        }
        if (child.getTop() >= 0 && dy > 0 && !isBottomed) {
            scroll(parent, child, dy);
            consumed[1] = dy;
        }

        if (child.getTop() == 0 && dy < 0 && !isToped) {
            consumed[1] = -scroll(parent, child, dy);
        }
        if (child.getTop() < 0 && dy < 0 && !isToped) {
            scroll(parent, child, dy);
            consumed[1] = dy;
        }
        //consumed[1] = dy;
        //shiftSlidings(consumed[1], parent, child);
        //super.onNestedPreScroll(parent, child, target, dx, dy, consumed, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout parent, @NonNull StackSlidingLayout child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        QDLogger.d("onNestedScroll...");
        int shift = scroll(parent, child, dyUnconsumed);
        consumed[1] = shift;
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

    private int scroll(CoordinatorLayout parent, StackSlidingLayout child, int dy) {
        //1.k控制自己的移动
        int initialOffset = child.getTop();
        View view = getPreviousChild(parent, child);
        int heightAll = getAllChildHeight(parent);
        StackSlidingLayout firstchild = getFirstChild(parent);
        StackSlidingLayout lastchild = getLastChild(parent);

        //dy>0上推，dy<0下拉
        int top = firstchild.getTop();
        int bottom = lastchild.getBottom() - parent.getHeight();
        QDLogger.d("height=" + parent.getHeight() + ",lastchild.getBottom()=" + lastchild.getBottom() + ",firstchild.getTop()=" + firstchild.getTop());
        QDLogger.d("dy=" + dy + ",min=" + top + ",max=" + top + ",initialOffset - dy=" + (initialOffset - dy));
        int offset = 0;//= clamp(initialOffset - dy,top ,top)-initialOffset;
        //offset = 目标值-当前值
        if (dy > 0) {
            offset = -Math.min(dy, bottom);
        } else if (dy < 0) {
            offset = -Math.max(dy, top);
        }
        child.offsetTopAndBottom(offset);

        moveOther(parent, child, offset);
        return dy - offset;//滑动方向
    }

    private StackSlidingLayout getFirstChild(CoordinatorLayout parent) {
        StackSlidingLayout firstchild = null;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view instanceof StackSlidingLayout) {
                firstchild = (StackSlidingLayout) view;
                return firstchild;
            }
        }
        return firstchild;
    }

    private StackSlidingLayout getLastChild(CoordinatorLayout parent) {
        StackSlidingLayout lastchild = null;
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            View view = parent.getChildAt(i);
            if (view instanceof StackSlidingLayout) {
                lastchild = (StackSlidingLayout) view;
                return lastchild;
            }
        }
        return lastchild;
    }

    private int getAllChildHeight(CoordinatorLayout parent) {
        int height = 0;
        for (int i = 0; i < parent.getChildCount(); i++) {
            height += parent.getChildAt(i).getHeight();
        }
        return height;
    }

    private void moveOther(CoordinatorLayout parent, StackSlidingLayout child, int offset) {
        StackSlidingLayout current = child;
        StackSlidingLayout above = getPreviousChild(parent, current);
        while (above != null) {
            int offset_c = current.getTop() - above.getHeight() - above.getTop();
            above.offsetTopAndBottom(offset_c);
            current = above;
            above = getPreviousChild(parent, current);
        }

        current = child;
        StackSlidingLayout below = getNextChild(parent, current);
        while (below != null) {
            int offset_c = current.getTop() + current.getHeight() - below.getTop();
            below.offsetTopAndBottom(offset_c);
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
