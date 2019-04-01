package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

import static cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface.ACTIONBAR_TYPE.NORMAL;

/**
 * Created by Squirrel桓 on 2018/11/9.
 */
public class ActionBarLayoutContentView extends FrameLayout {

    public ActionBarLayoutContentView(@NonNull Context context) {
        super(context);
        initView();
    }

    private FrameLayout contentViewBackLayout, actionBarTipLayout;
    public View contentViewBack;//, contentViewFront;
    public ActionBarLayoutView mActionBarLayoutView;

    public ActionBarLayoutContentView(Context context, ActionBarLayoutView actionBarLayoutView) {
        super(context);
        mActionBarLayoutView = actionBarLayoutView;
        initView();
    }

    public void setContentViewBack(int layoutResID) {
        View view = mInflater.inflate(layoutResID, this, true);
        setContentViewBack((FrameLayout) view);
    }

    public void setContentViewBack(View contentView) {
        this.contentViewBack = contentView;
        if (this.contentViewBackLayout != null) {
            this.contentViewBackLayout.removeAllViews();
            this.contentViewBackLayout.addView(contentView);
            paddingTop_back = contentViewBack.getPaddingTop();
        }
    }

    private LayoutInflater mInflater;

    private void initView() {
        mInflater = LayoutInflater.from(getContext());
        removeAllViews();
        initContentLayout();

    }

    private void initContentLayout() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentViewBackLayout = new FrameLayout(getContext());
        addView(contentViewBackLayout, layoutParams);
    }

    private void initActionBarTipLayout() {
        if (actionBarTipLayout == null) {
            actionBarTipLayout = new FrameLayout(getContext());
            addView(actionBarTipLayout);
        }
    }

    private boolean hasContainBackground = true;
    private int actionBar_Height = 0;
    private int paddingTop_old = 0;
    private int paddingTop_back = 0;
    private int paddingTop_front = 0;

    public void setHasContainBackground(boolean hasContainBackGround) {
        this.hasContainBackground = hasContainBackGround;
        setActionbarType(actionbarType);
    }

    private int header_height_last;

    public int getChangeHeight() {
        ActionBarLayoutHeaderView headerView = mActionBarLayoutView.getActionBarLayoutHeaderView();
        int h = headerView.getHeight();
        header_height_last = (header_height_last == 0 ? h : (h + (h - header_height_last)));
        return header_height_last == 0 ? headerView.getHeight() : headerView.getHeight();
    }

    /**
     * 获取内边距
     *
     * @return
     */
    public int getActionBarPaddingTop() {
        ActionBarLayoutHeaderView headerView = mActionBarLayoutView.getActionBarLayoutHeaderView();
        //设置padding
        switch (actionbarType) {
            case NORMAL:
                return paddingTop_old + getChangeHeight();
            case NO_STATUS:
                return headerView.getMeasuredHeight();
            case ACTION_STACK:
                return paddingTop_old + headerView.getStatusBar_Height();
            case NO_ACTION_BAR:
                return paddingTop_old + headerView.getStatusBar_Height();
            case ACTION_TRANSPARENT:
                return paddingTop_old + getChangeHeight();
            case ACTION_STACK_NO_STATUS:
                return paddingTop_old;
            case NO_ACTION_BAR_NO_STATUS:
                return paddingTop_old;
            default:
                return 0;
        }
    }

    public void setMarginTopOrPaddingTop() {
        QDLogger.d("setMarginTopOrPaddingTop");

        LayoutParams layoutParams = (LayoutParams) contentViewBack.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        int marginTop = 0;
        int paddingTopBack = 0;
        if (hasContainBackground) {//设置padding
            paddingTopBack = paddingTop_back + getActionBarPaddingTop();
        } else {
            marginTop = paddingTop_back + getActionBarPaddingTop();
            paddingTopBack = paddingTop_back;
        }
        layoutParams.topMargin = marginTop;

        if (contentViewBack != null) {
            contentViewBack.setPadding(contentViewBack.getPaddingLeft(), paddingTopBack, contentViewBack.getPaddingRight(), contentViewBack.getPaddingBottom());
            contentViewBack.setLayoutParams(layoutParams);
        }
        setActionBarTipLayoutTop();
    }

    private ActionBarInterface.ACTIONBAR_TYPE actionbarType = NORMAL;

    public void setActionbarType(ActionBarInterface.ACTIONBAR_TYPE actionbarType) {
        this.actionbarType = actionbarType;
        setMarginTopOrPaddingTop();
    }

    public void setActionbartipType(ActionBarTip.ACTIONBARTIP_TYPE actionbartipType) {
        this.actionbartipType = actionbartipType;
        setMarginTopOrPaddingTop();
    }

    public ActionBarTip.ACTIONBARTIP_TYPE actionbartipType = ActionBarTip.ACTIONBARTIP_TYPE.NORMAL;
    private ActionBarTip actionBarTip;
    public void addActionBarTipView(ActionBarTip actionBarTip) {
        this.actionBarTip = actionBarTip;
        actionBarTip.setBelowContent(this);
        if (actionBarTipLayout == null) {
            initActionBarTipLayout();
        }
        actionBarTipLayout.addView(actionBarTip);
        setActionBarTipLayoutTop();
    }

    private void setActionBarTipLayoutTop() {
        if (actionBarTipLayout == null) {
            initActionBarTipLayout();
        }
        if (actionBarTip != null) {
            actionBarTip.setActionTipType(actionbartipType);
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = getActionBarPaddingTop();
        actionBarTipLayout.setLayoutParams(layoutParams);
    }

}