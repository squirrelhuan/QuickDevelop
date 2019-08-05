package cn.demomaster.huan.quickdeveloplibrary.base;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayoutView;
import cn.demomaster.huan.quickdeveloplibrary.helper.ActivityManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;
import cn.demomaster.huan.quickdeveloplibrary.receiver.NetWorkChangReceiver;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;

import static cn.demomaster.huan.quickdeveloplibrary.constant.EventBusConstant.EVENT_REFRESH_LANGUAGE;
import static cn.demomaster.huan.quickdeveloplibrary.util.system.QDLanguageUtil.changeAppLanguage;
import static cn.demomaster.huan.quickdeveloplibrary.util.system.QDLanguageUtil.changeAppLanguageAndRefreshUI;

public class QDBaseFragmentActivity extends AppCompatActivity {
    public AppCompatActivity mContext;
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
        if (isUseActionBarLayout()) {//是否使用自定义导航栏
            StatusBarUtil.transparencyBar(mContext);
            actionBarLayout = getActionBarLayout(view);
            super.setContentView(actionBarLayout.generateView());
            FragmentActivityHelper.getInstance().bindActivity(this);
        } else {
            super.setContentView(view);
        }
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
        mContext = this;
        //initHelper();
        EventBus.getDefault().register(this);
        changeAppLanguage(this);
    }

    public void startFragment(AppCompatActivity activity, Fragment fragment) {
        FragmentActivityHelper.getInstance().startFragment(activity, fragment);
    }

    public int getContentViewId() {
        return android.R.id.content;//R.id.qd_fragment_content_view;
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


    public static class MessageEvent { /* Additional fields if needed */
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {/* Do something */}

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String str) {
        switch (str) {
            case EVENT_REFRESH_LANGUAGE:
                if(ActivityManager.getInstance().getCurrentActivity()==this){
                    onChangeAppLanguage();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
       // unregisterReceiver(netWorkChangReceiver);
    }

    /**
     * 切换语言并且立马应用
     */
    public void onChangeAppLanguage() {
        changeAppLanguageAndRefreshUI(mContext);
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
    public void initHelper() {
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
                mContext.registerReceiver(netWorkChangReceiver, filter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
