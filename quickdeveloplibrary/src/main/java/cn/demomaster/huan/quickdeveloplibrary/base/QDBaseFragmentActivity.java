package cn.demomaster.huan.quickdeveloplibrary.base;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayoutView;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;
import cn.demomaster.huan.quickdeveloplibrary.receiver.NetWorkChangReceiver;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;

public class QDBaseFragmentActivity extends AppCompatActivity {
    public AppCompatActivity mContext;
    public Bundle mBundle = null;
    private ActionBarInterface actionBarLayout;
    private int headlayoutResID = R.layout.quickdevelop_activity_actionbar_common;
    public int getHeadlayoutResID() {
        return headlayoutResID;
    }
    public ActionBarInterface getActionBarLayout(View view) {
        if (actionBarLayout == null) {
            ActionBarLayoutView.Builder builder = new ActionBarLayoutView.Builder(mContext).setContentView(view).setHeaderResId(getHeadlayoutResID());
            actionBarLayout = builder.creat();
        }
        return actionBarLayout;
    }

    public ActionBarInterface getActionBarLayout() {
        return actionBarLayout;
    }

    @Override
    public void setContentView(int layoutResID) {
        initQDContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        initQDContentView(view);
    }

    private void initQDContentView(View view){
        this.mContext = this;
        this.mBundle = getIntent().getExtras();
        if (isUseActionBarLayout()) {//是否使用自定义导航栏
            StatusBarUtil.transparencyBar(mContext);
            actionBarLayout = getActionBarLayout(view);
            super.setContentView(actionBarLayout.generateView());
            FragmentActivityHelper.getInstance().bindActivity(this);
        } else {
            super.setContentView(view);
        }
        initHelper();
    }

    public LayoutInflater mInflater;
    private void initQDContentView(int layoutResID){
        mInflater = LayoutInflater.from(this);
       View view = mInflater.inflate(layoutResID,null);
       initQDContentView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void startFragment(AppCompatActivity activity, Fragment fragment) {
        FragmentActivityHelper.getInstance().startFragment(activity, fragment);
    }

    public int getContentViewId() {
        return android.R.id.content;//R.id.qd_fragment_content_view;
    }

    private OptionsMenu optionsMenu;
    //获取自定义菜单
    public OptionsMenu getOptionsMenu() {
        if (optionsMenu == null) {
            optionsMenu = new OptionsMenu(this);
        }
        return optionsMenu;
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
            registerReceiver(netWorkChangReceiver, filter);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        QDLogger.d("getAction="+keyCode+",event="+event);
       if(!FragmentActivityHelper.getInstance().onBackPressed(mContext)){//未被fragment消费
           onBackPressed();
           return true;
           //return super.onKeyDown(keyCode, event);
       }else {
           QDLogger.d("onKeyDown="+true);
           return true;
       }
    }

    /*
 是否实用自定义导航
  */
    public boolean isUseActionBarLayout() {
        return true;
    }


}
