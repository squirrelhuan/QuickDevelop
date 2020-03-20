package cn.demomaster.huan.quickdeveloplibrary.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import cn.demomaster.huan.quickdeveloplibrary.constant.TAG;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDActivityManager;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

public class QDActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks{
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        QDActivityManager.getInstance().addActivity(activity);
        record(LifecycleType.onActivityCreated,activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        record(LifecycleType.onActivityStarted,activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        QDActivityManager.getInstance().onActivityResumed(activity);
        record(LifecycleType.onActivityResumed,activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        QDActivityManager.getInstance().onActivityPaused(activity);
        record(LifecycleType.onActivityPaused,activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        QDActivityManager.getInstance().onActivityStopped(activity);
        record(LifecycleType.onActivityStopped,activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        record(LifecycleType.onActivitySaveInstanceState,activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        QDActivityManager.getInstance().removeActivity(activity);
        record(LifecycleType.onActivityDestroyed,activity);
    }

    private void record(LifecycleType lifecycleType, Activity activity) {
        QDLogger.d(TAG.ACTIVITY, lifecycleType+" ==> [" + activity + "]");
        LifecycleRecorder.record(lifecycleType,activity);
    }

}
