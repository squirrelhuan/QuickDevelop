package cn.demomaster.huan.quickdeveloplibrary.widget.layout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import java.util.HashMap;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.qdrouter_library.base.OnReleaseListener;

/**
 * 显示隐藏布局,当子空间显示或者隐藏状态改变时触发动画
 */
public class VisibleLayout extends FrameLayout implements OnReleaseListener {
    public VisibleLayout(@NonNull Context context) {
        super(context);
        init(null);
    }

    public VisibleLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VisibleLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VisibleLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    boolean hide_with_child;//跟随子view一起隐藏

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.VisibleLayout);
            gravity = a.getInteger(R.styleable.VisibleLayout_start_from, gravity);
            hide_with_child = a.getBoolean(R.styleable.VisibleLayout_hide_with_child, hide_with_child);

            a.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        VisibilityMap.put(child.hashCode(), child.getVisibility());
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        VisibilityMap.put(child.hashCode(), child.getVisibility());
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        VisibilityMap.put(child.hashCode(), child.getVisibility());
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        VisibilityMap.put(child.hashCode(), child.getVisibility());
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        VisibilityMap.put(child.hashCode(), child.getVisibility());
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
    }

    Map<Integer, Integer> VisibilityMap;

    @Override
    public void requestLayout() {
        super.requestLayout();
        if (isRunning) {
            return;
        }
        //QDLogger.e("子view显示状态改变造成的重新布局");
        if (VisibilityMap == null) {
            VisibilityMap = new HashMap<>();
        }
        if (VisibilityMap.size() > 0) {

        }
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (VisibilityMap.containsKey(view.hashCode())) {
                int v = VisibilityMap.get(view.hashCode());
                if (v != view.getVisibility()) {//上一次保存的可视状态
                    onChildViewVisiableChanged(i, view, view.getVisibility());
                }
            }
            VisibilityMap.put(view.hashCode(), view.getVisibility());
            //QDLogger.d(view.hashCode()+":"+view.getVisibility());
        }
    }

    private void onChildViewVisiableChanged(int i, View view, int visibility) {
        //QDLogger.e("第"+i+"个child，显示状态已改变触发，目标状态="+(visibility==VISIBLE?"显示":"隐藏"));
        doAnimation(view, visibility);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        //QDLogger.e("onWindowVisibilityChanged");
    }

    /**
     * 伸缩方向
     */
    int gravity = Gravity.LEFT;

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //int width = getMeasuredWidth();
        //int height = getMeasuredHeight();
        for (int i = 0; i < 1; i++) {
            View child = getChildAt(i);
            switch (gravity) {
                case Gravity.LEFT:
                    child.setTranslationX(-animatedValue);
                    break;
                case Gravity.RIGHT:
                    child.setTranslationX(animatedValue);
                    break;
                case Gravity.TOP:
                    child.setTranslationY(animatedValue);
                    break;
                case Gravity.BOTTOM:
                    child.setTranslationY(-animatedValue);
                    break;
            }
        }
    }

    ValueAnimator animator;
    float animatedValue;
    boolean isRunning = false;
    private Interpolator mInterpolator = new FastOutSlowInInterpolator();
    private int mDuration = 300;

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public void doAnimation(View childView, final int visibility) {
        setVisibility(VISIBLE);
        childView.setVisibility(VISIBLE);
        //QDLogger.e("执行"+(visibility==VISIBLE?"显示":"隐藏"+"动画"));
        isRunning = true;
        float start = 0;
        float end = 0;
        if (visibility == VISIBLE) {
            switch (gravity) {
                case Gravity.LEFT:
                    start = -childView.getTranslationX();
                    end = 0;
                    break;
                case Gravity.RIGHT:
                    start = childView.getTranslationX();
                    end = 0;
                    break;
                case Gravity.TOP:
                    start = childView.getTranslationY();
                    end = 0;
                    break;
                case Gravity.BOTTOM:
                    start = -childView.getTranslationY();
                    end = 0;
                    break;
            }
        } else {
            animator = ValueAnimator.ofInt(getChildMaxWidth(), 0);
            switch (gravity) {
                case Gravity.LEFT:
                    end = getChildMaxWidth();
                    start = childView.getTranslationX();
                    break;
                case Gravity.RIGHT:
                    start = childView.getTranslationX();
                    end = getWidth();
                    break;
                case Gravity.TOP:
                    start = childView.getTranslationY();
                    end = -getChildMaxHeight();
                    break;
                case Gravity.BOTTOM:
                    start = childView.getTranslationY();
                    end = -getChildMaxHeight();
                    break;
            }
        }
        String str = null;
        switch (gravity) {
            case Gravity.LEFT:
                str = "居左";
                break;
            case Gravity.TOP:
                str = "顶部";
                break;
            case Gravity.RIGHT:
                str = "居右";
                break;
            case Gravity.BOTTOM:
                str = "底部";
                break;
        }
        // QDLogger.e("动画"+str+",start="+start+",end="+end);
        final int m1 = (int) start;
        final int m2 = (int) end;
        animator = ValueAnimator.ofInt((int) start, (int) end);
        animator.setInterpolator(mInterpolator);
        animator.setDuration(mDuration).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatedValue = (int) animation.getAnimatedValue();
                if (onVisiableChangedListener != null) {
                    float progress = (animatedValue - m1) / (m2 - m1);
                    onVisiableChangedListener.onVisiableChanaged(visibility, progress);
                }
                requestLayout();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isRunning = false;
                if (hide_with_child && (visibility == GONE || visibility == View.INVISIBLE)) {
                    setVisibility(INVISIBLE);
                }
/*
                if(onVisiableChangedListener!=null){
                    onVisiableChangedListener.onVisiableChanaged(visibility,1);
                }*/

                childView.setVisibility(visibility);
                VisibilityMap.put(childView.hashCode(), visibility);
            }
        });
    }

    OnVisiableChangedListener onVisiableChangedListener;

    public void setOnVisiableChangedListener(OnVisiableChangedListener onVisiableChangedListener) {
        this.onVisiableChangedListener = onVisiableChangedListener;
    }

    public interface OnVisiableChangedListener {
        void onVisiableChanaged(int visibility, float progress);
    }

    /**
     * 获取子view最大宽度
     *
     * @return
     */
    private int getChildMaxWidth() {
        int maxWidth = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            maxWidth = Math.max(maxWidth, child.getWidth());
        }
        return maxWidth;
    }

    /**
     * 获取子view最大高度
     *
     * @return
     */
    private int getChildMaxHeight() {
        int maxHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            maxHeight = Math.max(maxHeight, child.getHeight());
        }
        return maxHeight;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onRelease();
    }

    @Override
    public void onRelease() {
        if (VisibilityMap != null) {
            VisibilityMap.clear();
        }
        if (animator != null) {
            animator.cancel();
        }
    }
}
