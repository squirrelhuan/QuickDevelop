package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.customview.widget.ViewDragHelper;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * 可拖动子布局
 */
public class HorizontalDragView extends LinearLayout {

    private static final String TAG = HorizontalDragView.class.getSimpleName();

    private ViewDragHelper mLeftViewDragHelper;

    private ViewDragHelper.Callback mLeftDragCallback;

    public HorizontalDragView(Context context) {
        this(context, null);
    }

    public HorizontalDragView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalDragView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        initDragCallBack();
        mLeftViewDragHelper = ViewDragHelper.create(this, 1.0f, mLeftDragCallback);
        // 设置为可以捕获屏幕左边的滑动
        mLeftViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() >= 2) {
            leftView = getChildAt(0);
            rightView = getChildAt(1);
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    getMeasuredWidth() - leftView.getMeasuredWidth() - leftView.getLeft(), MeasureSpec.EXACTLY);
            rightView.measure(childWidthMeasureSpec, heightMeasureSpec);
        }
    }

    int leftOffset;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 锁屏后再点亮屏幕会调用一下onLayout，不加判断会让布局还原(为什么???)
        //super.onLayout(changed, l, t, r, b);
        if (getChildCount() >= 2) {
            leftView = getChildAt(0);
            rightView = getChildAt(1);
            leftOffset = leftView.getLeft();
            //QDLogger.println("onLayout leftOffset=" + leftOffset);
            leftView.layout(l + leftOffset, t, l + leftView.getMeasuredWidth() + leftOffset, b);
            rightView.layout(leftView.getLeft() + leftView.getMeasuredWidth(), t, r, b);
        }
    }

    View leftView;
    View rightView;
    boolean alignParent = true;//是否受父窗体约束

    private void initDragCallBack() {
        mLeftDragCallback = new ViewDragHelper.Callback() {
            /**
             * 返回true，表示传入的View可以被拖动
             */
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                if (getChildCount() >= 2) {
                    leftView = getChildAt(0);
                    rightView = getChildAt(1);
                    return true;
                }
                return false;
            }

            /**
             * 传入View即将到达的位置(left)，返回值为真正到达的位置
             */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (child.equals(leftView)) {
                    //QDLogger.println(TAG, "左边 到达" + left);
                    return Math.max(Math.min(0, left), -leftView.getMeasuredWidth());
                } else if (child.equals(rightView)) {
                    //QDLogger.println(TAG, "右边 到达" + left);
                    if (dx > 0) {
                        return Math.min(leftView.getMeasuredWidth(), left);
                    } else {
                        return Math.max(0, left);
                    }
                }
                return left;
            }

            /**
             * 传入View即将到达的位置(top)，返回值为真正到达的位置
             */
            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return 0;
            }

            /**
             * 返回横向能拖动的长度，默认返回0，如果被拖动的View设置了点击事件，返回0会不响应点击事件
             */
            @Override
            public int getViewHorizontalDragRange(View child) {
                return leftView.getMeasuredWidth();
            }

            /**
             * 返回纵向能拖动的长度，默认返回0，如果被拖动的View设置了点击事件，返回0会不响应点击事件
             */
            @Override
            public int getViewVerticalDragRange(View child) {
                return 0;
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                Log.i(TAG, "onEdgeDragStarted;" + edgeFlags);
                // 当从屏幕左边开始滑动的时候，开始滑动第一个子控件
                mLeftViewDragHelper.captureChildView(getChildAt(0), pointerId);
            }

            /**
             * 当手指离开以后的回调
             *
             * @param releasedChild 子View
             * @param xvel X轴的速度
             * @param yvel Y轴的速度
             */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                // 手指松开以后自动回到原始位置
                Log.i(TAG, "xvel = " + xvel + ",yvel=" + yvel);
                int scrollRange = leftView.getMeasuredWidth();
                if (releasedChild.equals(leftView)) {
                    if (xvel > 10) {
                        mLeftViewDragHelper.settleCapturedViewAt(0, releasedChild.getTop());
                    } else if (xvel < -10) {
                        mLeftViewDragHelper.settleCapturedViewAt(-scrollRange, releasedChild.getTop());
                    } else {
                        if ((leftView.getLeft() > -scrollRange / 2)) {
                            mLeftViewDragHelper.settleCapturedViewAt(0, releasedChild.getTop());
                        } else {
                            mLeftViewDragHelper.settleCapturedViewAt(-scrollRange, releasedChild.getTop());
                        }
                    }
                } else if (releasedChild.equals(rightView)) {
                    if (xvel > 10) {
                        mLeftViewDragHelper.settleCapturedViewAt(scrollRange, releasedChild.getTop());
                    } else if (xvel < -10) {
                        mLeftViewDragHelper.settleCapturedViewAt(0, releasedChild.getTop());
                    } else {
                        if ((leftView.getLeft() > -scrollRange / 2)) {
                            mLeftViewDragHelper.settleCapturedViewAt(scrollRange, releasedChild.getTop());
                        } else {
                            mLeftViewDragHelper.settleCapturedViewAt(0, releasedChild.getTop());
                        }
                    }
                }
                invalidate();
            }

            /**
             * 当某一个View在动的时候的回调，不管是用户手动滑动，还是使用settleCapturedViewAt或者smoothSlideViewTo，都会回调这里
             */
            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (changedView.equals(leftView)) {
                    //QDLogger.println(TAG, "左边" + "left=" + left + ",top=" + top + ",dx=" + dx + ",dy=" + dy);
                    rightView.setLeft(left + leftView.getMeasuredWidth());
                    rightView.setRight(getMeasuredWidth());
                } else if (changedView.equals(rightView)) {
                    //QDLogger.println(TAG, "右边" + "left=" + left + ",top=" + top + ",dx=" + dx + ",dy=" + dy);
                    int w = leftView.getMeasuredWidth();
                    leftView.setLeft(left - w);
                    leftView.setRight(left);
                }

                /*int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                        getMeasuredHeight(), MeasureSpec.EXACTLY);
                int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                        getMeasuredWidth()-leftView.getMeasuredWidth()-leftView.getLeft(), MeasureSpec.EXACTLY);
                rightView.measure(childWidthMeasureSpec, heightMeasureSpec);*/
                //rightView.invalidate();
                //rightView.requestLayout();
                //invalidate();
                requestLayout();
            }
        };
    }

    @Override
    public void computeScroll() {
        if (mLeftViewDragHelper != null && mLeftViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mLeftViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mLeftViewDragHelper.processTouchEvent(event);
        return true;
    }
}
