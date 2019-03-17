package cn.demomaster.huan.quickdeveloplibrary.widget.pushcardlayout;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ListViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;


/**
 * @author:Squirrel桓
 * @time:2018/8/28
 */
public class PushCardLayout extends FrameLayout implements NestedScrollingParent,
        NestedScrollingChild {

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
        Log.i("cardState", "this.cardState=" + cardState);
    }

    private int mTouchSlop;// 系统默认的最小滚动距离
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;

    //滚动内容区
    private View contentLayout;
    //底部布局，顶部布局
    private LinearLayout bottomLayout, topLayout;
    private View topLayoutView;
    private View bottomLayoutView;

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

    public View getTopLayoutView() {
        return topLayoutView;
    }

    /**
     * 设置顶部view
     *
     * @param topLayoutView
     */
    public void setTopLayoutView(View topLayoutView) {
        topLayout.removeAllViews();
        topLayout.addView(topLayoutView);
        this.topLayoutView = topLayoutView;
    }

    public View getBottomLayoutView() {
        return bottomLayoutView;
    }

    /**
     * 设置底部view
     *
     * @param bottomLayoutView
     */
    public void setBottomLayoutView(View bottomLayoutView) {
        bottomLayout.removeAllViews();
        bottomLayout.addView(bottomLayoutView);
        this.bottomLayoutView = bottomLayoutView;
    }

    public PushCardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
        setNestedScrollingEnabled(true);
        initView(context, attrs);
    }

    private int mMediumAnimationDuration;//动画时长
    private final DecelerateInterpolator mDecelerateInterpolator;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
    private static final int[] LAYOUT_ATTRS = new int[]{
            android.R.attr.enabled
    };

    private void initView(Context context, AttributeSet attrs) {
        final DisplayMetrics metrics = getResources().getDisplayMetrics();//分辨率
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();//距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。

        mMediumAnimationDuration = getResources().getInteger(
                android.R.integer.config_mediumAnimTime);//动画时长

        setWillNotDraw(false);//ViewGroup默认情况下，出于性能考虑，会被设置成WILL_NOT_DROW，这样，ondraw就不会被执行了。
        //调用setWillNotDraw（false），去掉其WILL_NOT_DRAW flag。就可以重写ondraw()

        creatTopLayout(context);
        creatBottomLayout(context);
        /****** (START)  测试添加 可删除   ***********/
        /*ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.mipmap.ic_launcher_round);
        setTopLayoutView(imageView);
        ImageView imageView2 = new ImageView(getContext());
        imageView2.setImageResource(R.mipmap.ic_launcher_round);
        setBottomLayoutView(imageView2);*/
        /******  (END) 测试添加 可删除   ***********/

        setChildrenDrawingOrderEnabled(true);//设置子view按照顺序绘制
        mSpinnerOffsetEnd = bottomLayoutHeight / 2;
        mTotalDragDistance = mSpinnerOffsetEnd;

        final TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
        setEnabled(a.getBoolean(0, true));
        a.recycle();
    }

    /**
     * 初始化顶部布局
     * TODO 暂时用底部布局高度作为默认高度，动态顶部，底部高度 ，用到时候在处理吧
     *
     * @param context
     */
    private int bottomLayoutHeight = (int) (100 * getResources().getDisplayMetrics().density);

    public int getBottomLayoutHeight() {
        return bottomLayoutHeight;
    }

    private void creatBottomLayout(Context context) {
        bottomLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, bottomLayoutHeight);
        layoutParams.gravity = Gravity.CENTER;
        bottomLayout.setLayoutParams(layoutParams);
        bottomLayout.setGravity(Gravity.CENTER);
        addView(bottomLayout);
    }

    /**
     * 初始化底部布局
     *
     * @param context
     */
    private int topLayoutHeight = (int) (100 * getResources().getDisplayMetrics().density);

    private void creatTopLayout(Context context) {
        topLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, topLayoutHeight);
        layoutParams.gravity = Gravity.CENTER;
        topLayout.setLayoutParams(layoutParams);
        topLayout.setGravity(Gravity.CENTER);
        addView(topLayout);
    }

    private int topLayoutOffsetTop = 0;//header距离顶部的距离
    private int bottomLayoutOffsetTop = 0;//footer距离顶部的距离

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

        final DisplayMetrics metrics = getResources().getDisplayMetrics();//分辨率
        int topLayoutWidth = topLayout.getMeasuredWidth();
        topLayoutHeight = topLayout.getMeasuredHeight();
        topLayoutOffsetTop = -topLayoutHeight;
        topLayout.layout((width / 2 - topLayoutWidth / 2), -topLayoutHeight,
                (width / 2 + topLayoutWidth / 2), 0);

        int bottomLayoutWidth = bottomLayout.getMeasuredWidth();
        bottomLayoutHeight = bottomLayout.getMeasuredHeight();
        bottomLayoutOffsetTop = height;
        bottomLayout.layout((width / 2 - bottomLayoutWidth / 2), height,
                (width / 2 + bottomLayoutWidth / 2), height + bottomLayoutHeight);

        //初始化默认状态
        if (contentLayout != null && defaultState != DefaultStateType.normal) {
            if (defaultState == DefaultStateType.top) {
                setCardState(StateType.isToping);
                showTopLayout();
            }
            if (defaultState == DefaultStateType.bottom) {
                setCardState(StateType.isBottoming);
                showTopLayout();
            }
        }

       Log.i("getMeasuredHeight","初始topLayout 高度:"+topLayout.getMeasuredHeight()+",topLayoutView 高度:"+topLayoutView.getMeasuredHeight()) ;
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
        topLayout.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int) topLayoutHeight, MeasureSpec.EXACTLY));

        bottomLayout.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int) bottomLayoutHeight, MeasureSpec.EXACTLY));
    }

    /**
     * 确认目标view
     */
    private void ensureTarget() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (!child.equals(topLayout) && !child.equals(bottomLayout)) {
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
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            Log.i("CGQ", "不拦截子view滚动");
        }
        if (isEnabled() && (!canChildScrollUp() || !canChildScrollDown())
                && !mRefreshing && !mNestedScrollInProgress) {
            handled = onTouchEvent(ev);
            //Log.i("CGQ", "事件分发");
        }

        //父控件消费，子空间就不执行了。父控件不消费，再交给子空间处理
        boolean b = !handled ? super.onInterceptTouchEvent(ev) : handled;
        Log.i("CGQ", "拦截触摸事件：" + handled);
        return b;
    }

    private float mInitialDownY;//初始按下Y轴坐标

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!canRefresh) {
            return false;
        }

        int action = event.getAction();
        boolean handled = false;//是否消费
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mInitialDownY = event.getY();// 移动的起点
                break;
            case MotionEvent.ACTION_MOVE:
                float offset_c = (event.getY() - mInitialDownY);//当前滑动间距
                if (Math.abs(offset_c) < mTouchSlop) {
                    Log.i("CGQ", "未达到滚动最小值");
                    return false;
                }
                if (offset_c > 0) {//下滑手势
                    if (cardState == StateType.isBottomed) {//已经是下滑到底状态
                        finishSpinner();
                    } else {//不是下滑到底状态
                        if (canChildScrollUp()) {
                            Log.i("CGQ", "下滑");
                        } else {
                            Log.i("CGQ", "下滑并触发");
                            handled = true;
                            final float overscrollTop = offset_c * DRAG_RATE;
                            //下滑并触发
                            moveSpinner(overscrollTop);
                        }
                    }
                } else {//上拉手势
                    if (cardState == StateType.isToped) {//已经是上拉到顶状态
                        finishSpinner();
                    } else {
                        if (canChildScrollDown()) {
                            Log.i("CGQ", "上滑");
                        } else {
                            Log.i("CGQ", "上滑并触发");
                            handled = true;
                            final float overscrollTop = offset_c * DRAG_RATE;
                            //下拉显示头部
                            moveSpinner(overscrollTop);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                finishSpinner();
                break;
            case MotionEvent.ACTION_CANCEL:
                finishSpinner();
                break;
        }
        Log.i("CGQ", "消耗触摸事件：" + handled);
        return handled;
    }

    /**
     * @param offset 偏移量
     */
    private void scrollCardLayout(int offset) {
        Log.i("getMeasuredHeight","topLayout 高度:"+topLayout.getMeasuredHeight()+",topLayoutView 高度:"+topLayoutView.getMeasuredHeight()) ;
        if (contentLayout != null) {
            ViewCompat.offsetTopAndBottom(contentLayout, offset);//正数向下移动，负数向上移动
            //topLayout.bringToFront();
            ViewCompat.offsetTopAndBottom(topLayout, offset);
            ViewCompat.offsetTopAndBottom(bottomLayout, offset);
            mContenViewOffsetTop = contentLayout.getTop();
        }
    }

    //private float mInitialMotionY;
    private static final float DRAG_RATE = .5f;//拖拽率阻尼系数

    private float mTotalDragDistance = -1;
    int mSpinnerOffsetEnd;
    protected int mOriginalOffsetTop;
    int mContenViewOffsetTop;

    /**
     * @param overscrollTop
     * @param
     */
    private void moveSpinner(float overscrollTop) {
        boolean isTop = false;//向上
        int targetY2 = 0;
        if (overscrollTop < 0) {
            isTop = true;
            overscrollTop = -overscrollTop;
            setCardState(StateType.isBottoming);
        } else {
            setCardState(StateType.isToping);
        }
        //Log.i("CGQ", "moveSpinner = " + overscrollTop);
        float originalDragPercent = overscrollTop / mTotalDragDistance;

        float dragPercent = Math.min(1f, Math.abs(originalDragPercent));
        float extraOS = Math.abs(overscrollTop) - mTotalDragDistance;
        float slingshotDist = mUsingCustomStart ? mSpinnerOffsetEnd - mOriginalOffsetTop
                : mSpinnerOffsetEnd;
        float tensionSlingshotPercent = Math.max(0, Math.min(extraOS, slingshotDist * 2)
                / slingshotDist);
        float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow(
                (tensionSlingshotPercent / 4), 2)) * 2f;
        float extraMove = (slingshotDist) * tensionPercent * 2;
        targetY2 = (int) ((slingshotDist * dragPercent) + extraMove);

        int h = !isTop ? (targetY2 - mContenViewOffsetTop) : (-targetY2 - mContenViewOffsetTop);

        if (animationListener != null) {
            animationListener.onRuning(cardState == StateType.isToping ? topLayoutView : bottomLayoutView, cardState == StateType.isToping, (float) Math.abs(contentLayout.getTop()) / bottomLayoutHeight);
        }
        Log.i("CGQ", "slingshotDist=" + slingshotDist + ",h=" + h);
        scrollCardLayout(h);
    }

    /**
     * 恢复
     */
    public void setCancel() {
        finishSpinner();
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

    public void dismiss() {
        finishSpinner();
    }

    /**
     * 恢复动画
     */
    private void finishSpinner() {
        if (contentLayout == null) {
            return;
        }
        //处理是否触发刷新或者加载更多//(默认拉到三分之二就触发加载)
        if (Math.abs(contentLayout.getTop()) > bottomLayoutHeight / 3 * 2 && (cardState == StateType.isToping || cardState == StateType.isBottoming)) {//拉到2/3以上则触发
            //填充动画（自动下拉到最大高度）
            Log.i("CGQ1", "填充动画=" );
            showTopLayout();
        } else if (cardState == StateType.isBottomed || cardState == StateType.isToped) {
            setCardState(cardState == StateType.isBottomed ? StateType.isBottoming : StateType.isToping);
            scrollCardLayout(-contentLayout.getTop());
        } else if (cardState == StateType.isBottoming || cardState == StateType.isToping) {
            //回滚动画
            Log.i("CGQ1", "回滚动画=" );
            ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.setDuration(200);
            animator.setInterpolator(mDecelerateInterpolator);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    scrollCardLayout((int) (-contentLayout.getTop() * animatedValue));
                    View view = ((cardState == StateType.isToping|| cardState==StateType.isToped)) ? topLayoutView : bottomLayoutView;
                    if (animatedValue == 0) {//动画开始
                        if (animationListener != null) {
                            animationListener.onStart(view);
                        }
                    } else if (animatedValue == 1f) {//动画结束
                        mTotalUnconsumed = 0;
                        setCardState(StateType.idle);
                        if (animationListener != null) {
                            animationListener.onEnd(view);
                        }
                    } else {//动画进行中
                        if (animationListener != null)
                            animationListener.onRuning(view, cardState == StateType.isToping, animatedValue);
                    }
                }
            });
            animator.start();
        }
    }

    public enum DefaultStateType {
        normal, top, bottom
    }

    private DefaultStateType defaultState = DefaultStateType.normal;//初始化状态

    public void setDefaultState(DefaultStateType defaultState) {
        this.defaultState = defaultState;
    }

    private void showTopLayout() {
        Log.i("CGQ1", "zhankai动画=" );
        float startValue = Math.abs(contentLayout.getTop()) / (float) bottomLayoutHeight;
        ValueAnimator animator = ValueAnimator.ofFloat(startValue, 1);
        animator.setDuration((int) (200 * (1 - startValue)));
        animator.setInterpolator(mDecelerateInterpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                //(cardState==StateType.isToping?1:-1)
                scrollCardLayout((int) ((((cardState == StateType.isToping|| cardState==StateType.isToped) ? 1 : -1) * bottomLayoutHeight - contentLayout.getTop()) * animatedValue));
                if (animationListener == null){
                    return;
                }
                if (animatedValue == 0) {//动画开始
                    animationListener.onStart((cardState == StateType.isToping|| cardState==StateType.isToped)? topLayoutView : bottomLayoutView);
                } else if (animatedValue == 1f) {//动画结束
                    defaultState = DefaultStateType.normal;
                    animationListener.onEnd((cardState == StateType.isToping|| cardState==StateType.isToped)? topLayoutView : bottomLayoutView);
                    if (cardState == StateType.isToping) {//上拉处理
                        setCardState(StateType.isToped);
                        if (dataListener != null)
                            dataListener.onRefreshData();
                    }
                    if (cardState == StateType.isBottoming) {//下拉处理
                        setCardState(StateType.isBottomed);
                        if (dataListener != null)
                            dataListener.onLoadMoreData();
                    }
                } else {//动画进行中
                    animationListener.onRuning((cardState == StateType.isToping|| cardState==StateType.isToped)? topLayoutView : bottomLayoutView, cardState == StateType.isToping, animatedValue);
                }
            }
        });
        animator.start();
    }

    // NestedScrollingParent
    private float mTotalUnconsumed;//所有未消耗的长度
    private boolean mNestedScrollInProgress;
    private final int[] mParentScrollConsumed = new int[2];
    private final int[] mParentOffsetInWindow = new int[2];
    // Whether the client has set a custom starting position;
    boolean mUsingCustomStart;
    //private boolean mReturningToStart;
    boolean mRefreshing = false;
    private OnChildScrollUpCallback mChildScrollUpCallback;

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return isEnabled() && !mRefreshing
                && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        // Dispatch up to the nested parent
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mTotalUnconsumed = 0;
        //mNestedScrollInProgress = true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
        // before allowing the list to scroll
        if (dy > 0 && mTotalUnconsumed > 0) {
            if (dy > mTotalUnconsumed) {
                consumed[1] = dy - (int) mTotalUnconsumed;
                mTotalUnconsumed = 0;
            } else {
                mTotalUnconsumed -= dy;
                consumed[1] = dy;
            }
            moveSpinner(mTotalUnconsumed);
        }

        // If a client layout is using a custom start position for the circle
        // view, they mean to hide it again before scrolling the child view
        // If we get back to mTotalUnconsumed == 0 and there is more to go, hide
        // the circle so it isn't exposed if its blocking content is moved
        if (mUsingCustomStart && dy > 0 && mTotalUnconsumed == 0
                && Math.abs(dy - consumed[1]) > 0) {
            //mCircleView.setVisibility(View.GONE);
        }

        // Now let our nested parent consume the leftovers
        final int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }


    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        mNestedScrollInProgress = false;
        // Finish the spinner for nested scrolling if we ever consumed any
        // unconsumed nested scroll
        if (mTotalUnconsumed > 0) {
            finishSpinner();
            mTotalUnconsumed = 0;
        }
        // Dispatch up our nested parent
        stopNestedScroll();
    }


    @Override
    public void onNestedScroll(final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        // Dispatch up to the nested parent first
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                mParentOffsetInWindow);

        // This is a bit of a hack. Nested scrolling works from the bottom up, and as we are
        // sometimes between two nested scrolling views, we need a way to be able to know when any
        // nested scrolling parent has stopped handling events. We do that by using the
        // 'offset in window 'functionality to see if we have been moved from the event.
        // This is a decent indication of whether we should take over the event stream or not.
        final int dy = dyUnconsumed + mParentOffsetInWindow[1];
        if (dy < 0 && !canChildScrollUp()) {
            mTotalUnconsumed += Math.abs(dy);
            moveSpinner(mTotalUnconsumed);
        }
    }

    // NestedScrollingChild

    /**
     * 启用或者禁止嵌套滑动
     */
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    /**
     * 用于判断嵌套滑动是否被启用
     */
    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    /**
     * 开始嵌套滑动,参数为滑动方向,参数有如下几个
     * <p>
     * 没有滑动方向
     * public static final int SCROLL_AXIS_NONE = 0;
     * <p>
     * 横向滑动
     * public static final int SCROLL_AXIS_HORIZONTAL = 1 << 0;
     * <p>
     * 纵向滑动
     * public static final int SCROLL_AXIS_VERTICAL = 1 << 1;
     * <p>
     * 其返回值代表父View是否接受嵌套滑动,如果不接受返回false,后续的嵌套滑动都将失效
     */
    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    /**
     * 是否有实现了NestedScrollingParent的父View
     * 如果父View没有实现接口,此方法返回false,且所有嵌套滑动无效
     */
    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    /**
     * 分发嵌套滑动事件,在子View滑动处理完之后调用
     */
    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    /**
     * 分发预嵌套滑动事件,在子View滑动处理之前调用
     */
    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed,
                                           int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(
                dx, dy, consumed, offsetInWindow);
    }

    /**
     * 分发嵌套滑动的惯性滑动处理,返回值表示是否处理
     */
    @Override
    public boolean onNestedPreFling(View target, float velocityX,
                                    float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY,
                                 boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    /**
     * 分发嵌套滑动的惯性滑动处理,返回值表示是否处理
     */
    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    /**
     * 分发嵌套滑动的惯性滑动预处理,返回值表示是否处理,在子View处理之前调用
     */
    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    //判断是否可以下拉
    public boolean canChildScrollUp() {
        if (mChildScrollUpCallback != null) {
            return mChildScrollUpCallback.canChildScrollUp(this, contentLayout);
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

    public void setOnChildScrollUpCallback(@Nullable OnChildScrollUpCallback callback) {
        mChildScrollUpCallback = callback;
    }

    /**
     * Classes that wish to override {@link PushCardLayout#canChildScrollUp()} method
     * behavior should implement this interface.
     */
    public interface OnChildScrollUpCallback {
        /**
         * Callback that will be called when {@link PushCardLayout#canChildScrollUp()} method
         * is called to allow the implementer to override its behavior.
         *
         * @param parent SwipeRefreshLayout that this callback is overriding.
         * @param child  The child view of SwipeRefreshLayout.
         * @return Whether it is possible for the child view of parent layout to scroll up.
         */
        boolean canChildScrollUp(@NonNull PushCardLayout parent, @Nullable View child);

        /**
         * Callback that will be called when {@link PushCardLayout#canChildScrollDown()} method
         * is called to allow the implementer to override its behavior.
         *
         * @param parent SwipeRefreshLayout that this callback is overriding.
         * @param child  The child view of SwipeRefreshLayout.
         * @return Whether it is possible for the child view of parent layout to scroll down.
         */
        boolean canChildScrollDown(@NonNull PushCardLayout parent, @Nullable View child);
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
         * 开始初始化操作
         */
        void onStart(View targetView);

        /**
         * 0-1属性动画，下拉百分比动画
         *
         * @param targetView 目标view(头部或者底部)
         * @param isUpper    //判断头部动画还是底部动画
         * @param value      动画百分比
         */
        void onRuning(View targetView, boolean isUpper, float value);

        /**
         * 动画结束
         */
        void onEnd(View targetView);
    }

}
