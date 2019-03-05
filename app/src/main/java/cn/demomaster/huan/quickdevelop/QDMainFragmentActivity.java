package cn.demomaster.huan.quickdevelop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.fragment.BlankFragment;
import cn.demomaster.huan.quickdevelop.fragment.ComponentFragment;
import cn.demomaster.huan.quickdevelop.fragment.MainFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityRoot;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseFragmentActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.view.adapter.ScrollingTabsAdapter;
import cn.demomaster.huan.quickdeveloplibrary.widget.ScrollableTabView;


public class QDMainFragmentActivity extends BaseActivityParent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_qdmain);
        setContentView(R.layout.activity_empty);
        if (savedInstanceState == null) {
            BaseFragment fragment = new ComponentFragment();
            FragmentActivityHelper.getInstance().startFragment( mContext,fragment);
        }

        //getActionBarLayout().setActionBarModel(ActionBarLayout.ACTIONBAR_TYPE.NORMAL);
        //getActionBarLayout().getLeftView().setVisibility(View.GONE);

    }


}
