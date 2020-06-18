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

import java.lang.ref.WeakReference;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBar;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDActivityManager;
import cn.demomaster.huan.quickdeveloplibrary.receiver.NetWorkChangReceiver;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;

/**
 * Created by Squirrel桓 on 2019/1/3.
 */
public abstract class QDFragment extends Fragment implements QDFragmentInterface {

    public AppCompatActivity mContext;
    public Bundle mBundle;
    private ActionBar actionBarLayout;
    private int backgroundColor = Color.WHITE;
    public int getBackgroundColor() {
        return backgroundColor;
    }

    private int headlayoutResID = R.layout.quickdevelop_activity_actionbar_common;
    public int getHeadlayoutResID() {
        return headlayoutResID;
    }

    /**
     * 获取自定义导航佈局
     * @param view
     * @return
     */
    public ActionBar getActionBarLayout(View view) {
        if (!isUseActionBarLayout()) {
            return null;
        }
        if (actionBarLayout == null) {
            actionBarLayout = new ActionBar(this);
            actionBarLayout.setHeaderResId(getHeadlayoutResID());
            actionBarLayout.setContentView(view);
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
        StatusBarUtil.transparencyBar(new WeakReference<>(mContext));
        initHelper();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = getContentView(inflater);
        }
        if (isUseActionBarLayout()) {//是否使用自定义导航栏
            rootView = getActionBarLayout(mView);
        } else {
            rootView = mView;
        }
        rootView.setBackgroundColor(getBackgroundColor());
        rootView.setClickable(true);
        initView(rootView, getActionBarLayout());
        return rootView;
    }

    boolean isRootFragment ;//是否是根fragment
    public boolean isRootFragment() {
        return isRootFragment;
    }

    public void setRootFragment(boolean rootFragment) {
        isRootFragment = rootFragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(!isRootFragment) {
            getActivity().onBackPressed();
        }else {
            getActivity().finish();
        }
        return true;//当返回true时，表示已经完整地处理了这个事件，并不希望其他的回调方法再次进行处理，而当返回false时，表示并没有完全处理完该事件，更希望其他回调方法继续对其进行处理
    }
   /* @Override
    public void back() {
        mContext.onBackPressed();
    }*/

    //获取自定义导航
    public ActionBar getActionBarLayout() {
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
        if(getContext() instanceof QDActivity){
            photoHelper =((QDActivity)getContext()).getPhotoHelper();
        }
        /*if (photoHelper == null) {
            photoHelper = new PhotoHelper(mContext);
        }*/
        if (netWorkChangReceiver == null) {
            netWorkChangReceiver = new NetWorkChangReceiver(onNetStateChangedListener);
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            try {
                //mContext.registerReceiver(netWorkChangReceiver, filter);
            } catch (Exception e) {
                QDLogger.e(e);
            }
        }
    }

    /**
     * 是否使用自定义导航
     * @return
     */
    @Override
    public boolean isUseActionBarLayout() {
        return true;
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

   /* @Override
    public void initView(View rootView, ActionBarInterface actionBarLayout) {

    }*/

    @Override
    public void onDestroy() {
        if(actionBarLayout!=null) {
            actionBarLayout.onDestroy();
        }
        QDActivityManager.onFinishActivityOrFragment(this);
        super.onDestroy();
    }
}
