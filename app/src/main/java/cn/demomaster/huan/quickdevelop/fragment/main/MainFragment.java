package cn.demomaster.huan.quickdevelop.fragment.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.fragment.component.BlankFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.ScreenShotUitl;
import cn.demomaster.huan.quickdeveloplibrary.view.adapter.ScrollingTabsAdapter;
import cn.demomaster.huan.quickdeveloplibrary.widget.ScrollableTabView;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.CustomDialog;


/**
 * Squirrel桓
 * 2018/8/25
 */
public class MainFragment extends BaseFragment {
    //Components
    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        return (ViewGroup) inflater.inflate(R.layout.fragment_layout_main, null);
    }

    @Override
    public void initView(View rootView, ActionBarLayout actionBarLayout) {
        //actionBarLayout.setActionBarModel(ActionBarLayout.ACTIONBAR_TYPE.NORMAL);
        actionBarLayout.getLeftView().setVisibility(View.GONE);

        List<Class> list = new ArrayList<>();
        list.add(ComponentFragment.class);
        list.add(HelperFragment.class);
        list.add(DesignPatternFragment.class);
        MainFragmentAdapter mPagerAdapter1 = new MainFragmentAdapter(getActivity().getSupportFragmentManager(), list);
        // Initiate ViewPager
        ViewPager mViewPager = rootView.findViewById(R.id.viewPager);
        //mViewPager.setPageMargin(getResources().getInteger(R.integer.viewpager_margin_width));
        //mViewPager.setPageMarginDrawable(R.drawable.viewpager_margin);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mPagerAdapter1);
        mViewPager.setCurrentItem(0);

        // Tabs
        initScrollableTabs(rootView, mViewPager);
        init();
    }

    private void init() {
        getActionBarLayout().setActionBarModel(ActionBarLayout.ACTIONBAR_TYPE.NORMAL);
        getActionBarLayout().setTitle("首页");
        getActionBarLayout().setStateBarColorAuto(true);//状态栏文字颜色自动
        getActionBarLayout().setActionBarThemeColors(Color.WHITE, Color.BLACK);
        getActionBarLayout().setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*LoadingDialog.Builder builder = new LoadingDialog.Builder(MainActivity.this);
                final LoadingDialog loadingDialog = builder.setMessage("登陆中").setCanTouch(false).create();
                loadingDialog.show();*/

                CustomDialog.Builder builder = new CustomDialog.Builder(mContext, R.layout.layout_dialog_loading_default);
                final CustomDialog customDialog = builder.setCanTouch(false).create();
                customDialog.show();
            }
        });
        getActionBarLayout().setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOptionsMenu().show();
            }
        });
        initOptionsMenu();
    }

    private void initOptionsMenu() {
        List<OptionsMenu.Menu> menus = new ArrayList<>();
        String[] menuNames = {"我的二维码", "扫描", "截图分享"};
        for (int i = 0; i < menuNames.length; i++) {
            OptionsMenu.Menu menu = new OptionsMenu.Menu();
            menu.setTitle(menuNames[i]);
            menu.setPosition(i);
            menus.add(menu);
        }
        getOptionsMenu().setMenus(menus);
        getOptionsMenu().setAlpha(.86f);
        getOptionsMenu().setMargin(2);
        getOptionsMenu().setAnchor(getActionBarLayout().getRightView());
        getOptionsMenu().setOnMenuItemClicked(new OptionsMenu.OnMenuItemClicked() {
            @Override
            public void onItemClick(int position, View view) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        photoHelper.selectPhotoFromGalleryAndCrop(new PhotoHelper.OnTakePhotoResult() {
                            @Override
                            public void onSuccess(Intent data, String path) {
                                /*setImageToView(data);*/
                            }

                            @Override
                            public void onFailure(String error) {

                            }
                        });
                        break;
                    case 2:
                        ScreenShotUitl.shot((Activity) mContext);
                        break;
                }
            }
        });
    }

    /**
     * Initiate the tabs
     */
    public void initScrollableTabs(View rootView, ViewPager mViewPager) {
        ScrollableTabView mScrollingTabs = (ScrollableTabView) rootView.findViewById(R.id.scrollingTabs);
        ScrollingTabsAdapter mScrollingTabsAdapter = new ScrollingTabsAdapter(getActivity());
        mScrollingTabs.setScrollingTabsAdapter(mScrollingTabsAdapter);
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