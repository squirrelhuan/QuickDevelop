package cn.demomaster.huan.quickdevelop;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import cn.demomaster.huan.quickdevelop.fragment.main.MainFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ACTIONBAR_TYPE;

/**
 *
 */
public class QDMainFragmentActivity extends QDActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qdmain);
        if (savedInstanceState == null) {
            QDBaseFragment fragment = new MainFragment();
            getFragmentActivityHelper().startFragment(mContext, fragment);
        }
        getActionBarLayout().setActionBarType(ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);
        getActionBarLayout().setHeaderBackgroundColor(Color.GRAY);
        //actionBarLayoutView.setHeaderBackgroundColor();
        getActionBarLayout().setActionBarType(ACTIONBAR_TYPE.NORMAL);
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

    }


}
