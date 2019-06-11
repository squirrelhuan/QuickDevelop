package cn.demomaster.huan.quickdevelop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.net.RetrofitInterface;
import cn.demomaster.huan.quickdevelop.activity.sample.CenterHorizontalActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.CsqliteActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.component.LoadingActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.PickActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.PictureSelectActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.component.QDialogActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.TabMenuActivity;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout2;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarState;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.OptionsMenu;
import cn.demomaster.huan.quickdeveloplibrary.camera.idcard.FileUtil;
import cn.demomaster.huan.quickdeveloplibrary.camera.idcard.IDCardActivity;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.SharedPreferencesHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.UpdatePopDialog;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;
import cn.demomaster.huan.quickdeveloplibrary.http.HttpUtils;
import cn.demomaster.huan.quickdeveloplibrary.model.Version;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.util.ScreenShotUitl;
import cn.demomaster.huan.quickdeveloplibrary.widget.RatingBar;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.ToggleButton;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.CustomDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

//import cn.demomaster.huan.quickdevelop.activity.sample.PictureSelectActivity;

@ActivityPager(name = "MainActivity",preViewClass = ToggleButton.class,resType = ResType.Custome)
public class MainActivity extends BaseActivityParent implements View.OnClickListener {

    Button btn_scan, btn_db;
    Button btn_ac_01, btn_ac_02, btn_ac_03, btn_ac_04, btn_ac_05, btn_ac_06;
    Button btn_loading_animation, btn_center_horizontal, btn_action_tip;
    TextView tv_test;
    LinearLayout ll_layout;
    RatingBar ratingBar;

    private int stateIndex;

    @Override
    public int getHeadlayoutResID() {
        return R.layout.activity_actionbar_common_a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setColor(Color.RED, Color.LTGRAY);
        ratingBar.setActivateCount(3);
        btn_db = findViewById(R.id.btn_db);
        btn_db.setOnClickListener(this);
        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(this);

        //GuiderHelper.getInstance().startGuider(mContext,btn_db,"DBGUIDER");
        getActionBarLayoutOld().getActionBarTip().setLoadingStateListener(new ActionBarState.OnLoadingStateListener() {
            @Override
            public void onLoading(final ActionBarState.Loading loading) {
                //TODO 处理状态
                loading.setText("处理状态");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //loading.hide();
                loading.success("加载success");
            }

        });
        btn_action_tip = findViewById(R.id.btn_action_tip);
        btn_action_tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getActionBarLayoutOld().getActionBarTip().show();
                stateIndex++;
                switch (stateIndex % 4) {
                    case 0:
                        getActionBarLayoutOld().getActionBarTip().showComplete("完成");
                        break;
                    case 1:
                        getActionBarLayoutOld().getActionBarTip().showWarning("警告警告");
                        break;
                    case 2:
                        getActionBarLayoutOld().getActionBarTip().showError("发生错误啦");
                        break;
                    case 3:
                        getActionBarLayoutOld().getActionBarTip().showLoading("loading...");
                        break;
                }
            }
        });
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
        btn_loading_animation = findViewById(R.id.btn_loading_animation);
        btn_center_horizontal = findViewById(R.id.btn_center_horizontal);


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
        btn_loading_animation.setOnClickListener(this);
        btn_center_horizontal.setOnClickListener(this);

        String conf = FileUtil.getFromAssets(mContext, "config/update.his");
        if (conf != null) {
            List<Version> versions = JSON.parseArray(conf,Version.class);
            final Version version =  versions.get(versions.size()-1);
            updatePopDialog = new UpdatePopDialog(mContext, versions.get(versions.size()-1).getDescription());
            updatePopDialog.setOnCloseListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferencesHelper.getInstance().putBoolean(version.getVersionCode()+"",false);
                }
            });
            if(SharedPreferencesHelper.getInstance().getBoolean(version.getVersionCode()+"",true)){
                updatePopDialog.show();
            }
        }
        init();
    }

    private UpdatePopDialog updatePopDialog;

    private void init() {
        getActionBarLayoutOld().setActionBarModel(ActionBarLayout2.ACTIONBAR_TYPE.NORMAL);
        getActionBarLayoutOld().setTitle("aaa");
        getActionBarLayoutOld().setStateBarColorAuto(true);//状态栏文字颜色自动
        getActionBarLayoutOld().setActionBarThemeColors(Color.WHITE, Color.BLACK);
        getActionBarLayoutOld().setLeftOnClickListener(new View.OnClickListener() {
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
        getActionBarLayoutOld().setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOptionsMenu().show();
            }
        });
        initOptionsMenu();
        http();
    }

    private void http() {
        //Retrofit
        RetrofitInterface retrofitInterface = HttpUtils.getInstance().getRetrofit(RetrofitInterface.class, "http://www.demomaster.cn/");
        retrofitInterface.getSession()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Object>() {
                    @Override
                    public void onNext(@NonNull Object response) {
                        QDLogger.i(TAG, "onNext: " + JSON.toJSONString(response));
                        try {
                            //JSONObject jsonObject = JSON.parseObject(response.getData().toString());
                            //List doctors1 = JSON.parseArray(response.getData().toString(), DoctorModelApi.class);
                            //String token = jsonObject.get("token").toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onStart() {
                        QDLogger.i(TAG, "onStart: ");
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        QDLogger.i(TAG, "onError: " + throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        QDLogger.i(TAG, "onComplete: ");
                    }
                });
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
        getOptionsMenu().setAnchor(getActionBarLayoutOld().getRightView());
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

    private int position;
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_center_horizontal:
                intent = new Intent(MainActivity.this, CenterHorizontalActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_loading_animation:
                intent = new Intent(MainActivity.this, LoadingActivity.class);
                startActivityForResult(intent, 1);
                break;
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
                getActionBarLayoutOld().setActionBarModel(ActionBarLayout2.ACTIONBAR_TYPE.NORMAL);
                break;

            case R.id.btn_ac_02:
                getActionBarLayoutOld().setActionBarModel(ActionBarLayout2.ACTIONBAR_TYPE.NO_ACTION_BAR);
                break;

            case R.id.btn_ac_03:
                getActionBarLayoutOld().setActionBarModel(ActionBarLayout2.ACTIONBAR_TYPE.ACTION_STACK);
                break;
            case R.id.btn_ac_04:
                getActionBarLayoutOld().setActionBarModel(ActionBarLayout2.ACTIONBAR_TYPE.ACTION_STACK_NO_STATUS);
                break;
            case R.id.btn_ac_05:
                getActionBarLayoutOld().setActionBarModel(ActionBarLayout2.ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);
                break;
            case R.id.btn_ac_06:
                getActionBarLayoutOld().setActionBarModel(ActionBarLayout2.ACTIONBAR_TYPE.ACTION_TRANSPARENT);
                break;
            case R.id.btn_color_black:
                getActionBarLayoutOld().setBackGroundColor(getResources().getColor(R.color.black));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.black), getResources().getColor(R.color.white));
                showMessage("黑色主题");
                break;
            case R.id.btn_color_white:
                getActionBarLayoutOld().setBackGroundColor(getResources().getColor(R.color.white));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.white), getResources().getColor(R.color.black));
                showMessage("白色主题");
                break;
            case R.id.btn_color_red:
                getActionBarLayoutOld().setBackGroundColor(getResources().getColor(R.color.red));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.red), getResources().getColor(R.color.white));
                PopToastUtil.ShowToast(this, "红色主题");
                break;
            case R.id.btn_color_gray:
                getActionBarLayoutOld().setBackGroundColor(getResources().getColor(R.color.gray));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.gray), getResources().getColor(R.color.white));
                PopToastUtil.ShowToast(this, "灰色主题");
                break;
            case R.id.btn_color_green:
                getActionBarLayoutOld().setBackGroundColor(getResources().getColor(R.color.green));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.green), getResources().getColor(R.color.white));
                PopToastUtil.ShowToast(this, "绿色主题");
                break;
            case R.id.btn_color_yellow:
                getActionBarLayoutOld().setBackGroundColor(getResources().getColor(R.color.yellow));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.yellow), getResources().getColor(R.color.black));
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
                getActionBarLayoutOld().changeChildView(ll_layout);
                break;
        }
    }


}
