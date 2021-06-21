package cn.demomaster.huan.quickdeveloplibrary.widget.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import cn.demomaster.huan.quickdeveloplibrary.operatguid.GuiderView;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDRoundArrowDrawable;

public class QDTipPopup extends QDPopup {
    // private Builder builder;
    private WeakReference<Context> contextWeakReference;
    public int textColor = Color.WHITE;
    public int textSize = 14;
    public int padding;
    public int animationStyleID = -1;
    private String message;
    private int backgroundColor = Color.BLACK;
    private float[] backgroundRadius = new float[8];
    private Direction direction;
    private boolean withArrow = true;
    private int arrowWidth;
    private int arrowHeight;

    public QDTipPopup(Context context, Builder builder) {
        super(context);
        this.contextWeakReference = new WeakReference<>(context);
        textColor = builder.textColor;
        textSize = builder.textSize;
        padding = builder.padding;
        animationStyleID = builder.animationStyleID;
        message = builder.message;
        backgroundColor = builder.backgroundColor;
        backgroundRadius = builder.backgroundRadius;
        direction = builder.direction;
        withArrow = builder.withArrow;
        arrowWidth = builder.arrowWidth;
        arrowHeight = builder.arrowHeight;
        if (builder.animationStyleID != -1) {
            setAnimationStyle(builder.animationStyleID);
        }
        // setEnterTransition(new );
        init();
    }

    public void showTip(View anchor, GuiderView.Gravity gravity) {
        this.mAnchor = anchor;
        setDirection(gravity);
    }

    private GuiderView.Gravity mGravity;
    //private int arrowHeight;
    private int arrowWidht;
    private QDRoundArrowDrawable drawable_bg;

    public void setDirection(GuiderView.Gravity gravity) {
        this.mGravity = gravity;
        //新建一个Drawable对象
        drawable_bg = new QDRoundArrowDrawable(mAnchor, mGravity, rootLayout);
        //drawable_bg.setCornerRadii(builder.backgroundRadius);
        drawable_bg.setBackGroundColor(backgroundColor);
        //drawable_bg.setCornerRadius(builder.backgroundRadiu);
        drawable_bg.setCornerRadii(backgroundRadius);
        if (withArrow) {
            arrowHeight = arrowHeight;//DisplayUtil.dip2px(context, 8);
            arrowWidht = arrowWidth;
        } else {
            arrowHeight = 0;
            arrowWidht = 0;
        }
        drawable_bg.setArrowSize(arrowWidht, arrowHeight);
        rootLayout.setBackground(drawable_bg);
        //rootLayout.setBackgroundColor(Color.GREEN);
        show();
    }

    private View mAnchor;

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        mAnchor = parent;
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void showAsDropDown(View anchor) {
        mAnchor = anchor;
        setDirection(GuiderView.Gravity.BOTTOM);
    }

    private void show() {
        ViewGroup.LayoutParams layoutParams = getContentView().getLayoutParams();
        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = getContentView().getMeasuredWidth();
        int popupHeight = getContentView().getMeasuredHeight();
        //QDLogger.d("getScreenWidth="+DisplayUtil.getScreenWidth(contextWeakReference.get())+"popupWidth="+popupWidth+",popupHeight="+popupHeight);
        // 设置好参数之后再show
        int[] location = new int[2];
        mAnchor.getLocationOnScreen(location);

        if (mGravity == GuiderView.Gravity.BOTTOM || mGravity == GuiderView.Gravity.TOP) {
            if (popupWidth >= maxWidth) {
                layoutParams.width = maxWidth;
                getContentView().setLayoutParams(layoutParams);
            }
            popupHeight = popupHeight + arrowHeight;
            if (mGravity == GuiderView.Gravity.BOTTOM) {
                showAtLocation(mAnchor, Gravity.NO_GRAVITY, (location[0] + mAnchor.getWidth() / 2) - popupWidth / 2 - padding, location[1] + mAnchor.getHeight());
            } else {
                showAtLocation(mAnchor, Gravity.NO_GRAVITY, (location[0] + mAnchor.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
            }
        }
        if (mGravity == GuiderView.Gravity.LEFT || mGravity == GuiderView.Gravity.RIGHT) {
            popupWidth = popupWidth + arrowWidht;
            if (mGravity == GuiderView.Gravity.LEFT) {
                if (popupWidth >= maxWidth) {
                    layoutParams.width = location[0] - padding1;
                    getContentView().setLayoutParams(layoutParams);
                }
                showAtLocation(mAnchor, Gravity.NO_GRAVITY, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                if (popupWidth >= maxWidth) {
                    layoutParams.width = location[0] + mAnchor.getWidth();
                    getContentView().setLayoutParams(layoutParams);
                }
                showAtLocation(mAnchor, Gravity.NO_GRAVITY, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
        // 自动调整箭头的位置
        updatePosition();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        mAnchor = anchor;
        super.showAsDropDown(anchor, xoff, yoff);
        if (xoff > 0) {
            setDirection(GuiderView.Gravity.LEFT);
        } else {
            setDirection(GuiderView.Gravity.RIGHT);
        }
        if (yoff > 0) {
            setDirection(GuiderView.Gravity.TOP);
        } else {
            setDirection(GuiderView.Gravity.BOTTOM);
        }
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        mAnchor = anchor;
        super.showAsDropDown(anchor, xoff, yoff, gravity);

        if (gravity == Gravity.TOP) {
            setDirection(GuiderView.Gravity.TOP);
        }
        if (gravity == Gravity.BOTTOM) {
            setDirection(GuiderView.Gravity.BOTTOM);
        }
        if (gravity == Gravity.LEFT) {
            setDirection(GuiderView.Gravity.LEFT);
        }
        if (gravity == Gravity.RIGHT) {
            setDirection(GuiderView.Gravity.RIGHT);
        }

    }

    private boolean isCustomView = true;

    @Override
    public void setContentView(final View contentView) {
        //兼容正常的setContentView
        if (isCustomView) {
            mContentView = contentView;
            init();
            return;
        }
        super.setContentView(contentView);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 自动调整箭头的位置
                updatePosition();
                contentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        //内部类调用setContentView先设置isCustomView = false;
        isCustomView = true;
    }

    private void updatePosition() {
        // 设置好参数之后再show
        int[] location = new int[2];
        mAnchor.getLocationOnScreen(location);
        ViewGroup.LayoutParams layoutParams = getContentView().getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = getContentView().getMeasuredWidth();
        int popupHeight = getContentView().getMeasuredHeight();
        //QDLogger.d("getScreenWidth="+DisplayUtil.getScreenWidth(contextWeakReference.get())+"popupWidth="+popupWidth+",popupHeight="+popupHeight);
        int x = 0, y = 0, w = 0, h = 0;
        if (mGravity == GuiderView.Gravity.BOTTOM || mGravity == GuiderView.Gravity.TOP) {
            int b = ((popupWidth >= maxWidth) ? padding1 : 0);
            x = location[0] + mAnchor.getWidth() / 2 - getContentView().getWidth() / 2 + b;
            x = ((popupWidth >= maxWidth) ? padding1 : x);
            w = popupWidth > maxWidth ? maxWidth : ViewGroup.LayoutParams.WRAP_CONTENT;
            h = ViewGroup.LayoutParams.WRAP_CONTENT;
            if (mGravity == GuiderView.Gravity.BOTTOM) {
                y = location[1] + mAnchor.getHeight();
            } else {
                y = location[1] - popupHeight - arrowHeight;
            }
        }
        if (mGravity == GuiderView.Gravity.LEFT || mGravity == GuiderView.Gravity.RIGHT) {
            popupWidth = popupWidth + arrowWidht;
            y = location[1] + (getContentView().getHeight() < mAnchor.getHeight() ? (mAnchor.getHeight() - getContentView().getHeight()) / 2 : 0);
            w = layoutParams.width;
            h = ViewGroup.LayoutParams.WRAP_CONTENT;
            if (mGravity == GuiderView.Gravity.LEFT) {
                int maxWidth = location[0];
                getContentView().setLayoutParams(layoutParams);
                x = location[0] - popupWidth;
            } else {
                int maxWidth = DisplayUtil.getScreenWidth(contextWeakReference.get()) - location[0] - mAnchor.getWidth();
                getContentView().setLayoutParams(layoutParams);
                x = location[0] + mAnchor.getWidth();
            }
        }
        update(x, y, w, h);

    }

    private FrameLayout rootLayout;//根部局
    private FrameLayout contentLayout;//内容包裹布局
    private View mContentView;//内容视图

    private int maxWidth;
    private int maxHeight;
    private int screenHeight;
    private int screenWidth;
    private int padding1;

    private void init() {
        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        padding1 = DisplayUtil.dip2px(contextWeakReference.get(), 30);
        screenHeight = DisplayUtil.getScreenHeight(contextWeakReference.get());
        screenWidth = DisplayUtil.getScreenWidth(contextWeakReference.get());
        maxWidth = screenWidth - padding1 * 2;
        maxHeight = screenHeight - padding1 * 2;
        rootLayout = new FrameLayout(contextWeakReference.get());
        if (mContentView == null) {//非自定义布局时，仅为提示框
            TextView textView = new TextView(contextWeakReference.get());
            textView.setText(message);
            textView.setTextSize(textSize);
            textView.setTextColor(textColor);
            // textView.setBackgroundColor(Color.YELLOW);
            textView.setPadding(0, 0, 0, 0);
            mContentView = textView;
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentLayout = new FrameLayout(contextWeakReference.get());
        contentLayout.setLayoutParams(layoutParams);
        contentLayout.addView(mContentView, layoutParams);
        int p = padding;//DisplayUtil.dip2px(context, 6);
        contentLayout.setPadding(p, p, p, p);
        rootLayout.addView(contentLayout, layoutParams);
        rootLayout.setLayoutParams(layoutParams);
        //内部类调用setContentView先设置isCustomView = false;
        isCustomView = false;
        setContentView(rootLayout);
    }

    public static enum Direction {
        top, left, right, bottom
    }

    public static class Builder {
        public int textColor = Color.WHITE;
        public int textSize = 14;
        public int padding;
        public int animationStyleID = -1;
        public WeakReference<Context> contextWeakReference;
        private String message;
        public int backgroundColor = Color.BLACK;
        public float[] backgroundRadius = new float[8];
        private Direction direction;
        private boolean withArrow = true;
        private int arrowWidth;
        private int arrowHeight;

        public GuiderView.Gravity gravity = GuiderView.Gravity.TOP;


        public Builder(Context context) {
            this.contextWeakReference = new WeakReference<>(context);
            arrowWidth = DisplayUtil.dip2px(context, 8);
            arrowHeight = DisplayUtil.dip2px(context, 8);
            padding = DisplayUtil.dip2px(context, 6);
            setBackgroundRadius(DisplayUtil.dip2px(context, 5));
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setBackgroundRadius(float backgroundRadiu) {
            if (backgroundRadius == null) {
                return this;
            }
            for (int i = 0; i < backgroundRadius.length; i++) {
                this.backgroundRadius[i] = backgroundRadiu;
            }
            return this;
        }

        public Builder setBackgroundRadius(float[] backgroundRadius) {
            this.backgroundRadius = backgroundRadius;
            return this;
        }

        public Builder setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Builder setPadding(int padding) {
            this.padding = padding;
            return this;
        }

        public Builder setArrowWidth(int arrowWidth) {
            this.arrowWidth = arrowWidth;
            return this;
        }

        public Builder setArrowHeight(int arrowHeight) {
            this.arrowHeight = arrowHeight;
            return this;
        }

        public Builder setWithArrow(boolean withArrow) {
            this.withArrow = withArrow;
            return this;
        }

        public Builder setAnimationStyleID(int animationStyleID) {
            this.animationStyleID = animationStyleID;
            return this;
        }

        public Builder setGravity(GuiderView.Gravity gravity) {
            this.gravity = gravity;
            return this;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public QDTipPopup create() {
            return new QDTipPopup(contextWeakReference.get(), this);
        }
    }
}
