package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.view.View;

public interface ActionBarLayoutInterface {
    View getRightView();

    void setHeaderBackgroundColor(int red);

    void setTitle(CharSequence string);

    void setStateBarColorAuto(boolean b);

    void setLeftOnClickListener(View.OnClickListener onClickListener);

    void setActionBarThemeColors(int white, int black);

    void setRightOnClickListener(View.OnClickListener onClickListener);
}
