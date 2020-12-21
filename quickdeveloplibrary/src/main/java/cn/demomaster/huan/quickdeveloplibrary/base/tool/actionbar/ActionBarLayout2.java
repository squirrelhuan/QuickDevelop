package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import cn.demomaster.qdlogger_library.QDLogger;

public class ActionBarLayout2 extends FrameLayout {
    public ActionBarLayout2(@NonNull Context context) {
        super(context);
    }

    public ActionBarLayout2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionBarLayout2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ActionBarLayout2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    Builder mBuilder;
    public ActionBarLayout2(Builder builder) {
        super(builder.activity);
        mBuilder = builder;
        update();
    }

    private final ArrayList<View> mMatchParentChildren = new ArrayList<>(1);
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
            if (getMeasureAllChildren() || child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth,
                        child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                maxHeight = Math.max(maxHeight,
                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
               // if (measureMatchParentChildren||child == mBuilder.contentView) {
                    if (lp.width == LayoutParams.MATCH_PARENT ||
                            lp.height == LayoutParams.MATCH_PARENT) {
                        mMatchParentChildren.add(child);
                    }
                //}
            }
        }

        // Account for padding too
        maxWidth += getPaddingLeft() + getPaddingRight();
        maxHeight += getPaddingTop() + getPaddingBottom();

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
                            - getPaddingLeft() - getPaddingRight()
                            - lp.leftMargin - lp.rightMargin);
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                            width, MeasureSpec.EXACTLY);
                } else {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                            getPaddingLeft() + getPaddingRight() +
                                    lp.leftMargin + lp.rightMargin,
                            lp.width);
                }

                final int childHeightMeasureSpec;
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    int height = 0;
                    if (child == mBuilder.contentView) {
                        int h1=0;
                        int statusBarHeight = 0;
                        if (mBuilder.hasStatusBar&&mBuilder.statusbarView != null&&!mBuilder.mixStatusActionBar) {
                            statusBarHeight = mBuilder.statusHeight;
                        }
                        int actionBarHeight = 0;
                        if (mBuilder.hasActionBar && mBuilder.actionBarView != null) {
                            actionBarHeight = mBuilder.actionBarView.getMeasuredHeight();
                        }
                        switch (mBuilder.contentViewPaddingTop){
                            case parent:
                                break;
                            case actionBar:
                                h1= actionBarHeight+mBuilder.actionBarView.getTop();
                                break;
                            case statusBar:
                                h1= statusBarHeight;
                                break;
                        }
                        height = Math.max(0, getMeasuredHeight()-h1
                                - getPaddingTop() - getPaddingBottom()
                                - lp.topMargin - lp.bottomMargin);
                    }else {
                        height = Math.max(0, getMeasuredHeight()
                                - getPaddingTop() - getPaddingBottom()
                                - lp.topMargin - lp.bottomMargin);
                    }
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                            height, MeasureSpec.EXACTLY);
                } else {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                            getPaddingTop() + getPaddingBottom() +
                                    lp.topMargin + lp.bottomMargin,
                            lp.height);
                }

                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // super.onLayout(changed, left, top, right, bottom);
        layoutChildren(left, top, right, bottom, false /* no force left gravity */);
    }

    private static final int DEFAULT_CHILD_GRAVITY = Gravity.TOP | Gravity.START;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void layoutChildren(int left, int top, int right, int bottom, boolean forceLeftGravity) {
        //按顺序
        layoutChildren2(mBuilder.statusbarView,left,top,right,bottom,forceLeftGravity);
        layoutChildren2(mBuilder.actionBarView,left,top,right,bottom,forceLeftGravity);
        layoutChildren2(mBuilder.contentView,left,top,right,bottom,forceLeftGravity);

        final int count = getChildCount();
        for (int i = count-1; i >0; i--) {
            final View child = getChildAt(i);
            if(child==mBuilder.statusbarView||child==mBuilder.actionBarView||child==mBuilder.contentView){
                continue;
            }
            layoutChildren2(mBuilder.contentView,left,top,right,bottom,forceLeftGravity);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void layoutChildren2(final View child,int left, int top, int right, int bottom, boolean forceLeftGravity) {
        if(child==null){
            return;
        }
        final int parentLeft = getPaddingLeft();
        final int parentRight = right - left - getPaddingRight();

        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();

        int statusBarHeight = 0;
        if (mBuilder.hasStatusBar&&mBuilder.statusbarView != null&&!mBuilder.mixStatusActionBar) {
            statusBarHeight = mBuilder.statusHeight;
        }

        int actionBarHeight = 0;
        if (mBuilder.hasActionBar && mBuilder.actionBarView != null) {
            actionBarHeight = mBuilder.actionBarView.getMeasuredHeight();
        }

        //Log.e("action",child.toString());
        if (child!=null&&child.getVisibility() != GONE) {
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if(lp==null){
                lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }

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

            if (child == mBuilder.actionBarView) {
                if(!mBuilder.mixStatusActionBar) {
                    childTop += statusBarHeight;
                }
            } else if (child == mBuilder.contentView) {
                switch (mBuilder.contentViewPaddingTop){
                    case parent:
                        break;
                    case actionBar:
                        childTop += actionBarHeight+mBuilder.actionBarView.getTop();
                        break;
                    case statusBar:
                        childTop += statusBarHeight;
                        break;
                }
            }
            child.layout(childLeft, childTop, childLeft + width, childTop + height);
        }
    }

    /**
     * 设置导航栏样式
     *
     * @param actionbarType
     */
    public void setActionBarType(ACTIONBAR_TYPE actionbarType) {
        mBuilder.actionbarType = actionbarType;
        switch (actionbarType) {
            case NORMAL:
                mBuilder.hasStatusBar = true;
                mBuilder.hasActionBar = true;
                setContentViewPaddingTop(ContentView_Layout_Below.actionBar);
                break;
            case NO_ACTION_BAR:
                mBuilder.hasStatusBar = true;
                mBuilder.hasActionBar = false;/*
                Drawable drawable = mBuilder.actionBarView.getBackground();
                mBuilder.statusbarView.setBackground(drawable);
                mBuilder.statusbarView.setBackgroundColor(Color.RED);*/
                setContentViewPaddingTop(ContentView_Layout_Below.statusBar);
                break;
            case NO_ACTION_BAR_NO_STATUS:
                mBuilder.hasStatusBar = false;
                mBuilder.hasActionBar = false;
                setContentViewPaddingTop(ContentView_Layout_Below.parent);
                break;
            case NO_STATUS:
                mBuilder.hasStatusBar = false;
                mBuilder.hasActionBar = true;
                setContentViewPaddingTop(ContentView_Layout_Below.actionBar);
                break;
            case ACTION_STACK:
                mBuilder.hasStatusBar = true;
                mBuilder.hasActionBar = true;
                setContentViewPaddingTop(ContentView_Layout_Below.parent);
                break;
            case ACTION_TRANSPARENT:
                mBuilder.hasStatusBar = true;
                mBuilder.hasActionBar = true;
                setContentViewPaddingTop(ContentView_Layout_Below.parent);
                break;
            case ACTION_STACK_NO_ACTION:
                mBuilder.hasStatusBar = true;
                mBuilder.hasActionBar = false;
                setContentViewPaddingTop(ContentView_Layout_Below.statusBar);
                break;
            case ACTION_STACK_NO_STATUS:
                mBuilder.hasStatusBar = false;
                mBuilder.hasActionBar = true;
                setContentViewPaddingTop(ContentView_Layout_Below.actionBar);
                break;
        }

        update();
    }

    public void update() {
        //QDLogger.d("update() "+this.hashCode());
        if(mBuilder.contentView!=null&&mBuilder.contentView.getParent()==null){
            addView(mBuilder.contentView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        if(mBuilder.hasActionBar){//有无导航栏
            if (mBuilder.actionBarView != null && mBuilder.actionBarView.getParent() == null) {
                addView(mBuilder.actionBarView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }else {
            if (mBuilder.actionBarView != null && mBuilder.actionBarView.getParent() != null) {
                removeView(mBuilder.actionBarView);
            }
        }

        if(mBuilder.hasStatusBar&&!mBuilder.mixStatusActionBar){//有無狀態欄
            if(mBuilder.statusbarView==null){
                mBuilder.statusbarView = new FrameLayout(mBuilder.activity);
            }
            if (mBuilder.statusbarView != null && mBuilder.statusbarView.getParent() == null) {
                addView(mBuilder.statusbarView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mBuilder.statusHeight));
            }
        }else {
            if (mBuilder.statusbarView != null && mBuilder.statusbarView.getParent() != null) {
                removeView(mBuilder.statusbarView);
            }
        }

        if(mBuilder.actionBarView!=null){
            int paddingTop;
            if(mBuilder.mixStatusActionBar){
                paddingTop = mBuilder.hasStatusBar?(mBuilder.statusHeight+mBuilder.actionBarPaddingTop_Last):mBuilder.actionBarPaddingTop_Last;
            }else {
                paddingTop = mBuilder.actionBarPaddingTop_Last;
            }
            if(mBuilder.actionBarView.getPaddingTop()!=paddingTop) {
                mBuilder.actionBarView.setPadding(mBuilder.actionBarView.getPaddingLeft(), paddingTop, mBuilder.actionBarView.getPaddingRight(), mBuilder.actionBarView.getPaddingBottom());
            }
        }
       /* if(hasRequestLayout){
            requestLayout();
        }*/
    }

    public enum ContentView_Layout_Below {
        parent,
        statusBar,
        actionBar
    }

    public void setHasActionBar(boolean hasActionBar) {
        mBuilder.hasActionBar = hasActionBar;
        //QDLogger.d("setHasActionBar");
    }

    public void setHasStatusBar(boolean hasStatusBar) {
        mBuilder.hasStatusBar = hasStatusBar;
        //QDLogger.d("setHasStatusBar");
    }

    public void setContentViewPaddingTop(ContentView_Layout_Below actionBarPaddingTop) {
        mBuilder.contentViewPaddingTop = actionBarPaddingTop;
        //QDLogger.d("setContentViewPaddingTop");
        update();
    }
    public void setStatusHeight(int statusHeight) {
        mBuilder.statusHeight = statusHeight;
        //QDLogger.d("setStatusHeight");
        update();
    }

    public void setMixStatusActionBar(boolean mixStatusActionBar) {
        mBuilder.mixStatusActionBar = mixStatusActionBar;
        //QDLogger.d("setMixStatusActionBar");
        update();
    }

    public static class Builder{
        ACTIONBAR_TYPE actionbarType;
        private int statusHeight;
        private int actionBarPaddingTop_Last =0;
        private boolean mixStatusActionBar = true;//是否使用融合模式，即actionbar padding 状态栏高度,优先级高于hasStatusBar
        private boolean hasStatusBar = true;
        private boolean hasActionBar = true;
        private ContentView_Layout_Below contentViewPaddingTop = ContentView_Layout_Below.actionBar;
        View statusbarView;
        View actionBarView;
        View contentView;
        Activity activity;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setActionbarType(ACTIONBAR_TYPE actionbarType) {
            this.actionbarType = actionbarType;
            return this;
        }

        public Builder setStatusHeight(int statusHeight) {
            this.statusHeight = statusHeight;
            return this;
        }

        public Builder setActionBarView(View actionBarView) {
            this.actionBarView = actionBarView;
            actionBarPaddingTop_Last = actionBarView.getPaddingTop();
            return this;
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder setHasStatusBar(boolean hasStatusBar) {
            this.hasStatusBar = hasStatusBar;
            return this;
        }

        public Builder setContentViewPaddingTop(ContentView_Layout_Below contentViewPaddingTop) {
            this.contentViewPaddingTop = contentViewPaddingTop;
            return this;
        }

        public Builder setHasActionBar(boolean hasActionBar) {
            this.hasActionBar = hasActionBar;
            return this;
        }

        public Builder setMixStatusActionBar(boolean mixStatusActionBar) {
            this.mixStatusActionBar = mixStatusActionBar;
            return this;
        }

        public ActionBarLayout2 creat() {
            return new ActionBarLayout2(this);
        }
    }

}
