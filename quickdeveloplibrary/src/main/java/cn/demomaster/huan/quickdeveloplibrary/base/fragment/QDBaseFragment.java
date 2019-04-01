package cn.demomaster.huan.quickdeveloplibrary.base.fragment;

import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout2;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayoutInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayoutView;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.receiver.NetWorkChangReceiver;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;

/**
 * Created by Squirrel桓 on 2019/1/3.
 */
public abstract class QDBaseFragment extends Fragment implements BaseFragmentActivityInterface {

    public AppCompatActivity mContext;
    public Bundle mBundle;
    private ActionBarInterface actionBarLayout;

    private int backgroundColor = Color.WHITE;

    public int getBackgroundColor() {
        return backgroundColor;
    }

    private int headlayoutResID = R.layout.quickdevelop_activity_actionbar_common;
    public int getHeadlayoutResID() {
        return headlayoutResID;
    }

    public ActionBarInterface getActionBarLayout(View view) {
        if (actionBarLayout == null) {
            ActionBarLayoutView.Builder builder = new ActionBarLayoutView.Builder(mContext).setContentView(view).setFragment(this).setHeaderResId(getHeadlayoutResID());
            actionBarLayout = builder.creat();
        }
        return actionBarLayout;
    }

    private int layoutResID;
    private OptionsMenu optionsMenu;
    private OptionsMenu.Builder optionsMenubuilder;
    ViewGroup mView;
    View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = (AppCompatActivity) this.getContext();
        mBundle = getArguments();
        super.onCreate(savedInstanceState);
        StatusBarUtil.transparencyBar(mContext);
        initHelper();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = (AppCompatActivity) this.getContext();
        if (mView == null) {
            mView = getContentView(inflater);
        }
        if (isUseActionBarLayout()) {//是否使用自定义导航栏
            rootView = getActionBarLayout(mView).generateView();
        } else {
            rootView = mView;
        }
        rootView.setBackgroundColor(getBackgroundColor());
        rootView.setClickable(true);
        initView(rootView, getActionBarLayout());
        return rootView;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void back() {
        mContext.onBackPressed();
    }

    //获取自定义导航
    public ActionBarInterface getActionBarLayout() {
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
            } catch (Exception e) {
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

    //获取自定义菜单
    public OptionsMenu.Builder getOptionsMenuBuilder() {
        if (optionsMenubuilder == null) {
            optionsMenubuilder = new OptionsMenu.Builder(mContext);
        }
        return optionsMenubuilder;
    }
}
