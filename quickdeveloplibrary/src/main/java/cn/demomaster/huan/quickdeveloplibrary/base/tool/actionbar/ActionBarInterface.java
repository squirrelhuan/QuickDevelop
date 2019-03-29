package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.graphics.Bitmap;
import android.view.View;

public interface ActionBarInterface {

    void setTitle(String title);

    void setStateBarColorAuto(Boolean barColorAuto);

    void setLeftOnClickListener(View.OnClickListener leftOnClickListener);

    void setRightOnClickListener(View.OnClickListener rightOnClickListener);

    void setActionBarThemeColors(int lightColor,int darkColor);

     ActionBarTip getActionBarTip();

    void setHeaderBackgroundColor(int blue);

    void setActionBarType(ACTIONBAR_TYPE noActionBarNoStatus);

    View getLeftView();

    View generateView();

    View getRightView();

    void setFullScreen(boolean b);

    View getActionBarLayoutHeaderView();

    void setActionBarTipType(ActionBarTip.ACTIONBARTIP_TYPE actionbartip_type);

    void setHasContainBackground(boolean isChecked);

    /**
     * 导航栏样式三种
     */
    public static enum ACTIONBAR_TYPE {
        //无导航栏
        NO_ACTION_BAR,
        //无导航栏并且内容可填充到状态栏
        NO_ACTION_BAR_NO_STATUS,
        //有导航栏
        NORMAL,
        //层叠
        ACTION_STACK,
        //层叠并且内容可填充到状态栏
        ACTION_STACK_NO_STATUS,
        //透明导航栏
        ACTION_TRANSPARENT
    }


    public static enum  ContentType{
        ActivityModel,FragmentModel
    }
}
