package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.QDBaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.QDBaseFragmentActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.AnimationUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIDisplayHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.ScreenShotUitl;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;

import static cn.demomaster.huan.quickdeveloplibrary.ApplicationParent.TAG;

/**
 * Created by Squirrel桓 on 2018/11/9.
 */
public class ActionBarLayoutView extends FrameLayout implements ActionBarInterface {

    private ActionBarLayoutContentView actionBarLayoutContentView;
    private ActionBarLayoutHeaderView actionBarLayoutHeaderView;
    private ActionBarLayoutFragmentView actionBarLayoutFragmentView;
    private Builder mBuilder;
    private boolean hasContainBackground = true;

    public void setHasContainBackground(boolean hasContainBackground) {
        this.hasContainBackground = hasContainBackground;
        if (actionBarLayoutContentView != null) {
            actionBarLayoutContentView.setHasContainBackground(hasContainBackground);
        }
    }

    private QDBaseFragmentActivity context;

    /**
     * 获取中间视图
     *
     * @return
     */
    public ImageTextView getCenterView() {
        return actionBarLayoutHeaderView.getCenterView();
    }

    /**
     * 获取左侧控件
     *
     * @return
     */
    public ImageTextView getLeftView() {
        return actionBarLayoutHeaderView.getLeftView();
    }

    /**
     * 获取右侧侧视图控件
     *
     * @return
     */
    public ImageTextView getRightView() {
        return actionBarLayoutHeaderView.getRightView();
    }

    /**
     * 获取导航栏视图用来更改背景或其他操作
     *
     * @return
     */
    public ViewGroup getHeadView() {
        return actionBarLayoutHeaderView;
    }

    public ActionBarLayoutView(Builder builder) {
        super(builder.context);
        context = (QDBaseFragmentActivity) builder.context;
        mBuilder = builder;
        initView();
    }

    public ActionBarLayoutContentView getActionBarLayoutContentView() {
        return actionBarLayoutContentView;
    }

    public void setActionBarLayoutContentView(ActionBarLayoutContentView actionBarLayoutContentView) {
        this.actionBarLayoutContentView = actionBarLayoutContentView;
    }

    public ActionBarLayoutHeaderView getActionBarLayoutHeaderView() {
        return actionBarLayoutHeaderView;
    }

    public void setActionBarLayoutHeaderView(ActionBarLayoutHeaderView actionBarLayoutHeaderView) {
        this.actionBarLayoutHeaderView = actionBarLayoutHeaderView;
    }

    public ActionBarLayoutView(@NonNull Context context) {
        super(context);
        initView();
    }

    public ActionBarLayoutView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ActionBarLayoutView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private int headerBackgroundColor = Color.WHITE;

    public void setHeaderBackgroundColor(int headerBackgroundColor) {
        this.headerBackgroundColor = headerBackgroundColor;
        if (actionBarLayoutHeaderView != null) {
            actionBarLayoutHeaderView.setBackgroundColor(headerBackgroundColor);
        }
    }

    private LayoutInflater mInflater;

    private void initView() {
        mInflater = LayoutInflater.from(getContext());

        initChildView();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setActionbarType(actionbarType);
                setFullScreen(isFullScreen);
            }
        });
    }

    /**
     * init内容
     */
    private void initChildView() {
        actionBarLayoutContentView = new ActionBarLayoutContentView(getContext(), this);
        actionBarLayoutFragmentView = new ActionBarLayoutFragmentView(getContext());
        //内容区
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(actionBarLayoutContentView, layoutParams);
        addContentBackView(mBuilder.contentResId);
        addContentBackView(mBuilder.contentView);

        //头部导航
        actionBarLayoutHeaderView = new ActionBarLayoutHeaderView(getContext());
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(actionBarLayoutHeaderView, layoutParams2);
        addHeadView(mBuilder.headerResId);
        actionBarLayoutHeaderView.setBackgroundColor(headerBackgroundColor);
        actionBarLayoutHeaderView.setContentType(mBuilder.contextType);
        isFullScreen = mBuilder.isFullScreen;

     /*   ActionBarLayoutHeaderView.GlobalLayoutListener globalLayoutListener = new ActionBarLayoutHeaderView.GlobalLayoutListener() {
            @Override
            public void onLoadFinish() {
                setFullScreen(isFullScreen);
            }
        };
        actionBarLayoutHeaderView.addGlobalLayoutListener(globalLayoutListener);*/


        if (mBuilder.contextType != ActionBarLayout.ContextType.FragmentModel) {
            //Front导航
            FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView(actionBarLayoutFragmentView, layoutParams3);
            addContentFragmentView(mBuilder.fragmentResId);
            addContentFragmentView(mBuilder.fragmentView);
            actionBarLayoutContentView.setActionbarType(actionbarType);
            setFullScreen(isFullScreen);
        }
    }

    private boolean isFullScreen;
    public void setFullScreen(boolean isFullScreen) {
        this.isFullScreen = isFullScreen;
        LayoutParams layoutParams_c = (LayoutParams) getLayoutParams();
        if (layoutParams_c == null) {
            layoutParams_c = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if (!isFullScreen) {
            int h = context.getActionBarLayoutView().getActionBarLayoutHeaderView().getHeight();
            QDLogger.d("h="+h);
            layoutParams_c.topMargin = h;
            setLayoutParams(layoutParams_c);
        } else {
            setLayoutParams(layoutParams_c);
        }
    }

    public void addContentBackView(int layoutResID) {
        if (layoutResID == -1) return;
        View view = mInflater.inflate(layoutResID, this, false);
        addContentBackView(view);
    }

    private void addContentBackView(View view) {
        if (view == null) return;
        actionBarLayoutContentView.setContentViewBack(view);
    }

    public void addContentFragmentView(int layoutResID) {
        if (layoutResID == -1) {
            return;
        }
        View view = mInflater.inflate(layoutResID, this, false);
        addContentFragmentView(view);
    }

    private void addContentFragmentView(View view) {
        if (view == null) return;
        actionBarLayoutFragmentView.setContentView(view);
    }

    public void addHeadView(int layoutResID) {
        if (layoutResID == -1) return;
        View view = mInflater.inflate(layoutResID, this, false);
        addHeadView(view);
    }

    private void addHeadView(View view) {
        if (view == null) return;
        actionBarLayoutHeaderView.setContentView((FrameLayout) view);
    }

    private ActionBarLayout.ACTIONBAR_TYPE actionbarType = ActionBarLayout.ACTIONBAR_TYPE.NORMAL;

    public void setActionbarType(ActionBarLayout.ACTIONBAR_TYPE actionbarType) {
        this.actionbarType = actionbarType;
        if (actionBarLayoutContentView != null) {
            actionBarLayoutContentView.setActionbarType(actionbarType);
        }
        if (actionBarLayoutHeaderView != null) {
            actionBarLayoutHeaderView.setActionbarType(actionbarType);
        }
    }

    public void setFragmentActionbarType(ActionBarLayout.ACTIONBAR_TYPE actionbarType) {
        this.actionbarType = actionbarType;
        if (actionBarLayoutContentView != null) {
            actionBarLayoutContentView.setActionbarType(actionbarType);
        }
        if (actionBarLayoutHeaderView != null) {
            actionBarLayoutHeaderView.setActionbarType(actionbarType);
        }
    }

    private ActionBarTip actionBarTip;

    public ActionBarTip getActionBarTip() {
        if (actionBarTip == null) {
            actionBarTip = new ActionBarTip(getContext());
            actionBarTip.setContentView(R.layout.quickdevelop_activity_actionbar_tip);
            FrameLayout.LayoutParams layoutParams_tip = (FrameLayout.LayoutParams) this.actionBarTip.getLayoutParams();
            if (layoutParams_tip == null) {
                layoutParams_tip = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            actionBarTip.setLayoutParams(layoutParams_tip);
            actionBarLayoutContentView.addActionBarTipView(actionBarTip);
        }
        return actionBarTip;
    }

    @Override
    public void setTitle(String title) {
        getActionBarLayoutHeaderView().setTitle(title);
    }

    @Override
    public void setStateBarColorAuto(Boolean barColorAuto) {
        getActionBarLayoutHeaderView().setStateBarColorAuto(barColorAuto);
    }

    @Override
    public void setLeftOnClickListener(OnClickListener leftOnClickListener) {
        getActionBarLayoutHeaderView().setLeftOnClickListener(leftOnClickListener);
    }

    @Override
    public void setRightOnClickListener(OnClickListener rightOnClickListener) {
        getActionBarLayoutHeaderView().setRightOnClickListener(rightOnClickListener);
    }

    @Override
    public void setActionBarThemeColors(int lightColor, int darkColor) {
        getActionBarLayoutHeaderView().setActionBarThemeColors(lightColor,darkColor);
    }

    public void setActionBarTipType(ActionBarTip.ACTIONBARTIP_TYPE actionbartip_type) {
        getActionBarLayoutContentView().setActionbartipType(actionbartip_type);
    }

    /*
    */
/**
     * 导航栏样式三种
     *//*

    public enum ACTIONBAR_TYPE {
        //无导航栏
        NO_ACTION_BAR,
        //有导航栏
        NORMAL,
        //无导航栏并且内容可填充到状态栏
        NO_ACTION_BAR_NO_STATUS,
        //层叠
        ACTION_STACK,
        //层叠并且内容可填充到状态栏
        ACTION_STACK_NO_STATUS,
        //透明导航栏
        ACTION_TRANSPARENT
    }
*/


    /**
     * 导航栏构建者
     */
    public static class Builder {
        private Context context;
        private int contentResId = -1;
        private int fragmentResId = -1;
        private int headerResId = -1;
        private View contentView;
        private View fragmentView;
        private boolean isFullScreen = true;
        private ActionBarLayout.ACTIONBAR_TYPE actionbarType;
        private ActionBarLayout.ContextType contextType = ActionBarLayout.ContextType.ActivityModel;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder setFragmentView(View fragmentView) {
            this.fragmentView = fragmentView;
            return this;
        }

        public Builder setFragmentResId(int fragmentResId) {
            this.fragmentResId = fragmentResId;
            return this;
        }

        public Builder setContentResId(int contentResId) {
            this.contentResId = contentResId;
            return this;
        }

        public Builder setHeaderResId(int headerResId) {
            this.headerResId = headerResId;
            return this;
        }

        public Builder setContextType(ActionBarLayout.ContextType contextType) {
            this.contextType = contextType;
            return this;
        }

        public Builder setFullScreen(boolean fullScreen) {
            isFullScreen = fullScreen;
            return this;
        }

        public ActionBarLayoutView creat() {
            return new ActionBarLayoutView(this);
        }
    }
}