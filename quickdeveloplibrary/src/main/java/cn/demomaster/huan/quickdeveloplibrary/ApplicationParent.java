package cn.demomaster.huan.quickdeveloplibrary;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.db.CBHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.ActivityManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.NotifycationHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.SharedPreferencesHelper;
import cn.demomaster.huan.quickdeveloplibrary.http.HttpUtils;

public class ApplicationParent extends Application {

    public static String TAG ="CGQ";
    private static ApplicationParent instance = null;
    public static ApplicationParent getInstance(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化全局SharedPreferences
        SharedPreferencesHelper.init(this);
        initDB();
        ActivityManager.init(this);
        NotifycationHelper.getInstance().init(this);


    }

    public CBHelper dbHelper;
    public SQLiteDatabase db;
    private void initDB() {
        dbHelper = new CBHelper(this,"yidao.db",null,1);
        //得到一个可读的SQLiteDatabase对象
        db =dbHelper.getReadableDatabase();
    }

    //添加 <功能详细描述>
    public void addActivity(Activity activity) {
        ActivityManager.getInstance().addActivity(activity);
    }
    //删除
    public void deleteActivity(Activity activity) {
        ActivityManager.getInstance().deleteActivity(activity);
    }
    //删除
    public void deleteActivity(Class clazz) {
        ActivityManager.getInstance().deleteActivityByClass(clazz);
    }
    //退出
    public void deleteAllActivity() {
        ActivityManager.getInstance().deleteAllActivity();
    }
    //退出
    public void deleteOtherActivity(Activity activity) {
        ActivityManager.getInstance().deleteOtherActivity(activity);
    }
}
