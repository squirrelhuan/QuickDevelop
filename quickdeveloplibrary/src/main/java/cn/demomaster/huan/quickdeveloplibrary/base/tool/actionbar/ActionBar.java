package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragmentInterface;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;
import cn.demomaster.qdlogger_library.QDLogger;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_BACK;
import static cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ACTIONBAR_TYPE.NORMAL;

public class ActionBar extends FrameLayout {

    public ActionBar(QDFragment qdFragment) {
        super(qdFragment.getContext());
        this.fragmentWeakReference = new WeakReference<>(qdFragment);
        this.contextType = ActivityContentType.FragmentModel;
        init();
    }

    public ActionBar(@NonNull Context context) {
        super(context);
        init();
    }

    public ActionBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ActionBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private int statusHeight = DisplayUtil.getStatusBarHeight(getContext());
    private int actionHeight = DisplayUtil.getActionBarHeight(getContext());
    ActionLayout actionLayout;
    FrameLayout contentLayout;
    FrameLayout fragmentLayout;

    private int contentResId = -1;
    private LayoutInflater mInflater;

    public int contentViewTopPadding = -10000;
    public void setContentViewTopPadding(int contentViewTopPadding) {
        this.contentViewTopPadding = contentViewTopPadding;
        setContentViewPaddingTop();
    }

    private void init() {
        mInflater = LayoutInflater.from(getContext());
        contentLayout = new FrameLayout(getContext());
        fragmentLayout = new FrameLayout(getContext());
        fragmentLayout.setId(R.id.qd_fragment_content_view);

        actionLayout = new ActionLayout(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setId(View.generateViewId());
            actionLayout.setId(View.generateViewId());
            contentLayout.setId(View.generateViewId());
        }

        addViewLayout();
    }

    private void addViewLayout() {
        LayoutParams layoutParams01 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(contentLayout, layoutParams01);

        setContentViewPaddingTop();

        LayoutParams layoutParams02 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(actionLayout, layoutParams02);

        LayoutParams layoutParams04 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(fragmentLayout, layoutParams04);
    }

    private void setContentViewPaddingTop() {
        int count = contentLayout.getChildCount();
        if (count > 0) {
            View view = contentLayout.getChildAt(0);
            if(contentViewTopPadding!=10000) {
                view.setPadding(view.getPaddingLeft(), contentViewTopPadding, view.getPaddingRight(), view.getPaddingBottom());
            }
        }
    }

    public void setContentView(View contentView) {
        if (contentView == null) return;
        contentLayout.addView(contentView);
    }

    public void setContentView(int contentResId) {
        this.contentResId = contentResId;
        if (contentResId == -1) return;
        View view = mInflater.inflate(contentResId, this, false);
        setContentView(view);
    }

    public void addHeadView(int layoutResID) {
        if (layoutResID == -1) return;
        View view = mInflater.inflate(layoutResID, actionLayout, false);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //layoutParams.gravity= Gravity.CENTER;
        actionLayout.addView(view);
    }

    public void setHeaderResId(int headlayoutResID) {
        addHeadView(headlayoutResID);
    }

    /**
     * 设置导航栏背景色
     *
     * @param color
     */
    public void setHeaderBackgroundColor(int color) {
        //actionLayout.setBackgroundColor(color);
        //statusLayout.setBackgroundColor(transparent);
        actionLayout.post(new Runnable() {
            @Override
            public void run() {
                setColorWithAnimation(actionLayout, color);
            }
        });
    }

    private void setColorWithAnimation(final View view, final int colorTo) {
        int x = view.getLeft();
        int y = view.getBottom();

        //findViewById(R.id.appBarContainer).setBackgroundColor(mCurrentColor);
        view.setBackgroundColor(colorTo);

        int startRadius = 0;
        int endRadius = (int) Math.hypot(view.getWidth(), view.getHeight());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if(view.isAttachedToWindow()) {
                Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, endRadius);
                anim.setDuration(500);
                anim.start();
            }
        }
    }

    ACTIONBAR_TYPE actionbarType = NORMAL;

    /**
     * 设置导航栏样式
     *
     * @param actionbarType
     */
    public void setActionBarType(ACTIONBAR_TYPE actionbarType) {
        this.actionbarType = actionbarType;
        switchActivonBarType();
    }

    /**
     * 重置布局
     */
    public void switchActivonBarType() {
        LayoutParams layoutParams_content = (LayoutParams) contentLayout.getLayoutParams();
        if (actionLayout != null) {
            actionHeight = actionLayout.getHeight();
            //QDLogger.d("switchActivonBarType actionHeight =" + actionHeight);
        }
        switch (actionbarType) {
            case NORMAL:
                actionLayout.setHasStatusBar(true);
                actionLayout.setVisibility(VISIBLE);
                layoutParams_content.topMargin = actionHeight;
                contentLayout.setLayoutParams(layoutParams_content);
                break;
            case NO_STATUS:
                actionLayout.setVisibility(VISIBLE);
                actionLayout.setHasStatusBar(false);
                layoutParams_content.topMargin = actionHeight;
                contentLayout.setLayoutParams(layoutParams_content);
                break;
            case NO_ACTION_BAR:
                actionLayout.setHasStatusBar(true);
                actionLayout.setVisibility(GONE);
                contentLayout.setVisibility(VISIBLE);
                layoutParams_content.topMargin = statusHeight;
                contentLayout.setLayoutParams(layoutParams_content);
                break;
            case NO_ACTION_BAR_NO_STATUS:
                actionLayout.setVisibility(GONE);
                layoutParams_content.topMargin = 0;
                contentLayout.setLayoutParams(layoutParams_content);
                break;
            case ACTION_STACK:
                actionLayout.setHasStatusBar(true);
                actionLayout.setVisibility(VISIBLE);
                layoutParams_content.topMargin = 0;
                contentLayout.setLayoutParams(layoutParams_content);
                break;
            case ACTION_STACK_NO_ACTION:
                actionLayout.setHasStatusBar(false);
                actionLayout.setVisibility(GONE);

                contentLayout.setVisibility(VISIBLE);
                layoutParams_content.topMargin = 0;
                contentLayout.setLayoutParams(layoutParams_content);
                break;
            case ACTION_STACK_NO_STATUS:
                actionLayout.setVisibility(VISIBLE);
                actionLayout.setHasStatusBar(false);
                layoutParams_content.topMargin = 0;
                contentLayout.setLayoutParams(layoutParams_content);
                break;
            case ACTION_TRANSPARENT:
                actionLayout.setHasStatusBar(true);
                break;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutChildren(left, top, right, bottom, false /* no force left gravity */);
    }

    boolean hasActionLayoutChanged = true;

    void layoutChildren(int left, int top, int right, int bottom, boolean forceLeftGravity) {
        final int count = getChildCount();

        final int parentLeft = 0;
        final int parentRight = 0;

        final int parentTop = 0;
        final int parentBottom = bottom - top - 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();

                int childLeft;
                int childTop;

                int gravity = lp.gravity;
                if (gravity == -1) {
                    gravity = Gravity.TOP | Gravity.START;
                }

                final int layoutDirection;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    layoutDirection = getLayoutDirection();
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

                    if (child instanceof ActionLayout) {
                        child.layout(childLeft, childTop, childLeft + width, childTop + height);
                        actionHeight = ((ActionLayout) child).hasStatusBar ? child.getHeight() - statusHeight : child.getHeight();
                        if (actionBarTip != null) {
                            actionBarTip.setActionBarHeight(statusHeight + actionHeight);
                        }
                        if (hasActionLayoutChanged) {
                            hasActionLayoutChanged = false;
                            switchActivonBarType();
                            //Log.e("CGQ", "actionHeight=" + actionHeight);
                        }
                    } else if (child.getId() == contentLayout.getId() && hasActionLayoutChanged) {
                        int ctop = childTop;
                        switch (actionbarType) {
                            case NORMAL:
                                ctop = childTop + actionHeight;
                                break;
                        }
                        child.layout(childLeft, ctop, childLeft + width, childTop + height);
                    } else {
                        child.layout(childLeft, childTop, childLeft + width, childTop + height);
                    }
                }
            }
        }
    }

    public void setStateBarColorAuto(boolean b) {
    }

    public void setTitle(CharSequence title) {
        actionLayout.setTitle(title);
    }

    public void setRightOnClickListener(OnClickListener onClickListener) {
        actionLayout.setRightOnClickListener(onClickListener);
    }

    public ImageTextView getRightView() {
        return findViewById(R.id.it_actionbar_common_right);
    }

    public View getActionBarLayoutHeaderView() {
        return actionLayout;
    }

    public void setActionBarTipType(ActionBarTip.ACTIONBARTIP_TYPE actionbartip_type) {
    }

    private ActionBarTip actionBarTip;

    // activity提示layout
    //调整为动态加载
    public ActionBarTip getActionBarTip() {
        if (actionBarTip == null) {
            actionBarTip = new ActionBarTip(getContext());
            LayoutParams layoutParams_tip = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            addView(actionBarTip, layoutParams_tip);
            actionBarTip.setContentView(R.layout.quickdevelop_activity_actionbar_tip);
        }
        actionBarTip.setActionBarHeight(statusHeight + actionHeight);
        return actionBarTip;
    }

    @Override
    public View getChildAt(int index) {
        View view = super.getChildAt(index);
        if (actionBarTip != null) {
            if (view == actionLayout) {
                return actionBarTip;
            } else if (view == actionBarTip) {
                return actionLayout;
            }
        }
        return super.getChildAt(index);
    }

    public void setHasContainBackground(boolean isChecked) {
    }

    public ImageTextView getLeftView() {
        return findViewById(R.id.it_actionbar_common_left);
    }

    public void setFullScreen(boolean b) {
    }

    public void setLeftOnClickListener(OnClickListener onClickListener) {
        actionLayout.setLeftOnClickListener(onClickListener);
    }

    private WeakReference<Fragment> fragmentWeakReference;
    private ActivityContentType contextType = ActivityContentType.ActivityModel;

    public void onClickBack() {
        if (contextType == ActivityContentType.FragmentModel && fragmentWeakReference.get() instanceof QDFragmentInterface) {
            int eventCode = KEYCODE_BACK;
            long now = SystemClock.uptimeMillis();
            KeyEvent down = new KeyEvent(now, now, ACTION_DOWN, eventCode, 0);
            boolean ret = ((QDFragmentInterface) fragmentWeakReference.get()).onKeyDown(eventCode, down);
            if (ret) {
                QDLogger.d(fragmentWeakReference.get() + " fragment 消费了返回事件" + ret);
                return;
            }
        } else if (contextType == ActivityContentType.ActivityModel) {
            ((Activity) getContext()).onBackPressed();
        }
        //context.get().onKeyDown(eventCode,down);
        //((QDActivityInterface)context.get()).onClickBack();
    }

    boolean isLightModle = false;//亮色

    /**
     * 设置颜色模式
     *
     * @param lightModle
     */
    public void setLightModle(boolean lightModle) {
        isLightModle = lightModle;
        resetTextColor(this);
    }

    public int textLightColor = Color.WHITE;
    public int textDarkColor = Color.BLACK;

    public void setActionBarThemeColors(int lightColor, int darkColor) {
        textLightColor = lightColor;
        textDarkColor = darkColor;
        resetTextColor(actionLayout);
    }

    public int getTextColor() {
        return isLightModle ? textLightColor : textDarkColor;
    }

    /**
     * 重置文本顔色
     *
     * @param view
     */
    private void resetTextColor(View view) {
        int color = getTextColor();
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (viewGroup.getChildAt(i) instanceof ImageView) {
                    Drawable drawable = (((ImageView) viewGroup.getChildAt(i)).getDrawable());
                    if (drawable != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            drawable.setTint(color);
                        }
                    }
                }
                if (viewGroup.getChildAt(i) instanceof ImageTextView) {
                    ((ImageTextView) viewGroup.getChildAt(i)).setTextColor(color);
                } else if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                    resetTextColor(viewGroup.getChildAt(i));
                } else if (viewGroup.getChildAt(i) instanceof TextView) {
                    ((TextView) viewGroup.getChildAt(i)).setTextColor(color);
                }
            }
        }
    }

    public void onDestroy() {
        if (actionBarTip != null) {
            actionBarTip.onDestroy();
        }
    }

    public void hideTitle() {
        View view = actionLayout.getTitleView();
        if (view != null) {
            view.setVisibility(GONE);
        }
    }
}
