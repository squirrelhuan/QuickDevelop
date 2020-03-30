package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.view.View;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;

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
    ImageTextView getRightView();
    void setFullScreen(boolean b);

    View getActionBarLayoutHeaderView();

    void setActionBarTipType(ActionBarTip.ACTIONBARTIP_TYPE actionbartip_type);

    void setHasContainBackground(boolean isChecked);

    void onClickBack();
}
