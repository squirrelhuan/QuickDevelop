package cn.demomaster.huan.quickdeveloplibrary.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.demomaster.huan.quickdeveloplibrary.ApplicationParent;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;

public class BaseActivityParent extends AppCompatActivity {

    public Activity mContext;
    public Bundle mBundle = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mBundle = getIntent().getExtras();
        ((ApplicationParent)getApplicationContext()).addActivity(this);
    }

    /**
     * 设置导航栏透明
     */
    public void transparentBar(){
        StatusBarUtil.transparencyBar(this);
    }

}
