package cn.demomaster.huan.quickdeveloplibrary.widget.scroll;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static cn.demomaster.huan.quickdeveloplibrary.helper.AudioRecordHelper.TAG;


/**
 * Created by Squirrel桓 on 2019/1/29.
 */

public class QDNestedScrollParent extends LinearLayout implements NestedScrollingParent {
    private QDNestedFixedView fixedView;
    private QDNestedScrollChild myNestedScrollChild;
    private NestedScrollingParentHelper mNestedScrollingParentHelper;
    //private int hideHeight;//即将要隐藏部分的高度
    //private int minHeight;//顶部或者底部空余的最小距离

    public QDNestedScrollParent(Context context) {
        super(context);
    }

    public QDNestedScrollParent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }

    public QDNestedFixedView getFixedView() {
        return fixedView;
    }

    public void setFixedView(QDNestedFixedView fixedView) {
        this.fixedView = fixedView;
    }

    //获取子view
    @Override
    protected void onFinishInflate() {
        fixedView = (QDNestedFixedView) getChildAt(0);
        myNestedScrollChild = (QDNestedScrollChild) getChildAt(1);
        //hideHeight = fixedView.getMinHeight();

    }

    //在此可以判断参数target是哪一个子view以及滚动的方向，然后决定是否要配合其进行嵌套滚动
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if (target instanceof QDNestedScrollChild) {
            return true;
        }
        return false;
    }


    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
    }

    //先于child滚动
    //前3个为输入参数，最后一个是输出参数
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (showImg(dy) || hideImg(dy)) {//如果需要显示或隐藏图片，即需要自己(parent)滚动
            scrollBy(0, -dy);//滚动
            consumed[1] = dy;//告诉child我消费了多少
            if (fixedView != null && fixedView.getOnVisibleHeightChangeListener() != null) {
                fixedView.getOnVisibleHeightChangeListener().onChange(dx, dy);
            }
        }
    }

    //后于child滚动
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    //返回值：是否消费了fling
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    //返回值：是否消费了fling
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    //下拉的时候是否要向下滚动以显示图片
    public boolean showImg(int dy) {
        if (dy > 0) {
            if (getScrollY() > 0 && myNestedScrollChild.getScrollY() == 0) {
                return true;
            }
        }

        return false;
    }

    //上拉的时候，是否要向上滚动，隐藏图片
    public boolean hideImg(int dy) {
        if (dy < 0) {
            int toTop = fixedView.getMeasuredHeight()- fixedView.getMinHeight();
            Log.d(TAG, "hideImg toTop: "+toTop );
            //toTop = fixedView.getMinHeight();
            if (getScrollY() < toTop) {
                return true;
            }
        }
        return false;
    }

    //scrollBy内部会调用scrollTo
    //限制滚动范围
    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        int toTop = fixedView.getMaxHeight();
        Log.d(TAG, "scrollTo toTop: "+toTop);
        //toTop = fixedView.getMinHeight();
        if (y > toTop) {
            y = toTop;
        }

        super.scrollTo(x, y);
    }
}