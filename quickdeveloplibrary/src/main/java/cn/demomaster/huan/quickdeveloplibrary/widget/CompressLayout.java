package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.RequiresApi;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.animation.QDValueAnimator;

/**
 * 压缩布局，适用于左上右下弹出面板，并压缩当前面板
 */
public class CompressLayout extends ViewGroup {
    public CompressLayout(Context context) {
        super(context);
        init(null);
    }

    public CompressLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CompressLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CompressLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    int originalHeight = -400;
    int lastHeight, lastWidth;

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.VisibleLayout);
            gravity = a.getInteger(R.styleable.VisibleLayout_start_from, gravity);
            //hide_with_child = a.getBoolean(R.styleable.VisibleLayout_hide_with_child, hide_with_child);

            a.recycle();
        }

        getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            //compressLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            //QdToast.show(getContext(),"height="+getMeasuredHeight());
            //QDLogger.e("getViewTreeObserver height=" + getMeasuredHeight());
            //PopToastUtil.ShowToast((Activity) getContext(),"height="+getMeasuredHeight());

            if (originalHeight == -400) {
                originalHeight = getMeasuredHeight();
            }
            if (lastHeight != getMeasuredHeight() || lastWidth != getMeasuredWidth()) {
                int oldHeight = lastHeight;
                lastHeight = getMeasuredHeight();
                lastWidth = getMeasuredWidth();

                if (oldHeight > getMeasuredHeight()) {//键盘展开
                    hideView();
                } else {//键盘关闭
                    showView();
                    reLayout();
                }
            }
        });
    }

    /**
     * 布局是否被软键盘占用或压缩
     *
     * @return
     */
    public boolean isCompressed() {
        return originalHeight > getMeasuredHeight();
    }

    public void hideView() {
        for (int i = 1; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(View.GONE);
        }
        if (progress != 0) {
            progress = 0;
            reLayout();
        }
        animator = null;
    }

    public void showView() {
        for (int i = 1; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏软键盘
     **/
    /*private void hideKeyboard(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }*/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //根据提供的测量值提取模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //根据提供的测量值提取大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        /*
         * 依据得到的specMode值，
         * 如果是AT_MOST，specSize 代表的是最大可获得的空间；
         * 如果EXACTLY，   specSize 代表的是精确的尺寸；
         * 如果是UNSPECIFIED，对于控件尺寸来说，没有任何参考意义。
         * 当以EXACT方式标记测量尺寸，父元素会坚持在一个指定的精确尺寸区域放置View。在父元素问子元素要多大空间时，
         * AT_MOST指示者会说给我最大的范围。在很多情况下，你得到的值都是相同的。在两种情况下，你必须绝对的处理这些限制。
         * 在一些情况下，它可能会返回超出这些限制的尺寸，在这种情况下，你可以让父元素选择如何对待超出的View，使用裁剪还是滚动等技术。
         */
       /* if(widthMode == MeasureSpec.EXACTLY){
            //不需要重新计算
            widthMeasureSpec = widthSize;
        }else {
            //重新计算，这里计算你需要绘制的视图的宽
            widthMeasureSpec = getPaddingLeft()+getPaddingRight()+getWidth();
        }

        if(heightMode == MeasureSpec.EXACTLY){
            heightMeasureSpec = heightSize;
        }else {
            heightMeasureSpec = getPaddingTop()+getPaddingBottom()+getHeight();
        }*/
        //然后调用自setMeasuredDimension()方法将测量好的宽高保存
        //setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);

        switch (gravity) {
            case Gravity.LEFT:
            case Gravity.RIGHT:
                widthMeasureSpec -= (int) (-panel_width * progress);
                break;
            case Gravity.TOP:
            case Gravity.BOTTOM:
                heightMeasureSpec -= (int) (-panel_height * progress);
                break;
        }
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 1) {
            panel_height = getChildAt(1).getMeasuredHeight();
            panel_width = getChildAt(1).getMeasuredWidth();
        }
        /*measureChildren(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState));*/
        // measureChildren(MeasureSpec.makeMeasureSpec(maxWidth, widthMeasureSpec),
        //        MeasureSpec.makeMeasureSpec(maxHeight, heightMeasureSpec));
    }

    float progress;
    private static float panel_height = 0;
    private static float panel_width = 0;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //super.onLayout(changed,l,t,r,b);
        reLayout();
    }

    /**
     * 伸缩方向
     */
    int gravity = Gravity.BOTTOM;

    public void setGravity(int gravity) {
        this.gravity = gravity;
        reLayout();
    }

    private void reLayout() {
        int childCount = getChildCount();

        if (childCount > 1) {
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(),
                    View.MeasureSpec.EXACTLY);
            getChildAt(1).measure(widthMeasureSpec,
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            panel_height = Math.max(panel_height, getChildAt(1).getMeasuredHeight());
            panel_width = getChildAt(1).getMeasuredWidth();
        }
        int childMaxHeight = (int) panel_height;
        //QDLogger.d("控件初始高度=" + originalHeight + ",键盘高度=" + (originalHeight - getMeasuredHeight()));
        //QDLogger.d("表情栏高度=" + panel_height);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int left = 0;
            int right = left + getMeasuredWidth();
            int top = 0;
            int bottom = top + getMeasuredHeight();
            if (i == 0) {
                switch (gravity) {
                    case Gravity.LEFT:
                        left = (int) (panel_width * progress);
                        right = left + child.getMeasuredWidth();
                        bottom = top + child.getMeasuredHeight();
                        break;
                    case Gravity.RIGHT:
                        right = getMeasuredWidth() - (int) (panel_width * progress);
                        left = right - child.getMeasuredWidth();
                        bottom = top + child.getMeasuredHeight();
                        break;
                    case Gravity.TOP:
                        right = left + child.getMeasuredWidth();
                        top = (int) (panel_height * progress);
                        bottom = (top + child.getMeasuredHeight());
                        break;
                    case Gravity.BOTTOM:
                        right = left + child.getMeasuredWidth();
                        bottom = (int) (originalHeight - Math.max(originalHeight - getMeasuredHeight(), panel_height * progress));
                        break;
                }
                //child.requestLayout();
                //measureChildren(mWidthMeasureSpec, mHeightMeasureSpec);
                //measureChild(child,mWidthMeasureSpec,mHeightMeasureSpec);
            } else {
                switch (gravity) {
                    case Gravity.LEFT:
                        right = (int) (panel_width * progress);
                        left = (int) (right - panel_width);
                        bottom = top + childMaxHeight;
                        break;
                    case Gravity.RIGHT:
                        left = getMeasuredWidth() - (int) (panel_width * progress);
                        right = (int) (left + panel_width);
                        bottom = top + childMaxHeight;
                        break;
                    case Gravity.TOP:
                        bottom = (int) (panel_height * progress);
                        top = (int) (bottom - panel_height);
                        break;
                    case Gravity.BOTTOM:
                        //top = (int) (getMeasuredHeight() - Math.max(0, panel_height * progress));
                        top = (int) (originalHeight - Math.max(0, childMaxHeight * progress));
                        bottom = top + childMaxHeight;
                        break;
                }
            }
            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            if (child.getMeasuredHeight() != (bottom - top) || child.getMeasuredWidth() != (right - left)) {
                layoutParams.height = bottom - top;
                layoutParams.width = right - left;
                child.setLayoutParams(layoutParams);
            }

            //QDLogger.d("onLayout(" + i + "),left=" + left + ",top=" + top + ",right=" + right + ",bottom=" + bottom + ",w=" + child.getMeasuredWidth() + ",h=" + child.getMeasuredHeight());
            child.layout(left, top, right, bottom);
        }
    }

    QDValueAnimator animator;

    long mDuration;

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    /**
     * 显示侧边栏
     */
    public void showPanel() {
        if (isExpanded()) {
            reLayout();
            return;
        }
        int h = (originalHeight - getMeasuredHeight());
        float startValue = Math.min(h / panel_height, 1);
        //QDLogger.d( "showPanelWithSorftKeyBoard startValue=" + startValue+",progress="+progress);
        animator = QDValueAnimator.ofFloat(startValue, 1f);
        animator.setDuration((long) (mDuration * (1 - startValue) + 5));
        animator.addUpdateListener(animation -> {
            progress = (float) animation.getAnimatedValue();
            //QDLogger.d( "showPanelWithSorftKeyBoard progress=" + progress);
            reLayout();
        });
        //animator.setRepeatMode(ValueAnimator.RESTART);
        //animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new AccelerateInterpolator());
        //animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    /**
     * 隐藏侧边栏
     */
    public void dissmissPanel() {
        if (isHide()) {
            reLayout();
            return;
        }
        animator = QDValueAnimator.ofFloat(progress, 0);
        animator.setDuration((long) ((mDuration / 2) * (progress)));
        animator.addUpdateListener(animation -> {
            progress = (float) animation.getAnimatedValue();
            reLayout();
        });
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    /**
     * 是否已经展开
     *
     * @return
     */
    public boolean isExpanded() {
        return progress == 1;
    }

    /**
     * 是否隐藏了
     *
     * @return
     */
    public boolean isHide() {
        return progress == 0;
    }
}
