package cn.demomaster.huan.quickdeveloplibrary.widget.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
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
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDRoundDrawable;

public class QuickMessage extends QDPopup {
    // private Builder builder;
    private WeakReference<Context> contextWeakReference;
    public int textColor = Color.WHITE;
    public int textSize = 14;
    public int padding;
    public int animationStyleID = -1;
    private String message;
    private int backgroundColor = Color.BLACK;
    private float[] backgroundRadius = new float[8];

    public QuickMessage(Context context, QuickMessage.Builder builder) {
        super(context);
        handler = new Handler();
        this.contextWeakReference = new WeakReference<>(context);
        textColor = builder.textColor;
        textSize = builder.textSize;
        padding = builder.padding;
        animationStyleID = builder.animationStyleID;
        message = builder.message;
        backgroundColor = builder.backgroundColor;
        backgroundRadius = builder.backgroundRadius;
        if (builder.animationStyleID != -1) {
            setAnimationStyle(builder.animationStyleID);
        }
        setTouchable(builder.mTouchable);
        init();
    }
    Handler handler;
    public void showTip(View anchor, GuiderView.Gravity gravity,long time) {
        this.mAnchor = anchor;
        setDirection(gravity);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        },time);
    }
    
    private GuiderView.Gravity mGravity;
    private QDRoundDrawable drawable_bg;
    public void setDirection(GuiderView.Gravity gravity) {
        this.mGravity = gravity;
        //新建一个Drawable对象
        drawable_bg = new QDRoundDrawable();
        //drawable_bg.setCornerRadii(builder.backgroundRadius);
        drawable_bg.setBackGroundColor(backgroundColor);
        //drawable_bg.setCornerRadius(builder.backgroundRadiu);
        drawable_bg.setCornerRadii(backgroundRadius);
        //drawable_bg.setArrowSize(arrowWidht, arrowHeight);
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

        if (popupWidth >= maxWidth) {
            layoutParams.width = maxWidth;
            getContentView().setLayoutParams(layoutParams);
        }
        showAtLocation(mAnchor, Gravity.NO_GRAVITY, (location[0] + mAnchor.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
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
        int x = 0, y = 0;
        int w = popupWidth;
        int h = popupHeight;
        if (mGravity == GuiderView.Gravity.TOP) {
            x = DisplayUtil.getScreenWidth(contextWeakReference.get()) / 2 - popupWidth / 2;
            y = 0;
        } else if (mGravity == GuiderView.Gravity.BOTTOM) {
            x = DisplayUtil.getScreenWidth(contextWeakReference.get()) / 2 - popupWidth / 2;
            y = DisplayUtil.getScreenHeight(contextWeakReference.get()) - popupHeight;
        } else if (mGravity == GuiderView.Gravity.CENTER) {
            x = DisplayUtil.getScreenWidth(contextWeakReference.get()) / 2 - popupWidth / 2;
            y = DisplayUtil.getScreenHeight(contextWeakReference.get()) / 2 - popupHeight / 2;
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
        //setTouchable(true);
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

    public enum Direction {
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
        private boolean mTouchable = false;

        public GuiderView.Gravity gravity = GuiderView.Gravity.TOP;


        public Builder(Context context) {
            this.contextWeakReference = new WeakReference<>(context);
            padding = DisplayUtil.dip2px(context, 6);
            setBackgroundRadius(DisplayUtil.dip2px(context, 5));
        }

        public QuickMessage.Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public QuickMessage.Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public QuickMessage.Builder setTouchable(boolean mTouchable) {
            this.mTouchable = mTouchable;
            return this;
        }

        public QuickMessage.Builder setBackgroundRadius(float backgroundRadiu) {
            if (backgroundRadius == null) {
                return this;
            }
            for (int i = 0; i < backgroundRadius.length; i++) {
                this.backgroundRadius[i] = backgroundRadiu;
            }
            return this;
        }

        public QuickMessage.Builder setBackgroundRadius(float[] backgroundRadius) {
            this.backgroundRadius = backgroundRadius;
            return this;
        }

        public QuickMessage.Builder setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public QuickMessage.Builder setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public QuickMessage.Builder setPadding(int padding) {
            this.padding = padding;
            return this;
        }

        public QuickMessage.Builder setAnimationStyleID(int animationStyleID) {
            this.animationStyleID = animationStyleID;
            return this;
        }

        public QuickMessage.Builder setGravity(GuiderView.Gravity gravity) {
            this.gravity = gravity;
            return this;
        }

        public QuickMessage create() {
            return new QuickMessage(contextWeakReference.get(), this);
        }
    }
}