package cn.demomaster.huan.quickdeveloplibrary.view.banner.qdlayout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.LinkedHashMap;
import java.util.Map;

public class PercentLayout extends FrameLayout {
    /*int row = 3;
    int colunms = 3;

    public void setRow(int row) {
        this.row = row;
    }

    public void setColunms(int colunms) {
        this.colunms = colunms;
    }*/

    public PercentLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public PercentLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PercentLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PercentLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        final Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
            maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
        }

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //int cell_height = (height / row);//转int默认向下取值
        //int cell_width = (width / colunms);//转int默认向下取值
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);

                int child_h;
                int child_w;
                //QDLogger.e("h1="+(viewPosionConfigMap.get(child).y+viewPosionConfigMap.get(child).height)+",h2="+row);

                child_h = (int) (viewPosionConfigMap.get(child).height * height);
                child_w = (int) (viewPosionConfigMap.get(child).width * width);

                final int childWidthMeasureSpec;
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                        child_w, MeasureSpec.EXACTLY);

                final int childHeightMeasureSpec;
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                        child_h, MeasureSpec.EXACTLY);
                // QDLogger.i(i+",width="+width+",height="+height+",h="+child_h+",w="+child_w);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // super.onLayout(changed, left, top, right, bottom);
        layoutChildren(left, top, right, bottom, false /* no force left gravity */);
    }

    void layoutChildren(int left, int top, int right, int bottom, boolean forceLeftGravity) {
        final int count = getChildCount();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                // final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();
                PosionConfig posionConfig = viewPosionConfigMap.get(child);
                if (posionConfig != null) {
                    int childLeft = (int) (posionConfig.x * getMeasuredWidth());
                    int childTop = (int) (posionConfig.y * getMeasuredHeight());
                    child.layout(childLeft, childTop, childLeft + width, childTop + height);
                }
            }
        }
    }

    Map<View, PosionConfig> viewPosionConfigMap = new LinkedHashMap<>();

    public void addView(View child, PosionConfig posionConfig) {
        viewPosionConfigMap.put(child, posionConfig);
        super.addView(child);
    }

    public static class PosionConfig {
        public PosionConfig() {

        }

        float x;
        float y;
        float width;
        float height;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getWidth() {
            return width;
        }

        public void setWidth(float width) {
            this.width = width;
        }

        public float getHeight() {
            return height;
        }

        public void setHeight(float height) {
            this.height = height;
        }
    }
}
