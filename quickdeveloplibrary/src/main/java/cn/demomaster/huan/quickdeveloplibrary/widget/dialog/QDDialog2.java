package cn.demomaster.huan.quickdeveloplibrary.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatDialog;
import androidx.core.view.ScrollingView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.DividerGravity;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDividerDrawable;
import cn.demomaster.huan.quickdeveloplibrary.widget.slidingpanellayout.SlidingUpPanelLayout;

public class QDDialog2 extends AppCompatDialog {

    public QDDialog2(Context context) {
        super(context);
        init();
    }

    public QDDialog2(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public QDDialog2(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    @Override
    public void show() {
        if (getContext() != null) {
            if (getContext() instanceof Activity) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (((Activity) getContext()).isDestroyed()) {
                        return;
                    }
                }
                if (((Activity) getContext()).isFinishing()) {
                    return;
                }
            }
        }
        super.show();
    }

    boolean hasPadding = false;
    public boolean isHasPadding() {
        return hasPadding;
    }
    private void init() {
        Window win = getWindow();
        if (animationStyleID != -1) {
            win.setWindowAnimations(animationStyleID);
        }
        if (!isHasPadding()) {
            win.getDecorView().setPadding(0, 0, 0, 0);
            win.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
            win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        // 在底部，宽度撑满
        WindowManager.LayoutParams params = win.getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        win.setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //win.setDimAmount(0);
        setBackgroundColor(backgroundColor);
    }

    int panelMarginTop;

    public void setPanelMaginTop(int maginTop) {
        panelMarginTop = maginTop;
        if (slidingUpPanelLayout != null) {
            slidingUpPanelLayout.setPanelMaginTop(panelMarginTop);
        }
    }

    SlidingUpPanelLayout slidingUpPanelLayout;

    @Override
    public void setContentView(View view) {
        contentView = view;
        if (isCanSliding()) {
            slidingUpPanelLayout = (SlidingUpPanelLayout) getLayoutInflater().inflate(R.layout.qd_layout_dialog, null);
            slidingUpPanelLayout.setShowBackground(false);
            slidingUpPanelLayout.setPanelMaginTop(panelMarginTop);
            ViewGroup view1 = slidingUpPanelLayout.findViewById(R.id.dragView);
            view1.addView(contentView);
            setScrollView(slidingUpPanelLayout, contentView);
            super.setContentView(slidingUpPanelLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            FrameLayout contentViewTmp = new FrameLayout(getContext());
            contentViewTmp.addView(contentView);
            super.setContentView(contentViewTmp, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        contentView = getLayoutInflater().inflate(layoutResID, null);
        if (isCanSliding()) {
            slidingUpPanelLayout = (SlidingUpPanelLayout) getLayoutInflater().inflate(R.layout.qd_layout_dialog, null);
            slidingUpPanelLayout.setShowBackground(false);
            slidingUpPanelLayout.setPanelMaginTop(panelMarginTop);
            ViewGroup view1 = slidingUpPanelLayout.findViewById(R.id.dragView);
            view1.addView(contentView);
            setScrollView(slidingUpPanelLayout, contentView);
            //FrameLayout fl_bg = container.findViewById(R.id.fl_bg);
            super.setContentView(slidingUpPanelLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            FrameLayout contentViewTmp = new FrameLayout(getContext());
            contentViewTmp.addView(contentView);
            super.setContentView(contentViewTmp, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        contentView = view;
        if (isCanSliding()) {
            slidingUpPanelLayout = (SlidingUpPanelLayout) getLayoutInflater().inflate(R.layout.qd_layout_dialog, null);
            slidingUpPanelLayout.setShowBackground(false);
            slidingUpPanelLayout.setPanelMaginTop(panelMarginTop);
            ViewGroup view1 = slidingUpPanelLayout.findViewById(R.id.dragView);
            view1.addView(contentView, params);
            setScrollView(slidingUpPanelLayout, contentView);
            super.setContentView(slidingUpPanelLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            FrameLayout contentViewTmp = new FrameLayout(getContext());
            contentViewTmp.addView(contentView, params);
            super.setContentView(contentViewTmp, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    /**
     * 设置scrollview
     *
     * @param container
     * @param view
     * @return
     */
    private boolean setScrollView(SlidingUpPanelLayout container, View view) {
        // container.setPanelHeight(600);
        // container.setAnchorPoint(0.7f);
        if (view instanceof ScrollingView) {
            container.setScrollableView(view);
            return true;
        } else if (view instanceof AbsListView) {
            container.setScrollableView(view);
            return true;
        }
        if (view instanceof ViewGroup) {
            int count = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < count; i++) {
                boolean b = setScrollView(container, ((ViewGroup) view).getChildAt(i));
                if (b) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
       /* int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);*/
        if (slidingUpPanelLayout != null) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            (slidingUpPanelLayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cancelable) {
                        QDDialog2.this.dismiss();
                    }
                }
            });
            (slidingUpPanelLayout.findViewById(R.id.fl_bg)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cancelable) {
                        QDDialog2.this.dismiss();
                    }
                }
            });
            if (panelSlideListener == null) {
                panelSlideListener = new SlidingUpPanelLayout.PanelSlideListener() {
                    @Override
                    public void onPanelSlide(View panel, float slideOffset) {

                    }

                    @Override
                    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                        if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                            dismiss();
                        }
                    }
                };
            }
            slidingUpPanelLayout.addPanelSlideListener(panelSlideListener);
        }
    }

    SlidingUpPanelLayout.PanelSlideListener panelSlideListener;

    boolean cancelable = true;

    @Override
    public void setCancelable(boolean flag) {
        cancelable = flag;
        super.setCancelable(flag);
    }

    boolean canSliding = false;//可滑动布局

    public boolean isCanSliding() {
        return canSliding;
    }

    public View contentView;
    public int contentViewLayoutID;
    public int backgroundColor = Color.TRANSPARENT;
    public float[] backgroundRadius = new float[8];
    public int animationStyleID = R.style.qd_dialog_animation_center_scale;

    public QDDialog2(Context context, Builder builder) {
        super(context);
        //this.builder = builder;
        contentView = builder.contentView;
        contentViewLayoutID = builder.contentViewLayoutID;

        backgroundColor = builder.backgroundColor;
        backgroundRadius = builder.backgroundRadius;
        animationStyleID = builder.animationStyleID;

        init();
        //getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        QDividerDrawable drawable_bg = new QDividerDrawable(DividerGravity.NONE);
        drawable_bg.setCornerRadii(backgroundRadius);
        drawable_bg.setBackGroundColor(backgroundColor);
        setBackgroundDrawable(drawable_bg);
    }

    public void setBackgroundDrawable(QDividerDrawable drawable_bg) {
        getWindow().setBackgroundDrawable(drawable_bg);
    }

    public static class Builder {
        private Context context;
        private View contentView;
        private int contentViewLayoutID;

        private int backgroundColor = Color.TRANSPARENT;
        private float[] backgroundRadius = new float[8];
        private int animationStyleID = R.style.qd_dialog_animation_center_scale;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setContext(Context context) {
            this.context = context;
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

        public Builder setAnimationStyleID(int animationStyleID) {
            this.animationStyleID = animationStyleID;
            return this;
        }

        public QDDialog2 create() {
            return new QDDialog2(context, this);
        }
    }
}
