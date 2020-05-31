package cn.demomaster.huan.quickdeveloplibrary.widget.pushcardlayout;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;

import androidx.annotation.NonNull;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ListViewCompat;

import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.OverScroller;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadingTengxuntvView;


/**
 * @author:Squirrel桓
 * @time:2018/8/28
 */
public class PushCardLayout extends FrameLayout implements NestedScrollingParent2 {

    private StateType cardState = StateType.idle;


    public enum StateType {
        idle,//空闲
        isToping,//顶部下拉状态
        isToped,//顶部下拉悬停状态
        isBottoming,//底部下拉状态
        isBottomed//底部下拉悬停状态
    }

    public void setCardState(StateType cardState) {
        this.cardState = cardState;
        //Log.i("cardState", "this.cardState=" + cardState);
    }

    private int mTouchSlop;// 系统默认的最小滚动距离
    //private final NestedScrollingChildHelper mNestedScrollingChildHelper;
    private VelocityTracker mVelocityTracker;

    /**
     * 初始化速度计算器
     */
    private void initVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    //滚动内容区
    private View contentLayout;
    //底部布局，顶部布局
    private LinearLayout footerLayout, headerLayout;

    //数据加载监听
    private PushCardDatalistener dataListener;
    //动画加载监听
    private PushCardAnimationListener animationListener;

    public void setDataListener(PushCardDatalistener dataListener) {
        this.dataListener = dataListener;
    }

    public void setAnimationListener(PushCardAnimationListener animationListener) {
        this.animationListener = animationListener;
    }

    /**
     * 设置顶部view
     *
     * @param headerLayoutView
     */
    public void addHeaderView(View headerLayoutView) {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        addHeaderView(headerLayoutView, layoutParams);
    }

    public void addHeaderView(View headerLayoutView, LayoutParams layoutParams) {
        headerLayout.removeAllViews();
        headerLayout.addView(headerLayoutView, layoutParams);
    }


    /**
     * 设置底部view
     *
     * @param footerLayoutView
     */
    public void addFooterView(View footerLayoutView) {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        addFooterView(footerLayoutView, layoutParams);
    }

    public void addFooterView(View footerLayoutView, LayoutParams layoutParams) {
        footerLayout.removeAllViews();
        footerLayout.addView(footerLayoutView, layoutParams);
    }

    private OverScroller mScroller;
    private int mMaximumVelocity, mMinimumVelocity;//最大最小速度
    private NestedScrollingParentHelper mParentHelper;

    public PushCardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);

        ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop();//距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。
        mMaximumVelocity = config.getScaledMaximumFlingVelocity();
        mMinimumVelocity = config.getScaledMinimumFlingVelocity();
        mParentHelper = new NestedScrollingParentHelper(this);

        mScroller = new OverScroller(context);
        initView(context, attrs);
    }

    private final DecelerateInterpolator mDecelerateInterpolator;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
    private static final int[] LAYOUT_ATTRS = new int[]{
            android.R.attr.enabled
    };

    private void initView(Context context, AttributeSet attrs) {

        setWillNotDraw(false);//ViewGroup默认情况下，出于性能考虑，会被设置成WILL_NOT_DROW，这样，ondraw就不会被执行了。
        //调用setWillNotDraw（false），去掉其WILL_NOT_DRAW flag。就可以重写ondraw()


        setChildrenDrawingOrderEnabled(true);//设置子view按照顺序绘制

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PushCardLayout);
            mHeaderViewId = a.getResourceId(R.styleable.PushCardLayout_nest_scroll_header_view, -1);
            mFooterViewId = a.getResourceId(R.styleable.PushCardLayout_nest_scroll_footer_view, -1);
            mContentViewId = a.getResourceId(R.styleable.PushCardLayout_nest_scroll_content, -1);
            a.recycle();
        }
        creatheaderLayout(context);
        creatfooterLayout(context);

        int w = (int) getResources().getDimension(R.dimen.dp_50);
        addHeaderView(new LoadingTengxuntvView(context), new LayoutParams(w, w));
        addFooterView(new LoadingTengxuntvView(context), new LayoutParams(w, w));

    }

    int mHeaderViewId = -1;
    int mFooterViewId = -1;
    int mContentViewId = -1;

    /**
     * 初始化顶部布局
     * TODO 暂时用底部布局高度作为默认高度，动态顶部，底部高度 ，用到时候在处理吧
     *
     * @param context
     */
    private int footerHeight = (int) (100 * getResources().getDisplayMetrics().density);

    public int getfooterHeight() {
        return footerHeight;
    }

    private void creatfooterLayout(Context context) {
        if (mFooterViewId != -1) {
            footerLayout = findViewById(mFooterViewId);
        }
        footerLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, footerHeight);
        footerLayout.setGravity(Gravity.CENTER);
        addView(footerLayout, layoutParams);
    }

    /**
     * 初始化底部布局
     *
     * @param context
     */
    private int headerHeight = (int) (100 * getResources().getDisplayMetrics().density);

    private void creatheaderLayout(Context context) {
        if (headerLayout == null) {
            if (mHeaderViewId != -1) {
                headerLayout = findViewById(mHeaderViewId);
                return;
            }

            headerLayout = new LinearLayout(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, headerHeight);
            headerLayout.setGravity(Gravity.CENTER);
            addView(headerLayout, layoutParams);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }
        if (contentLayout == null) {
            ensureTarget();
        }
        if (contentLayout == null) {
            return;
        }
        final View child = contentLayout;
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

        int headerLayoutWidth = headerLayout.getMeasuredWidth();
        headerHeight = headerLayout.getMeasuredHeight();
        headerLayout.layout((width / 2 - headerLayoutWidth / 2), -headerHeight,
                (width / 2 + headerLayoutWidth / 2), 0);

        int footerLayoutWidth = footerLayout.getMeasuredWidth();
        footerHeight = footerLayout.getMeasuredHeight();
        footerLayout.layout((width / 2 - footerLayoutWidth / 2), height,
                (width / 2 + footerLayoutWidth / 2), height + footerHeight);

        //初始化默认状态
        if (contentLayout != null) {
            if (defaultState == DefaultStateType.top) {
                setCardState(StateType.isToping);
                resetAnimation(headerLayout, true);
            }
            if (defaultState == DefaultStateType.bottom) {
                setCardState(StateType.isBottoming);
                resetAnimation(footerLayout, true);
            }
        }

        Log.i("getMeasuredHeight", "初始headerLayout 高度:" + headerLayout.getMeasuredHeight() + ",headerLayoutView 高度:" + headerLayout.getMeasuredHeight());
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (contentLayout == null) {
            ensureTarget();
        }
        if (contentLayout == null) {
            return;
        }
        contentLayout.measure(MeasureSpec.makeMeasureSpec(
                getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
        headerLayout.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int) headerHeight, MeasureSpec.EXACTLY));

        footerLayout.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int) footerHeight, MeasureSpec.EXACTLY));
    }

    /**
     * 确认目标view
     */
    private void ensureTarget() {
        if (mContentViewId != -1) {
            contentLayout = findViewById(mContentViewId);
            if (contentLayout != null) {
                return;
            }
        }
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (!child.equals(headerLayout) && !child.equals(footerLayout)) {
                contentLayout = child;
                break;
            }
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        // if this is a List < L or another view that doesn't support nested
        // scrolling, ignore this request so that the vertical scroll event
        // isn't stolen
        if ((android.os.Build.VERSION.SDK_INT < 21 && contentLayout instanceof AbsListView)
                || (contentLayout != null && !ViewCompat.isNestedScrollingEnabled(contentLayout))) {
            // Nope.
        } else {
            super.requestDisallowInterceptTouchEvent(b);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();
        boolean handled = false;
        if (isEnabled() && (!canChildScrollUp() || !canChildScrollDown())) {
            handled = onTouchEvent(ev);
            //Log.i("CGQ", "事件分发");
        }
        if (contentLayout.getTop() != 0) {
            Log.i("CGQ", "拦截触摸事件");
            return true;
        }

        //父控件消费，子空间就不执行了。父控件不消费，再交给子空间处理
        boolean b = !handled ? super.onInterceptTouchEvent(ev) : handled;
        Log.i("CGQ", "拦截触摸事件：" + handled);
        return b;
    }

    private float mInitialDownY;//初始按下Y轴坐标
    private float mMoveY;//初始按下Y轴坐标

    boolean touchForceClose;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!canRefresh) {
            return false;
        }
        initVelocityTracker();
        mVelocityTracker.addMovement(event);

        int action = event.getAction();
        boolean handled = false;//是否消费
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mInitialDownY = event.getY();// 移动的起点
                mMoveY = event.getY();
                return (contentLayout.getTop() != 0);
            case MotionEvent.ACTION_MOVE:
                touchForceClose = false;
                float offset_c = (event.getY() - mInitialDownY);//当前滑动间距
                float dy = event.getY() - mMoveY;
                mMoveY = event.getY();
                if (Math.abs(offset_c) < mTouchSlop) {
                    //Log.i("CGQ", "未达到滚动最小值");
                    return false;
                }
                if (contentLayout.getTop() != 0) {
                    float overscrollTop = DRAG_RATE * dy;
                    handled = true;
                    Log.i("CGQ", "整体是" + (offset_c > 0 ? "下滑" : "上滑") + "手势,局部" + (dy > 0 ? "下滑" : "上滑"));
                    if (contentLayout.getTop() > 0 && offset_c < 0) {
                        touchForceClose = true;
                    } else if (contentLayout.getTop() < 0 && offset_c > 0) {
                        touchForceClose = true;
                    }
                    scrollCardLayout((int) overscrollTop);
                }
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity();
                Log.i("CGQ", "ACTION_UP velocityY：" + velocityY + ",mMinimumVelocity=" + mMinimumVelocity);
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    finishSpinner(touchForceClose);
                    // fling(-velocityY);
                }
                //ViewCompat.stopNestedScroll(contentLayout, ViewCompat.TYPE_TOUCH);
                recycleVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i("CGQ", "ACTION_CANCEL ");
                recycleVelocityTracker();
                finishSpinner(touchForceClose);
                break;
        }
        Log.i("CGQ", "消耗触摸事件：" + handled);

        return super.onTouchEvent(event);
        // return  handled;
    }

    public void fling(int velocityY) {
        Log.i("CGQ", "惯性滚动 velocityY=" + velocityY);
        // 第三步-二小步，fling调整最大Y偏移，以便把fling传递给nestedScrollingChild。
        mScroller.fling(0, contentLayout.getTop(), 0, velocityY, 0, 0, 0, Integer.MAX_VALUE);
        invalidate();
    }

    /**
     * @param offset 偏移量
     * @return
     */
    private int scrollCardLayout(int offset) {
        Log.i("CGQ1", "scrollCardLayout=" + offset);
        if (contentLayout.getTop() < 0) {
            setCardState(StateType.isBottoming);
        } else {
            setCardState(StateType.isToping);
        }
        if (animationListener != null) {
            animationListener.onRuning(getTop() > 0, (float) contentLayout.getTop() / (contentLayout.getTop() > 0 ? headerHeight : footerHeight));
        }
        //Log.i("getMeasuredHeight","headerLayout 高度:"+headerLayout.getMeasuredHeight()+",headerLayoutView 高度:"+headerLayout.getMeasuredHeight()) ;
        offset = (int) (DRAG_RATE * offset);
        if (contentLayout != null) {
            int top = contentLayout.getTop();
            if (offset < 0) {//向上移动
                offset = Math.max((-footerHeight - top), offset);
            }
            if (offset > 0) {//向下移动
                offset = Math.min((headerHeight - top), offset);
            }

            ViewCompat.offsetTopAndBottom(contentLayout, offset);//正数向下移动，负数向上移动
            //headerLayout.bringToFront();
            ViewCompat.offsetTopAndBottom(headerLayout, top - headerHeight - headerLayout.getTop());
            ViewCompat.offsetTopAndBottom(footerLayout, top + getHeight() - footerLayout.getTop());
            if (contentLayout.getTop() == 0) {
                headerLayout.setVisibility(INVISIBLE);
                footerLayout.setVisibility(INVISIBLE);
            } else if (contentLayout.getTop() > 0) {
                headerLayout.setVisibility(VISIBLE);
                footerLayout.setVisibility(INVISIBLE);
            } else if (contentLayout.getTop() < 0) {
                headerLayout.setVisibility(INVISIBLE);
                footerLayout.setVisibility(VISIBLE);
            }
        }
        //Log.i("scrollCardLayout", "contentLayout top:" + contentLayout.getTop());
        return offset;
    }

    /**
     * @param offset 偏移量
     */
    private void scrollCardLayoutTo(int offset) {
        //Log.i("getMeasuredHeight","headerLayout 高度:"+headerLayout.getMeasuredHeight()+",headerLayoutView 高度:"+headerLayout.getMeasuredHeight()) ;
        if (contentLayout != null) {
            int top = contentLayout.getTop();
            if (offset > headerHeight) {
                offset = headerHeight;
            }
            if (offset < -footerHeight) {
                offset = -footerHeight;
            }
            ViewCompat.offsetTopAndBottom(contentLayout, offset - top);//正数向下移动，负数向上移动
            //headerLayout.bringToFront();
            ViewCompat.offsetTopAndBottom(headerLayout, top - headerHeight - headerLayout.getTop());
            ViewCompat.offsetTopAndBottom(footerLayout, top + getHeight() - footerLayout.getTop());

            if (contentLayout.getTop() == 0) {
                headerLayout.setVisibility(INVISIBLE);
                footerLayout.setVisibility(INVISIBLE);
            } else if (contentLayout.getTop() > 0) {
                headerLayout.setVisibility(VISIBLE);
                footerLayout.setVisibility(INVISIBLE);
            } else if (contentLayout.getTop() < 0) {
                headerLayout.setVisibility(INVISIBLE);
                footerLayout.setVisibility(VISIBLE);
            }
        }
        Log.i("scrollCardLayout", "contentLayout top to:" + contentLayout.getTop());
    }

    //private float mInitialMotionY;
    private static final float DRAG_RATE = .5f;//拖拽率阻尼系数


    /**
     * 恢复
     */
    public void cancel() {
        finishSpinner(true);
    }

    //是否可以上拉下拉滑动
    private boolean canRefresh = true;

    /**
     * 是否可以上拉下拉滑动
     *
     * @param canRefresh
     */
    public void setCanRefresh(boolean canRefresh) {
        this.canRefresh = canRefresh;
    }

    ValueAnimator animator;

    /**
     * 根据当前状态处理未结束的事件
     *
     * @param forceClose 强制恢复到初始状态
     */
    private void finishSpinner(boolean forceClose) {
        if (animator != null) {
            animator.cancel();
        }
        if (contentLayout == null) {
            return;
        }
        if (forceClose) {
            //回滚动画
            Log.i("CGQ1", "强制回滚动画1");
            resetAnimation(null, false);
        } else {
            //处理是否触发刷新或者加载更多//(默认拉到三分之二就触发加载)
            if (Math.abs(contentLayout.getTop()) > footerHeight / 3 * 2 && (cardState == StateType.isToping || cardState == StateType.isBottoming)) {//拉到2/3以上则触发
                //填充动画（自动下拉到最大高度）
                Log.i("CGQ1", "展开动画1");
                resetAnimation((cardState == StateType.isToping || cardState == StateType.isToped) ? headerLayout : footerLayout, true);
            } else if (cardState == StateType.isBottomed || cardState == StateType.isToped) {
                Log.i("CGQ1", "展开动画1" + cardState);
                setCardState(cardState == StateType.isBottomed ? StateType.isBottoming : StateType.isToping);
                scrollCardLayout(-contentLayout.getTop());
            } else if (cardState == StateType.isBottoming || cardState == StateType.isToping) {
                //回滚动画
                Log.i("CGQ1", "回滚动画1");
                resetAnimation(null, false);
            }
        }
    }

    public enum DefaultStateType {
        normal, top, bottom
    }

    private DefaultStateType defaultState = DefaultStateType.normal;//初始化状态

    public void setDefaultState(DefaultStateType defaultState) {
        this.defaultState = defaultState;
    }

    /**
     * 重置动画状态
     * @param view
     * @param isOpen true 展开动画 false 关闭动画
     */
    private void resetAnimation(View view, boolean isOpen) {
        Log.i("CGQ1", isOpen ? "展开动画" : "关闭动画");
        int startValue = 0;
        int endValue = 0;
        if (isOpen) {
            startValue = contentLayout.getTop();
            endValue = (view == headerLayout) ? headerHeight : -footerHeight;
        } else {
            startValue = contentLayout.getTop();
            endValue = 0;
        }
        boolean isUpper = endValue > startValue;
        final int start = startValue;
        final int end = endValue;
        if (start == end) {
            return;
        }
        animator = ValueAnimator.ofInt(startValue, endValue);
        int duration =50+150 *((view == headerLayout)?(Math.abs(end-start))/headerHeight:(Math.abs(end-start)/footerHeight));

        animator.setDuration(duration).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                scrollCardLayoutTo(value);
                if (animationListener != null) {
                    animationListener.onRuning(isUpper, (value - start) / (start - end));
                }
                // postInvalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (contentLayout.getTop() == headerHeight) {//上拉处理
                    setCardState(StateType.isToped);
                    if (dataListener != null)
                        dataListener.onRefreshData();
                }
                if (contentLayout.getTop() == -footerHeight) {//下拉处理
                    setCardState(StateType.isBottomed);
                    if (dataListener != null)
                        dataListener.onLoadMoreData();
                }
            }
        });
    }

    ///22222222222222222222222222222222222222222
    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        QDLogger.d("Start Nested Scroll " + (type==ViewCompat.TYPE_TOUCH?"触摸滑动开始":"惯性滑动开始"));
        return true;
    }
     boolean isfling;
    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        isfling =type ==ViewCompat.TYPE_NON_TOUCH;
        QDLogger.d("accept Start Nested Scroll " + type+",isfling="+isfling);
        mParentHelper.onNestedScrollAccepted(child, target, axes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        QDLogger.d("onStopNestedScroll " + (type==ViewCompat.TYPE_TOUCH?"触摸滑动结束":"惯性滑动结束"));
        mParentHelper.onStopNestedScroll(target);

        if (type == ViewCompat.TYPE_TOUCH) {
            if (contentLayout.getTop() != 0&&!isfling) {
                finishSpinner(false);
            }
        }else if (type == ViewCompat.TYPE_NON_TOUCH) {
            if (contentLayout.getTop() != 0) {
                finishSpinner(false);
            }
        }
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        QDLogger.d("onNestedScroll " + (type==ViewCompat.TYPE_TOUCH?"触摸滑动":"惯性滑动"));
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (type == ViewCompat.TYPE_TOUCH) {//手指触发的滑动
            QDLogger.e("onNestedPreScroll dy=" + dy + ",getTop()=" + contentLayout.getTop()+",canChildScrollUp()="+canChildScrollUp());
            if (dy < 0&&!canChildScrollUp()) {
                //下滑
                int y = scrollCardLayout(-dy);
                consumed[1] = -Math.abs(y);
                QDLogger.e("onNestedPreScroll 下滑=" + y + ",dy=" + dy +  ",consumed=" + consumed[1]);
            } else if (dy > 0&&!canChildScrollDown()) {
                //上滑
                int y = scrollCardLayout(-dy);
                consumed[1] = Math.abs(y);
                QDLogger.e("onNestedPreScroll 上滑=" + y + ",dy=" + dy + ",consumed=" + consumed[1]);
            }
        }else if (type == ViewCompat.TYPE_NON_TOUCH) {//惯性滑动
            QDLogger.e("onNestedPreScroll dy=" + dy + ",getTop()=" + contentLayout.getTop()+",canChildScrollUp()="+canChildScrollUp());
            if (dy < 0&&!canChildScrollUp()) {
                //下滑
                int y = scrollCardLayout((int) (-dy*.1f));
                consumed[1] = -Math.abs(y);
                QDLogger.e("onNestedPreScroll 惯性下滑=" + y + ",dy=" + dy +  ",consumed=" + consumed[1]);
            } else if (dy > 0&&!canChildScrollDown()) {
                //上滑
                int y = scrollCardLayout((int) (-dy*.1f));
                consumed[1] = Math.abs(y);
                QDLogger.e("onNestedPreScroll 惯性上滑=" + y + ",dy=" + dy + ",consumed=" + consumed[1]);
            }
        }
    }

    //判断是否可以下拉
    public boolean canChildScrollUp() {
        if (contentLayout == null) {
            return false;
        }
        if (contentLayout instanceof ListView) {
            return ListViewCompat.canScrollList((ListView) contentLayout, -1);
        }
        boolean b = contentLayout.canScrollVertically(-1);
        return b;
    }

    //判断是否可以上拉
    public boolean canChildScrollDown() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (contentLayout instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) contentLayout;
                // AppLog.e(absListView.getFirstVisiblePosition()+"  :   "+absListView.getChildAt(absListView.getChildCount()-1).getBottom()+"  :   "+absListView.getPaddingBottom());
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return contentLayout.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(contentLayout, 1);
        }
    }

    /**
     * 数据监听器
     */
    public interface PushCardDatalistener {
        /**
         * 上拉加载更多
         */
        void onLoadMoreData();

        /**
         * 下拉刷新
         */
        void onRefreshData();
    }

    /**
     * 动画监听器
     */
    public interface PushCardAnimationListener {

        /**
         * 0-1属性动画，下拉百分比动画
         *
         * @param isUpper //判断头部动画还是底部动画
         * @param value   动画百分比
         */
        void onRuning(boolean isUpper, float value);
    }
}
