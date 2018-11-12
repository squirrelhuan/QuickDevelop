package cn.demomaster.huan.quickdeveloplibrary.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.demomaster.huan.quickdeveloplibrary.ApplicationParent;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;

public class BaseActivityParent extends BaseActivityRoot {


    //private LayoutInflater inflater;
    public ActionBarLayout actionBarLayout;
    @Override
    public void setContentView(int layoutResID) {
        mContext = this;
        actionBarLayout = ActionBarHelper.init(this,layoutResID);
        View view = actionBarLayout.getFinalView();
        super.setContentView(view);
    }


}
