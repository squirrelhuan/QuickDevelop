package cn.demomaster.huan.quickdeveloplibrary.widget.scroll;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

/**
 * Created by Squirrel桓 on 2019/1/29.
 */

public class QuickNestedScrollChild extends LinearLayout implements NestedScrollingChild2 {

    private NestedScrollingChildHelper mChildHelper;
    private int mViewHeight;
    private int mCanScrollY;
    private int mLastMotionY;
    /**
     * Used during scrolling to retrieve the new offset within the window.
     */
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];


    public QuickNestedScrollChild(Context context) {
        this(context, null);
    }

    public QuickNestedScrollChild(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickNestedScrollChild(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public QuickNestedScrollChild(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        this.mChildHelper = new NestedScrollingChildHelper(this);
        this.mChildHelper.setNestedScrollingEnabled(true);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return this.mChildHelper.startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll(int type) {
        this.mChildHelper.stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return this.mChildHelper.hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        return this.mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        return this.mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.mLastMotionY = (int) event.getY();
                this.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH);
                break;
            case MotionEvent.ACTION_MOVE:
                final int y = (int) event.getY();
                int deltaY = this.mLastMotionY - y;
                if (this.dispatchNestedPreScroll(0, deltaY, this.mScrollConsumed,
                        this.mScrollOffset, ViewCompat.TYPE_TOUCH)) {
                    deltaY -= this.mScrollConsumed[1];
                }
                this.scrollBy(0, deltaY);
                break;
            case MotionEvent.ACTION_UP:
                this.stopNestedScroll(ViewCompat.TYPE_TOUCH);
                break;
            case MotionEvent.ACTION_CANCEL:
                this.stopNestedScroll(ViewCompat.TYPE_TOUCH);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mViewHeight <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            this.mViewHeight = this.getMeasuredHeight();
        } else {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            this.mCanScrollY = this.getMeasuredHeight() - this.mViewHeight;
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        y = y < 0 ? 0 : y;
        y = y > this.mCanScrollY ? this.mCanScrollY : y;
        super.scrollTo(x, y);
    }
}