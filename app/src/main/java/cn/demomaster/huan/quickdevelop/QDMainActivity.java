package cn.demomaster.huan.quickdevelop;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import cn.demomaster.huan.quickdevelop.ui.fragment.component.BlankFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ACTIONBAR_TYPE;
import cn.demomaster.huan.quickdeveloplibrary.view.adapter.ScrollingTabsAdapter;
import cn.demomaster.huan.quickdeveloplibrary.widget.ScrollableTabView;

public class QDMainActivity extends QDActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qdmain);

        getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NORMAL);
        getActionBarTool().getLeftView().setVisibility(View.GONE);
        getActionBarTool().setHeaderBackgroundColor(Color.RED);

     /*   List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new ComponentFragment());
        fragments.add(new BlankFragment());
        FragmentAdapter mPagerAdapter = new FragmentAdapter(getSupportFragmentManager(),fragments);
*/
        /*List<Class> list = new ArrayList<>();
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
        initScrollableTabs(mViewPager);*/
    }

    /**
     * Initiate the tabs
     */
    public void initScrollableTabs(ViewPager mViewPager) {
        ScrollableTabView mScrollingTabs = (ScrollableTabView) findViewById(R.id.scrollingTabs);
        ScrollingTabsAdapter mScrollingTabsAdapter = new ScrollingTabsAdapter(this);
        mScrollingTabs.setScrollingTabsAdapter(mScrollingTabsAdapter);
        mScrollingTabs.setViewPager(mViewPager);
    }

    private static class MainFragmentAdapter extends FragmentPagerAdapter {
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
