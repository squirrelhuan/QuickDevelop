package cn.demomaster.huan.quickdevelop;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.view.adapter.ScrollingTabsAdapter;
import cn.demomaster.huan.quickdeveloplibrary.widget.ScrollableTabView;
import cn.demomaster.qdrouter_library.actionbar.ACTIONBAR_TYPE;

public class QDMainActivity extends QDActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qdmain);

        getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NORMAL);
        getActionBarTool().getLeftView().setVisibility(View.GONE);
        getActionBarTool().getActionBarLayout().getActionBarView().setBackgroundColor(Color.RED);

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
    public void initScrollableTabs(ViewPager2 mViewPager) {
        ScrollableTabView mScrollingTabs =  findViewById(R.id.scrollingTabs);
        ScrollingTabsAdapter mScrollingTabsAdapter = new ScrollingTabsAdapter(this);
        mScrollingTabs.setScrollingTabsAdapter(mScrollingTabsAdapter);
        mScrollingTabs.setViewPager(mViewPager);
    }

}
