package cn.demomaster.huan.quickdevelop;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import cn.demomaster.huan.quickdevelop.fragment.main.MainFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.QDBaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;

/**
 *
 */
public class QDMainFragmentActivity extends QDBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qdmain);
        if (savedInstanceState == null) {
            QDBaseFragment fragment = new MainFragment();
            FragmentActivityHelper.getInstance().startFragment(mContext, fragment);
        }
        getActionBarLayout().setActionBarType(ActionBarInterface.ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);
        getActionBarLayout().setHeaderBackgroundColor(Color.GRAY);
        //actionBarLayoutView.setHeaderBackgroundColor();
        getActionBarLayout().setActionBarType(ActionBarInterface.ACTIONBAR_TYPE.NORMAL);
        getActionBarLayout().getLeftView().setVisibility(View.GONE);
        //EventBus.getDefault().register(this);
        //changeAppLanguage(mContext);
        initHelper();

        //设置系统时间需要系统权限
       /* long time = 1243567568;
        //同步服务器时间
        SystemClock.setCurrentTimeMillis(time);

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        System.out.println("当前时间：" + simpleDateFormat.format(date));*/


        Handler handler = new Handler();
        handler.removeCallbacks(reConnectRunnable);
        handler.postDelayed(reConnectRunnable, 5000);
        handler.postDelayed(reConnectRunnable, 5000);
        handler.postDelayed(reConnectRunnable, 5000);
        handler.postDelayed(reConnectRunnable, 5000);
        handler.postDelayed(reConnectRunnable, 5000);
    }
    private Runnable reConnectRunnable = new Runnable() {
        @Override
        public void run() {
            System.out.println("123");
        }
    };

  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //return super.onKeyDown(keyCode, event);
        *//*if(keyCode==KeyEvent.KEYCODE_BACK){
            return false;
        }*//*
        return true;
    }*/
}
