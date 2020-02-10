package cn.demomaster.huan.quickdeveloplibrary.base;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.Locale;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayoutView;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDActivityManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager2;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.receiver.NetWorkChangReceiver;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;

import static cn.demomaster.huan.quickdeveloplibrary.constant.EventBusConstant.EVENT_REFRESH_LANGUAGE;
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
            ActionBarLayoutView.Builder builder = new ActionBarLayoutView.Builder(new WeakReference<AppCompatActivity>(mContext)).setContentView(view).setHeaderResId(getHeadlayoutResID());
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

    private void initQDContentView(View view) {
        this.mContext = this;
        if (isUseActionBarLayout()) {//是否使用自定义导航栏
            StatusBarUtil.transparencyBar(new WeakReference<Activity>(mContext));
            actionBarLayout = getActionBarLayout(view);
            super.setContentView(actionBarLayout.generateView());
            FragmentActivityHelper.getInstance().bindActivity(new WeakReference<FragmentActivity>(mContext));
        } else {
            super.setContentView(view);
        }
    }

    public LayoutInflater mInflater;

    private void initQDContentView(int layoutResID) {
        mInflater = LayoutInflater.from(this);
        View view = mInflater.inflate(layoutResID, null);
        initQDContentView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //initHelper();
        EventBus.getDefault().register(this);
        //changeAppLanguage(this);
    }

    public void startFragment(AppCompatActivity activity, Fragment fragment) {
        FragmentActivityHelper.getInstance().startFragment(activity, fragment);
    }

    public int getContentViewId() {
        return android.R.id.content;//R.id.qd_fragment_content_view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        QDLogger.d("getAction=" + keyCode + ",event=" + event);
        if (!FragmentActivityHelper.getInstance().onBackPressed(mContext)) {//未被fragment消费
            onBackPressed();
            return true;
            //return super.onKeyDown(keyCode, event);
        } else {
            QDLogger.d("onKeyDown=" + true);
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
                if (QDActivityManager.getInstance().getCurrentActivity() == this) {
                    onChangeAppLanguage();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
            if (netWorkChangReceiver != null) {
                unregisterReceiver(netWorkChangReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换语言并且立马应用
     */
    public void onChangeAppLanguage() {
        changeAppLanguageAndRefreshUI(mContext, new Locale("ko"));
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

    private OptionsMenu optionsMenu;
    //获取自定义菜单
    public OptionsMenu getOptionsMenu() {
        if (optionsMenu == null) {
            optionsMenu = new OptionsMenu(new OptionsMenu.Builder(mContext));
        }
        return optionsMenu;
    }

    private OptionsMenu.Builder optionsMenubuilder;
    //获取自定义菜单
    public OptionsMenu.Builder getOptionsMenuBuilder() {
        if (optionsMenubuilder == null) {
            optionsMenubuilder = new OptionsMenu.Builder(mContext);
        }
        return optionsMenubuilder;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_SCAN_QRCODE://扫描
                photoHelper.onActivityResult(requestCode, PhotoHelper.RESULT_CODE_SCAN_QRCODE, data);
                break;
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_CAMERA://自定义拍照
                photoHelper.onActivityResult(requestCode, resultCode, data);
                break;
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_TAKE_PHOTO://拍照
                photoHelper.onActivityResult(requestCode, PhotoHelper.RESULT_CODE_TAKE_PHOTO, data);
                break;
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_GALLERY://相册选取
                photoHelper.onActivityResult(requestCode, PhotoHelper.RESULT_CODE_SELECT_PHOTO_FROM_GALLERY, data);//偷梁换柱
                break;
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_PHOTO_CROP://图片截取
                photoHelper.onActivityResult(requestCode, PhotoHelper.RESULT_CODE_PHOTO_CROP, data);//偷梁换柱
                break;
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_GALLERY_AND_CROP://相册并截取
                photoHelper.onActivityResult(requestCode, PhotoHelper.RESULT_CODE_SELECT_PHOTO_FROM_GALLERY_AND_CROP, data);//偷梁换柱
                break;
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_SIMPLE_PICTURE://自定义的图片选择器
                photoHelper.onActivityResult(requestCode, PhotoHelper.RESULT_CODE_SIMPLE_PICTURE, data);//偷梁换柱
                break;
        }
        PermissionManager2.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        QDLogger.d(requestCode+permissions.toString()+grantResults.toString());
        PermissionManager2.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
