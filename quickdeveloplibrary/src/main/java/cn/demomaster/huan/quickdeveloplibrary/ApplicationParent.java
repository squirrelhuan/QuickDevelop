package cn.demomaster.huan.quickdeveloplibrary;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.db.CBHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.SharedPreferencesHelper;

public class ApplicationParent extends Application {

    public static String TAG ="CGQ";
    private static ApplicationParent instance = null;
    public static Application getInstance(){
        return instance;
    }
    //本地activity栈
    public static List<Activity> activitys = new ArrayList<Activity>();
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化全局SharedPreferences
        SharedPreferencesHelper.init(this);
        initDB();

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
