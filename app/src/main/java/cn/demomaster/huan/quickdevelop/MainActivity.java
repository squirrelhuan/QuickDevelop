package cn.demomaster.huan.quickdevelop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.adapter.AppListAdapter;
import cn.demomaster.huan.quickdevelop.sample.CsqliteActivity;
import cn.demomaster.huan.quickdevelop.sample.PickActivity;
import cn.demomaster.huan.quickdevelop.sample.PictureSelectActivity;
import cn.demomaster.huan.quickdevelop.sample.QDialogActivity;
import cn.demomaster.huan.quickdevelop.sample.TabMenuActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.camera.idcard.IDCardActivity;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.ScreenShotUitl;
import cn.demomaster.huan.quickdeveloplibrary.widget.RatingBar;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.CustomDialog;

//import cn.demomaster.huan.quickdevelop.sample.PictureSelectActivity;

public class MainActivity extends BaseActivityParent implements View.OnClickListener {

    Button btn_scan, btn_db;
    Button btn_ac_01, btn_ac_02, btn_ac_03, btn_ac_04, btn_ac_05, btn_ac_06;
    TextView tv_test;
    LinearLayout ll_layout;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setColor(Color.RED, Color.LTGRAY);
        ratingBar.setActivateCount(3);
        btn_db = findViewById(R.id.btn_db);
        btn_db.setOnClickListener(this);
        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(this);

        btn_ac_01 = findViewById(R.id.btn_ac_01);
        btn_ac_01.setOnClickListener(this);

        btn_ac_02 = findViewById(R.id.btn_ac_02);
        btn_ac_02.setOnClickListener(this);


        btn_ac_03 = findViewById(R.id.btn_ac_03);
        btn_ac_03.setOnClickListener(this);
        btn_ac_04 = findViewById(R.id.btn_ac_04);
        btn_ac_04.setOnClickListener(this);
        btn_ac_05 = findViewById(R.id.btn_ac_05);
        btn_ac_05.setOnClickListener(this);
        btn_ac_06 = findViewById(R.id.btn_ac_06);
        btn_ac_06.setOnClickListener(this);
        tv_test = findViewById(R.id.tv_test);


        findViewById(R.id.btn_tab_menu).setOnClickListener(this);
        findViewById(R.id.btn_dialog).setOnClickListener(this);
        findViewById(R.id.btn_photo).setOnClickListener(this);
        findViewById(R.id.btn_simple_picture).setOnClickListener(this);

        findViewById(R.id.btn_pick).setOnClickListener(this);
        findViewById(R.id.btn_color_black).setOnClickListener(this);
        findViewById(R.id.btn_color_red).setOnClickListener(this);
        findViewById(R.id.btn_color_gray).setOnClickListener(this);
        findViewById(R.id.btn_color_yellow).setOnClickListener(this);
        findViewById(R.id.btn_color_green).setOnClickListener(this);
        findViewById(R.id.btn_color_white).setOnClickListener(this);
        findViewById(R.id.btn_change_bg).setOnClickListener(this);
        ll_layout = findViewById(R.id.ll_layout);


        init();
        setUpRecyclerView();
    }

    private void init() {
        getActionBarLayout().setActionBarModel(ActionBarLayout.ACTIONBAR_TYPE.NORMAL);
        getActionBarLayout().setTitle("aaa");
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
        String[] menuNames = {"我的二维码","扫描","截图分享"};
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
                switch (position){
                    case 0:
                        break;
                    case 1:
                        photoHelper.selectPhotoFromGalleryAndCrop(new PhotoHelper.OnTakePhotoResult(){
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

    private int position;

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_tab_menu:
                intent = new Intent(MainActivity.this, TabMenuActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_dialog:
                intent = new Intent(MainActivity.this, QDialogActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_db:
                intent = new Intent(MainActivity.this, CsqliteActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_pick:
                intent = new Intent(MainActivity.this, PickActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_photo:
                intent = new Intent(MainActivity.this, IDCardActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_simple_picture:
                intent = new Intent(MainActivity.this, PictureSelectActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_scan:
                //Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                photoHelper.scanQrcode(new PhotoHelper.OnTakePhotoResult() {
                    @Override
                    public void onSuccess(Intent data, String path) {
                        if (data != null) {
                            tv_test.setText("扫描结果为：" + path);
                        }
                    }
                    @Override
                    public void onFailure(String error) {

                    }
                });
                break;

            case R.id.btn_ac_01:
                getActionBarLayout().setActionBarModel(ActionBarLayout.ACTIONBAR_TYPE.NORMAL);
                break;

            case R.id.btn_ac_02:
                getActionBarLayout().setActionBarModel(ActionBarLayout.ACTIONBAR_TYPE.NO_ACTION_BAR);
                break;

            case R.id.btn_ac_03:
                getActionBarLayout().setActionBarModel(ActionBarLayout.ACTIONBAR_TYPE.ACTION_STACK);
                break;
            case R.id.btn_ac_04:
                getActionBarLayout().setActionBarModel(ActionBarLayout.ACTIONBAR_TYPE.ACTION_STACK_NO_STATUS);
                break;
            case R.id.btn_ac_05:
                getActionBarLayout().setActionBarModel(ActionBarLayout.ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);
                break;
            case R.id.btn_ac_06:
                getActionBarLayout().setActionBarModel(ActionBarLayout.ACTIONBAR_TYPE.ACTION_TRANSPARENT);
                break;
            case R.id.btn_color_black:
                getActionBarLayout().setBackGroundColor(getResources().getColor(R.color.black));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.black), getResources().getColor(R.color.white));
                showMessage("黑色主题");
                break;
            case R.id.btn_color_white:
                getActionBarLayout().setBackGroundColor(getResources().getColor(R.color.white));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.white), getResources().getColor(R.color.black));
                showMessage("白色主题");
                break;
            case R.id.btn_color_red:
                getActionBarLayout().setBackGroundColor(getResources().getColor(R.color.red));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.red), getResources().getColor(R.color.white));
                PopToastUtil.ShowToast(this, "红色主题");
                break;
            case R.id.btn_color_gray:
                getActionBarLayout().setBackGroundColor(getResources().getColor(R.color.gray));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.gray), getResources().getColor(R.color.white));
                PopToastUtil.ShowToast(this, "灰色主题");
                break;
            case R.id.btn_color_green:
                getActionBarLayout().setBackGroundColor(getResources().getColor(R.color.green));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.green), getResources().getColor(R.color.white));
                PopToastUtil.ShowToast(this, "绿色主题");
                break;
            case R.id.btn_color_yellow:
                getActionBarLayout().setBackGroundColor(getResources().getColor(R.color.yello));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.yello), getResources().getColor(R.color.black));
                PopToastUtil.ShowToast(this, "黄色主题");
                break;
            case R.id.btn_change_bg:
                if (position == 0) {
                    ll_layout.setBackgroundResource(R.mipmap.meizi);
                    position = 1;
                } else {
                    ll_layout.setBackgroundResource(R.mipmap.gudaimeizi);
                    position = 0;
                }
                getActionBarLayout().changeChildView(ll_layout);
                break;
        }
    }
    public RecyclerView centerSnapRecyclerView;
    private AppListAdapter appListCenterAdapter;
    private void setUpRecyclerView() {

        centerSnapRecyclerView = findViewById(R.id.centerSnapRecyclerView);

        LinearLayoutManager layoutManagerCenter
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        centerSnapRecyclerView.setLayoutManager(layoutManagerCenter);
        appListCenterAdapter = new AppListAdapter(this);
        centerSnapRecyclerView.setAdapter(appListCenterAdapter);
        List<String> names =new ArrayList<>();
        for(int i=0;i<50;i++){
            names.add("第几"+i+"个");
        }
        appListCenterAdapter.updateList(names);
        SnapHelper snapHelperCenter = new LinearSnapHelper();
        snapHelperCenter.attachToRecyclerView(centerSnapRecyclerView);


    }

}
