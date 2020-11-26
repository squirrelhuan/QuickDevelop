package cn.demomaster.huan.quickdeveloplibrary.lifecycle;

public class LifeCycleEvent {
    long time;
    LifecycleType lifecycleType;
    String tag;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public LifecycleType getLifecycleType() {
        return lifecycleType;
    }

    public void setLifecycleType(LifecycleType lifecycleType) {
        this.lifecycleType = lifecycleType;
    }
}
