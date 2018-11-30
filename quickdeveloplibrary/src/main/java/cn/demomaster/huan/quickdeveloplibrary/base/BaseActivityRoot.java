package cn.demomaster.huan.quickdeveloplibrary.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import cn.demomaster.huan.quickdeveloplibrary.ApplicationParent;
import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;

public class BaseActivityRoot extends AppCompatActivity {

    public Activity mContext;
    public Bundle mBundle = null;
    public static String TAG = "CGQ";

    @Override
    public void setContentView(View view) {
        mContext = this;
        mBundle = getIntent().getExtras();
        super.setContentView(view);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;
        mBundle = getIntent().getExtras();
        ((ApplicationParent) getApplicationContext()).addActivity(this);
        super.onCreate(savedInstanceState);
        StatusBarUtil.transparencyBar(mContext);
        initHelper();
    }
   public PhotoHelper photoHelper;
    //实例化各种帮助类
    private void initHelper() {
        photoHelper = new PhotoHelper(mContext);
    }

    public void onCreateView(@Nullable Bundle savedInstanceState) {

    }

    public View getContentView() {
        return this.findViewById(android.R.id.content);
    }

    //设置导航栏透明
    public void transparentBar() {
        StatusBarUtil.transparencyBar(this);
    }

    public void startActivity(Class<?> clazz) {
        startActivity(clazz, null);
    }

    public void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_SCAN_QRCODE://扫描
                photoHelper.onActivityResult(requestCode, PhotoHelper.RESULT_CODE_SCAN_QRCODE, data);
                break;
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_CAMERA://自定义拍照
                photoHelper.onActivityResult(requestCode, resultCode, data);
                break;
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_TAKE_PHOTO://拍照
                photoHelper.onActivityResult(requestCode, PhotoHelper.RESULT_CODE_TAKE_PHOTO, data);
                break;
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_GALLERY://相册选取
                photoHelper.onActivityResult(requestCode, PhotoHelper.RESULT_CODE_SELECT_PHOTO_FROM_GALLERY, data);//偷梁换柱
                break;
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_PHOTO_CROP://图片截取
                photoHelper.onActivityResult(requestCode, PhotoHelper.RESULT_CODE_PHOTO_CROP, data);//偷梁换柱
                break;
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE_GALLERY_AND_CROP://相册并截取
                photoHelper.onActivityResult(requestCode, PhotoHelper.RESULT_CODE_SELECT_PHOTO_FROM_GALLERY_AND_CROP, data);//偷梁换柱
                break;


        }


    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionManager.REQUEST_PERMISS_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //用户同意，执行操作
                PermissionManager.getInstance(mContext).onPass();
            }

    }*/




    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());   //收起键盘
            }
        }
        return super.dispatchTouchEvent(me);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }



}
