package cn.demomaster.huan.quickdeveloplibrary;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.helper.SharedPreferencesHelper;


public class ApplicationParent extends Application {

    public static String TAG ="CGQ";
    public static ApplicationParent instance = null;
    //本地activity栈
    public static List<Activity> activitys = new ArrayList<Activity>();
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化全局SharedPreferences
        SharedPreferencesHelper.init(this);
    }


    //添加 <功能详细描述>
    public void addActivity(Activity activity) {
        activitys.add(activity);
    }

    //删除
    public void deleteActivity(Activity activity) {
        if (activity != null) {
            activitys.remove(activity);
            activity.finish();
        }
    }
    //退出
    public void deleteAllActivity() {
        if (activitys != null) {
            for (Activity activity : activitys) {
                activity.finish();
            }
        }
    }
}
