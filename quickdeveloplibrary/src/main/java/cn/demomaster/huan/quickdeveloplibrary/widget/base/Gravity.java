package cn.demomaster.huan.quickdeveloplibrary.widget.base;

import cn.demomaster.qdlogger_library.QDLogger;

public enum Gravity {
    LEFT(android.view.Gravity.LEFT),
    RIGHT(android.view.Gravity.RIGHT),
    TOP(android.view.Gravity.TOP),
    BOTTOM(android.view.Gravity.BOTTOM),
    CENTER(android.view.Gravity.CENTER);
    
    private int value = 0;
    public int value() {
        return this.value;
    }
    Gravity(int value) {
        this.value = value;
    }

    public static Gravity getEnum(int value) {
        Gravity[] enumArray = Gravity.values();
        for (Gravity gravity : enumArray) {
            if (gravity.value() == value) {
                return gravity;
            }
        }
        QDLogger.println("未找到 value="+value);
        return null;
    }
}
