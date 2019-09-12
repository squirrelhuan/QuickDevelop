package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author squirrel桓
 * @date 2018/12/27.
 * description：
 */
public class ActivityManager {
    private static ActivityManager instance;
    private static Context context;
    //本地activity栈
    private List<Activity> activitys = new ArrayList<Activity>();

    public ActivityManager(Context context) {
        this.context = context.getApplicationContext();
    }

    //必须要在application里初始化
    public static ActivityManager init(Context context) {
        if (instance == null) {
            instance = new ActivityManager(context);
        }
        return instance;
    }

    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager(context);
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        activitys.add(activity);
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
            activitys.remove(activity);
        }
    }

    //删除clazz类的activity
    public void deleteActivityByClass(Class clazz) {
        if (clazz != null) {
            for (Activity activity : activitys) {
                if (activity.getClass().equals(clazz)) {
                    activitys.remove(activity);
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
            for (Activity activity : activitys) {
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
            for (Activity activity : activitys) {
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
            for (Activity activity : activitys) {
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
            for (Activity activity : activitys) {
                if (!activity.getClass().equals(clazz)) {
                    activity.finish();
                    activitys.remove(activity);
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
    private Activity currentActivity_tmp;
    public void setCurrentActivity(Activity tmp) {
        currentActivity_tmp = tmp;
        handler.removeCallbacks(runnable);
        if(tmp==null&&currentActivity!=null){
            runnable.setActivity(tmp);
            handler.postDelayed(runnable,500);//延迟过滤app页面切换过程中的制空
        }else if(tmp!=null&&currentActivity==null){
            currentActivity = tmp;
            onStateChanged();
        }
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    Handler handler = new Handler();
    ARunnable runnable = new ARunnable() {
        @Override
        public void run() {
            if (currentActivity_tmp == null) {
                currentActivity=activityRef.get();
                handler.removeCallbacks(runnable);
                onStateChanged();
            }
        }
    };

    /**
     * 设置app前后台切换事件
     */
    private void onStateChanged() {
        if (onAppRunStateChangedListenner != null) {
            if (currentActivity==null) {
                onAppRunStateChangedListenner.onBackground();
            } else {
                onAppRunStateChangedListenner.onForeground();
            }
        }
    }

    OnAppRunStateChangedListenner onAppRunStateChangedListenner;

    /**
     * 设置app前后台运行切换状态监听
     * @param onAppRunStateChangedListenner
     */
    public void setOnAppRunStateChangedListenner(OnAppRunStateChangedListenner onAppRunStateChangedListenner) {
        this.onAppRunStateChangedListenner = onAppRunStateChangedListenner;
    }

    public static interface OnAppRunStateChangedListenner {
        void onForeground();//前台显示
        void onBackground();//后台显示
    }

    public static class ARunnable implements Runnable{
        public SoftReference<Activity>  activityRef;
        public void setActivity(Activity activity) {
           this.activityRef = new SoftReference<>(activity);
        }

        @Override
        public void run() {

        }
    }
}
