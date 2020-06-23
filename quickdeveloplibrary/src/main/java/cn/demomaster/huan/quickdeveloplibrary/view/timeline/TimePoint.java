package cn.demomaster.huan.quickdeveloplibrary.view.timeline;

import android.graphics.Point;

public class TimePoint extends Point {
    boolean isActive;//是否已经激活
    boolean isOrigin;//是起点

    public TimePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public TimePoint(Point src) {
        this.x = src.x;
        this.y = src.y;
    }
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isOrigin() {
        return isOrigin;
    }

    public void setOrigin(boolean origin) {
        isOrigin = origin;
    }
}
