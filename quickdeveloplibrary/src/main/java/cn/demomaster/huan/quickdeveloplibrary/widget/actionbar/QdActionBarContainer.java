package cn.demomaster.huan.quickdeveloplibrary.widget.actionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import cn.demomaster.qdlogger_library.QDLogger;

@CoordinatorLayout.DefaultBehavior(QdActionBarBehavior.class)
public class QdActionBarContainer extends FrameLayout {
    private RecyclerView recyclerView;
    
    public QdActionBarContainer(@NonNull Context context) {
        super(context);
        init(null);
    }

    public QdActionBarContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public QdActionBarContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        return super.addViewInLayout(child, index, params);
    }

    private void init(AttributeSet attrs) {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                findRecyclerView(QdActionBarContainer.this);
            }
        });
    }

    /**
     * 查找唯一的一个RecyclerView
     *
     * @param child
     */
    private void findRecyclerView(View child) {
        if (child instanceof ViewGroup && !(child instanceof RecyclerView)) {
            for (int i = 0; i < ((ViewGroup) child).getChildCount(); i++) {
                View v = ((ViewGroup) child).getChildAt(i);
                findRecyclerView(v);
            }
        } else if (child instanceof RecyclerView) {
            recyclerView = (RecyclerView) child;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        QDLogger.d("onTouchEvent=" + event);
        /**
         * 把自身及其他子空间的触摸事件传递给recyclerView,其他控件如果不使用触摸滑动可以在ontouch中消耗掉即可
         */
        if (recyclerView != null) {
            recyclerView.dispatchTouchEvent(event);
        }
        return true;
        //return super.onTouchEvent(event);
    }

    private int mHeaderViewHeight;
    private int mRecycleViewHeight;
    private int mViewHeight;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        findRecyclerView(this);
        if (recyclerView == null) return;
        QDLogger.d("onSizeChanged w=" + w + ",h=" + h + ",oldw=" + oldw + ",oldh=" + oldh);
        if (w != oldw || h != oldh) {
            // mHeaderViewHeight = findViewById(R.id.header).getMeasuredHeight();
            mRecycleViewHeight = recyclerView.getMeasuredHeight();
            mViewHeight = mHeaderViewHeight + mRecycleViewHeight;
        }
        QDLogger.d("mHeaderViewHeight=" + mHeaderViewHeight);
    }

    public int getHeaderViewHeight() {
        return mHeaderViewHeight;
    }

    public int getViewHeight() {
        return mViewHeight;
    }
}
