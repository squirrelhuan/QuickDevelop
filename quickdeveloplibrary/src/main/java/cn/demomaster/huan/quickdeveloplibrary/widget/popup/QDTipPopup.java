package cn.demomaster.huan.quickdeveloplibrary.widget.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.demomaster.huan.quickdeveloplibrary.operatguid.GuiderView;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDRoundArrowDrawable;

public class QDTipPopup extends QDPopup {
    private Builder builder;
    private Context context;


    public QDTipPopup(Context context, Builder builder) {
        super(context);
        this.builder = builder;
        this.context = context;
        init();
    }

    public void showTip(View anchor, GuiderView.Gravity gravity) {
        this.mAnchor = anchor;
        setDirection(gravity);
    }

    private GuiderView.Gravity mGravity;
    private int arrowHeight;
    private QDRoundArrowDrawable drawable_bg;
    public void setDirection(GuiderView.Gravity gravity) {
        this.mGravity = gravity;
        //新建一个Drawable对象
        drawable_bg = new QDRoundArrowDrawable(mAnchor, mGravity, rootLayout);
        //drawable_bg.setCornerRadii(builder.backgroundRadius);
        drawable_bg.setBackGroundColor(builder.backgroundColor);
        int radius = DisplayUtil.dip2px(context, 5);
        drawable_bg.setCornerRadius(radius);
        if (builder.withArrow) {
            arrowHeight = DisplayUtil.dip2px(context, 8);
        } else {
            arrowHeight = 0;
        }
        drawable_bg.setArrowHeight(arrowHeight);
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
        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        //getContentView().setBackgroundColor(Color.RED);
        int popupWidth = getContentView().getMeasuredWidth();
        int popupHeight = getContentView().getMeasuredHeight();
        // 设置好参数之后再show
        int[] location = new int[2];
        mAnchor.getLocationOnScreen(location);
        if (mGravity == GuiderView.Gravity.BOTTOM || mGravity == GuiderView.Gravity.TOP) {
            popupHeight = popupHeight + arrowHeight;
            if (mGravity == GuiderView.Gravity.BOTTOM) {
                showAtLocation(mAnchor, Gravity.NO_GRAVITY, (location[0] + mAnchor.getWidth() / 2) - popupWidth / 2, location[1] + mAnchor.getHeight());
            } else {
                showAtLocation(mAnchor, Gravity.NO_GRAVITY, (location[0] + mAnchor.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
            }
        }
        if (mGravity == GuiderView.Gravity.LEFT || mGravity == GuiderView.Gravity.RIGHT) {
            popupWidth = popupWidth + arrowHeight;
            if (mGravity == GuiderView.Gravity.LEFT) {
                //showAtLocation(mAnchor, Gravity.NO_GRAVITY, location[0] - popupWidth, location[1]);
                showAtLocation(mAnchor, Gravity.NO_GRAVITY, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                showAtLocation(mAnchor, Gravity.NO_GRAVITY, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
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
        int popupWidth = getContentView().getWidth();
        int popupHeight = getContentView().getHeight();
        // 设置好参数之后再show
        int[] location = new int[2];
        mAnchor.getLocationOnScreen(location);
        ViewGroup.LayoutParams layoutParams = getContentView().getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        if (mGravity == GuiderView.Gravity.BOTTOM || mGravity == GuiderView.Gravity.TOP) {
            popupHeight = popupHeight + arrowHeight;
            if (mGravity == GuiderView.Gravity.BOTTOM) {
               /* int maxWidth = popupWidth;
                if(getContentView().getWidth()>=maxWidth) {
                    layoutParams.width = maxWidth;
                }*/
                getContentView().setLayoutParams(layoutParams);
                update((location[0] + mAnchor.getWidth() / 2) - popupWidth / 2, location[1] + mAnchor.getHeight(), getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                /*int maxWidth = DisplayUtil.getScreenWidth(context);
                if(getContentView().getWidth()>=maxWidth) {
                    layoutParams.width = maxWidth;
                }*/
                getContentView().setLayoutParams(layoutParams);
                update((location[0] + mAnchor.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight, getWidth(),  ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            setWidth(getContentView().getWidth());
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        }
        if (mGravity == GuiderView.Gravity.LEFT || mGravity == GuiderView.Gravity.RIGHT) {
            popupWidth = popupWidth + arrowHeight;
            if (mGravity == GuiderView.Gravity.LEFT) {
                int maxWidth = location[0];
                if(getContentView().getWidth()>=maxWidth) {
                    layoutParams.width = location[0];
                }
                getContentView().setLayoutParams(layoutParams);
                update(location[0] - popupWidth, location[1], layoutParams.width, ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                int maxWidth = DisplayUtil.getScreenWidth(context)-location[0]-mAnchor.getWidth();
               if(getContentView().getWidth()>=maxWidth) {
                   layoutParams.width = maxWidth;
               }
               getContentView().setLayoutParams(layoutParams);
               update(location[0] + mAnchor.getWidth(), location[1], layoutParams.width, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }

        //drawable_bg.setArrowHeight(arrowHeight);
        getContentView().postInvalidate();

    }

    private FrameLayout rootLayout;//根部局
    private FrameLayout contentLayout;//内容包裹布局
    private View mContentView;//内容视图

    private void init() {
        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rootLayout = new FrameLayout(context);
        if (mContentView == null) {//非自定义布局时，仅为提示框
            TextView textView = new TextView(context);
            textView.setText(builder.message);
            textView.setTextSize(builder.textSize);
            textView.setTextColor(builder.textColor);
           // textView.setBackgroundColor(Color.YELLOW);
            textView.setPadding(0,0,0,0);
            mContentView = textView;
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentLayout = new FrameLayout(context);
        contentLayout.setLayoutParams(layoutParams);
        contentLayout.addView(mContentView, layoutParams);
        int p = DisplayUtil.dip2px(context, 6);
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
        private Context context;
        private String message;
        private int backgroundColor = Color.BLACK;
        private float[] backgroundRadius = new float[8];
        private Direction direction;
        private boolean withArrow = true;

        public Builder(Context context) {
            this.context = context;
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

        public Builder setWithArrow(boolean withArrow) {
            this.withArrow = withArrow;
            return this;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public QDTipPopup create() {
            return new QDTipPopup(context, this);
        }
    }
}
