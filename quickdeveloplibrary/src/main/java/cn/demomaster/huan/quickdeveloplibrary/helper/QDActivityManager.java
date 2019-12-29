package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

/**
 * @author squirrel桓
 * @date 2018/12/27.
 * description：
 */
public class QDActivityManager {
    private static QDActivityManager instance;
    private static Context context;
    //本地activity栈
    private LinkedHashMap<String,Activity> activitys = new LinkedHashMap<>();

    public QDActivityManager(Context context) {
        this.context = context.getApplicationContext();
    }

    private QDActivityManager() {
    }

    //必须要在application里初始化
    public QDActivityManager init(Context context) {
        this.context = context;
        return instance;
    }

    public static QDActivityManager getInstance() {
        if (instance == null) {
            instance = new QDActivityManager();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        QDLogger.d("QDActivityManager","addActivity:"+activity);
        activitys.put(activity+"",activity);
    }

    //删除具体的activity
    public void deleteActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activitys.remove(activity);
        }
    }

    //删除具体的activity
    public void removeActivity(Activity activity) {
        if (activity != null) {
            QDLogger.d("QDActivityManager","removeActivity:"+activity);
            if(currentActivity==activity){
                currentActivity = null;
            }
            activitys.remove(activity+"");
        }
    }

    //删除clazz类的activity
    public void deleteActivityByClass(Class clazz) {
        if (clazz != null) {
            for (Map.Entry entry : activitys.entrySet()) {
                Activity activity = (Activity) entry.getValue();
                if (activity.getClass().equals(clazz)) {
                    activitys.remove(entry.getKey());
                    activity.finish();
                    deleteActivityByClass(clazz);
                    return;
                }
            }
        }
    }

    //删除clazzes类的activity
    public void deleteActivityByClasses(List<Class> clazz) {
        if (clazz != null) {
            for (Map.Entry entry : activitys.entrySet()) {
                Activity activity = (Activity) entry.getValue();
                if (clazz.contains(activity.getClass())) {
                    activitys.remove(activity);
                    activity.finish();
                    deleteActivityByClasses(clazz);
                    return;
                }
            }
        }
    }

    /**
     * 关闭所有activity
     */
    public void deleteAllActivity() {
        if (activitys != null) {
            for (int i = 0; i < activitys.size(); i++) {
                activitys.get(i).finish();
                activitys.remove(i);
                deleteAllActivity();
                return;
            }
        }
    }

    /**
     * 关闭除了a,b,c类，之外的所有activity
     */
    public void deleteOtherActivityByClasses(List<Class> clazz) {
        if (activitys != null && clazz != null) {
            for (Map.Entry entry : activitys.entrySet()) {
                Activity activity = (Activity) entry.getValue();
                if (!clazz.contains(activity.getClass())) {
                    activity.finish();
                    activitys.remove(activity);
                    deleteOtherActivityByClasses(clazz);
                    return;
                }
            }
        }
    }

    /**
     * 关闭除了a list类，之外的所有activity
     */
    public void deleteOtherActivityByClass(List<Class> clazzs) {
        if (activitys != null && clazzs != null) {
            for (Map.Entry entry : activitys.entrySet()) {
                Activity activity = (Activity) entry.getValue();
                if (!clazzs.contains(activity.getClass())) {
                    activity.finish();
                    activitys.remove(activity);
                    deleteOtherActivityByClass(clazzs);
                    return;
                }
            }
        }
    }

    /**
     * 关闭除了a类，之外的所有activity
     */
    public void deleteOtherActivityByClass(Class clazz) {
        if (activitys != null && clazz != null) {
           // android.app.QDActivityManager mActivityManager = (android.app.QDActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (int i = activitys.size()-1; i >= 0; i--) {
                Activity activity = activitys.get(i);
                if (!activity.getClass().equals(clazz)) {
                    activity.finish();
                    activitys.remove(activity);
                    //QDLogger.d("移除"+activity.getClass().getName());
                    deleteOtherActivityByClass(clazz);
                    return;
                }
            }
        }
    }

    /**
     * 关闭除了a之外的所有activity
     */
    public void deleteOtherActivity(Activity activity) {
        if (activitys != null && activity != null) {
            for (int i = 0; i < activitys.size(); i++) {
                if (!activity.equals(activitys.get(i))) {
                    activitys.get(i).finish();
                    activitys.remove(i);
                    deleteOtherActivity(activity);
                    return;
                }
            }
        }
    }

    private Activity currentActivity;

    public void setCurrentActivity(Activity tmp) {
        currentActivity = tmp;
        onStateChanged();
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    /**
     * 设置app前后台切换事件
     */
    private void onStateChanged() {
        if (onAppRunStateChangedListenner != null) {
            if (currentActivity == null) {
                onAppRunStateChangedListenner.onBackground();
            } else {
                android.app.ActivityManager mAm = (android.app.ActivityManager) currentActivity.getSystemService(Context.ACTIVITY_SERVICE);
                String activity_name = mAm.getRunningTasks(1).get(0).topActivity.getPackageName();
                if (!activity_name.equals(currentActivity.getPackageName())) {
                    onAppRunStateChangedListenner.onBackground();
                } else {
                    onAppRunStateChangedListenner.onForeground();
                }
            }
        }
    }

    OnAppRunStateChangedListenner onAppRunStateChangedListenner;

    /**
     * 设置app前后台运行切换状态监听
     *
     * @param onAppRunStateChangedListenner
     */
    public void setOnAppRunStateChangedListenner(OnAppRunStateChangedListenner onAppRunStateChangedListenner) {
        this.onAppRunStateChangedListenner = onAppRunStateChangedListenner;
    }

    public void onActivityPaused(Activity activity) {
        onStateChanged();
    }

    public static interface OnAppRunStateChangedListenner {
        void onForeground();//前台显示

        void onBackground();//后台显示
    }


    private static android.app.ActivityManager manager;
    private static List<android.app.ActivityManager.RunningAppProcessInfo> runningProcesses;
    private static String packName;
    private static PackageManager pManager;

    /**
     * 杀死其他正在运行的程序
     *
     * @param context
     */
    public static void killOthers(Context context, String pkgName) {
        pManager = context.getPackageManager();
        manager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        runningProcesses = manager.getRunningAppProcesses();
        for (android.app.ActivityManager.RunningAppProcessInfo runningProcess : runningProcesses) {
            try {
                packName = runningProcess.processName;
                PackageInfo packageInfo = pManager.getPackageInfo(packName, 0);
                if (packageInfo != null) {
                    ApplicationInfo applicationInfo = pManager.getPackageInfo(packName, 0).applicationInfo;
                    if (!pkgName.equals(packName) && filterApp(applicationInfo)) {
                        forceStopPackage(context, packName);
                        System.out.println(packName + "JJJJJJ");
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
                QDLogger.e(e.getMessage());
            }
        }
    }

    /**
     * 强制停止应用程序
     *
     * @param pkgName
     */
    public static void forceStopPackage(Context context, String pkgName) throws Exception {
        android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
        method.invoke(am, pkgName);
    }

    /**
     * 判断某个应用程序是 不是三方的应用程序
     *
     * @param info
     * @return
     */
    public static boolean filterApp(ApplicationInfo info) {
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }
}
