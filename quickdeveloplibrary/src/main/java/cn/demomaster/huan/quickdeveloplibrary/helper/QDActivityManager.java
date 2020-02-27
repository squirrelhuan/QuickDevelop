package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * @author squirrel桓
 * @date 2018/12/27.
 * description：
 */
public class QDActivityManager {
    private static QDActivityManager instance;
    private Context context;
    //本地activity栈
    private LinkedHashMap<String, WeakReference<Activity>> activitys = new LinkedHashMap<>();
    private QDActivityManager() {
    }

    //必须要在application里初始化
    public QDActivityManager init(Context context) {
        this.context = context.getApplicationContext();
        return instance;
    }

    public static QDActivityManager getInstance() {
        if (instance == null) {
            instance = new QDActivityManager();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        QDLogger.d("QDActivityManager", "addActivity:" + activity);
        activitys.put(activity + "",new WeakReference(activity) );
    }

    //删除具体的activity
    public void deleteActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activitys.remove(activity+ "");
        }
    }

    //删除具体的activity
    public void removeActivity(Activity activity) {
        if (activity != null) {
            QDLogger.d("QDActivityManager", "removeActivity:" + activity);
            if (currentActivity == activity) {
                currentActivity = null;
            }
            activitys.remove(activity + "");
        }
    }

    //删除clazz类的activity
    public void deleteActivityByClass(Class clazz) {
        if (clazz != null) {
            for (Map.Entry entry : activitys.entrySet()) {
                Activity activity = ((WeakReference<Activity>) entry.getValue()).get();
                if (activity!=null&&activity.getClass().equals(clazz)) {
                    activitys.remove(entry.getKey());
                    activity.finish();
                    deleteActivityByClass(clazz);
                    return;
                }
                activitys.remove(entry.getKey()+"");
                deleteActivityByClass(clazz);
                return;
            }
        }
    }

    //删除clazzes类的activity
    public void deleteActivityByClasses(List<Class> clazz) {
        if (clazz != null) {
            for (Map.Entry entry : activitys.entrySet()) {
                Activity activity = ((WeakReference<Activity>) entry.getValue()).get();
                if (activity!=null&&clazz.contains(activity.getClass())) {
                    activitys.remove(entry.getKey()+"");
                    activity.finish();
                    deleteActivityByClasses(clazz);
                    return;
                }
                if(activity==null){
                    activitys.remove(entry.getKey()+"");
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
        for (Map.Entry entry : activitys.entrySet()) {
            Activity activity = ((WeakReference<Activity>) entry.getValue()).get();
            activitys.remove(entry.getKey()+ "");
            if(activity!=null){
                activity.finish();
            }
            deleteAllActivity();
            return;
        }
    }

    /**
     * 关闭除了a,b,c类，之外的所有activity
     */
    public void deleteOtherActivityByClasses(List<Class> clazz) {
        if ( clazz != null) {
            for (Map.Entry entry : activitys.entrySet()) {
                Activity activity = ((WeakReference<Activity>) entry.getValue()).get();
                if (activity!=null&&!clazz.contains(activity.getClass())) {
                    activitys.remove(entry.getKey()+ "");
                    activity.finish();
                    deleteOtherActivityByClasses(clazz);
                    return;
                }else if(activity==null){
                    activitys.remove(entry.getKey()+ "");
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
        if ( clazzs != null) {
            for (Map.Entry entry : activitys.entrySet()) {
                Activity activity = ((WeakReference<Activity>) entry.getValue()).get();
                if (activity!=null&&!clazzs.contains(activity.getClass())) {
                    activitys.remove(entry.getKey()+ "");
                    activity.finish();
                    deleteOtherActivityByClass(clazzs);
                    return;
                }else if(activity==null){
                    activitys.remove(entry.getKey()+ "");
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
        if (clazz != null) {
            // android.app.QDActivityManager mActivityManager = (android.app.QDActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (Map.Entry entry : activitys.entrySet()) {
                Activity activity = ((WeakReference<Activity>) entry.getValue()).get();
                if (activity!=null&&!activity.getClass().equals(clazz)) {
                    activitys.remove(entry.getKey());
                    activity.finish();
                    //QDLogger.d("移除"+activity.getClass().getName());
                    deleteOtherActivityByClass(clazz);
                    return;
                }else if(activity==null){
                    activitys.remove(entry.getKey());
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
        if (activity != null) {
            for (Map.Entry entry : activitys.entrySet()) {
                Activity activityT = ((WeakReference<Activity>) entry.getValue()).get();
                if (activityT!=null&&!activity.equals(activityT)) {
                    activitys.remove(entry.getKey()+"");
                    activityT.finish();
                    deleteOtherActivity(activity);
                    return;
                }else if(activityT==null){
                    activitys.remove(entry.getKey()+"");
                    deleteOtherActivity(activity);
                    return;
                }
            }
        }
    }

    private Activity currentActivity;
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
        onStateChanged(activity,true);
    }

    public void onActivityStopped(Activity activity) {
        onStateChanged(activity,false);
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    /**
     * 设置app前后台切换事件
     */
    private void onStateChanged(Activity activity ,boolean b) {
        if (onAppRunStateChangedListenner != null) {
            if (currentActivity != null) {
               // ActivityManager mAm = (ActivityManager) currentActivity.getSystemService(ACTIVITY_SERVICE);
               // String activity_name = mAm.getRunningTasks(1).get(0).topActivity.getPackageName();
                if (!isRunningOnForeground(context)) {
                    if(isOnForground) {
                        isOnForground = false;
                        onAppRunStateChangedListenner.onBackground();
                    }
                } else {
                    if(!isOnForground) {
                        isOnForground = true;
                        onAppRunStateChangedListenner.onForeground();
                    }
                }
            }
        }
    }

    boolean isOnForground = true;
    /**TODO 待驗證first
     * Activity是否在前台
     * @param context
     * @return
     */
    public boolean isRunningOnForeground2(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
        if(appProcessInfoList == null){
            QDLogger.e("appProcessInfoList = null");
            return false;
        }

        String processName = context.getApplicationInfo().processName;
        for(ActivityManager.RunningAppProcessInfo processInfo : appProcessInfoList){
            QDLogger.i(""+processInfo.processName+","+processInfo.importance);
            if(processInfo.processName.equals(processName) && processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND ){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    public boolean isRunningOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        // 枚举进程
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            //QDLogger.i("isRunningOnForeground="+appProcessInfo.processName+appProcessInfo.importance );
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                //ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE
                if (appProcessInfo.processName.equals(context.getApplicationInfo().processName)) {
                    QDLogger.i("isRunningOnForeground="+true);
                    return true;
                }
            }
        }
        //QDLogger.i("isRunningOnForeground="+false);
        return false;
    }
    OnAppRunStateChangedListenner onAppRunStateChangedListenner;
    /**
     * 设置app前后台运行切换状态监听
     * @param onAppRunStateChangedListenner
     */
    public void setOnAppRunStateChangedListenner(OnAppRunStateChangedListenner onAppRunStateChangedListenner) {
        this.onAppRunStateChangedListenner = onAppRunStateChangedListenner;
    }

    public void onActivityPaused(Activity activity) {
        onStateChanged(activity,false);
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
     * @param context
     */
    public static void killOthers(Context context, String pkgName) {
        pManager = context.getPackageManager();
        manager = (android.app.ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
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
     * @param pkgName
     */
    public static void forceStopPackage(Context context, String pkgName) throws Exception {
        android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
        method.invoke(am, pkgName);
    }

    /**
     * 判断某个应用程序是 不是三方的应用程序
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

    public boolean isExistActivity(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity    
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList =am.getRunningTasks(10);//
            //这里获取的是APP栈的数量，一般也就两个
            ActivityManager.RunningTaskInfo runningTaskInfo = taskInfoList.get(0);// 只是拿          当前运行的栈
            int numActivities = runningTaskInfo.numActivities;
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) {// 说明它已经启动了
                    flag = true;
                    break;//跳出循环，优化效率
                }
            }
        }
        return flag;
    }

    public void backToApp( Context context,Class activityClass){
        if (!isRunningOnForeground(context)) {
            //获取ActivityManager
            ActivityManager mAm = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            //获得当前运行的task
            List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
            for (ActivityManager.RunningTaskInfo rti : taskList) {
                //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
                if (rti.topActivity.getPackageName().equals(context.getPackageName())) {
                    mAm.moveTaskToFront(rti.id, 0);
                    return;
                }
            }
            //若没有找到运行的task，用户结束了task或被系统释放，则重新启动mainactivity
            Intent resultIntent = new Intent(context, activityClass);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(resultIntent);
        }
    }
}
