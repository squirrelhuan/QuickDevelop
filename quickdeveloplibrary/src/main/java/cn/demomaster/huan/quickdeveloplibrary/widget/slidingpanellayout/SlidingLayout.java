package cn.demomaster.huan.quickdeveloplibrary.widget.slidingpanellayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.core.view.ViewCompat;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.qdlogger_library.QDLogger;

public class SlidingLayout extends LinearLayout {


    /**
     * Default initial state for the component
     * 默认状态
     */
    private static PanelState DEFAULT_SLIDE_STATE = PanelState.COLLAPSED;
    private PanelState mSlideState = DEFAULT_SLIDE_STATE;
    private Scroller mScroller;

    public SlidingLayout(Context context) {
        super(context);
        init(null);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    int scroll_marginTop;
    int scroll_marginBottom;

    /**
     * Default Minimum velocity that will be detected as a fling
     * 将被检测为投掷的默认最小速度
     */
    private static final int DEFAULT_MIN_FLING_VELOCITY = 1000; // dips per second
    /**
     * Minimum velocity that will be detected as a fling
     * 将被检测为抛掷的最小速度
     */
    private int mMinFlingVelocity = DEFAULT_MIN_FLING_VELOCITY;

    private void init(AttributeSet attrs) {
        Interpolator scrollerInterpolator = null;
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SlidingLayout);

            if (ta != null) {
                scroll_marginTop = ta.getDimensionPixelSize(R.styleable.SlidingLayout_scroll_marginTop, 0);
                scroll_marginBottom = ta.getDimensionPixelSize(R.styleable.SlidingLayout_scroll_marginBottom, 0);
                mSlideState = PanelState.values()[ta.getInt(R.styleable.SlidingLayout_initialState, DEFAULT_SLIDE_STATE.ordinal())];
                int interpolatorResId = ta.getResourceId(R.styleable.SlidingUpPanelLayout_umanoScrollInterpolator, -1);
                if (interpolatorResId != -1) {
                    scrollerInterpolator = AnimationUtils.loadInterpolator(getContext(), interpolatorResId);
                }else {
                    scrollerInterpolator = new LinearInterpolator();
                }
                ta.recycle();
            }
        }
        final float density = getContext().getResources().getDisplayMetrics().density;
        mScroller = new Scroller(getContext());
        mDragHelper = ViewDragHelper.create(this, 1f, scrollerInterpolator, new DragHelperCallback());
        mDragHelper.setMinVelocity(mMinFlingVelocity * density);
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == getChildAt(0);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            onScrollChanged();
        }
    }

    private void onScrollChanged() {
        float progress = 1-(scrollView.getTop()-minMarginTop)/((maxMarginTop-minMarginTop)*1f);
        if(mScrollListener!=null){
            mScrollListener.onScroll(scrollView,progress);
        }
        //QDLogger.e("onScrollChanged=" + progress);
        if(progress==1){
            setSlideState(PanelState.EXPANDED);
        }else if(progress==0){
            setSlideState(PanelState.COLLAPSED);
        }else {
            setSlideState(PanelState.DRAGGING);
        }
    }

    public PanelState getSlideState() {
        return mSlideState;
    }

    //设置状态
    public void setSlideState(PanelState slideState) {
        PanelState previousState=mSlideState;

        if (slideState == PanelState.COLLAPSED){
            if(scrollView.getTop() == maxMarginTop){
                this.mSlideState = slideState;
            }else {
                QDLogger.println("slideState="+mSlideState+", top=" + scrollView.getTop()+",maxMarginTop="+maxMarginTop);
                mSlideState = PanelState.DRAGGING;
                smoothTo(maxMarginTop);
            }
        }else if (slideState == PanelState.EXPANDED){//展开
            if(scrollView.getTop() == minMarginTop){
                this.mSlideState = slideState;
            }else {
                QDLogger.println("slideState="+mSlideState+", top=" + scrollView.getTop()+",maxMarginTop="+maxMarginTop);
                mSlideState = PanelState.DRAGGING;
                smoothTo(minMarginTop);
            }
        }

        boolean isChanged = previousState!=mSlideState;
        if(isChanged&&mScrollListener!=null){
            mScrollListener.onPanelStateChanged(scrollView,previousState,mSlideState);
        }
    }

    View scrollView;
    private ViewDragHelper mDragHelper;

    /**
     * Smoothly animate mDraggingPane to the target X position within its range.
     *
     * @param offsetTop position to animate to
     */
    boolean smoothTo(int offsetTop) {
        if (mDragHelper.smoothSlideViewTo(scrollView, scrollView.getLeft(), offsetTop)) {
            QDLogger.d("smoothSlideTo panelTop=" + offsetTop + "," + scrollView.getTop());
            isFling = false;
            isAutoScrolling = true;
            invalidate();
            return true;
        } else {
            QDLogger.e("smoothTo="+offsetTop);
            scrollView.setTop(offsetTop);
        }
        return false;
    }

    public enum PanelState {
        EXPANDED,//展开
        COLLAPSED,//折叠的
        ANCHORED,//锚定
        HIDDEN,//隐藏
        DRAGGING//拖拽
    }

    ScrollListener mScrollListener;

    public void setScrollListener(ScrollListener scrollListener) {
        this.mScrollListener = scrollListener;
    }

    @Override
    protected void onFinishInflate() {
        //QDLogger.e("onFinishInflate");
        super.onFinishInflate();
        scrollView = getChildAt(0);
    }
    //设置子控件top实现wei'yi
    void setScrollTop(int top) {
        scrollView.setTop(top);
        onScrollChanged();
    }

    private void initState() {
        switch (mSlideState) {
            case EXPANDED:
                setScrollTop(minMarginTop);
                break;
            case COLLAPSED:
                setScrollTop(maxMarginTop);
                break;
        }
    }

    boolean isFirstInflate = true;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        minMarginTop = scroll_marginTop;
        maxMarginTop = getMeasuredHeight() - scroll_marginBottom;
        if (isFirstInflate) {
            isFirstInflate = false;
            initState();
        }
        //QDLogger.e("minMarginTop="+minMarginTop+",maxMarginTop="+maxMarginTop);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final View child = getChildAt(0);
        child.layout(l, child.getTop(), r, child.getTop() + b - t);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //QDLogger.e("dispatchTouchEvent=" + ev.getAction());
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            autoScroll();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        isFling = false;
        isAutoScrolling = false;
        return super.onInterceptTouchEvent(ev);
    }

    ///////////////////////////////////////////////
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        //return super.onStartNestedScroll(child, target, nestedScrollAxes);
        return true;
    }

    //移动edv_content
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (scrollView != null) {
            offset(dy, consumed);
        }
    }

    private void offset(int dy, int[] consumed) {
        float currentY = scrollView.getTop();//位置

        int dy2 = 0;
        //往上滑,y的边界为titleHeight
        if (dy > 0) {
            dy2 = Math.min(dy, Math.max(0, (int) (currentY - minMarginTop)));
        } else if (dy < 0) {//往下滑,y的边界为titleHeight + headerHeight
            if (scrollView.canScrollVertically(dy)) {//当target能向下滑时
                return;
            }
            dy2 = Math.max(dy, Math.min(0, (int) (currentY - maxMarginTop)));
        }

        //第二个参数为正代表向下，为负代表向上
        //ViewCompat.offsetTopAndBottom(scrollView, -dy2);
        setScrollTop(scrollView.getTop() - dy2);
        consumed[0] = 0;
        consumed[1] = dy2;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (!isAdsorbent) {//是否吸附
            if (scrollView != null) {
                float top = scrollView.getTop();
                if (velocityY > 0) {//向上惯性滚动
                    if (top > minMarginTop) {
                        fling(velocityY);
                        return true;
                    }
                } else if (velocityY < 0) {//向下惯性滚动
                    if (scrollView.canScrollVertically(-1)) {//当target能向下滑时
                        return false;
                    } else {
                        if (top < maxMarginTop) {
                            fling(velocityY);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    boolean isAdsorbent = true;//吸附

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        //QDLogger.d("onNestedFling velocityY=" + velocityY + ",consumed=" + consumed);
        if (!isAdsorbent) {//是否吸附
            fling(velocityY);
        } else {
            autoScroll();
        }
        return true;
    }

    /**
     * 自动吸附最近的边界
     */
    private void autoScroll() {
        if (scrollView.getTop() != minMarginTop && scrollView.getTop() != maxMarginTop) {
            int top = 0;
            if (Math.abs(scrollView.getTop() - minMarginTop) > Math.abs(scrollView.getTop() - maxMarginTop)) {
                top = maxMarginTop;
            } else {
                top = minMarginTop;
            }
            //QDLogger.e("autoScroll=" + top);
            smoothTo(top);
        }
    }

    int maxMarginTop = 1800;
    int minMarginTop = 300;
    boolean isFling = false;
    boolean isAutoScrolling = false;

    //惯性滚动
    public void fling(float velocityY) {
        isFling = true;
        isAutoScrolling = false;
        //mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), 0, 1000, 2000);
        mScroller.fling(0, (int) scrollView.getTop(), 0, (int) -velocityY / 2, 0, 0, minMarginTop, maxMarginTop);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //QDLogger.d("computeScroll isFling=" + isFling + ",isAutoScrolling=" + isAutoScrolling);
        if (isFling) {
            if (mScroller.computeScrollOffset()) {
                //offset(-dy, new int[2]);
                setScrollTop(mScroller.getCurrY());
                invalidate();
            }
        }
        if (isAutoScrolling) {
            if (mDragHelper.continueSettling(true)) {
                requestLayout();
            }
        }
    }


    /**
     * Listener for monitoring events about sliding panes.
     * 用于监视有关滑动窗格的事件的侦听器。
     */
    public interface ScrollListener {
        /**
         * Called when a sliding pane's position changes.
         *
         * @param panel    The child view that was moved
         * @param progress The new offset of this sliding pane within its range, from 0-1
         */
        void onScroll(View panel, float progress);

        /**
         * Called when a sliding panel state changes
         *
         * @param panel The child view that was slid to an collapsed position
         */
        void onPanelStateChanged(View panel, PanelState previousState, PanelState newState);
    }
}
