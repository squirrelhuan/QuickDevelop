package cn.demomaster.huan.quickdeveloplibrary.base.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.Locale;

import butterknife.ButterKnife;
import cn.demomaster.huan.quickdeveloplibrary.ApplicationParent;
import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBar;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDActivityManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager2;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.MessageHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;
import cn.demomaster.huan.quickdeveloplibrary.receiver.NetWorkChangReceiver;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;

import static cn.demomaster.huan.quickdeveloplibrary.constant.EventBusConstant.EVENT_REFRESH_LANGUAGE;
import static cn.demomaster.huan.quickdeveloplibrary.util.system.QDLanguageUtil.changeAppLanguageAndRefreshUI;

@ActivityPager(name = "QDActivity",preViewClass = StateView.class,resType = ResType.Custome)
public class QDActivity extends AppCompatActivity implements QDActivityInterface {
    public static String TAG = "CGQ";
    public AppCompatActivity mContext;
    public Bundle mBundle = null;
    private ActionBar actionBarLayout;
    private int headlayoutResID = R.layout.quickdevelop_activity_actionbar_common;
    public int getHeadlayoutResID() {
        return headlayoutResID;
    }

    public ActionBar getActionBarLayout(View view) {
        if (actionBarLayout == null) {
            actionBarLayout = new ActionBar(this);
            actionBarLayout.setHeaderResId(getHeadlayoutResID());
            actionBarLayout.setContentView(view);
        }
        return actionBarLayout;
    }

    /**
     *
     * @return ActionBarInterface
     */
    public ActionBar getActionBarLayout() {
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
        if (isUseActionBarLayout()) {//是否使用自定义导航栏
            StatusBarUtil.transparencyBar(new WeakReference<Activity>(mContext));
            actionBarLayout = getActionBarLayout(view);
            super.setContentView(actionBarLayout);
        } else {
            super.setContentView(view);
        }
        //bind在setContentView之后
        ButterKnife.bind(this);
    }

    //public LayoutInflater mInflater;
    private void initQDContentView(int layoutResID) {
       // mInflater = LayoutInflater.from(this);
        View view = getLayoutInflater().inflate(layoutResID, null);
        initQDContentView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        mBundle = getIntent().getExtras();
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getApplicationContext() != null && getApplicationContext() instanceof ApplicationParent) {
            StatusBarUtil.transparencyBar(new WeakReference<Activity>(mContext));
            initHelper();
        }
        //initHelper();
        //changeAppLanguage(this);
    }

   private FragmentHelper fragmentHelper;
    public FragmentHelper getFragmentHelper() {
        if(fragmentHelper==null){
            fragmentHelper = new FragmentHelper(mContext);
        }
        return fragmentHelper;
    }

    public void startFragment(AppCompatActivity activity, QDFragment fragment) {
        getFragmentHelper().startFragment(activity, fragment);
    }

    public void startActivity(Class<?> clazz) {
        startActivity(clazz, null);
    }

    public void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) intent.putExtras(bundle);
        startActivity(intent);
        mContext.overridePendingTransition(R.anim.translate_from_right_to_left_enter,  R.anim.anim_null);
    }

    //设置导航栏透明
    public void transparentBar() {
        StatusBarUtil.transparencyBar(new WeakReference<Activity>(this));
    }
    /**
     * 动态设置状态栏的显示隐藏
     *
     * @param enable
     */
    public void setFullScreen(boolean enable) {
        if (enable) {
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
        } else {
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
        }
    }
    public int getContentViewId() {
        return android.R.id.content;//R.id.qd_fragment_content_view;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        QDLogger.d("getAction=" + keyCode + ",event=" + event);
        if (fragmentHelper !=null) {
            if(!fragmentHelper.onKeyDown(mContext,keyCode,event)) {
                return super.onKeyDown(keyCode, event);
            }else {
                return true;
            }
        } else {
            QDLogger.d("onKeyDown=" + true);
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());   //收起键盘
            }
        }
        return super.dispatchTouchEvent(me);
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /*
 是否使用自定义导航
  */
    public boolean isUseActionBarLayout() {
        return true;
    }

    @Override
    public void onClickBack() {
        finish();
    }

    public MessageHelper getMesageHelper() {
        return MessageHelper.getInstance(mContext.getApplicationContext());
    }

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
            if(mBundle!=null){
                mBundle=null;
            }
            if(fragmentHelper!=null){
                fragmentHelper.onDestroy();
                fragmentHelper = null;
            }
            if(netWorkChangReceiver!=null) {
                netWorkChangReceiver.setOnNetStateChangedListener(null);
                unregisterReceiver(netWorkChangReceiver);
            }
            EventBus.getDefault().unregister(this);
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
        if(netWorkChangReceiver==null) {
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
        }else {
            netWorkChangReceiver.setOnNetStateChangedListener(onNetStateChangedListener);
            //mContext.unregisterReceiver(netWorkChangReceiver);
        }
    }



    /* if (netWorkChangReceiver == null) {
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
    }*/

    //实例化各种帮助类
    public void initHelper() {
        if (photoHelper == null) {
            photoHelper = new PhotoHelper(mContext);
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
    public void showMessage(String message){
        PopToastUtil.ShowToast(this, message);
        //getMesageHelper().showMessage(message);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(photoHelper!=null){
            photoHelper.onActivityResult(requestCode,resultCode,data);
        }
        PermissionManager2.onActivityResult(this,requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        QDLogger.d(requestCode+permissions.toString()+grantResults.toString());
        PermissionManager2.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public PhotoHelper getPhotoHelper() {
        if (photoHelper == null) {
            photoHelper = new PhotoHelper(mContext);
        }
        return photoHelper;
    }
}
