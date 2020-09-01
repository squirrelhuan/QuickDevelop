package cn.demomaster.huan.quickdeveloplibrary.widget.layout;


import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.qdlogger_library.QDLogger;

public class QDFrameLayout extends android.widget.FrameLayout {
    public QDFrameLayout(@NonNull Context context) {
        super(context);
    }

    public QDFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QDFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public QDFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /*@Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutChildren(left, top, right, bottom, false *//* no force left gravity *//*);
        }
    }*/

    @Override
    public void setChildrenDrawingOrderEnabled(boolean enabled) {
        super.setChildrenDrawingOrderEnabled(enabled);
    }

    private static final int DEFAULT_CHILD_GRAVITY = Gravity.TOP | Gravity.START;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void layoutChildren(int left, int top, int right, int bottom, boolean forceLeftGravity) {
        final int count = getChildCount();

        final int parentLeft = getPaddingLeft();
        final int parentRight = right - left - getPaddingRight();

        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();
        setChildrenDrawingOrderEnabled(true);
        IndexZMap.clear();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int indexz = 0;
            if (child != null && child.getTag() != null) {
                try {
                    indexz = (int) child.getTag();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        child.setZ(indexz);
                    }
                } catch (Exception e) {
                    QDLogger.e(e);
                }
            }
            if (child != null) {
                List<Integer> integerList = getListByIndexZ(indexz);
                integerList.add(child.getId());
                IndexZMap.put(indexz, integerList);
            }
        }
        for (int inz = 0; inz < IndexZMap.size(); inz++) {
            List<Integer> list = getListByIndexZ(inz);
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                if (child.getVisibility() != GONE) {
                    final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                    final int width = child.getMeasuredWidth();
                    final int height = child.getMeasuredHeight();

                    int childLeft;
                    int childTop;

                    int gravity = lp.gravity;
                    if (gravity == -1) {
                        gravity = DEFAULT_CHILD_GRAVITY;
                    }

                    final int layoutDirection = getLayoutDirection();
                    final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
                    final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

                    switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                        case Gravity.CENTER_HORIZONTAL:
                            childLeft = parentLeft + (parentRight - parentLeft - width) / 2 +
                                    lp.leftMargin - lp.rightMargin;
                            break;
                        case Gravity.RIGHT:
                            if (!forceLeftGravity) {
                                childLeft = parentRight - width - lp.rightMargin;
                                break;
                            }
                        case Gravity.LEFT:
                        default:
                            childLeft = parentLeft + lp.leftMargin;
                    }

                    switch (verticalGravity) {
                        case Gravity.TOP:
                            childTop = parentTop + lp.topMargin;
                            break;
                        case Gravity.CENTER_VERTICAL:
                            childTop = parentTop + (parentBottom - parentTop - height) / 2 +
                                    lp.topMargin - lp.bottomMargin;
                            break;
                        case Gravity.BOTTOM:
                            childTop = parentBottom - height - lp.bottomMargin;
                            break;
                        default:
                            childTop = parentTop + lp.topMargin;
                    }

                    child.layout(childLeft, childTop, childLeft + width, childTop + height);
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
    }

    Map<Integer, List<Integer>> IndexZMap = new LinkedHashMap<>();

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        //QDLogger.d("addView:" + index);
        super.addView(child, index, params);
    }

    private List<Integer> getListByIndexZ(int indexz) {
        if (IndexZMap.containsKey(indexz)) {
            return IndexZMap.get(indexz);
        }
        return new ArrayList<>();
    }

}
