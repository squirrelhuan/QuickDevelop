package cn.demomaster.huan.quickdeveloplibrary.widget.stackslidingLayout;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.VelocityTrackerCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import cn.demomaster.qdlogger_library.QDLogger;

public class MultiRecycleBehavior extends CoordinatorLayout.Behavior<MultiRecycleContainer> {

    CoordinatorLayout parent;

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull MultiRecycleContainer child, int layoutDirection) {
        QDLogger.println("onLayoutChild");
        parent.onLayoutChild(child, layoutDirection);
        //获取上一个MultiRecycleContainer容器
        MultiRecycleContainer previous = getPreviousChild(parent, child);
        if (previous == null) {//为空说明child的position=0
            int offset = child.getTop();
            QDLogger.println("SlidingBehavior", child.getId() + "offsetTopAndBottom=" + offset + ", top=" + child.getTop() + ",y=" + child.getY());
        } else {
            //获取上一个MultiRecycleContainer容器，来确定当前child的位置 (当前child的位置为，上一个的top+上一个的height)
            int offset = previous.getTop() + previous.getHeight();
            child.offsetTopAndBottom(offset);
        }
        return true;
    }

    /**
     * 获取MultiRecycleContainer列表中的上一个
     *
     * @param parent CoordinatorLayout父容器
     * @param child  当前MultiRecycleContainer
     * @return
     */
    private MultiRecycleContainer getPreviousChild(CoordinatorLayout parent, MultiRecycleContainer child) {
        int cartindex = parent.indexOfChild(child);
        for (int i = cartindex - 1; i >= 0; i--) {
            View v = parent.getChildAt(i);
            if (v instanceof MultiRecycleContainer) {
                return (MultiRecycleContainer) v;
            }
        }
        return null;
    }

    boolean isVerticalScroll;//是否是垂直滚动

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull MultiRecycleContainer child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        QDLogger.d("onStartNestedScroll axes=" + axes + ",target=" + target.getClass().getName());
        this.parent = coordinatorLayout;
        if (!isInital) {//初始化惯性滚动
            init(parent);
        }
        boolean isVertical = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        isVerticalScroll = isVertical && child == directTargetChild;//返回true则子view不在进行滚动
        return isVerticalScroll;
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout parent, @NonNull MultiRecycleContainer child, @NonNull View target, float velocityX, float velocityY) {
        //int shift = scroll(parent, child, (int) velocityY);
        // return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
        return false;
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout parent, @NonNull MultiRecycleContainer child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        QDLogger.d("onNestedFling...");
        //return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
        scroll(parent, child, consumed ? 0 : (int) velocityY);
        scrollParent(parent, child, null, consumed ? 0 : (int) velocityY);
        return false;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout parent, @NonNull MultiRecycleContainer child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        QDLogger.d("dy=" + dy + ",consumed x=" + consumed[0] + ",y=" + consumed[1]);

        //dy>0上推，dy<0下拉
        boolean canScrollDown = target.canScrollVertically(-1);//的值表示是否能向下滚动
        boolean isToped = !canScrollDown;//表示是否已经滚动到顶部
        boolean canScrollUp = ViewCompat.canScrollVertically(target, 1);//的值表示是否能向上滚动
        boolean isBottomed = !canScrollUp;//表示已经滚动到底部
        QDLogger.d("isToped=" + isToped + ",isBottomed=" + isBottomed + ",child.getTop()=" + child.getTop());

        if (child.getTop() == 0 && ((dy > 0 && canScrollUp) || (dy < 0 && canScrollDown))) {
            consumed[1] = scrollChild(parent, child, target, dy);
        } else {
            consumed[1] = scrollParent(parent, child, target, dy);
        }
        //super.onNestedPreScroll(parent, child, target, dx, dy, consumed, type);
    }

    /**
     * 优先父view 消耗
     *
     * @param parent
     * @param target
     * @param dy
     * @return
     */
    private int scrollParent(CoordinatorLayout parent, MultiRecycleContainer child, View target, int dy) {
        int py = 0;
        QDLogger.d("获取下一个可滚动的视图");
        //1.获取下一个可滚动的视图
        if (dy > 0) {//上推
            QDLogger.d("上推");
            MultiRecycleContainer upper = getUpperChild(parent);
            QDLogger.d("获取下一个可上推的视图:" + upper);
            if (upper == null) {
                py = dy;
            } else {
                QDLogger.d("获取下一个可上推的视图:" + upper + ",dy=" + dy + ",upper.getTop()=" + upper.getTop());
                py = Math.min(upper.getTop(), dy);
            }
        } else {//下拉
            QDLogger.d("下拉");
            MultiRecycleContainer downer = getDownerChild(parent);//下一个
            //QDLogger.d("获取下一个可下拉的视图:" + downer);
            if (downer == null) {
                py = dy;
            } else {
                QDLogger.d("获取下一个可下拉的视图dy=" + dy + ",downer.getTop()=" + downer.getTop());
                py = Math.max(downer.getTop(), dy);
            }
        }

        //2.scrollPrent
        scroll(parent, child, py);
        return py;
    }

    /**
     * 获取下一个即将要出现的可下拉滚动的控件
     *
     * @param parent
     * @return
     */
    private MultiRecycleContainer getDownerChild(CoordinatorLayout parent) {
        MultiRecycleContainer current = getCurrentChild(parent);
        int index = parent.indexOfChild(current);
        for (int i = index; i > 0; i--) {
            View v = parent.getChildAt(i);
            if (v instanceof MultiRecycleContainer) {
                boolean canScrollDown = (getChildRecyclerView((ViewGroup) v)).canScrollVertically(-1);//的值表示是否能向下滚动
                if (canScrollDown) {//下拉
                    return (MultiRecycleContainer) v;
                }
            }
        }
        return null;
    }

    /**
     * 获取下一个即将要出现的可上推滚动的控件
     *
     * @param parent
     * @return
     */
    private MultiRecycleContainer getUpperChild(CoordinatorLayout parent) {
        MultiRecycleContainer current = getCurrentChild(parent);
        int index = parent.indexOfChild(current);
        W:
        for (int i = index; i < parent.getChildCount(); i++) {
            View v = parent.getChildAt(i);
            if (v instanceof MultiRecycleContainer) {
                if (getChildRecyclerView((ViewGroup) v) == null) continue W;
                boolean canScrollUp = ViewCompat.canScrollVertically(getChildRecyclerView((ViewGroup) v), 1);//的值表示是否能向上滚动
                if (canScrollUp) {//可上推
                    return (MultiRecycleContainer) v;
                }
            }
        }
        return null;
    }

    /**
     * 获取child中的第一个recycleView，所以每个StackSlidingLayout中只能放一个recycleView
     *
     * @param v
     * @return
     */
    private RecyclerView getChildRecyclerView(ViewGroup v) {
        for (int i = 0; i < v.getChildCount(); i++) {
            if (v.getChildAt(i) instanceof RecyclerView) {
                return (RecyclerView) v.getChildAt(i);
            }
            if (v.getChildAt(i) instanceof ViewGroup) {
                RecyclerView recyclerView = findRecycleView((ViewGroup) v.getChildAt(i));
                if (recyclerView != null) {
                    return recyclerView;
                }
            }
        }
        return null;
    }

    /**
     * 获取当前子容器中的recycle,注意每个容器中只存放一个recyclerView
     *
     * @param viewGroup
     * @return
     */
    private RecyclerView findRecycleView(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof RecyclerView) {
                return (RecyclerView) viewGroup.getChildAt(i);
            }
            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                RecyclerView recyclerView = findRecycleView((ViewGroup) viewGroup.getChildAt(i));
                if (recyclerView != null) {
                    return recyclerView;
                }
            }
        }
        return null;
    }

    /**
     * 优先子view滚动
     *
     * @param parent
     * @param target
     * @param dy
     * @return
     */
    private int scrollChild(CoordinatorLayout parent, MultiRecycleContainer child, View target, int dy) {
        //1.优先parent滑动
        //2.寻找到可以滚动的下一个child
        return 0;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout parent, @NonNull MultiRecycleContainer child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        QDLogger.d("onNestedScroll...");

        //dy>0上推，dy<0下拉
        boolean canScrollDown = target.canScrollVertically(-1);//的值表示是否能向下滚动
        boolean isToped = !canScrollDown;//表示是否已经滚动到顶部
        boolean canScrollUp = ViewCompat.canScrollVertically(target, 1);//的值表示是否能向上滚动
        boolean isBottomed = !canScrollUp;//表示已经滚动到底部
        QDLogger.d("isToped=" + isToped + ",isBottomed=" + isBottomed + ",child.getTop()=" + child.getTop());

        if (child.getTop() == 0 && ((dyUnconsumed > 0 && canScrollUp) || (dyUnconsumed < 0 && canScrollDown))) {
            consumed[1] = scrollChild(parent, child, target, dyUnconsumed);
        } else {
            consumed[1] = scrollParent(parent, child, target, dyUnconsumed);
        }
    }

    private int scroll(CoordinatorLayout parent, MultiRecycleContainer child, int dy) {
        //1.k控制自己的移动
        int initialOffset = child.getTop();
        MultiRecycleContainer firstchild = getFirstChild(parent);
        MultiRecycleContainer lastchild = getLastChild(parent);

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

    /**
     * 获取下一个MultiRecycleContainer
     *
     * @param parent
     * @param child
     * @return
     */
    private MultiRecycleContainer getNextChild(CoordinatorLayout parent, MultiRecycleContainer child) {
        int cartindex = parent.indexOfChild(child);
        for (int i = cartindex + 1; i < parent.getChildCount(); i++) {
            View v = parent.getChildAt(i);
            if (v instanceof MultiRecycleContainer) {
                return (MultiRecycleContainer) v;
            }
        }
        return null;
    }

    /**
     * 获取第一个MultiRecycleContainer
     *
     * @param parent
     * @return
     */
    private MultiRecycleContainer getFirstChild(CoordinatorLayout parent) {
        MultiRecycleContainer firstchild = null;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view instanceof MultiRecycleContainer) {
                firstchild = (MultiRecycleContainer) view;
                return firstchild;
            }
        }
        return firstchild;
    }

    /**
     * 获取当前MultiRecycleContainer
     *
     * @param parent
     * @return
     */
    private MultiRecycleContainer getCurrentChild(CoordinatorLayout parent) {
        MultiRecycleContainer current = null;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view.getTop() >= 0) {
                return (MultiRecycleContainer) view;
            }
        }
        return current;
    }

    /**
     * 获取最后一个MultiRecycleContainer
     *
     * @param parent
     * @return
     */
    private MultiRecycleContainer getLastChild(CoordinatorLayout parent) {
        MultiRecycleContainer lastchild = null;
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            View view = parent.getChildAt(i);
            if (view instanceof MultiRecycleContainer) {
                lastchild = (MultiRecycleContainer) view;
                return lastchild;
            }
        }
        return lastchild;
    }

    /**
     * 获取CoordinatorLayout所有子view视图高度
     *
     * @param parent
     * @return
     */
    private int getAllChildHeight(CoordinatorLayout parent) {
        int height = 0;
        for (int i = 0; i < parent.getChildCount(); i++) {
            height += parent.getChildAt(i).getHeight();
        }
        return height;
    }

    /**
     * 移动其他平级的MultiRecycleContainer
     *
     * @param parent
     * @param child
     * @param offset
     */
    private void moveOther(CoordinatorLayout parent, MultiRecycleContainer child, int offset) {
        MultiRecycleContainer current = child;
        MultiRecycleContainer above = getPreviousChild(parent, current);
        while (above != null) {
            int offset_c = current.getTop() - above.getHeight() - above.getTop();
            above.offsetTopAndBottom(offset_c);
            current = above;
            above = getPreviousChild(parent, current);
        }

        current = child;
        MultiRecycleContainer below = getNextChild(parent, current);
        while (below != null) {
            int offset_c = current.getTop() + current.getHeight() - below.getTop();
            below.offsetTopAndBottom(offset_c);
            current = below;
            below = getNextChild(parent, current);
        }
    }

    /****************   以下参考网上的惯性滚动   效果还不是很好     ****************************************************************/
    private boolean isInital;

    private void init(View view) {
        isInital = true;
        this.mContext = view.getContext();
        mViewFlinger = new ViewFlinger(mContext, view);
        final ViewConfiguration vc = ViewConfiguration.get(mContext);
        mTouchSlop = vc.getScaledTouchSlop();
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        DisplayMetrics metric = mContext.getResources().getDisplayMetrics();
        SCREEN_WIDTH = metric.widthPixels;
        SCREEN_HEIGHT = metric.heightPixels;
    }

    private Context mContext;
    private int SCREEN_WIDTH = 0;
    private int SCREEN_HEIGHT = 0;
    private int mWidth = 0;
    private int mHeight = 0;
    private static final int INVALID_POINTER = -1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_SETTLING = 2;

    private int mScrollState = SCROLL_STATE_IDLE;
    private int mScrollPointerId = INVALID_POINTER;
    private int mLastTouchY;
    private int mTouchSlop;
    private int mMinFlingVelocity;
    private int mMaxFlingVelocity;
    private ViewFlinger mViewFlinger;
    private VelocityTracker mVelocityTracker;//滑动速度跟踪器

    @Override
    public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull MultiRecycleContainer child, @NonNull MotionEvent event) {

        if (!isInital) {
            init(parent);
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        boolean eventAddedToVelocityTracker = false;
        final int action = MotionEventCompat.getActionMasked(event);
        final int actionIndex = MotionEventCompat.getActionIndex(event);
        final MotionEvent vtev = MotionEvent.obtain(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                setScrollState(SCROLL_STATE_IDLE);
                mScrollPointerId = event.getPointerId(0);
                mLastTouchY = (int) (event.getY() + 0.5f);
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                mScrollPointerId = event.getPointerId(actionIndex);
                mLastTouchY = (int) (event.getY(actionIndex) + 0.5f);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final int index = event.findPointerIndex(mScrollPointerId);
                if (index < 0) {
                    Log.e("zhufeng", "Error processing scroll; pointer index for id " + mScrollPointerId + " not found. Did any MotionEvents get skipped?");
                    return false;
                }

                final int y = (int) (event.getY(index) + 0.5f);
                int dy = mLastTouchY - y;

                if (mScrollState != SCROLL_STATE_DRAGGING) {
                    boolean startScroll = false;

                    if (Math.abs(dy) > mTouchSlop) {
                        if (dy > 0) {
                            dy -= mTouchSlop;
                        } else {
                            dy += mTouchSlop;
                        }
                        startScroll = true;
                    }
                    if (startScroll) {
                        setScrollState(SCROLL_STATE_DRAGGING);
                    }
                }

                if (mScrollState == SCROLL_STATE_DRAGGING) {
                    mLastTouchY = y;
                    constrainScrollBy(dy);
                }
                break;
            }
            case MotionEventCompat.ACTION_POINTER_UP: {
                if (event.getPointerId(actionIndex) == mScrollPointerId) {
                    // Pick a new pointer to pick up the slack.
                    final int newIndex = actionIndex == 0 ? 1 : 0;
                    mScrollPointerId = event.getPointerId(newIndex);
                    mLastTouchY = (int) (event.getY(newIndex) + 0.5f);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                mVelocityTracker.addMovement(vtev);
                eventAddedToVelocityTracker = true;
                mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
                float yVelocity = -VelocityTrackerCompat.getYVelocity(mVelocityTracker, mScrollPointerId);
                Log.i("zhufeng", "速度取值：" + yVelocity);
                if (Math.abs(yVelocity) < mMinFlingVelocity) {
                    yVelocity = 0F;
                } else {
                    yVelocity = Math.max(-mMaxFlingVelocity, Math.min(yVelocity, mMaxFlingVelocity));
                }
                if (yVelocity != 0) {
                    mViewFlinger.fling((int) yVelocity);
                } else {
                    setScrollState(SCROLL_STATE_IDLE);
                }
                resetTouch();
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                resetTouch();
                break;
            }
        }
        if (!eventAddedToVelocityTracker) {
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return true;
        //return super.onTouchEvent(parent, child, ev);
    }

    private void resetTouch() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
        }
    }

    private void setScrollState(int state) {
        if (state == mScrollState) {
            return;
        }
        mScrollState = state;
        if (state != SCROLL_STATE_SETTLING) {
            mViewFlinger.stop();
        }
    }

    private class ViewFlinger implements Runnable {
        private int mLastFlingY = 0;
        private OverScroller mScroller;
        private boolean mEatRunOnAnimationRequest = false;
        private boolean mReSchedulePostAnimationCallback = false;
        private View mView;

        public ViewFlinger(Context context, View view) {
            this.mView = view;
            mScroller = new OverScroller(context, sQuinticInterpolator);
        }

        @Override
        public void run() {
            disableRunOnAnimationRequests();
            final OverScroller scroller = mScroller;
            if (scroller.computeScrollOffset()) {
                final int y = scroller.getCurrY();
                int dy = y - mLastFlingY;
                mLastFlingY = y;
                constrainScrollBy(dy);
                postOnAnimation();
            }
            enableRunOnAnimationRequests();
        }

        public void fling(int velocityY) {
            mLastFlingY = 0;
            setScrollState(SCROLL_STATE_SETTLING);
            mScroller.fling(0, 0, 0, velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postOnAnimation();
        }

        public void stop() {
            mView.removeCallbacks(this);
            mScroller.abortAnimation();
        }

        private void disableRunOnAnimationRequests() {
            mReSchedulePostAnimationCallback = false;
            mEatRunOnAnimationRequest = true;
        }

        private void enableRunOnAnimationRequests() {
            mEatRunOnAnimationRequest = false;
            if (mReSchedulePostAnimationCallback) {
                postOnAnimation();
            }
        }

        void postOnAnimation() {
            if (mEatRunOnAnimationRequest) {
                mReSchedulePostAnimationCallback = true;
            } else {
                mView.removeCallbacks(this);
                ViewCompat.postOnAnimation(mView, this);
            }
        }

    }

    private void constrainScrollBy(int dy) {
       /* Rect viewport = new Rect();
        getGlobalVisibleRect(viewport);
        int height = viewport.height();
        int width = viewport.width();*/
        if (parent == null) return;
        int height = getAllChildHeight(parent);
        int width = parent.getWidth();
        //QDLogger.d("height=" + height + ",width=" + width);

        int scrollY = getFirstChild(parent).getTop();
        MultiRecycleContainer child = getCurrentChild(parent);

        //下边界
        if (mHeight - scrollY - dy < height) {
            dy = mHeight - scrollY - height;
        }
        //上边界
        if (scrollY + dy < 0) {
            dy = -scrollY;
        }
        // scroll(parent, child, dy);
        scrollParent(parent, child, null, dy);
    }

    //f(x) = (x-1)^5 + 1
    private static final Interpolator sQuinticInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };
}
