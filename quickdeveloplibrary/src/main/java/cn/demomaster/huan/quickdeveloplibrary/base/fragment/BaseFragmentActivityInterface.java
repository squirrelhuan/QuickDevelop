package cn.demomaster.huan.quickdeveloplibrary.base.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayoutView;

/**
 * @author squirrel桓
 * @date 2018/12/7.
 * description：
 */
public interface BaseFragmentActivityInterface {
    //是否使用自定义导航栏
    boolean isUseActionBarLayout();
    ViewGroup getContentView(LayoutInflater inflater);
    //void initActionBarLayout(ActionBarLayout actionBarLayout);
    void initView(View rootView,ActionBarLayout actionBarLayout);
    void initView(View rootView,ActionBarLayoutView actionBarLayout);
}
