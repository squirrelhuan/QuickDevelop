package cn.demomaster.huan.quickdeveloplibrary.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayoutView;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;

public abstract class BaseActivityParent extends BaseActivityRoot {

    //private LayoutInflater inflater;
    private int layoutResID;
    private int headlayoutResID = R.layout.activity_actionbar_common;
    private ActionBarLayout actionBarLayout;
    private OptionsMenu optionsMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        this.mContext = this;
        if(layoutResID==-1){
            layoutResID = R.layout.activity_layout_empty;
        }
        if (isUseActionBarLayout()) {//是否使用自定义导航栏
            this.layoutResID = layoutResID;
            View view = getActionBarLayout().getFinalView();
            super.setContentView(view);
        } else {
            super.setContentView(layoutResID);
        }
    }

    public int getHeadlayoutResID() {
        return headlayoutResID;
    }

    //获取自定义导航
    public ActionBarLayout getActionBarLayout() {
        if (!isUseActionBarLayout()) {
            //throw new IllegalStateException("Base URL required.");
            return null;
        }
        if (actionBarLayout == null) {
            actionBarLayout = ActionBarHelper.init(this, layoutResID,getHeadlayoutResID());
        }
        return actionBarLayout;
    }

  /*  public ActionBarLayoutView getActionBarLayout2(){
        ActionBarLayoutView.Builder builder = new ActionBarLayoutView.Builder(mContext,layoutResID,layoutResID,R.layout.activity_action_bar_test);
       return builder.creat();
    }*/

    //获取自定义菜单
    public OptionsMenu getOptionsMenu() {
        if (optionsMenu == null) {
            optionsMenu = new OptionsMenu(this);
        }
        return optionsMenu;
    }

    public void showMessage(String message){
        PopToastUtil.ShowToast(this, message);
        //getMesageHelper().showMessage(message);
    }
}
