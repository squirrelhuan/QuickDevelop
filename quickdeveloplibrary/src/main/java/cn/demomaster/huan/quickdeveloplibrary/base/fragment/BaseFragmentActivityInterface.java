package cn.demomaster.huan.quickdeveloplibrary.base.fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayoutView;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;

/**
 * @author squirrel桓
 * @date 2018/12/7.
 * description：
 */
public interface BaseFragmentActivityInterface {
    //是否使用自定义导航栏
    boolean isUseActionBarLayout();

    ViewGroup getContentView(LayoutInflater inflater);

    void initView(View rootView, ActionBarInterface actionBarLayout);
    boolean onKeyDown(int keyCode, KeyEvent event);
    //void back();
}
