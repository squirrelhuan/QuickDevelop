package cn.demomaster.huan.quickdeveloplibrary.base.fragment;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;
import cn.demomaster.qdrouter_library.base.fragment.ViewLifecycle;
import cn.demomaster.qdrouter_library.manager.QuickFragmentHelper;
import cn.demomaster.qdrouter_library.view.ImageTextView;

/**
 * Created by Squirrel桓 on 2019/1/3.
 */
public abstract class QDFragment extends QuickFragment {

    private OptionsMenu optionsMenu;
    private OptionsMenu.Builder optionsMenubuilder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initHelper();
    }
    
    public boolean dispatchTouchEvent(MotionEvent me) {
        return false;
    }
    public PhotoHelper photoHelper;
    
    //实例化各种帮助类
    private void initHelper() {
        if (getContext() instanceof QDActivity) {
            photoHelper = ((QDActivity) getContext()).getPhotoHelper();
        }
    }

    //获取自定义菜单
    public OptionsMenu getOptionsMenu() {
        if (optionsMenu == null) {
            optionsMenu = new OptionsMenu(new OptionsMenu.Builder(mContext));
        }
        return optionsMenu;
    }
    
    //获取自定义菜单
    public OptionsMenu.Builder getOptionsMenuBuilder() {
        if (optionsMenubuilder == null) {
            optionsMenubuilder = new OptionsMenu.Builder(mContext);
        }
        return optionsMenubuilder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //NetworkUtil.unRegisterNetworkStatusChangedListener(getContext());
        //请手动注销，已添加的广播监听
    }
    
}
