package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

import static cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayoutView.ACTIONBAR_TYPE.NORMAL;

/**
 * Created by Squirrel桓 on 2018/11/9.
 */
public class ActionBarLayoutContentView extends FrameLayout {

    public ActionBarLayoutContentView(@NonNull Context context) {
        super(context);
        initView();
    }

    public ActionBarLayoutContentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ActionBarLayoutContentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private FrameLayout contentViewBackLayout;//, contentViewFrontLayout;
    private View contentViewBack;//, contentViewFront;
    private ActionBarLayoutView mActionBarLayoutView;

    public ActionBarLayoutContentView(Context context, ActionBarLayoutView actionBarLayoutView) {
        super(context);
        mActionBarLayoutView = actionBarLayoutView;
        initView();
    }

    public void setContentViewBack(int layoutResID) {
        View view = mInflater.inflate(layoutResID, this, true);
        setContentViewBack((FrameLayout) view);
    }

   /* public void setContentViewFront(int layoutResID) {
        View view = mInflater.inflate(layoutResID, this, true);
        setContentViewFront((FrameLayout) view);
    }*/

    public void setContentViewBack(View contentView) {
        this.contentViewBack = contentView;
        if (this.contentViewBackLayout != null) {
            this.contentViewBackLayout.removeAllViews();
            this.contentViewBackLayout.addView(contentView);
            paddingTop_back = contentViewBack.getPaddingTop();
        }
    }
/*

    public void setContentViewFront(View contentView) {
        this.contentViewFront = contentView;
        if (this.contentViewFrontLayout != null) {
            this.contentViewFrontLayout.removeAllViews();
            this.contentViewFrontLayout.addView(contentView);
            paddingTop_front = contentViewFront.getPaddingTop();
        }
    }
*/

    private LayoutInflater mInflater;

    private void initView() {
        mInflater = LayoutInflater.from(getContext());
        removeAllViews();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentViewBackLayout = new FrameLayout(getContext());
        contentViewBackLayout.setBackgroundColor(Color.YELLOW);
        addView(contentViewBackLayout, layoutParams);
        /*contentViewFrontLayout = new FrameLayout(getContext());
        contentViewFrontLayout.setId(R.id.qd_fragment_content_view);
        addView(contentViewFrontLayout, layoutParams);*/
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
        QDLogger.d(getActionBarPaddingTop());

        LayoutParams layoutParams = (LayoutParams) contentViewBack.getLayoutParams();
        int marginTop =0;
        int paddingTopBack =0;
        int paddingTopFront =0;
        if (hasContainBackground) {//设置padding

            paddingTopBack = paddingTop_back + getActionBarPaddingTop();
            paddingTopFront = paddingTop_front + getActionBarPaddingTop();
        }else {
            marginTop = paddingTop_back + getActionBarPaddingTop();
            paddingTopBack = paddingTop_back;
            paddingTopFront = paddingTop_front;
        }
        layoutParams.topMargin = marginTop;

        if (contentViewBack != null) {
            contentViewBack.setPadding(contentViewBack.getPaddingLeft(), paddingTopBack, contentViewBack.getPaddingRight(), contentViewBack.getPaddingBottom());
            contentViewBack.setLayoutParams(layoutParams);
        }
       /* if (contentViewFront != null) {
            contentViewFront.setPadding(contentViewFront.getPaddingLeft(), paddingTopFront , contentViewFront.getPaddingRight(), contentViewFront.getPaddingBottom());
            contentViewFront.setLayoutParams(layoutParams);
        }*/


        if (hasContainBackground) {//设置padding
         /*   if (contentViewBack != null)
                contentViewBack.setPadding(contentViewBack.getPaddingLeft(), paddingTop_back + getActionBarPaddingTop(), contentViewBack.getPaddingRight(), contentViewBack.getPaddingBottom());

            if (contentViewFront != null)
                contentViewFront.setPadding(contentViewFront.getPaddingLeft(), paddingTop_front + getActionBarPaddingTop(), contentViewFront.getPaddingRight(), contentViewFront.getPaddingBottom());
        */} else {//设置margin
           /* if (contentViewBack != null) {
                LayoutParams layoutParams = (LayoutParams) contentViewBack.getLayoutParams();
                layoutParams.topMargin = paddingTop_back + getActionBarPaddingTop();
                contentViewBack.setLayoutParams(layoutParams);
            }
            if (contentViewFront != null) {
                LayoutParams layoutParams = (LayoutParams) contentViewFront.getLayoutParams();
                layoutParams.topMargin = paddingTop_front + getActionBarPaddingTop();
                contentViewFront.setLayoutParams(layoutParams);
            }*/
        }
    }

    private ActionBarLayoutView.ACTIONBAR_TYPE actionbarType = NORMAL;

    public void setActionbarType(ActionBarLayoutView.ACTIONBAR_TYPE actionbarType) {
        this.actionbarType = actionbarType;
        setMarginTopOrPaddingTop();
    }


}