package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ListViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import cn.demomaster.qdlogger_library.QDLogger;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import static androidx.core.view.ViewCompat.TYPE_TOUCH;
import static androidx.customview.widget.ViewDragHelper.INVALID_POINTER;

/**
 * Created by CGQ on 2017/7/19.
 * <p>
 * 自定义控件为了解决scrollview 和 listview gridview 冲突
 *  解決嵌套滾動問題
 * @author CGQ
 */
public class QDListView extends ListView implements 
        NestedScrollingChild2, NestedScrollingChild3 {

    public QDListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    public QDListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QDListView(Context context) {
        super(context);
    }

    //弹性效果,maxOverScrollY最大滚出距离默认0
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY,
                scrollX, scrollY,
                scrollRangeX, scrollRangeY,
                maxOverScrollX, maxOverScrollY,
                isTouchEvent);
    }

    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /* 方案1 展示list view所有子view 相当于 linearlayout 失去了view复用优势
         int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
         super.onMeasure(widthMeasureSpec, expandSpec);
         */
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
        View parentView = (View) getParent();
        int parentHeight = parentView.getMeasuredHeight()
                -parentView.getPaddingTop()
                -parentView.getPaddingBottom()
                -layoutParams.topMargin
                -layoutParams.bottomMargin;
        //QDLogger.i("widthMode="+widthMode+",heightMode="+heightMode+",widthSize="+widthSize+",heightSize="+heightSize);
        if(heightMode == MeasureSpec.UNSPECIFIED){
            //resolveSizeAndState(maxWidth, widthMeasureSpec, childState)
            //super.onMeasure(widthMeasureSpec, heightSize);
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

           /* int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h2 = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            measure(w, h2);*/
           /* expandSpec = MeasureSpec.makeMeasureSpec(h2 >> 2, MeasureSpec.AT_MOST);
            QDLogger.i("h2="+h2);*/
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(2169, MeasureSpec.EXACTLY));
            //MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            int h = Math.min(parentHeight,getMeasuredHeight());
            //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            //setMeasuredDimension(widthSize, h);
            /*setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                    getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));*/
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        QDLogger.i("parentHeight="+parentHeight+",getMeasuredHeight()="+getMeasuredHeight());
        /*if(heightMode == MeasureSpec.EXACTLY){
        } else if(widthMode == MeasureSpec.AT_MOST){
        }*/
    }
    
    //判断是否可以上滑
    public boolean canScrollY(int dy) {
        boolean b = ListViewCompat.canScrollList(this,  dy);//大于0则判断是否触底，小于0则判断是否触顶
        QDLogger.e("canScrollY=" +b+",dy="+dy);
        return b;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean b =super.dispatchTouchEvent(ev);
        //QDLogger.i("子view dispatchTouchEvent b="+b);
        return b;
    }
    
    private int mScrollPointerId = INVALID_POINTER;
    private int mInitialTouchX;
    private int mInitialTouchY;
    private int mLastTouchX;
    private int mLastTouchY;
    private int mTouchSlop;

    private int mScrollState = SCROLL_STATE_IDLE;

    /**
     * The RecyclerView is currently being dragged by outside input such as user touch input.
     */
    public static final int SCROLL_STATE_DRAGGING = 1;

    /**
     * The RecyclerView is currently animating to a final position while not under
     * outside control.
     */
    public static final int SCROLL_STATE_SETTLING = 2;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
       // QDLogger.i("onInterceptTouchEvent y="+e.getY());
        final boolean canScrollHorizontally = false;//mLayout.canScrollHorizontally();
        final boolean canScrollVertically = true;// mLayout.canScrollVertically();
        final int action = e.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mScrollPointerId = e.getPointerId(0);
                mInitialTouchX = mLastTouchX = (int) (e.getX() + 0.5f);
                mInitialTouchY = mLastTouchY = (int) (e.getY() + 0.5f);

                mNestedOffsets[0] = mNestedOffsets[1] = 0;

                int nestedScrollAxis = ViewCompat.SCROLL_AXIS_NONE;
                if (canScrollHorizontally) {
                    nestedScrollAxis |= ViewCompat.SCROLL_AXIS_HORIZONTAL;
                }
                if (canScrollVertically) {
                    nestedScrollAxis |= ViewCompat.SCROLL_AXIS_VERTICAL;
                }
                //startNestedScroll(nestedScrollAxis, TYPE_TOUCH);
            } break;
            case MotionEvent.ACTION_MOVE: {
                final int index = e.findPointerIndex(mScrollPointerId);

                final int x = (int) (e.getX(index) + 0.5f);
                final int y = (int) (e.getY(index) + 0.5f);

                if (mScrollState != SCROLL_STATE_DRAGGING) {
                    final int dx = x - mInitialTouchX;
                    final int dy = y - mInitialTouchY;
                    boolean startScroll = false;
                    if (canScrollHorizontally && Math.abs(dx) > mTouchSlop) {
                        mLastTouchX = x;
                        startScroll = true;
                    }
                    if (canScrollVertically && Math.abs(dy) > mTouchSlop) {
                        mLastTouchY = y;
                        startScroll = true;
                    }
                    if (startScroll) {
                        setScrollState(SCROLL_STATE_DRAGGING);
                    }
                }
            } break;
        }

       // QDLogger.println("子视图 onInterceptTouchEvent="+true);
        return true;// mScrollState == SCROLL_STATE_DRAGGING;
       // return super.onInterceptTouchEvent(e);
    }

    void setScrollState(int state) {
        if (state == mScrollState) {
            return;
        }
        mScrollState = state;
        if (state != SCROLL_STATE_SETTLING) {
            //stopScrollersInternal();
        }
        //dispatchOnScrollStateChanged(state);
    }

    final int[] mReusableIntPair = new int[2];
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        final boolean canScrollHorizontally = false;//mLayout.canScrollHorizontally();
        final boolean canScrollVertically = true;// mLayout.canScrollVertically();

        boolean canScrollY =true;
        switch (e.getActionMasked()){
            case MotionEvent.ACTION_DOWN: {
                mScrollPointerId = e.getPointerId(0);
                mInitialTouchX = mLastTouchX = (int) (e.getX() + 0.5f);
                mInitialTouchY = mLastTouchY = (int) (e.getY() + 0.5f);

                int nestedScrollAxis = ViewCompat.SCROLL_AXIS_NONE;
                if (canScrollHorizontally) {
                    nestedScrollAxis |= ViewCompat.SCROLL_AXIS_HORIZONTAL;
                }
                if (canScrollVertically) {
                    nestedScrollAxis |= ViewCompat.SCROLL_AXIS_VERTICAL;
                }
                startNestedScroll(nestedScrollAxis, TYPE_TOUCH);
            } break;
            case MotionEvent.ACTION_MOVE:
                final int index = e.findPointerIndex(mScrollPointerId);
                final int x = (int) (e.getX(index) + 0.5f);
                final int y = (int) (e.getY(index) + 0.5f);
                int dx = mLastTouchX - x;
                int dy = mLastTouchY - y;
                canScrollY = canScrollY(dy>0?1:-1);
                //QDLogger.e("子view dy3 =" + dy+",canScrollY="+canScrollY);
                if(canScrollY) {
                    //QDLogger.e("子view dy2 =" + dy);
                    //if (mScrollState == SCROLL_STATE_DRAGGING) {
                    mReusableIntPair[0] = 0;
                    mReusableIntPair[1] = 0;
                    mScrollOffset[1] = 2;
                    //QDLogger.println("子view onTouchEvent ="+dy);
                    if (dispatchNestedPreScroll(
                            canScrollHorizontally ? dx : 0,
                            canScrollVertically ? dy : 0,
                            mReusableIntPair, mScrollOffset, TYPE_TOUCH
                    )) {
                        dx -= mReusableIntPair[0];
                        dy -= mReusableIntPair[1];
                        // Updated the nested offsets
                        mNestedOffsets[0] += mScrollOffset[0];
                        mNestedOffsets[1] += mScrollOffset[1];
                        // Scroll has initiated, prevent parents from intercepting
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                mLastTouchX = x ;
                mLastTouchY = y ;
                //}
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
        }
        boolean b = super.onTouchEvent(e);
        //QDLogger.println("子视图 onTouchEvent="+b+",canScrollY="+canScrollY);
        return canScrollY;
    }
    
    @Override
    public void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type, @NonNull int[] consumed) {
        QDLogger.i("dispatchNestedScroll ");
        getScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow, type, consumed);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        //QDLogger.i("startNestedScroll "+axes +",type="+type);
        //return getScrollingChildHelper().startNestedScroll(axes, type);
        return true;
    }

    @Override
    public void stopNestedScroll(int type) {
        QDLogger.i("stopNestedScroll "+type);
        getScrollingChildHelper().stopNestedScroll(type);
    }
    @Override
    public boolean hasNestedScrollingParent(int type) {
        QDLogger.i("hasNestedScrollingParent "+type);
        return true;// getScrollingChildHelper().hasNestedScrollingParent(type);
        //return false;
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable @org.jetbrains.annotations.Nullable int[] offsetInWindow, int type) {
        QDLogger.i("dispatchNestedScroll "+type);
        return getScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
        //return false;
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
      //  boolean b = getScrollingChildHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
//        QDLogger.i("dispatchNestedPreScroll canScrollUp="+canScrollY(1)+",canScrollDown="+canScrollY(-1) +",dy="+dy);//上拉dy>0  下拉 dy<0
        //canScrollY(int dy) {
        //    return ListViewCompat.canScrollList(this,  dy);//大于0则判断是否触底，小于0则判断是否触顶

        consumed[0] = 0;
        consumed[1] = 0;
        if(dy>0&&!canScrollY(1)){
            return false;
        }else if(dy<0&&!canScrollY(-1)){
            return false;
        }
        return true;
    }

    private NestedScrollingChildHelper mScrollingChildHelper;
    private final int[] mScrollOffset = new int[2];
    private final int[] mNestedOffsets = new int[2];
    private NestedScrollingChildHelper getScrollingChildHelper() {
        if (mScrollingChildHelper == null) {
            mScrollingChildHelper = new NestedScrollingChildHelper(this);
        }
        return mScrollingChildHelper;
    }
}