package cn.demomaster.huan.quickdevelop.fragment.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.fragment.component.BlankFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ACTIONBAR_TYPE;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.operatguid.GuiderView;
import cn.demomaster.huan.quickdeveloplibrary.util.ScreenShotUitl;
import cn.demomaster.huan.quickdeveloplibrary.view.adapter.ScrollingTabsAdapter;
import cn.demomaster.huan.quickdeveloplibrary.widget.ScrollableTabView;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.CustomDialog;


/**
 * Squirrel桓
 * 2018/8/25
 */
public class MainFragment extends QDBaseFragment {
    //Components
    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        return (ViewGroup) inflater.inflate(R.layout.fragment_layout_main, null);
    }

   /* @Override
    public void initView(View rootView, ActionBarLayout2 actionBarLayout) {
        *//*actionBarLayout.setActionBarModel(ActionBarLayout2.ACTIONBAR_TYPE.NORMAL);
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
        init();*//*
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       // return super.onKeyDown(keyCode, event);
        return true;//屏蔽返回键
    }

    @Override
    public void initView(View rootView, ActionBarInterface actionBarLayout) {
        actionBarLayout.setFullScreen(true);
        actionBarLayout.setActionBarType(ACTIONBAR_TYPE.NORMAL);
        actionBarLayout.setHeaderBackgroundColor(getResources().getColor(R.color.colorPrimary));

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
        /*ImageView iv_store_pic = rootView.findViewById(R.id.iv_store_pic);
        Glide.with(mContext).load("https://baekteoriappimg.s3.ap-northeast-2.amazonaws.com/charge/158633285847768584298").into(iv_store_pic);
        iv_store_pic.setVisibility(View.VISIBLE);*/
    }

    private void init() {
        getActionBarLayout().setTitle(getString(R.string.title_home));
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
                optionsMenu.show(v);
            }
        });
        initOptionsMenu();
    }

    OptionsMenu optionsMenu;
    private void initOptionsMenu() {
        List<OptionsMenu.Menu> menus = new ArrayList<>();
        String[] menuNames = {"我的二维码", "扫描", "截图分享"};
        for (int i = 0; i < menuNames.length; i++) {
            OptionsMenu.Menu menu = new OptionsMenu.Menu();
            menu.setTitle(menuNames[i]);
            menu.setPosition(i);
            menu.setIconId(R.mipmap.quickdevelop_ic_launcher);
            menu.setIconPadding(30);
            menu.setIconWidth(80);
            menus.add(menu);
        }
        getOptionsMenuBuilder().setMenus(menus)
                .setAlpha(.6f)
                .setUsePadding(true)
                .setBackgroundColor(Color.RED)
                .setBackgroundRadius(20)
                .setTextColor(Color.WHITE)
                .setTextSize(16)
                .setPadding(0)
                .setWithArrow(true)
                .setArrowHeight(30)
                .setArrowWidth(30)
                .setGravity(GuiderView.Gravity.BOTTOM)
                .setDividerColor(getResources().getColor(R.color.transparent))
                .setAnchor(getActionBarLayout().getRightView());
        getOptionsMenuBuilder().setOnMenuItemClicked(new OptionsMenu.OnMenuItemClicked() {
            @Override
            public void onItemClick(int position, View view) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        photoHelper.scanQrcode(new PhotoHelper.OnTakePhotoResult() {
                            @Override
                            public void onSuccess(Intent data, String path) {
                                QdToast.show(mContext,path+"");
                            }

                            @Override
                            public void onFailure(String error) {
                                QdToast.show(mContext,"error:"+error);
                            }
                        });
                       /* photoHelper.selectPhotoFromGalleryAndCrop(new PhotoHelper.OnTakePhotoResult() {
                            @Override
                            public void onSuccess(Intent data, String path) {
                                *//*setImageToView(data);*//*
                            }

                            @Override
                            public void onFailure(String error) {

                            }
                        });*/
                        break;
                    case 2:
                        ScreenShotUitl.shot((Activity) mContext);
                        break;
                }
            }
        });
        optionsMenu = getOptionsMenuBuilder().creat();
        /*getOptionsMenu().setMenus(menus);
        getOptionsMenu().setAlpha(.6f);
        getOptionsMenu().setMargin(80);
        getOptionsMenu().setUsePadding(true);
        getOptionsMenu().setBackgroundRadiu(20);
        getOptionsMenu().setBackgroundColor(Color.RED);
        getOptionsMenu().setAnchor(getActionBarLayoutOld().getRightView());*/
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