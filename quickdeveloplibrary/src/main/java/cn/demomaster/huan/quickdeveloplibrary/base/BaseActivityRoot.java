package cn.demomaster.huan.quickdeveloplibrary.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.demomaster.huan.quickdeveloplibrary.ApplicationParent;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;

public class BaseActivityRoot extends AppCompatActivity {

    public Activity mContext;
    public Bundle mBundle = null;
    public static String TAG = "CGQ";

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
            case PhotoHelper.PHOTOHELPER_REQUEST_CODE://图片采集类
                photoHelper.onActivityResult(requestCode, resultCode, data);
                break;
        }


    }
}
