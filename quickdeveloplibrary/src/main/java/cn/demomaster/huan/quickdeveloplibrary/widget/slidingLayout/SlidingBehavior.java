package cn.demomaster.huan.quickdeveloplibrary.widget.slidingLayout;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

public class SlidingBehavior extends CoordinatorLayout.Behavior<SlidingLayout> {

    private int mInitialOffset;

    @Override
    public boolean onMeasureChild(@NonNull CoordinatorLayout parent, @NonNull SlidingLayout child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        QDLogger.d("onMeasureChild");
        int offset = getChildMeasureOffset(parent, child);
        int height = View.MeasureSpec.getSize(parentHeightMeasureSpec) - offset;
        child.measure(parentWidthMeasureSpec, View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        return true;
    }

    private int getChildMeasureOffset(CoordinatorLayout parent, SlidingLayout child) {
        QDLogger.d("getChildMeasureOffset");
        int offset = 0;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view != child && view instanceof SlidingLayout) {
                offset += ((SlidingLayout) view).getHeaderViewHeight();
            }
        }
        return offset;
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull SlidingLayout child, int layoutDirection) {
        QDLogger.d("onLayoutChild");
        parent.onLayoutChild(child, layoutDirection);
        SlidingLayout previous = getPreviousChild(parent, child);
        if (previous != null) {
            int offset = previous.getTop() + previous.getHeaderViewHeight();
            child.offsetTopAndBottom(offset);
            //CoordinatorLayout.LayoutParams layoutParams_c = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            //layoutParams_c.topMargin = offset;
            //child.setLayoutParams(layoutParams_c);
            //child.setY();
            QDLogger.d("SlidingBehavior", child.getId() + "offsetTopAndBottom=" + offset+", top="+ child.getTop()+",y="+child.getY());
        }
        mInitialOffset = child.getTop();
        QDLogger.d("SlidingBehavior", child.getId() + "mInitialOffset=" + mInitialOffset);
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    private SlidingLayout getPreviousChild(CoordinatorLayout parent, SlidingLayout child) {
        int cartindex = parent.indexOfChild(child);
        for (int i = cartindex - 1; i >= 0; i--) {
            View v = parent.getChildAt(i);
            if (v instanceof SlidingLayout) {
                return (SlidingLayout) v;
            }
        }
        return null;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull SlidingLayout child, @NonNull View directTargetChild, @NonNull View target, int axes) {
        QDLogger.d("onStartNestedScroll2");
        boolean isVertical = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        return isVertical && child == directTargetChild;
        // return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull SlidingLayout child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        QDLogger.d("onStartNestedScroll");
        boolean isVertical = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        return isVertical && child == directTargetChild;
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout parent, @NonNull SlidingLayout child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        //return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
        int shift = scroll(child, consumed?0:(int) velocityY, mInitialOffset, mInitialOffset + child.getHeight() - child.getHeaderViewHeight());
        shiftSlidings(shift, parent, child);
        return false;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout parent, @NonNull SlidingLayout child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        consumed[1] = scroll(child, dy, mInitialOffset, mInitialOffset + child.getHeight() - child.getHeaderViewHeight());
        shiftSlidings(consumed[1], parent, child);
       // super.onNestedPreScroll(parent, child, target, dx, dy, consumed, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout parent, @NonNull SlidingLayout child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        int shift = scroll(child, dyUnconsumed, mInitialOffset, mInitialOffset + child.getHeight() - child.getHeaderViewHeight());
        shiftSlidings(shift, parent, child);
    }

    private void shiftSlidings(int shift, CoordinatorLayout parent, SlidingLayout child) {
        if (shift == 0) {
            return;
        }
        if (shift > 0) {//往上推
            SlidingLayout current = child;
            SlidingLayout card = getPreviousChild(parent, current);
            while (card != null) {
                int offset = getHeaderOverlap(card,current);
                if (offset > 0){
                    card.offsetTopAndBottom(-offset);
                    //CoordinatorLayout.LayoutParams layoutParams_c = (CoordinatorLayout.LayoutParams) card.getLayoutParams();
                    //layoutParams_c.topMargin = -offset;
                }
                current = card;
                card = getPreviousChild(parent, current);
            }
        } else {//往下推
            SlidingLayout current = child;
            SlidingLayout card = getNextChild(parent, current);
            while (card != null) {
                int offset = getHeaderOverlap(current, card);
                if (offset > 0) {
                    card.offsetTopAndBottom(offset);
                    //CoordinatorLayout.LayoutParams layoutParams_c = (CoordinatorLayout.LayoutParams) card.getLayoutParams();
                    //layoutParams_c.topMargin = offset;
                }
                current = card;
                card = getNextChild(parent, current);
            }
        }
    }

    private int getHeaderOverlap(SlidingLayout above, SlidingLayout below) {
        return above.getTop() + above.getHeaderViewHeight() - below.getTop();
    }

    private SlidingLayout getNextChild(CoordinatorLayout parent, SlidingLayout child) {
        int cartindex = parent.indexOfChild(child);
        for (int i = cartindex + 1; i < parent.getChildCount(); i++) {
            View v = parent.getChildAt(i);
            if (v instanceof SlidingLayout) {
                return (SlidingLayout) v;
            }
        }
        return null;
    }

    private int scroll(SlidingLayout child, int dy, int minOffset, int maxOffset) {
        //1.k控制自己的移动
        int initialOffset = child.getTop();
        int offset = clamp(initialOffset - dy, minOffset, maxOffset)-initialOffset;
        child.offsetTopAndBottom(offset);
        //CoordinatorLayout.LayoutParams layoutParams_c = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        //layoutParams_c.topMargin = offset;
        return -offset;//滑动方向
        //2,控制上边和下边child的移动
        // super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
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
