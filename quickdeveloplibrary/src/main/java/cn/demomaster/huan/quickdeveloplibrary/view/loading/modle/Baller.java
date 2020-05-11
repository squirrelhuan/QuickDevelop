package cn.demomaster.huan.quickdeveloplibrary.view.loading.modle;

/**
 * 圓形，球状
 */
public class Baller extends Obj{
    //
    float radius;//半徑
    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        left = (int) (x-radius/2);
        top = (int) (y-radius/2);
        right = (int) (x+radius/2);
        bottom = (int) (y+radius/2);
    }

    public void setY(int y) {
        this.y = y;
        top = (int) (y-radius/2);
        bottom = (int) (y+radius/2);
    }
    public void setX(int x) {
        this.x = x;
        left = (int) (x-radius/2);
        right = (int) (x+radius/2);
    }

    //在坐标系中的半径
    private double radiusLocal;
    //在坐标系中的角度
    private double degreesLocal;

    public double getRadiusLocal() {
        return radiusLocal;
    }

    public void setRadiusLocal(double radiusLocal) {
        this.radiusLocal = radiusLocal;
    }

    public double getDegreesLocal() {
        return degreesLocal;
    }

    public void setDegreesLocal(double degreesLocal) {
        this.degreesLocal = degreesLocal;
    }
}
