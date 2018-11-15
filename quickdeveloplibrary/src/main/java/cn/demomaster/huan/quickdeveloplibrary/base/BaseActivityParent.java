package cn.demomaster.huan.quickdeveloplibrary.base;

import android.view.View;

import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.CustomDialog;

public class BaseActivityParent extends BaseActivityRoot {


    //private LayoutInflater inflater;
    public ActionBarLayout actionBarLayout;
    public OptionsMenu optionsMenu;
    @Override
    public void setContentView(int layoutResID) {
        mContext = this;
        actionBarLayout = ActionBarHelper.init(this,layoutResID);
        View view = actionBarLayout.getFinalView();
        super.setContentView(view);
        optionsMenu = new OptionsMenu(this);

    }


}
