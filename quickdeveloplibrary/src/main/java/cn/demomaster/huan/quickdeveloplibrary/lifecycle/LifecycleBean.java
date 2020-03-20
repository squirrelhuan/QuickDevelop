package cn.demomaster.huan.quickdeveloplibrary.lifecycle;

import android.text.TextUtils;

import androidx.annotation.NonNull;

public class LifecycleBean {
    private LifecycleType lifecycleType;
    private String activityClass;
    private int activityHashCode;
    private long time;

    public LifecycleBean(LifecycleType lifecycleType, String activityClass) {
    }

    public LifecycleBean(LifecycleType lifecycleType, String activityClass, int hashCode) {
        this.lifecycleType = lifecycleType;
        this.activityClass = activityClass;
        this.activityHashCode = hashCode;
        this.time = System.currentTimeMillis();
    }

    public LifecycleType getLifecycleType() {
        return lifecycleType;
    }

    public void setLifecycleType(LifecycleType lifecycleType) {
        this.lifecycleType = lifecycleType;
    }

    public String getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(String activityClass) {
        this.activityClass = activityClass;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @NonNull
    @Override
    public String toString() {
        //return super.toString();
        String activity = null;
        if(!TextUtils.isEmpty(activityClass)){
           String[] strs = activityClass.split("\\.");
           if(strs!=null&&strs.length>2) {
               activity = strs[strs.length - 1];
           }
        }
        return activity+"@"+activityHashCode+"-"+lifecycleType+"-"+time;
    }
}
