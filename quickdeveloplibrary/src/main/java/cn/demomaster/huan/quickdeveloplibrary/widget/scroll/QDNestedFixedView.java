package cn.demomaster.huan.quickdeveloplibrary.widget.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import static cn.demomaster.huan.quickdeveloplibrary.helper.AudioRecordHelper.TAG;

/**
 * @author squirrel桓
 * @date 2019/1/29.
 * description：滚动导航固定部分视图
 */
public class QDNestedFixedView extends FrameLayout {
    public QDNestedFixedView( Context context) {
        super(context);
        init();
    }

    public QDNestedFixedView( Context context,  AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QDNestedFixedView( Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                maxHeight = getMeasuredHeight();
            }
        });
    }

    private int minHeight = 100;
    private int maxHeight;
    private float progress;

    public float getProgress() {
        progress =  (float)(getMeasuredHeight()-minHeight)/(float)(maxHeight-minHeight);
        Log.d(TAG, "getProgress: "+progress+",getMeasuredHeight="+getMeasuredHeight()+",maxHeight="+maxHeight+",minHeight="+minHeight);
        return progress<0?0:progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    private OnVisibleHeightChangeListener onVisibleHeightChangeListener;
    public void setOnVisibleHeightChangeListener(OnVisibleHeightChangeListener onVisibleHeightChangeListener) {
        this.onVisibleHeightChangeListener = onVisibleHeightChangeListener;
    }

    public OnVisibleHeightChangeListener getOnVisibleHeightChangeListener() {
        return onVisibleHeightChangeListener;
    }

    /**
     * 可视高度改变时触发
     */
    public static interface OnVisibleHeightChangeListener{
       void onChange(int px,int py);
    }
}
