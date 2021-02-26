package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 *
 */
public class WrapLayout extends FrameLayout {
    public WrapLayout(@NonNull Context context) {
        super(context);
    }

    public WrapLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WrapLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    boolean mMeasureAllChildren = false;
    private final ArrayList<View> mMatchParentChildren = new ArrayList<>(1);
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        int count = getChildCount();

        final boolean measureMatchParentChildren =
                MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY ||
                        MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
        mMatchParentChildren.clear();

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (mMeasureAllChildren || child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth,
                        child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                maxHeight = Math.max(maxHeight,
                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                if (measureMatchParentChildren) {
                    if (lp.width == LayoutParams.MATCH_PARENT ||
                            lp.height == LayoutParams.MATCH_PARENT) {
                        mMatchParentChildren.add(child);
                    }
                }
            }
        }

        // Account for padding too
        //maxWidth += getPaddingLeftWithForeground() + getPaddingRightWithForeground();
        //maxHeight += getPaddingTopWithForeground() + getPaddingBottomWithForeground();

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        // Check against our foreground's minimum height and width
        final Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
            maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
        }

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));

        count = mMatchParentChildren.size();
        if (count > 1) {
            for (int i = 0; i < count; i++) {
                final View child = mMatchParentChildren.get(i);
                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                final int childWidthMeasureSpec;
                if (lp.width == LayoutParams.MATCH_PARENT) {
                    final int width = Math.max(0, getMeasuredWidth()
                            - lp.leftMargin - lp.rightMargin);
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                            width, MeasureSpec.EXACTLY);
                } else {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                                    lp.leftMargin + lp.rightMargin,
                            lp.width);
                }

                final int childHeightMeasureSpec;
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    final int height = Math.max(0, getMeasuredHeight()
                            - lp.topMargin - lp.bottomMargin);
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                            height, MeasureSpec.EXACTLY);
                } else {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                                    lp.topMargin + lp.bottomMargin,
                            lp.height);
                }

                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }

/*
        //根据提供的测量值提取模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //根据提供的测量值提取大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        int maxHeight = heightSize;
        int maxWidth = widthSize;
        int childState = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child != null) {
                maxWidth = Math.max(maxWidth,child.getMeasuredWidth());
                maxHeight = Math.max(maxHeight,child.getMeasuredHeight());
            }
        }

        QDLogger.e("widthMode="+widthMode+",heightMode="+heightMode);
        //依据得到的specMode值，
        // 精确模式（MeasureSpec.EXACTLY）：在这种模式下，尺寸的值是多少，那么这个组件的长或宽就是多少，对应 MATCH_PARENT 和确定的值。
        // 最大模式（MeasureSpec.AT_MOST）：这个也就是父组件，能够给出的最大的空间，当前组件的长或宽最大只能为这么大，当然也可以比这个小。对应 WRAP_CONETNT。
        // 未指定模式（MeasureSpec.UNSPECIFIED）：这个就是说，当前组件，可以随便用空间，不受限制。
        if(widthMode == MeasureSpec.EXACTLY){
            //不需要重新计算
            widthMeasureSpec = widthSize;
        }else {
            //重新计算，这里计算你需要绘制的视图的宽
            widthMeasureSpec = maxWidth;
        }

        if(heightMode == MeasureSpec.EXACTLY){
            heightMeasureSpec = heightSize;
        }else if(heightMode == MeasureSpec.AT_MOST){
            heightMeasureSpec = maxHeight;
        }else {
            heightMeasureSpec = maxHeight;
        }
        //然后调用自setMeasuredDimension()方法将测量好的宽高保存
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);

       */
/* setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));
        measureChildren(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState));*//*

        //measureChildren(widthMeasureSpec, heightMeasureSpec);
        //super.onMeasure(widthMeasureSpec,heightMeasureSpec);

*/
/*
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child != null) {
                maxWidth = Math.max(maxWidth,child.getMeasuredWidth());
                maxHeight = Math.max(maxHeight,child.getMeasuredHeight());
            }
        }
         measureChildren(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState));*//*

        // measureChildren(MeasureSpec.makeMeasureSpec(maxWidth, widthMeasureSpec),
        //        MeasureSpec.makeMeasureSpec(maxHeight, heightMeasureSpec));

*/

    }
}
