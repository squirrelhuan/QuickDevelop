package cn.demomaster.huan.quickdeveloplibrary.util.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import cn.demomaster.huan.quickdeveloplibrary.lifecycle.LifeCycleEvent;
import cn.demomaster.huan.quickdeveloplibrary.lifecycle.LifecycleTimerData;
import cn.demomaster.huan.quickdeveloplibrary.lifecycle.LifecycleType;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * 生命周期监听器
 */
public class LifecycleManager {
    boolean enable;//是否启用
    Context context;

    public void setEnable(boolean enable) {
        this.enable = enable;
        if (enable) {
            ((Application) context).registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        } else {
            ((Application) context).unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        }
    }

    public boolean isEnable() {
        return enable;
    }

    private static LifecycleManager instance;

    public static LifecycleManager getInstance() {
        if (instance == null) {
            instance = new LifecycleManager();
        }
        return instance;
    }

    private LifecycleManager() {

    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
        lifecycleTimerData = new LifecycleTimerData();
    }

    //生命周期数据
    LifecycleTimerData lifecycleTimerData = null;

    public LifecycleTimerData getLifecycleTimerData() {
        return lifecycleTimerData;
    }


    /**
     * 页面切换监听
     */
    Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            QDLogger.i("onActivityCreated");
            LifeCycleEvent lifeCycleEvent = new LifeCycleEvent();
            lifeCycleEvent.setTime(System.currentTimeMillis());
            lifeCycleEvent.setLifecycleType(LifecycleType.onActivityCreated);
            lifecycleTimerData.addLifecycleEvent(activity, lifeCycleEvent);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            QDLogger.i("onActivityStarted");
            LifeCycleEvent lifeCycleEvent = new LifeCycleEvent();
            lifeCycleEvent.setTime(System.currentTimeMillis());
            lifeCycleEvent.setLifecycleType(LifecycleType.onActivityResumed);
            lifecycleTimerData.addLifecycleEvent(activity, lifeCycleEvent);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            // QDLogger.d("onActivityResumed-----------"+activity.getClass().getName());
            QDLogger.i("onActivityResumed");
            LifeCycleEvent lifeCycleEvent = new LifeCycleEvent();
            lifeCycleEvent.setTime(System.currentTimeMillis());
            lifeCycleEvent.setLifecycleType(LifecycleType.onActivityResumed);
            lifecycleTimerData.addLifecycleEvent(activity, lifeCycleEvent);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            //QDLogger.d("onActivityPaused"+activity.getClass().getName());
            QDLogger.i("onActivityPaused");
            LifeCycleEvent lifeCycleEvent = new LifeCycleEvent();
            lifeCycleEvent.setTime(System.currentTimeMillis());
            lifeCycleEvent.setLifecycleType(LifecycleType.onActivityPaused);
            lifecycleTimerData.addLifecycleEvent(activity, lifeCycleEvent);
        }

        @Override
        public void onActivityStopped(Activity activity) {
            QDLogger.i("onActivityStopped");
            LifeCycleEvent lifeCycleEvent = new LifeCycleEvent();
            lifeCycleEvent.setTime(System.currentTimeMillis());
            lifeCycleEvent.setLifecycleType(LifecycleType.onActivityStopped);
            lifecycleTimerData.addLifecycleEvent(activity, lifeCycleEvent);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            QDLogger.i("onActivityDestroyed");
            LifeCycleEvent lifeCycleEvent = new LifeCycleEvent();
            lifeCycleEvent.setTime(System.currentTimeMillis());
            lifeCycleEvent.setLifecycleType(LifecycleType.onActivityDestroyed);
            lifecycleTimerData.addLifecycleEvent(activity, lifeCycleEvent);
        }
    };
}
