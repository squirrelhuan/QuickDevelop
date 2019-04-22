package cn.demomaster.huan.quickdevelop;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;


import java.util.Locale;

import cn.demomaster.huan.quickdevelop.fragment.main.MainFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.QDBaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout2;

import static cn.demomaster.huan.quickdeveloplibrary.constant.EventBusConstant.EVENT_REFRESH_LANGUAGE;
import static cn.demomaster.huan.quickdeveloplibrary.util.system.QDLanguageUtil.changeAppLanguage;
import static cn.demomaster.huan.quickdeveloplibrary.util.system.QDLanguageUtil.changeAppLanguageAndRefreshUI;
import static cn.demomaster.huan.quickdeveloplibrary.util.system.QDLanguageUtil.getLanguageLocal;

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
    }


}
