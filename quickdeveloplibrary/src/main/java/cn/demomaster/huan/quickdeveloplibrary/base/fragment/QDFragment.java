package cn.demomaster.huan.quickdeveloplibrary.base.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarTool;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDActivityManager;
import cn.demomaster.huan.quickdeveloplibrary.receiver.NetWorkChangReceiver;
import cn.demomaster.huan.quickdeveloplibrary.util.NetworkUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * Created by Squirrel桓 on 2019/1/3.
 */
public abstract class QDFragment extends Fragment implements ViewLifecycle {

    public AppCompatActivity mContext;
    private int backgroundColor = Color.WHITE;
    public int getBackgroundColor() {
        return backgroundColor;
    }

    private int headlayoutResID = R.layout.qd_activity_actionbar_common;

    public int getHeadlayoutResID() {
        return headlayoutResID;
    }

    private View fragmentView;
    public View findViewById(int id) {
        return fragmentView.findViewById(id);
    }
    private String mTitle;

    public void setTitle(String title) {
        this.mTitle = title;
        if(getActionBarTool()!=null) {
            getActionBarTool().setTitle(getTitle());
        }
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        QDLogger.println("onHiddenChanged="+this.getClass().getSimpleName()+",hidden="+hidden);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = onGenerateView(inflater, container, savedInstanceState);
        if (getBackgroundColor() != -1) {
            view.setBackgroundColor(getBackgroundColor());
        }
        if (isUseActionBarLayout()) {
            getActionBarTool().setContentView(view);
            getActionBarTool().setActionView(getHeadlayoutResID());
            //生成最終的view
            fragmentView = getActionBarTool().inflateView();
            fragmentView.setBackgroundColor(Color.WHITE);

            getActionBarTool().setTitle(getTitle());
            ImageTextView imageTextView = getActionBarTool().getLeftView();
            if(imageTextView!=null) {
                imageTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickBack();
                    }
                });
            }
        }else {
            fragmentView = view;
        }

        initCreatView(fragmentView);
        return fragmentView;
    }

    public void initCreatView(View mView) {
        initView(mView);
    }

    public void finish() {
        onClickBack();
        if(fromFragment!=null) {
            fromFragment.onActivityResult(mRequestCode, mResultCode, mResultData);
        }
    }

    public void onClickBack() {
      /* 模拟点击 int eventCode = KEYCODE_BACK;
        long now = SystemClock.uptimeMillis();
        KeyEvent down = new KeyEvent(now, now, ACTION_DOWN, eventCode, 0);
        //boolean ret = ((QDFragmentInterface) qdFragment).onKeyDown(eventCode, down);
        getActivity().onKeyDown(eventCode, down);*/
      getFragmentHelper().OnBackPressed();
    }

    private OptionsMenu optionsMenu;
    private OptionsMenu.Builder optionsMenubuilder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = (AppCompatActivity) this.getContext();
        super.onCreate(savedInstanceState);
        StatusBarUtil.transparencyBar(new WeakReference<>(mContext));
        initHelper();
    }

    public boolean isRootFragment;//是否是根fragment

    public void setRootFragment(boolean rootFragment) {
        isRootFragment = rootFragment;
    }

    public boolean dispatchTouchEvent(MotionEvent me) {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isRootFragment) {
            getActivity().onBackPressed();
        } else {
            getActivity().finish();
        }
        return true;//当返回true时，表示已经完整地处理了这个事件，并不希望其他的回调方法再次进行处理，而当返回false时，表示并没有完全处理完该事件，更希望其他回调方法继续对其进行处理
    }

    private ActionBarTool actionBarTool;

    //获取自定义导航
    public ActionBarTool getActionBarTool() {
        if (actionBarTool == null) {
            actionBarTool = new ActionBarTool(this);
        }
        return actionBarTool;
    }

    public PhotoHelper photoHelper;
    //实例化各种帮助类
    private void initHelper() {
        if (getContext() instanceof QDActivity) {
            photoHelper = ((QDActivity) getContext()).getPhotoHelper();
        }
        /*if (photoHelper == null) {
            photoHelper = new PhotoHelper(mContext);
        }*/
    }

    /**
     * 是否使用自定义导航
     *
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        //NetworkUtil.unRegisterNetworkStatusChangedListener(getContext());
        //请手动注销，已添加的广播监听
    }

    int mResultCode;
    Intent mResultData;
    public void setResult(int resultCode, Intent data) {
        mResultCode = resultCode;
        mResultData = data;
    }

    int mRequestCode;
    ViewLifecycle fromFragment;
    @Override
    public void setRequestCode(ViewLifecycle qdFragmentInterface, int requestCode) {
        mRequestCode = requestCode;
        fromFragment = qdFragmentInterface;
    }

    Intent mIntent;

    @Override
    public void setIntent(Intent intent) {
        mIntent = intent;
    }

    public Intent getIntent() {
        return mIntent;
    }

    FragmentHelper mFragmentHelper;

    @Override
    public void setFragmentHelper(FragmentHelper fragmentHelper) {
        mFragmentHelper = fragmentHelper;
    }

    public FragmentHelper getFragmentHelper() {
        if (mFragmentHelper == null) {
            mFragmentHelper = new FragmentHelper(mContext);
        }
        return mFragmentHelper;
    }

    boolean isResumed;
    @Override
    public void doResume() {
        QDLogger.i("doResume isResumed="+isResumed+",hide="+isHidden());
        if(!isResumed){
            onResume();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        isPaused = false;
        isResumed = true;
        
         if(!isHidden()){
             onFragmentResume();
         }
    }

    boolean isPaused;
    @Override
    public void doPause() {
        if(!isPaused){
            onPause();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        isPaused = true;
        isResumed = false;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        QDLogger.d(this.getClass().getSimpleName(),"onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        QDLogger.d(this.getClass().getSimpleName(),"onDestroyView");
        QDActivityManager.destroyObject(this);
    }
}
