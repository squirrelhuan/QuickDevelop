package cn.demomaster.huan.quickdeveloplibrary.base;

import android.view.View;

import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.CustomDialog;

public abstract class BaseActivityParent extends BaseActivityRoot {


    //private LayoutInflater inflater;
    private int layoutResID;
    private ActionBarLayout actionBarLayout;
    private OptionsMenu optionsMenu;

    @Override
    public void setContentView(int layoutResID) {
        this.mContext = this;
        if (isUseActionBarLayout()) {//是否使用自定义导航栏
            this.layoutResID = layoutResID;
            View view = getActionBarLayout().getFinalView();
            super.setContentView(view);
        } else {
            super.setContentView(layoutResID);
        }

    }

    //获取自定义导航
    public ActionBarLayout getActionBarLayout() {
        if (!isUseActionBarLayout()) {
            return null;
        }
        if (actionBarLayout == null) {
            actionBarLayout = ActionBarHelper.init(this, layoutResID);
        }
        return actionBarLayout;
    }

    //获取自定义菜单
    public OptionsMenu getOptionsMenu() {
        if (optionsMenu == null) {
            optionsMenu = new OptionsMenu(this);
        }
        return optionsMenu;
    }

    public void showMessage(String message){
        getMesageHelper().showMessage(message);
    }
}
