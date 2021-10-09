package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * @author squirrel桓
 * @date 2018/12/14.
 * description：
 */
public class HorizontalScrollView extends android.widget.HorizontalScrollView {
    public HorizontalScrollView(Context context) {
        super(context);
        init();
    }

    public HorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private OnSelectChangeListener onSelectChangeListener;

    public void setOnSelectChangeListener(OnSelectChangeListener onSelectChangeListener) {
        this.onSelectChangeListener = onSelectChangeListener;
    }

    public interface OnSelectChangeListener {
        void onSelectChange(int position);
    }

    public interface HAdapter {
        int getCount();

        View getItemView(int position);
    }

    public void setAdapter(HAdapter adapter) {
        removeAllViews();
        LinearLayout linearLayout = new LinearLayout(getContext());
        for (int i = 0; i < adapter.getCount(); i++) {
            LinearLayout linearLayout1 = new LinearLayout(getContext());
            linearLayout1.addView(adapter.getItemView(i));
            linearLayout1.setTag(i);
            linearLayout1.setOnClickListener(view -> {
                int index = (int) view.getTag();
                if (onSelectChangeListener != null) {
                    onSelectChangeListener.onSelectChange(index);
                }
                smoothScrollTo(getChildCenterPosition(index), 0);
            });
            linearLayout.addView(linearLayout1);
        }

        addView(linearLayout);

        init();
    }

    private int getChildCenterPosition(int index) {
        currentIndex = index;
        offset_current = super.computeHorizontalScrollOffset();
        if (getChildCount() <= 0) {
            return 0;
        }
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        if (viewGroup == null || viewGroup.getChildCount() == 0) {
            return 0;
        }
        int offset_tmp = 0;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            int child_width = child.getWidth();
            offset_tmp = offset_tmp + child_width;
            if (i == index) {
                return offset_tmp - child_width;
            }
        }
        return 0;
    }

    private final int paddingLeft_origenal = 0;
    private final int paddingRight_origenal = 0;
    private int paddingLeft = 0;
    private int paddingRight = 0;


    void init() {
        this.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                handler.removeCallbacks(scrollerTask);
                handler.postDelayed(scrollerTask, delayMillis);
                //postDelayed(scrollerTask, delayMillis);
            }
            return false;
        });

        if (getChildCount() <= 0) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        if (viewGroup == null || viewGroup.getChildCount() == 0) {
            return;
        }
        View first = viewGroup.getChildAt(0);

        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        first.measure(w, h);
        int first_height = first.getMeasuredHeight();
        int first_width = first.getMeasuredWidth();

        View last = viewGroup.getChildAt(viewGroup.getChildCount() - 1);
        last.measure(w, h);
        int last_height = last.getMeasuredHeight();
        int last_width = last.getMeasuredWidth();
        paddingLeft = DisplayUtil.getScreenWidth(getContext()) / 2 - first_width / 2;
        paddingRight = DisplayUtil.getScreenWidth(getContext()) / 2 - last_width / 2;
       /* if(paddingLeft_origenal==0){
            paddingLeft_origenal = getPaddingLeft();
        }
        if(paddingRight_origenal==0){
            paddingRight_origenal = getPaddingRight();
        }*/
        setPadding(paddingLeft_origenal + paddingLeft, getPaddingTop(), paddingRight_origenal + paddingRight, getBottom());
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        init();
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        init();
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        init();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        init();
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("CGQ", "measureChildren");
        super.measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        QDLogger.println("measureChildWithMargins");
        Log.d("CGQ", "parentWidthMeasureSpec=" + parentWidthMeasureSpec);
        Log.d("CGQ", "widthUsed=" + widthUsed);
        Log.d("CGQ", "parentHeightMeasureSpec=" + parentHeightMeasureSpec);
        Log.d("CGQ", "heightUsed=" + heightUsed);
        super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    @Override
    public boolean getMeasureAllChildren() {
        Log.d("CGQ", "getMeasureAllChildren");
        return super.getMeasureAllChildren();
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        Log.d("measureChild", "parentWidthMeasureSpec=" + parentWidthMeasureSpec);
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
    }

    @Override
    public void fling(int velocityX) {
        super.fling(velocityX);
        //Log.d("AutoCenter", "fling velocityX=" + velocityX);

    }

    /**
     * Runnable延迟执行的时间
     */
    private final long delayMillis = 100;

    /**
     * 上次滑动的时间
     */
    private long lastScrollLeft = -1;
    private long nowScrollLeft = -1;
    private final Runnable scrollerTask = new Runnable() {
        @Override
        public void run() {
            if ((nowScrollLeft == lastScrollLeft)) {
                lastScrollLeft = nowScrollLeft;
                nowScrollLeft = -1;
                getCurrentIndex();
                if (offset_target != offset_current) {
                    //Log.d(tag, "offset_target=" + offset_target + ",offset_current=" + offset_current);
                    smoothScrollTo(offset_target, 0);
                }
                if (onSelectChangeListener != null) {
                    onSelectChangeListener.onSelectChange(getCurrentIndex());
                }
            } else {
                lastScrollLeft = nowScrollLeft;
                postDelayed(this, delayMillis);
            }
        }
    };

    private final Handler handler = new Handler();

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        /*Log.i(tag, "onOverScrolled scrollX=" + scrollX);
        Log.i(tag, "onOverScrolled scrollY=" + scrollY);
        Log.i(tag, "onOverScrolled clampedX=" + clampedX);
        Log.i(tag, "onOverScrolled clampedY=" + clampedY);*/
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //Log.i(tag, "left=" + l);
        // 更新ScrollView的滑动时间
        nowScrollLeft = l;

    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        //Log.i(tag, "onNestedScroll。。。");
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
        //Log.i(tag, "onNestedScrollAccepted。。。");
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        //Log.i(tag, "onStartNestedScroll。。。");
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
        //Log.i(tag, "onStopNestedScroll。。。");
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);
        //Log.i(tag, "onNestedPreScroll。。。");
    }

    @Override
    protected int computeHorizontalScrollExtent() {
        //Log.i(tag, "computeHorizontalScrollExtent。。。" + super.computeHorizontalScrollExtent());//
        return super.computeHorizontalScrollExtent();
    }

    @Override
    protected int computeVerticalScrollRange() {
        //Log.i(tag, "computeVerticalScrollRange。。。" + super.computeVerticalScrollRange());//
        return super.computeVerticalScrollRange();
    }

    @Override
    protected int computeVerticalScrollOffset() {
        //Log.i(tag, "computeVerticalScrollOffset。。。" + super.computeVerticalScrollOffset());
        return super.computeVerticalScrollOffset();
    }

    @Override
    protected int computeVerticalScrollExtent() {
        //Log.i(tag, "computeVerticalScrollExtent。。。" + super.computeVerticalScrollExtent());//
        return super.computeVerticalScrollExtent();
    }

    @Override
    protected int computeHorizontalScrollRange() {
        //Log.i(tag, "横向总宽度 computeHorizontalScrollRange:" + super.computeHorizontalScrollRange());
        //Log.i(tag, "computeHorizontalScrollRange2:" + (super.computeHorizontalScrollRange() + QMUIDisplayHelper.getScreenWidth(getContext())));
        return super.computeHorizontalScrollRange() + paddingLeft_origenal + paddingLeft + paddingRight_origenal + paddingRight;
    }

    private int currentIndex = 0;

    @Override
    protected int computeHorizontalScrollOffset() {
        //Log.i(tag, "当前位置 computeHorizontalScrollOffset:" + super.computeHorizontalScrollOffset());
        return super.computeHorizontalScrollOffset() + paddingLeft_origenal + paddingLeft;
    }


    private int offset_target;
    private int offset_current;

    public int getCurrentIndex() {
        currentIndex = 0;
        offset_current = super.computeHorizontalScrollOffset();
        if (getChildCount() <= 0) {
            return 0;
        }
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        if (viewGroup == null || viewGroup.getChildCount() == 0) {
            return 0;
        }
        int offset_tmp = 0;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            int child_width = child.getWidth();
            offset_tmp = offset_tmp + child_width;
            if (offset_tmp - child_width / 2 > offset_current) {
                offset_target = offset_tmp - child_width;
                currentIndex = i;
                break;
            }
        }
        return currentIndex;
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        //Log.i(tag, "computeScrollDeltaToGetChildRectOnScreen：" + rect);
        Rect rect_temp = rect;
        rect_temp.left = rect.left - DisplayUtil.getScreenWidth(getContext()) / 2;
        return super.computeScrollDeltaToGetChildRectOnScreen(rect);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        /*Log.i(tag, "onLayout changed：" + changed);
        Log.i(tag, "onLayout l：" + l);
        Log.i(tag, "onLayout t：" + t);
        Log.i(tag, "onLayout r：" + b);
        Log.i(tag, "onLayout b：" + b);*/
        super.onLayout(changed, l, t, r, b);
    }
    
    @Override
    public boolean pageScroll(int direction) {
        //Log.i(tag, "pageScroll direction= " + direction);
        return super.pageScroll(direction);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
       /* Log.i(tag, "onSizeChanged w= " + w);
        Log.i(tag, "onSizeChanged h= " + h);
        Log.i(tag, "onSizeChanged oldw= " + oldw);
        Log.i(tag, "onSizeChanged oldh= " + oldh);*/
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }
}
