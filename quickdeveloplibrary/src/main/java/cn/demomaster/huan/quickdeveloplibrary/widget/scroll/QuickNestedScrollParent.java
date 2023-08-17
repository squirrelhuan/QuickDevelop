package cn.demomaster.huan.quickdeveloplibrary.widget.scroll;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

import cn.demomaster.qdlogger_library.QDLogger;


/**
 * Created by Squirrel桓 on 2019/1/29.
 */
public class QuickNestedScrollParent extends LinearLayout implements NestedScrollingParent2 {

    private NestedScrollingParentHelper mParentHelper;
    private int mFirstChildHeight;

    public QuickNestedScrollParent(Context context) {
        this(context, null);
    }

    public QuickNestedScrollParent(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickNestedScrollParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public QuickNestedScrollParent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        this.mParentHelper = new NestedScrollingParentHelper(this);
    }

    public void setFirstChildHeight(int firstChildHeight) {
        this.mFirstChildHeight = firstChildHeight;
        //QDLogger.println("setFirstChildHeight ="+mFirstChildHeight);
    }
    private int mScrollViewHeight;
    public void setScrollViewHeight(int mScrollViewHeight) {
        this.mScrollViewHeight = mScrollViewHeight;
        requestLayout();
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final View firstChild = this.getChildAt(0);
        if (firstChild == null)
            throw new IllegalStateException(String.format("%s must own a child view", QuickNestedScrollParent.class.getSimpleName()));
        firstChild.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                QuickNestedScrollParent.this.mFirstChildHeight = firstChild.getMeasuredHeight();
                QDLogger.println("mFirstChildHeight ="+mFirstChildHeight);
                firstChild.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View nestedScrollingChild = getNestedScrollingChild();
        measureChild(nestedScrollingChild,widthMeasureSpec,heightMeasureSpec);
    }

    public View getNestedScrollingChild(){
        for(int i=0;i<getChildCount();i++){
            if(getChildAt(i) instanceof NestedScrollingChild) {
               return getChildAt(i);
            }
        }
        return null;
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        if(child instanceof NestedScrollingChild){
            //QDLogger.println("mScrollViewHeight="+mScrollViewHeight);
            super.measureChild(child,
                    parentWidthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(getMeasuredHeight()-mScrollViewHeight, MeasureSpec.EXACTLY));
        }else {
            super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        // only cares vertical scroll
        return (ViewCompat.SCROLL_AXIS_VERTICAL & axes) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        this.mParentHelper.onNestedScrollAccepted(child, target, axes, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        boolean isFirstChildVisible = (dy > 0 && this.getScrollY() < this.mFirstChildHeight)
                || (dy < 0 && target.getScrollY() <= 0);
        View nestedScrollingChild = getNestedScrollingChild();
        //QDLogger.println("getScrollY="+this.getScrollY()+",sY="+sY+",dy="+dy+",isFirstChildVisible="+isFirstChildVisible+",top="+nestedScrollingChild.getTop()+",y="+nestedScrollingChild.getY());
        setScrollViewHeight(nestedScrollingChild.getTop()-this.getScrollY());
        if(dy<0){//下拉
            //View nestedScrollingChild = getNestedScrollingChild();
            if(sY>0&&!nestedScrollingChild.canScrollVertically(dy)){
                consumed[1] = dy;
                this.scrollBy(0, dy);
            }
        }else {//上拉
            if (isFirstChildVisible) {//consume dy
                consumed[1] = dy;
                this.scrollBy(0, dy);
            }
        }
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
    }


    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        this.mParentHelper.onStopNestedScroll(target, type);
    }
    
    int sY = 0;
    @Override
    public void scrollTo(int x, int y) {
        y = y < 0 ? 0 : y;
        y = y > this.mFirstChildHeight ? this.mFirstChildHeight : y;
        sY = y;
        super.scrollTo(x, y);
    }
}