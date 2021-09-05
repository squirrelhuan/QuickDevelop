package cn.demomaster.huan.quickdevelop.ui.fragment.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.BlankFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.designer.WebViewFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.operatguid.GuiderView;
import cn.demomaster.huan.quickdeveloplibrary.util.ClipboardUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.adapter.ScrollingTabsAdapter;
import cn.demomaster.huan.quickdeveloplibrary.widget.ScrollableTabView;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDActionDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDDialog;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * Squirrel桓
 * 2018/8/25
 */
public class MainFragment extends BaseFragment {

    @Nullable
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_main, null);
        return view;
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
    public void initView(View rootView) {
        /*FloatViewImp floatView = new FloatViewImp() {
            @Override
            public View onCreateView(Bundle savedInstanceState) {
                ImageView imageView = new ImageView(mContext);
                imageView.setImageResource(R.mipmap.dk_doraemon);
                return imageView;
            }
        };
        FloatHelper.getInstance().showFloatView(mContext,floatView);*/

       /* actionBarLayout.setFullScreen(true);
        actionBarLayout.setActionBarType(ACTIONBAR_TYPE.NORMAL);
        actionBarLayout.setHeaderBackgroundColor(getResources().getColor(R.color.colorPrimary));
        actionBarLayout.getLeftView().setVisibility(View.GONE);*/
        List<Class> list = new ArrayList<>();
        list.add(ComponentFragment.class);
        list.add(HelperFragment.class);
        list.add(DesignPatternFragment.class);
        FragmentManager fragmentManager = getChildFragmentManager();
        MainFragmentAdapter mPagerAdapter1 = new MainFragmentAdapter(getActivity(), list);
        // Initiate ViewPager
        ViewPager2 mViewPager = rootView.findViewById(R.id.viewPager);
        //mViewPager.setPageMargin(getResources().getInteger(R.integer.viewpager_margin_width));
        //mViewPager.setPageMarginDrawable(R.drawable.viewpager_margin);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mPagerAdapter1);
        mViewPager.setCurrentItem(0);

        // Tabs
        initScrollableTabs(rootView, mViewPager);
        /*ImageView iv_store_pic = rootView.findViewById(R.id.iv_store_pic);
        Glide.with(mContext).load("https://baekteoriappimg.s3.ap-northeast-2.amazonaws.com/charge/158633285847768584298").into(iv_store_pic);
        iv_store_pic.setVisibility(View.VISIBLE);*/
        getActionBarTool().setTitle(getString(R.string.title_home));
        //getActionBarTool().setStateBarColorAuto(true);//状态栏文字颜色自动
        //getActionBarTool().setActionBarThemeColors(Color.WHITE, Color.BLACK);
        getActionBarTool().setLeftOnClickListener(view -> {
            /*LoadingDialog.Builder builder = new LoadingDialog.Builder(MainActivity.this);
            final LoadingDialog loadingDialog = builder.setMessage("登陆中").setCanTouch(false).create();
            loadingDialog.show();*/
            QDActionDialog.Builder builder = new QDActionDialog.Builder(mContext).setContentViewLayout(R.layout.layout_dialog_loading_default);
            final QDActionDialog customDialog = builder.setCancelable(false).create();
            customDialog.show();
        });
        getActionBarTool().getLeftView().setVisibility(View.GONE);
        getActionBarTool().setRightOnClickListener(v -> optionsMenu.show(v));
        initOptionsMenu();
    }

    OptionsMenu optionsMenu;
    private OptionsMenu.Builder optionsMenubuilder;
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
        optionsMenubuilder = new OptionsMenu.Builder(mContext);
        optionsMenubuilder.setMenus(menus)
                .setAlpha(.6f)
                .setUsePadding(true)
                .setBackgroundColor(getResources().getColor(R.color.blueviolet))
                .setBackgroundRadius(20)
                .setTextColor(Color.WHITE)
                .setTextSize(16)
                .setPadding(0)
                .setWithArrow(true)
                .setArrowHeight(30)
                .setArrowWidth(30)
                .setGravity(GuiderView.Gravity.BOTTOM)
                .setDividerColor(getResources().getColor(R.color.transparent))
                .setAnchor(getActionBarTool().getRightView());
        optionsMenubuilder.setOnMenuItemClicked((position, view) -> {
            switch (position) {
                case 0:
                    optionsMenu.dismiss();
                    break;
                case 1:
                    getPhotoHelper().scanQrcode(new PhotoHelper.OnTakePhotoResult() {
                        @Override
                        public void onSuccess(Intent data, String path) {
                            //QdToast.show(mContext, path + "");
                            showScanDialog(path);
                        }

                        @Override
                        public void onFailure(String error) {
                            QdToast.show(mContext, "error:" + error);
                        }
                    });
                    optionsMenu.dismiss();
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
                    //ScreenShotUitl.shot((Activity) mContext);
                    optionsMenu.dismiss();
                    break;
            }
        });
        optionsMenu = optionsMenubuilder.creat();
        optionsMenu.setMenus(menus);
        optionsMenu.setAlpha(.6f);
        optionsMenu.setMargin(80);
        optionsMenu.setUsePadding(true);
        optionsMenu.setBackgroundRadiu(20);
        optionsMenu.setBackgroundColor(Color.RED);
        optionsMenu.setAnchor(getActionBarTool().getRightView());
    }

    private void showScanDialog(String url) {
        QDDialog qdDialog = new QDDialog.Builder(mContext)
                .setTitle("要跳转到指定页面吗？")
                .setMessage(""+url)
                //.setMinHeight_body((int) getResources().getDimension(R.dimen.dp_100))
                //.setGravity_body(Gravity.CENTER).setText_size_body((int) getResources().getDimension(R.dimen.sp_10))
                //.setWidth((int) getResources().getDimension(R.dimen.dp_120))
                .setText_color_body(Color.BLUE)
                .addAction("复制", (dialog, view, tag) -> {
                    ClipboardUtil.setClip(getContext(),""+url);
                    QdToast.show("copy success");
                    dialog.dismiss();
                })
                .addAction("跳转", (dialog, view, tag) -> {
                    dialog.dismiss();
                    WebViewFragment webViewFragment = new WebViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", url);
                    webViewFragment.setArguments(bundle);
                    Intent intent =new Intent();
                    intent.putExtras(bundle);
                    startFragment(webViewFragment,R.id.container1,intent);
                })
                //.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setText_size_foot((int) getResources().getDimension(R.dimen.sp_6))
                //.setPadding_foot((int) getResources().getDimension(R.dimen.sp_6))
                //.setActionButtonPadding((int) getResources().getDimension(R.dimen.sp_6))
                .setText_color_foot(Color.GREEN)
                .setLineColor(Color.RED)
                //.setBackgroundRadius(backgroundRadio)
                .create();
        qdDialog.show();
    }

    /**
     * Initiate the tabs
     */
    public void initScrollableTabs(View rootView, ViewPager2 mViewPager) {
        ScrollableTabView mScrollingTabs = rootView.findViewById(R.id.scrollingTabs);
        ScrollingTabsAdapter mScrollingTabsAdapter = new ScrollingTabsAdapter(getActivity());
        mScrollingTabs.setScrollingTabsAdapter(mScrollingTabsAdapter);
        mScrollingTabs.setViewPager(mViewPager);
    }

    private static class MainFragmentAdapter extends FragmentStateAdapter {
        private List<Class> data;

        public MainFragmentAdapter(@NonNull @NotNull FragmentActivity fragmentActivity, List<Class> data) {
            super(fragmentActivity);
            this.data = data;
        }

        @NonNull
        @NotNull
        @Override
        public Fragment createFragment(int position) {
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
        public int getItemCount() {
            return data.size();
        }
    }


    //记录用户首次点击返回键的时间
    private long firstClickTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        QDLogger.i( "main onKeyDown " + isRootFragment());
        if (isRootFragment()) {
            if (System.currentTimeMillis() - firstClickTime > 2000) {
                QdToast.show(mContext, "再点击退出app");
                firstClickTime = System.currentTimeMillis();
            } else {
                getActivity().finish();
            }
            return true;
        }
        //QdToast.show(mContext, "onKeyDown isRootFragment=" + isRootFragment());
        return super.onKeyDown(keyCode, event);
    }
}