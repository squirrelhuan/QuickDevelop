package cn.demomaster.huan.quickdeveloplibrary.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import cn.demomaster.huan.quickdeveloplibrary.ApplicationParent;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.receiver.NetWorkChangReceiver;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;

/**
 * Created by Squirrel桓 on 2019/1/3.
 */
public abstract class BaseFragment extends Fragment implements BaseFragmentActivityInterface {

    public AppCompatActivity mContext;
    public Bundle mBundle;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = (AppCompatActivity) this.getContext();
        mBundle = getArguments();
        super.onCreate(savedInstanceState);
        StatusBarUtil.transparencyBar(mContext);
        initHelper();
    }

    private int layoutResID;
    private ActionBarLayout actionBarLayout;
    private OptionsMenu optionsMenu;
    ViewGroup mView;
    View rootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mContext = (AppCompatActivity) this.getContext();
        if (isUseActionBarLayout()) {//是否使用自定义导航栏
            if (mView == null) {
                mView = getContentView(inflater);
                mView.setClickable(true);
                actionBarLayout = ActionBarHelper.init(mContext, mView);
            }
            rootView = getActionBarLayout().getFinalView();
            initView(mView,actionBarLayout);
            rootView.setBackgroundColor(Color.WHITE);
            return rootView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //获取自定义导航
    public ActionBarLayout getActionBarLayout() {
        if (!isUseActionBarLayout()) {
            //throw new IllegalStateException("Base URL required.");
            return null;
        }
        if (actionBarLayout == null) {
            actionBarLayout = ActionBarHelper.init(mContext, layoutResID);
        }
        return actionBarLayout;
    }

    public PhotoHelper photoHelper;
    public NetWorkChangReceiver netWorkChangReceiver;
    public NetWorkChangReceiver.OnNetStateChangedListener onNetStateChangedListener;
    public void setOnNetStateChangedListener(NetWorkChangReceiver.OnNetStateChangedListener onNetStateChangedListener) {
        this.onNetStateChangedListener = onNetStateChangedListener;
        if (netWorkChangReceiver != null) {
            netWorkChangReceiver.setOnNetStateChangedListener(onNetStateChangedListener);
        }
    }
    //实例化各种帮助类
    private void initHelper() {
        if (photoHelper == null) {
            photoHelper = new PhotoHelper(mContext);
        }
        if (netWorkChangReceiver == null) {
            netWorkChangReceiver = new NetWorkChangReceiver(onNetStateChangedListener);
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            try {
                //mContext.registerReceiver(netWorkChangReceiver, filter);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /*
 是否实用自定义导航
  */
    @Override
    public boolean isUseActionBarLayout() {
        return true;
    }

    //获取自定义菜单
    public OptionsMenu getOptionsMenu() {
        if (optionsMenu == null) {
            optionsMenu = new OptionsMenu(mContext);
        }
        return optionsMenu;
    }
}
