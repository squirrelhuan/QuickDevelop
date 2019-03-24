package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.view.View;

public interface ActionBarInterface {

    void setTitle(String title);

    void setStateBarColorAuto(Boolean barColorAuto);

    void setLeftOnClickListener(View.OnClickListener leftOnClickListener);

    void setRightOnClickListener(View.OnClickListener rightOnClickListener);

    void setActionBarThemeColors(int lightColor,int darkColor);
}
