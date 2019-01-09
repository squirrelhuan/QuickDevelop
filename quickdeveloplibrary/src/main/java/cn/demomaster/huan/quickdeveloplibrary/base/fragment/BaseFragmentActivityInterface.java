package cn.demomaster.huan.quickdeveloplibrary.base.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * @author squirrel桓
 * @date 2018/12/7.
 * description：
 */
public interface BaseFragmentActivityInterface {
    //是否使用自定义导航栏
    boolean isUseActionBarLayout();
    ViewGroup getContentView(LayoutInflater inflater);
}
