package cn.demomaster.huan.quickdeveloplibrary.base.fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDBaseInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBar;

/**
 * @author squirrel桓
 * @date 2018/12/7.
 * description：
 */
public interface QDFragmentInterface extends QDBaseInterface {

    ViewGroup getContentView(LayoutInflater inflater);
    void initView(View rootView, ActionBar actionBarLayout);
    boolean onKeyDown(int keyCode, KeyEvent event);
    //void back();
}
