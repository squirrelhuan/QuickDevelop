package cn.demomaster.huan.quickdeveloplibrary.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.constant.TAG;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDActivityManager;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

public class QDActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks{

    private static QDActivityLifecycleCallbacks instance;
    public static QDActivityLifecycleCallbacks getInstance() {
        if(instance==null){
            instance = new QDActivityLifecycleCallbacks();
        }
        return instance;
    }

    /**
     * 私有化處理
     */
    private QDActivityLifecycleCallbacks(){

    }

    HashMap<Integer,Application.ActivityLifecycleCallbacks> activityLifecycleCallbackMap = new HashMap<>();

    /**
     * 添加activity生命周期监听器
     * @param activityLifecycleCallbackMap
     */
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks activityLifecycleCallbackMap) {
        this.activityLifecycleCallbackMap.remove(activityLifecycleCallbackMap);
        this.activityLifecycleCallbackMap.put(activityLifecycleCallbackMap.hashCode(),activityLifecycleCallbackMap);
    }

    /**
     * 移除监听回调
     * @param activityLifecycleCallbackMap
     */
    public void unregisterActivityLifecycleCallback(Application.ActivityLifecycleCallbacks activityLifecycleCallbackMap) {
        this.activityLifecycleCallbackMap.remove(activityLifecycleCallbackMap);
    }
    /**
     * 移除监听回调
     * @param hashcode
     */
    public void unregisterActivityLifecycleCallback(int hashcode) {
        this.activityLifecycleCallbackMap.remove(hashcode);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        QDActivityManager.getInstance().pushActivity(activity);

        if(activityLifecycleCallbackMap!=null){
            for(Map.Entry entry:activityLifecycleCallbackMap.entrySet()){
                if(entry!=null){
                    Application.ActivityLifecycleCallbacks callbacks = (Application.ActivityLifecycleCallbacks) entry.getValue();
                    if(callbacks!=null){
                        callbacks.onActivityCreated(activity,savedInstanceState);
                    }
                }
            }
        }
        record(LifecycleType.onActivityCreated,activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if(activityLifecycleCallbackMap!=null){
            for(Map.Entry entry:activityLifecycleCallbackMap.entrySet()){
                if(entry!=null){
                    Application.ActivityLifecycleCallbacks callbacks = (Application.ActivityLifecycleCallbacks) entry.getValue();
                    if(callbacks!=null){
                        callbacks.onActivityStarted(activity);
                    }
                }
            }
        }
        record(LifecycleType.onActivityStarted,activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        QDActivityManager.getInstance().onActivityResumed(activity);
        if(activityLifecycleCallbackMap!=null){
            for(Map.Entry entry:activityLifecycleCallbackMap.entrySet()){
                if(entry!=null){
                    Application.ActivityLifecycleCallbacks callbacks = (Application.ActivityLifecycleCallbacks) entry.getValue();
                    if(callbacks!=null){
                        callbacks.onActivityResumed(activity);
                    }
                }
            }
        }
        record(LifecycleType.onActivityResumed,activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        QDActivityManager.getInstance().onActivityPaused(activity);
        if(activityLifecycleCallbackMap!=null){
            for(Map.Entry entry:activityLifecycleCallbackMap.entrySet()){
                if(entry!=null){
                    Application.ActivityLifecycleCallbacks callbacks = (Application.ActivityLifecycleCallbacks) entry.getValue();
                    if(callbacks!=null){
                        callbacks.onActivityPaused(activity);
                    }
                }
            }
        }
        record(LifecycleType.onActivityPaused,activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        QDActivityManager.getInstance().onActivityStopped(activity);
        if(activityLifecycleCallbackMap!=null){
            for(Map.Entry entry:activityLifecycleCallbackMap.entrySet()){
                if(entry!=null){
                    Application.ActivityLifecycleCallbacks callbacks = (Application.ActivityLifecycleCallbacks) entry.getValue();
                    if(callbacks!=null){
                        callbacks.onActivityStopped(activity);
                    }
                }
            }
        }
        record(LifecycleType.onActivityStopped,activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        if(activityLifecycleCallbackMap!=null){
            for(Map.Entry entry:activityLifecycleCallbackMap.entrySet()){
                if(entry!=null){
                    Application.ActivityLifecycleCallbacks callbacks = (Application.ActivityLifecycleCallbacks) entry.getValue();
                    if(callbacks!=null){
                        callbacks.onActivitySaveInstanceState(activity,outState);
                    }
                }
            }
        }
        record(LifecycleType.onActivitySaveInstanceState,activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        QDActivityManager.getInstance().removeActivityFormStack(activity);
        if(activityLifecycleCallbackMap!=null){
            for(Map.Entry entry:activityLifecycleCallbackMap.entrySet()){
                if(entry!=null){
                    Application.ActivityLifecycleCallbacks callbacks = (Application.ActivityLifecycleCallbacks) entry.getValue();
                    if(callbacks!=null){
                        callbacks.onActivityDestroyed(activity);
                    }
                }
            }
        }
        record(LifecycleType.onActivityDestroyed,activity);
    }

    private void record(LifecycleType lifecycleType, Activity activity) {
        QDLogger.d(TAG.ACTIVITY, lifecycleType+" ==> [" + activity + "]");
        LifecycleRecorder.record(lifecycleType,activity);
    }

    /**
     * 初始化
     * @param context
     */
    public void init(Application context) {
        try {
            //注册
            context.unregisterActivityLifecycleCallbacks(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        context.registerActivityLifecycleCallbacks(this);
    }
}
