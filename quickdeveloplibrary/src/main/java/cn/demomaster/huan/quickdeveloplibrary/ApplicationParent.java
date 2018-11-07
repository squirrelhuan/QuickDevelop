package cn.demomaster.huan.quickdeveloplibrary;

import android.app.Application;

import cn.demomaster.huan.quickdeveloplibrary.helper.SharedPreferencesHelper;


public class ApplicationParent extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化全局SharedPreferences
        SharedPreferencesHelper.init(this);
    }
}
