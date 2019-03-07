package cn.demomaster.huan.quickdevelop;

import android.graphics.Color;
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
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.view.adapter.ScrollingTabsAdapter;
import cn.demomaster.huan.quickdeveloplibrary.widget.ScrollableTabView;


public class QDMainActivity extends BaseActivityParent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qdmain);

        getActionBarLayout().setActionBarModel(ActionBarLayout.ACTIONBAR_TYPE.NORMAL);
        getActionBarLayout().getLeftView().setVisibility(View.GONE);
        getActionBarLayout().setHeaderBackgroundColor(Color.RED);

     /*   List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new ComponentFragment());
        fragments.add(new BlankFragment());
        FragmentAdapter mPagerAdapter = new FragmentAdapter(getSupportFragmentManager(),fragments);
*/
        List<Class> list = new ArrayList<>();
        list.add(ComponentFragment.class);
        list.add(BlankFragment.class);
        MainFragmentAdapter mPagerAdapter1 = new MainFragmentAdapter(getSupportFragmentManager(), list);
        // Initiate ViewPager
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager);
        //mViewPager.setPageMargin(getResources().getInteger(R.integer.viewpager_margin_width));
        //mViewPager.setPageMarginDrawable(R.drawable.viewpager_margin);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mPagerAdapter1);
        mViewPager.setCurrentItem(0);

        // Tabs
        initScrollableTabs(mViewPager);
    }

    /**
     * Initiate the tabs
     */
    public void initScrollableTabs(ViewPager mViewPager) {
        ScrollableTabView mScrollingTabs = (ScrollableTabView) findViewById(R.id.scrollingTabs);
        ScrollingTabsAdapter mScrollingTabsAdapter = new ScrollingTabsAdapter(this);
        mScrollingTabs.setAdapter(mScrollingTabsAdapter);
        mScrollingTabs.setViewPager(mViewPager);

    }


    private class MainFragmentAdapter extends FragmentPagerAdapter {
        private List<Class> data;
        public MainFragmentAdapter(FragmentManager fm) {
            super(fm);
        }
        public MainFragmentAdapter(FragmentManager fm, List<Class> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            Bundle bundle = new Bundle();
            String className = "";
            Class catClass = data.get(position);
            try {
                // catClass = Class.forName(className);
                // 实例化这个类
                fragment = (Fragment) catClass.newInstance();
                fragment.setArguments(bundle);
                return fragment;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

            fragment = new BlankFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }

}
