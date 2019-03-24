package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.ScreenShotUitl;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;

import static cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayoutView.ACTIONBAR_TYPE.NORMAL;

/**
 * Created by Squirrel桓 on 2018/11/9.
 */
public class ActionBarLayoutHeaderView extends FrameLayout {

    private ImageTextView it_actionbar_title;
    private ImageTextView it_actionbar_common_left;
    private ImageTextView it_actionbar_common_right;
    private View.OnClickListener leftOnClickListener;
    private View.OnClickListener rightOnClickListener;
    private AppCompatActivity context;

    /**
     * 获取中间视图
     *
     * @return
     */
    public ImageTextView getCenterView() {
        return it_actionbar_title;
    }

    /**
     * 获取左侧控件
     *
     * @return
     */
    public ImageTextView getLeftView() {
        return it_actionbar_common_left;
    }

    /**
     * 获取右侧侧视图控件
     *
     * @return
     */
    public ImageTextView getRightView() {
        return it_actionbar_common_right;
    }


    public ActionBarLayoutHeaderView(@NonNull Context context) {
        super(context);
        initView();
    }

    public ActionBarLayoutHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ActionBarLayoutHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public int getStatusBar_Height() {
        return statusBar_Height;
    }

    private int statusBar_Height = 0;
    private int actionBar_Height = 0;
    private int paddingTop_old = 0;
    private FrameLayout contentView;

    public void setContentView(int layoutResID) {
        View view = mInflater.inflate(layoutResID, this, false);
        setContentView((FrameLayout) view);
    }

    public void setContentView(FrameLayout contentView) {
        this.contentView.removeAllViews();
        this.paddingTop_old = contentView.getPaddingTop();
        this.contentView.addView(contentView);
        //设置内边距
        contentView.setPadding(contentView.getPaddingLeft(), getActionBarPaddingTop(), contentView.getPaddingRight(), contentView.getPaddingBottom());
    }

    public int getActionBarPaddingTop() {
        switch (actionbarType) {
            case NORMAL:
                return paddingTop_old + statusBar_Height;
            case ACTION_STACK:
                return paddingTop_old + statusBar_Height;
            case NO_ACTION_BAR:
                setVisibility(GONE);
                return 0;
            case ACTION_TRANSPARENT:
                return paddingTop_old + statusBar_Height;
            case ACTION_STACK_NO_STATUS:
                return paddingTop_old + statusBar_Height;
            case NO_ACTION_BAR_NO_STATUS:
                return paddingTop_old + statusBar_Height;
        }
        return paddingTop_old;
    }

    private ActionBarLayout.ContextType mContextType = ActionBarLayout.ContextType.ActivityModel;
    private LayoutInflater mInflater;

    private void initView() {
        context = (AppCompatActivity) getContext();
        //记录状态栏高度
        statusBar_Height = DisplayUtil.getStatusBarHeight(getContext());
        mInflater = LayoutInflater.from(getContext());
        removeAllViews();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentView = new FrameLayout(getContext());
        addView(contentView, layoutParams);


        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // globalLayoutListener.onLoadFinish();
                initOnClickListener();
            }
        });
    }

    private GlobalLayoutListener globalLayoutListener;

    public void addGlobalLayoutListener(GlobalLayoutListener globalLayoutListener) {
        this.globalLayoutListener = globalLayoutListener;
    }

    public void setTitle(String title) {
        if (it_actionbar_title != null) {
            it_actionbar_title.setText(title);
        }
    }

    public void setStateBarColorAuto(Boolean barColorAuto) {
    }

    public void setLeftOnClickListener(OnClickListener leftOnClickListener) {
        this.leftOnClickListener = leftOnClickListener;
        if (it_actionbar_common_left != null)
            it_actionbar_common_left.setOnClickListener(leftOnClickListener);
    }

    public void setRightOnClickListener(OnClickListener rightOnClickListener) {
        this.rightOnClickListener = rightOnClickListener;
        if (it_actionbar_common_right != null)
            it_actionbar_common_right.setOnClickListener(rightOnClickListener);
    }

    public void setActionBarThemeColors(int lightColor, int darkColor) {

    }

    public static interface GlobalLayoutListener {
        void onLoadFinish();
    }

    private void initOnClickListener() {
        it_actionbar_title = findViewById(R.id.it_actionbar_common_title);
        if (context.getTitle() != null) {
            setTitle(context.getTitle().toString());
        }

        it_actionbar_common_left = findViewById(R.id.it_actionbar_common_left);
        it_actionbar_common_right = findViewById(R.id.it_actionbar_common_right);
        if (it_actionbar_common_left != null) {
            //it_actionbar_common_left.setOnClickListener(leftOnClickListener);
            if (leftOnClickListener != null) {
                it_actionbar_common_left.setOnClickListener(leftOnClickListener);
            } else {
                it_actionbar_common_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mContextType == ActionBarLayout.ContextType.FragmentModel) {
                            ActionBarLayoutInterface actionBarLayoutInterface = FragmentActivityHelper.getInstance().getActionBarLayoutInterface();
                            actionBarLayoutInterface.onBack(context);
                        } else {
                            context.finish();
                        }
                    }
                });
            }
        }
        if (it_actionbar_common_right != null) {
            if (rightOnClickListener != null) {
                it_actionbar_common_right.setOnClickListener(rightOnClickListener);
            } else {
                it_actionbar_common_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ScreenShotUitl.shot(context);
                    }
                });
            }
        }
    }

    private ActionBarLayoutView.ACTIONBAR_TYPE actionbarType = NORMAL;

    public void setActionbarType(ActionBarLayoutView.ACTIONBAR_TYPE actionbarType) {
        this.actionbarType = actionbarType;
        switch (actionbarType) {
            case NORMAL:
                setVisibility(VISIBLE);
                break;
            case ACTION_STACK:
                setVisibility(VISIBLE);
                break;
            case NO_ACTION_BAR:
                setVisibility(GONE);
                break;
            case ACTION_TRANSPARENT:
                setVisibility(VISIBLE);
                break;
            case ACTION_STACK_NO_STATUS:
                setVisibility(VISIBLE);
                break;
            case NO_ACTION_BAR_NO_STATUS:
                setVisibility(GONE);
                break;
        }
    }

    public void setContentType(ActionBarLayout.ContextType contextType) {
        mContextType = contextType;
    }

}