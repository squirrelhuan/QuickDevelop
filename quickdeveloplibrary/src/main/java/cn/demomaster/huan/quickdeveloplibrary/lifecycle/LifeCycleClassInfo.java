package cn.demomaster.huan.quickdeveloplibrary.lifecycle;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class LifeCycleClassInfo {
    long creatTime;
    Class clazz;
    int clazzHashCode;

    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public int getClazzHashCode() {
        return clazzHashCode;
    }

    public void setClazzHashCode(int clazzHashCode) {
        this.clazzHashCode = clazzHashCode;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LifeCycleClassInfo that = (LifeCycleClassInfo) o;
        return clazzHashCode == that.clazzHashCode &&
                Objects.equals(clazz, that.clazz);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(clazz, clazzHashCode);
    }
}
