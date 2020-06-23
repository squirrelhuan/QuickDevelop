package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.PopupWindow;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Stack;

import cn.demomaster.huan.quickdeveloplibrary.base.OnReleaseListener;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.constant.TAG;
import cn.demomaster.huan.quickdeveloplibrary.lifecycle.LifecycleRecorder;
import cn.demomaster.huan.quickdeveloplibrary.lifecycle.LifecycleType;
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
    private static Stack<Activity> activityStack;

    private QDActivityManager() {
    }

    Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            QDActivityManager.getInstance().pushActivity(activity);
            record(LifecycleType.onActivityCreated, activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            record(LifecycleType.onActivityStarted, activity);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            QDActivityManager.getInstance().onActivityResumed(activity);
            record(LifecycleType.onActivityResumed, activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            QDActivityManager.getInstance().onActivityPaused(activity);

            record(LifecycleType.onActivityPaused, activity);
        }

        @Override
        public void onActivityStopped(Activity activity) {
            QDActivityManager.getInstance().onActivityStopped(activity);

            record(LifecycleType.onActivityStopped, activity);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            record(LifecycleType.onActivitySaveInstanceState, activity);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            QDActivityManager.getInstance().removeActivityFormStack(activity);

            record(LifecycleType.onActivityDestroyed, activity);
        }

        private void record(LifecycleType lifecycleType, Activity activity) {
            QDLogger.d(TAG.ACTIVITY, lifecycleType + " ==> [" + activity + "]");
            LifecycleRecorder.record(lifecycleType, activity);
        }
    };

    /**
     * 页面关闭
     * 释放一些资源
     * @param Object
     */
    public static void onFinishActivityOrFragment(Object obj) {

        Field[] fields = obj.getClass().getDeclaredFields();
        QDLogger.i("acitivity属性个数：" + fields.length);
        for (int i = 0, len = fields.length; i < len; i++) {
            // 对于每个属性，获取属性名
            String varName = fields[i].getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o;
                try {
                    o = fields[i].get(obj);
                    if (o != null) {
                        QDLogger.d("变量：" + varName + " = " + o);
                        if(o instanceof OnReleaseListener){
                            QDLogger.d("释放 OnReleaseListener：" + varName);
                            ((OnReleaseListener) o).onRelease();
                        }else
                        if (o instanceof Handler) {
                            QDLogger.d("释放handler：" + varName);
                            ((Handler) o).removeCallbacksAndMessages(null);
                        } else if (o instanceof Dialog) {
                            QDLogger.d("释放dialog：" + varName);
                            ((Dialog) o).dismiss();
                        }  else if (o instanceof PopupWindow) {
                            QDLogger.d("释放PopupWindow：" + varName);
                            ((PopupWindow) o).dismiss();
                        } else if (o instanceof Bitmap) {
                            QDLogger.d("释放Bitmap：" + varName);
                                ((Bitmap) o).recycle();
                        }else if (o instanceof View) {
                            QDLogger.d("释放View：" + varName);
                            ((View) o).clearAnimation();
                        }else if (o instanceof Animator) {
                            QDLogger.d("释放Animator：" + varName);
                            ((Animator) o).cancel();
                        }
                    }
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
    }

    //必须要在application里初始化
    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public QDActivityManager init(Context context) {
        this.context = context.getApplicationContext();
        //注冊activity监听器
        try {
            //注册
            ((Application) this.context).unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((Application) this.context).registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        return instance;
    }

    public static QDActivityManager getInstance() {
        if (instance == null) {
            instance = new QDActivityManager();
        }
        return instance;
    }

    /**
     * 添加activity到棧中
     *
     * @param activity
     */
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        if (!activityStack.contains(activity)) {
            activityStack.add(activity);
        }
    }

    /**
     * 返回上一个 activity
     */
   /* public void popActivity() {
        if (activityStack == null) {
            activityStack = new Stack<>();
            return;
        }

        if (activityStack.isEmpty()) {
            return;
        }

        QDLogger.e("QDACTIVITY_","准备出栈："+getCurrentActivity().getClass().getName());
        Activity activity = activityStack.pop();
        QDLogger.e("QDACTIVITY_","出栈 ："+activity.getClass().getName());
        if (activity != null) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }*/

    /**
     * 把指定activity从栈中移除，并销毁该页面
     *
     * @param activity 要移除的activity
     */
    public void popActivity(Activity activity) {
        if (activity == null || activityStack == null || activityStack.isEmpty()) {
            return;
        }

        activityStack.remove(activity);
        //QDLogger.e("QDACTIVITY_","出栈移除："+activity.getClass().getName());
        if (!activity.isFinishing()) {
            activity.finish();
        }
    }

    /**
     * 从栈中移除activity，但不负责销毁
     *
     * @param activity
     */
    public void removeActivityFormStack(Activity activity) {
        if (activityStack == null || activityStack.isEmpty()) {
            return;
        }

        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 弹出其他所有activity，仅保留指定activity
     *
     * @param targetActivity 要保留的activity类
     */
    public void popOtherActivityExceptOne(Class targetActivity) {
        int c = activityStack.size();
        for (int i = 0; i < c; i++) {
            Activity activity = activityStack.get(i);
            if (activity != null && !activity.getClass().equals(targetActivity)) {
                popActivity(activity);
                popOtherActivityExceptOne(targetActivity);
                return;
            }
        }
    }

    /**
     * 判断画面栈中是否存在该 activity 对象
     *
     * @param activity
     * @return 存在返回TRUE ，不存在返回FALSE
     */
    public boolean containsActivity(Activity activity) {
        if (activityStack == null || !activityStack.isEmpty()) {
            return false;
        }
        return activityStack.contains(activity);
    }

    public boolean containsActivityByClass(Class clazz) {
        if (activityStack == null || !activityStack.isEmpty()) {
            return false;
        }
        int c = activityStack.size();
        boolean b = false;
        for (int i = 0; i < c; i++) {
            Class cla = activityStack.get(i).getClass();
            if (clazz.equals(cla)) {
                b = true;
                break;
            }
        }
        return b;
    }

    /**
     * 弹出其他栈，仅保留某class集合内的activity
     *
     * @param classList 要保留的 activity 类
     */
    public void popOtherActivityExceptList(List<Class> classList) {
        int c = activityStack.size();
        for (int i = 0; i < c; i++) {
            Class claz = activityStack.get(i).getClass();
            if (!classList.contains(claz)) {
                QDLogger.e("QDACTIVITY_", "popOtherActivityExceptList-" + claz);
                popActivity(activityStack.get(i));
                popOtherActivityExceptList(classList);
                return;
            }
        }
    }

    //删除具体的activity
    /*public void removeActivity(Activity activity) {
        if (activity != null) {
            QDLogger.d("QDActivityManager", "removeActivity:" + activity);
            if (currentActivity == activity) {
                currentActivity = null;
            }
            activitys.remove(activity + "");
        }
    }*/

    //删除clazz类的activity
    /*public void deleteActivityByClass(Class clazz) {
        if (clazz != null) {
            for (Map.Entry entry : activitys.entrySet()) {
                Activity activity = ((WeakReference<Activity>) entry.getValue()).get();
                if (activity != null && activity.getClass().equals(clazz)) {
                    activitys.remove(entry.getKey());
                    activity.finish();
                    deleteActivityByClass(clazz);
                    return;
                }
                activitys.remove(entry.getKey() + "");
                deleteActivityByClass(clazz);
                return;
            }
        }
    }*/

    //删除clazzes类的activity
    /*public void deleteActivityByClasses(List<Class> clazz) {
        if (clazz != null) {
            for (Map.Entry entry : activitys.entrySet()) {
                Activity activity = ((WeakReference<Activity>) entry.getValue()).get();
                if (activity != null && clazz.contains(activity.getClass())) {
                    activitys.remove(entry.getKey() + "");
                    activity.finish();
                    deleteActivityByClasses(clazz);
                    return;
                }
                if (activity == null) {
                    activitys.remove(entry.getKey() + "");
                    deleteActivityByClasses(clazz);
                    return;
                }
            }
        }
    }*/

    /**
     * 关闭所有activity
     */
    /*public void deleteAllActivity() {
        for (Map.Entry entry : activitys.entrySet()) {
            Activity activity = ((WeakReference<Activity>) entry.getValue()).get();
            activitys.remove(entry.getKey() + "");
            if (activity != null) {
                activity.finish();
            }
            deleteAllActivity();
            return;
        }
    }*/

    /**
     * 关闭除了a,b,c类，之外的所有activity
     */
    /*public void deleteOtherActivityByClasses(List<Class> clazz) {
        if (clazz != null) {
            for (Map.Entry entry : activitys.entrySet()) {
                Activity activity = ((WeakReference<Activity>) entry.getValue()).get();
                if (activity != null && !clazz.contains(activity.getClass())) {
                    activitys.remove(entry.getKey() + "");
                    activity.finish();
                    deleteOtherActivityByClasses(clazz);
                    return;
                } else if (activity == null) {
                    activitys.remove(entry.getKey() + "");
                    deleteOtherActivityByClasses(clazz);
                    return;
                }
            }
        }
    }*/

    /**
     * 关闭除了a list类，之外的所有activity
     */
    /*public void deleteOtherActivityByClass(List<Class> clazzs) {
        if (clazzs != null) {
            for (Map.Entry entry : activitys.entrySet()) {
                Activity activity = ((WeakReference<Activity>) entry.getValue()).get();
                if (activity != null && !clazzs.contains(activity.getClass())) {
                    activitys.remove(entry.getKey() + "");
                    activity.finish();
                    deleteOtherActivityByClass(clazzs);
                    return;
                } else if (activity == null) {
                    activitys.remove(entry.getKey() + "");
                    deleteOtherActivityByClass(clazzs);
                    return;
                }
            }
        }
    }*/

    /**
     * 关闭除了a类，之外的所有activity
     */
    /*public void deleteOtherActivityByClass(Class clazz) {
        if (clazz != null) {
            // android.app.QDActivityManager mActivityManager = (android.app.QDActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (Map.Entry entry : activitys.entrySet()) {
                Activity activity = ((WeakReference<Activity>) entry.getValue()).get();
                if (activity != null && !activity.getClass().equals(clazz)) {
                    activitys.remove(entry.getKey());
                    activity.finish();
                    //QDLogger.d("移除"+activity.getClass().getName());
                    deleteOtherActivityByClass(clazz);
                    return;
                } else if (activity == null) {
                    activitys.remove(entry.getKey());
                    deleteOtherActivityByClass(clazz);
                    return;
                }
            }
        }
    }*/

    /**
     * 关闭除了a之外的所有activity
     */
    /*public void deleteOtherActivity(Activity activity) {
        if (activity != null) {
            for (Map.Entry entry : activitys.entrySet()) {
                Activity activityT = ((WeakReference<Activity>) entry.getValue()).get();
                if (activityT != null && !activity.equals(activityT)) {
                    activitys.remove(entry.getKey() + "");
                    activityT.finish();
                    deleteOtherActivity(activity);
                    return;
                } else if (activityT == null) {
                    activitys.remove(entry.getKey() + "");
                    deleteOtherActivity(activity);
                    return;
                }
            }
        }
    }*/
    public void onActivityResumed(Activity activity) {
        onStateChanged(activity, true);
    }

    public void onActivityStopped(Activity activity) {
        onStateChanged(activity, false);
    }

    /**
     * 设置app前后台切换事件
     */
    private void onStateChanged(Activity activity, boolean b) {
        if (onAppRunStateChangedListenner != null) {
            if (getCurrentActivity() != null) {
                // ActivityManager mAm = (ActivityManager) currentActivity.getSystemService(ACTIVITY_SERVICE);
                // String activity_name = mAm.getRunningTasks(1).get(0).topActivity.getPackageName();
                if (!isRunningOnForeground(context)) {
                    if (isOnForground) {
                        isOnForground = false;
                        onAppRunStateChangedListenner.onBackground();
                    }
                } else {
                    if (!isOnForground) {
                        isOnForground = true;
                        onAppRunStateChangedListenner.onForeground();
                    }
                }
            }
        }
    }

    boolean isOnForground = true;

    /**
     * TODO 待驗證first
     * Activity是否在前台
     * @param context
     * @return
     */
    public boolean isRunningOnForeground2(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
        if (appProcessInfoList == null) {
            QDLogger.e("appProcessInfoList = null");
            return false;
        }

        String processName = context.getApplicationInfo().processName;
        for (ActivityManager.RunningAppProcessInfo processInfo : appProcessInfoList) {
            QDLogger.i("" + processInfo.processName + "," + processInfo.importance);
            if (processInfo.processName.equals(processName) && processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 判断应用是否在前台运行
     *
     * @param context
     * @return
     */
    public boolean isRunningOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        if (activityManager.getRunningTasks(1) != null || activityManager.getRunningTasks(1).size() == 1) {
            ComponentName componentName = activityManager.getRunningTasks(1).get(0).topActivity;
            String currentPackageName = componentName.getPackageName();
            //QDLogger.i("currentPackageName=" + currentPackageName + ",mypid=" + android.os.Process.myPid());
            if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
                // QDLogger.i("APP在前台运行,context.pid=" + android.os.Process.myPid());
                return true;
            }
        }
        // List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        // 枚举进程
        /*for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            //QDLogger.i("isRunningOnForeground="+appProcessInfo.processName+appProcessInfo.importance );
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                //ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE
                if (appProcessInfo.processName.equals(context.getApplicationInfo().processName)) {
                    QDLogger.i("isRunningOnForeground=" + true+",appProcessInfo.pid="+appProcessInfo.pid+",context.pid="+android.os.Process.myPid());
                    return true;
                }
            }
        }*/
        //QDLogger.i("isRunningOnForeground="+false);
        return false;
    }

    private boolean isForeground(Context context) {
        if (context != null) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : processes) {
                if (processInfo.processName.equals(context.getPackageName())) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return true;
                    }
                }
            }
        }
        return false;
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
        onStateChanged(activity, false);
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
                        System.out.println(packName + " has been killed");
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
        android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
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

    /**
     * 判断activity是否已经启动
     *
     * @param activityClass
     * @return true已启动，false未启动
     */
    public boolean containsActivity(Class<?> activityClass) {
        Intent intent = new Intent(context, activityClass);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity    
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);//
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

    /**
     * 返回到app，如果当前app在前台不会需要执行
     *
     * @param context
     * @param targetActivityClass 如果不在前台，则打开目标页面
     */
    public void backToApp(Context context, Class targetActivityClass) {
        if (!isRunningOnForeground(context)) {
            backToActivity(context, targetActivityClass);
            Activity activity = QDActivityManager.getInstance().getCurrentActivity();
            //若没有找到运行的task，用户结束了task或被系统释放，则重新启动mainactivity
            if (activity == null || !activity.getClass().getName().equals(targetActivityClass.getName())) {
                Intent resultIntent = new Intent(context, targetActivityClass);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(resultIntent);
            }
        }
    }

    /**
     * 返回到指定页面
     *
     * @param context
     * @param activityClass
     */
    public void backToActivity(Context context, Class activityClass) {
        //获取ActivityManager
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        //获得当前运行的task
        List<ActivityManager.RunningTaskInfo> taskList = activityManager.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo runningTaskInfo : taskList) {
            //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
            if (runningTaskInfo.topActivity.getPackageName().equals(context.getPackageName())) {
                activityManager.moveTaskToFront(runningTaskInfo.id, 0);
                return;
            }
        }
        //若没有找到运行的task，用户结束了task或被系统释放，则重新启动mainactivity
        Intent resultIntent = new Intent(context, activityClass);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(resultIntent);
    }

    /**
     * 获取当前的 activity
     *
     * @return act
     */
    public Activity getCurrentActivity() {
        if (activityStack == null || activityStack.isEmpty()) {
            return null;
        }
        return activityStack.lastElement();
    }
}
