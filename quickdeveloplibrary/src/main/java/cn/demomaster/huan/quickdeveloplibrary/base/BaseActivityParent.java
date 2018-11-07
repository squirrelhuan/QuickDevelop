package cn.demomaster.huan.quickdeveloplibrary.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.demomaster.huan.quickdeveloplibrary.ApplicationParent;

public class BaseActivityParent extends AppCompatActivity {

    public Activity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ((ApplicationParent)getApplicationContext()).addActivity(this);
    }
}
