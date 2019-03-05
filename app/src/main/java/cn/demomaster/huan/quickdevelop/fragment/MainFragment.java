package cn.demomaster.huan.quickdevelop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.QDMainActivity;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.view.adapter.ScrollingTabsAdapter;
import cn.demomaster.huan.quickdeveloplibrary.widget.ScrollableTabView;


/**
 * Squirrel桓
 * 2018/8/25
 */
public class MainFragment extends BaseFragment {
    //Components
    ViewGroup mView;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_main, null);
        }
        Bundle bundle = getArguments();
        String title = "主页面";
        init();
        return mView;
    }

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
         mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_main, null);
        return mView;
    }

    @Override
    public void initActionBarLayout(ActionBarLayout actionBarLayout) {

    }

    /**
     * Initiate the tabs
     */
    public void initScrollableTabs(ViewPager mViewPager) {
        ScrollableTabView mScrollingTabs = (ScrollableTabView) mView.findViewById(R.id.scrollingTabs);
        ScrollingTabsAdapter mScrollingTabsAdapter = new ScrollingTabsAdapter(getActivity());
        mScrollingTabs.setAdapter(mScrollingTabsAdapter);
        mScrollingTabs.setViewPager(mViewPager);

    }

    private void init() {
        //getActionBarLayout().setActionBarModel(ActionBarLayout.ACTIONBAR_TYPE.NORMAL);
        //getActionBarLayout().getLeftView().setVisibility(View.GONE);

     /*   List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new ComponentFragment());
        fragments.add(new BlankFragment());
        FragmentAdapter mPagerAdapter = new FragmentAdapter(getSupportFragmentManager(),fragments);
*/
        List<Class> list = new ArrayList<>();
        list.add(ComponentFragment.class);
        list.add(BlankFragment.class);
        MainFragmentAdapter mPagerAdapter1 = new MainFragmentAdapter(getActivity().getSupportFragmentManager(), list);
        // Initiate ViewPager
        ViewPager mViewPager = (ViewPager) mView.findViewById(R.id.viewPager);
        //mViewPager.setPageMargin(getResources().getInteger(R.integer.viewpager_margin_width));
        //mViewPager.setPageMarginDrawable(R.drawable.viewpager_margin);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mPagerAdapter1);
        mViewPager.setCurrentItem(0);

        // Tabs
        initScrollableTabs(mViewPager);
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
            } catch (java.lang.InstantiationException e) {
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