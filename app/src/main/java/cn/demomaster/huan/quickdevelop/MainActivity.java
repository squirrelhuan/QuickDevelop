package cn.demomaster.huan.quickdevelop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import cn.demomaster.huan.quickdevelop.sample.CsqliteActivity;
import cn.demomaster.huan.quickdevelop.sample.PickActivity;
import cn.demomaster.huan.quickdevelop.sample.QDialogActivity;
import cn.demomaster.huan.quickdevelop.sample.TabMenuActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.camera.idcard.IDCardActivity;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.SimplePictureActivity;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.RatingBar;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.CustomDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.loader.LoadingDialog;

public class MainActivity extends BaseActivityParent implements View.OnClickListener {

    Button btn_scan,btn_db;
    Button btn_ac_01, btn_ac_02, btn_ac_03;
    TextView tv_test;
    LinearLayout ll_layout;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setColor(Color.RED,Color.LTGRAY);
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
    }

    private void init() {
        actionBarLayout.setActionBarModel(ActionBarLayout.ACTIONBAR_TYPE.NORMAL);
        actionBarLayout.setTitle("aaa");
        actionBarLayout.setStateBarColorAuto(true);//状态栏文字颜色自动
        actionBarLayout.setActionBarThemeColors(Color.WHITE,Color.BLACK);
        actionBarLayout.setLeftOnClickListener(new View.OnClickListener() {
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
                intent = new Intent(MainActivity.this, SimplePictureActivity.class);
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
                actionBarLayout.setActionBarModel(ActionBarLayout.ACTIONBAR_TYPE.NORMAL);
                break;

            case R.id.btn_ac_02:
                actionBarLayout.setActionBarModel(ActionBarLayout.ACTIONBAR_TYPE.NO_ACTION_BAR);
                break;

            case R.id.btn_ac_03:
                actionBarLayout.setActionBarModel(ActionBarLayout.ACTIONBAR_TYPE.ACTION_STACK);
                break;

            case R.id.btn_color_black:
                actionBarLayout.setBackGroundColor(getResources().getColor(R.color.black));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.black),getResources().getColor(R.color.white));
                PopToastUtil.ShowToast(this,"黑色主题");
                break;
            case R.id.btn_color_white:
                actionBarLayout.setBackGroundColor(getResources().getColor(R.color.white));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.white),getResources().getColor(R.color.black));
                PopToastUtil.ShowToast(this,"白色主题");
                break;
            case R.id.btn_color_red:
                actionBarLayout.setBackGroundColor(getResources().getColor(R.color.red));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.red),getResources().getColor(R.color.white));
                PopToastUtil.ShowToast(this,"红色主题");
                break;
            case R.id.btn_color_gray:
                actionBarLayout.setBackGroundColor(getResources().getColor(R.color.gray));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.gray),getResources().getColor(R.color.white));
                PopToastUtil.ShowToast(this,"灰色主题");
                break;
            case R.id.btn_color_green:
                actionBarLayout.setBackGroundColor(getResources().getColor(R.color.green));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.green),getResources().getColor(R.color.white));
                PopToastUtil.ShowToast(this,"绿色主题");
                break;
            case R.id.btn_color_yellow:
                actionBarLayout.setBackGroundColor(getResources().getColor(R.color.yello));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.yello),getResources().getColor(R.color.black));
                PopToastUtil.ShowToast(this,"黄色主题");
                break;
            case R.id.btn_change_bg:
                if (position == 0) {
                    ll_layout.setBackgroundResource(R.mipmap.meizi);
                    position = 1;
                } else {
                    ll_layout.setBackgroundResource(R.mipmap.gudaimeizi);
                    position = 0;
                }
                actionBarLayout.changeChildView(ll_layout);
                break;
        }
    }
}
