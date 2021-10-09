package cn.demomaster.huan.quickdeveloplibrary.base.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;

import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;

/**
 * Created by Squirrel桓 on 2019/1/3.
 */
public abstract class QDFragment extends QuickFragment {
    
    /*private OptionsMenu optionsMenu;
    private OptionsMenu.Builder optionsMenubuilder;*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    /*public boolean dispatchTouchEvent(MotionEvent me) {1024
        return false;
    }*/
   /* public PhotoHelper getPhotoHelper() {
        if (getContext() instanceof QDActivity) {
            return ((QDActivity) getContext()).getPhotoHelper();
        }
        return null;
    }*/
    
   /* //获取自定义菜单
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
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        //NetworkUtil.unRegisterNetworkStatusChangedListener(getContext());
        //请手动注销，已添加的广播监听
    }
    
}
