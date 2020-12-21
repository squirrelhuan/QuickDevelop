package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;

import static cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ACTIONBAR_TYPE.NORMAL;

public class ActionBarTool implements ActionBarLayoutInterface{
    Activity activity;
    Fragment fragment;
    public ActionBarTool(Activity activity) {
        this.activity = activity;
    }
    public ActionBarTool(Fragment fragment) {
        this.activity = fragment.getActivity();
        this.fragment = fragment;
    }
    
    View mActionView;
    View mContentView;
    ActionBarLayout2 actionBarLayout2;

    public ActionBarLayout2 getActionBarLayout() {
        return actionBarLayout2;
    }

    public void setContentView(int contentViewId){
        setContentView(getLayoutInflater().inflate(contentViewId,null));
    }
    public void setContentView(View contentView){
        mContentView = contentView;
    }
    public LayoutInflater getLayoutInflater(){
       return  activity.getLayoutInflater();
    }
    public void setActionView(int actionbarViewId){
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(actionbarViewId,null);
        setActionView(view);
    }

    public void setActionView(View actionView){
        mActionView = actionView;
    }

    ACTIONBAR_TYPE actionbarType = NORMAL;
    /**
     * 设置导航栏样式
     *
     * @param actionbarType
     */
    public void setActionBarType(ACTIONBAR_TYPE actionbarType) {
        this.actionbarType = actionbarType;
        if(actionBarLayout2!=null){
            actionBarLayout2.setActionBarType(actionbarType);
        }
    }

    private boolean mixStatusActionBar = true;//状态栏和导航栏融合
    private boolean hasStatusBar = true;
    private boolean hasActionBar = true;
    private ActionBarLayout2.ContentView_Layout_Below actionBarPaddingTop = ActionBarLayout2.ContentView_Layout_Below.actionBar;

    public void setHasStatusBar(boolean hasStatusBar) {
        this.hasStatusBar = hasStatusBar;
        if(actionBarLayout2!=null){
            actionBarLayout2.setHasStatusBar(hasStatusBar);
        }
    }

    public void setHasActionBar(boolean hasActionBar) {
        this.hasActionBar = hasActionBar;
        if(actionBarLayout2!=null){
            actionBarLayout2.setHasActionBar(hasActionBar);
        }
    }

    public void setActionBarPaddingTop(ActionBarLayout2.ContentView_Layout_Below actionBarPaddingTop) {
        this.actionBarPaddingTop = actionBarPaddingTop;
        if(actionBarLayout2!=null){
            actionBarLayout2.setContentViewPaddingTop(actionBarPaddingTop);
        }
    }

    /**
     * 设置是否合并导航栏和状态栏
     * @param mixStatusActionBar
     */
    public void setMixStatusActionBar(boolean mixStatusActionBar) {
        this.mixStatusActionBar = mixStatusActionBar;
        if(actionBarLayout2!=null){
            actionBarLayout2.setMixStatusActionBar(mixStatusActionBar);
        }
    }

    public View inflateView(){
        if(mActionView!=null&&actionBarLayout2==null){
            ActionBarLayout2.Builder builder = new ActionBarLayout2.Builder(activity);
            int statusHeight = DisplayUtil.getStatusBarHeight(activity);
            actionBarLayout2 = builder.setStatusHeight(statusHeight)
                    .setHasStatusBar(hasStatusBar)
                    .setHasActionBar(hasActionBar)
                    .setContentView(mContentView)
                    .setContentViewPaddingTop(actionBarPaddingTop)
                    .setMixStatusActionBar(mixStatusActionBar)
                    .setActionBarView(mActionView)
                    .setActionbarType(actionbarType)
                    .creat();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                actionBarLayout2.setId(View.generateViewId());
            }
        }else {
            return mContentView;
        }
        return actionBarLayout2;
    }

    @Override
    public ImageTextView getRightView() {
        if(actionBarLayout2==null) {
            return null;
        }
        return actionBarLayout2.findViewById(R.id.it_actionbar_common_right);
    }

    public ImageTextView getTitleView() {
        return actionBarLayout2.findViewById(R.id.it_actionbar_common_title);
    }

    public void setHeaderBackgroundColor(int color) {
        if(mActionView!=null) {
            mActionView.setBackgroundColor(color);
        }
    }

    @Override
    public void setTitle(CharSequence string) {
        if(actionBarLayout2!=null) {
            ImageTextView imageTextView = actionBarLayout2.findViewById(R.id.it_actionbar_common_title);
            if (imageTextView != null) {
                imageTextView.setText((String) string);
            }
        }
    }

    @Override
    public void setStateBarColorAuto(boolean b) {

    }

    @Override
    public void setLeftOnClickListener(View.OnClickListener onClickListener) {
        getLeftView().setOnClickListener(onClickListener);
    }

    boolean isLightModle = false;//亮色

    /**
     * 设置颜色模式
     *
     * @param lightModle
     */
    public void setLightModle(boolean lightModle) {
        isLightModle = lightModle;
        resetTextColor(actionBarLayout2);
    }

    public int textLightColor = Color.WHITE;
    public int textDarkColor = Color.BLACK;

    @Override
    public void setActionBarThemeColors(int lightColor, int darkColor) {
        textLightColor = lightColor;
        textDarkColor = darkColor;
        resetTextColor(mActionView);
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
        if(view==null){
            return;
        }
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

    @Override
    public void setRightOnClickListener(View.OnClickListener onClickListener) {
        getRightView().setOnClickListener(onClickListener);
    }

    public View getActionBarTool() {
        return mActionView;
    }

    public ActionBarTip getActionBarTip() {
        return null;
    }

    public void setActionBarTipType(ActionBarTip.ACTIONBARTIP_TYPE actionbartip_type) {
    }

    public ImageTextView getLeftView() {
        if(actionBarLayout2==null) {
            return null;
        }
        return actionBarLayout2.findViewById(R.id.it_actionbar_common_left);
    }
}
