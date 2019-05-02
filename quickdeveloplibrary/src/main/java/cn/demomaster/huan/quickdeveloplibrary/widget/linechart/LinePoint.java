package cn.demomaster.huan.quickdeveloplibrary.widget.linechart;

import java.io.Serializable;

public class LinePoint implements Serializable {
    private float x;
    private float y;

    public LinePoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
