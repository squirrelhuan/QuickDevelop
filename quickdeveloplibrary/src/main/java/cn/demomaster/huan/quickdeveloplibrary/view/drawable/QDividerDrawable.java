package cn.demomaster.huan.quickdeveloplibrary.view.drawable;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Squirrel桓 on 2019/1/6.
 */
public class QDividerDrawable extends GradientDrawable {

    private int mStrokeWidth = 1;
    private int mStrokeColors = Color.GRAY;
    private int backGroundColor = Color.TRANSPARENT;
    /**
     * 圆角大小是否自适应为 View 的高度的一般
     */
    private boolean isRadiusAuto = false;
    private List<Gravity> gravityList;

    public QDividerDrawable() {
        init();
    }

    public QDividerDrawable(Gravity... gravitys) {
        this.gravityList = new ArrayList<>();
        for(Gravity gravity :gravitys){
            this.gravityList.add(gravity);
        }
        init();
    }

    public QDividerDrawable(int strokeWidth, int color) {
        this.mStrokeColors = color;
        this.mStrokeWidth = strokeWidth;
        init();
    }

    private void init() {
        if(gravityList==null){
            gravityList = new ArrayList<>();
            gravityList.add(Gravity.ALL);
        }

        //设置背景色
        setColor(backGroundColor);
        //设置边框的厚度以及边框的颜色
        setStroke(mStrokeWidth, mStrokeColors);
        //设置圆角的半径  当然也是可以一个个设置圆角的半径
        setCornerRadius(0);
        //设置背景的形状，默认就是矩形，跟xml文件中类型android:shape的值保持一致，具体有：GradientDrawable.LINE  GradientDrawable.OVAL GradientDrawable.RECTANGLE  GradientDrawable.RING
        setShape(GradientDrawable.RECTANGLE);


    }

    @Override
    public void setCornerRadii( float[] radii) {
        super.setCornerRadii(radii);
        setRadiusAuto(false);
    }

    @Override
    public void setCornerRadius(float radius) {
        super.setCornerRadius(radius);
        setRadiusAuto(false);
    }

    @Override
    protected void onBoundsChange(Rect r) {
        r.left = r.left - mStrokeWidth;
        r.top = r.top - mStrokeWidth;
        r.right = r.right + mStrokeWidth;
        r.bottom = r.bottom + mStrokeWidth;
        if(gravityList.contains(Gravity.LEFT)){//左边
            r.left = r.left + mStrokeWidth;
        }
        if(gravityList.contains(Gravity.TOP)){//上边
            r.top = r.top + mStrokeWidth;
        }
        if(gravityList.contains(Gravity.RIGHT)){//右边
            r.right = r.right - mStrokeWidth;
        }
        if(gravityList.contains(Gravity.BOTTOM)){//下边
            r.bottom = r.bottom - mStrokeWidth;
        }
        if(gravityList.contains(Gravity.VERTICAL)){//上下
            r.top = r.top + mStrokeWidth;
            r.bottom = r.bottom - mStrokeWidth;
        }
        if(gravityList.contains(Gravity.HORIZONTAL)){//左右
            r.left = r.left + mStrokeWidth;
            r.right = r.right - mStrokeWidth;
        }
        if(gravityList.contains(Gravity.LEFTTOP)){//左上
            r.left = r.left + mStrokeWidth;
            r.top = r.top + mStrokeWidth;
        }
        if(gravityList.contains(Gravity.LEFTRIGHT)){//左下
            r.left = r.left + mStrokeWidth;
            r.bottom = r.bottom - mStrokeWidth;
        }
        if(gravityList.contains(Gravity.RIGHTTOP)){//右上
            r.right = r.right - mStrokeWidth;
            r.top = r.top + mStrokeWidth;
        }
        if(gravityList.contains(Gravity.RIGHTBOTTOM)){//右下
            r.right = r.right - mStrokeWidth;
            r.bottom = r.bottom - mStrokeWidth;
        }
        if(gravityList.contains(Gravity.BESIDES_LEFT)){//非左
            r.top = r.top + mStrokeWidth;
            r.right = r.right - mStrokeWidth;
            r.bottom = r.bottom - mStrokeWidth;
        }
        if(gravityList.contains(Gravity.BESIDES_TOP)){//非上
            r.left = r.left + mStrokeWidth;
            r.right = r.right - mStrokeWidth;
            r.bottom = r.bottom - mStrokeWidth;
        }
        if(gravityList.contains(Gravity.BESIDES_RIGHT)){//非右
            r.top = r.top + mStrokeWidth;
            r.left = r.left + mStrokeWidth;
            r.bottom = r.bottom - mStrokeWidth;
        }
        if(gravityList.contains(Gravity.BESIDES_BOTTOM)){//非下
            r.top = r.top + mStrokeWidth;
            r.left = r.left + mStrokeWidth;
            r.right = r.right - mStrokeWidth;
        }
        if(gravityList.contains(Gravity.ALL)){//所有边
            r.left = r.left + mStrokeWidth;
            r.top = r.top + mStrokeWidth;
            r.right = r.right - mStrokeWidth;
            r.bottom = r.bottom - mStrokeWidth;
        }

        super.onBoundsChange(r);
        if (isRadiusAuto) {
            // 修改圆角为短边的一半
            setCornerRadius(Math.min(r.width(), r.height()) / 2);
        }
    }

    public static enum  Gravity{
        ALL,NONE,LEFT,TOP,RIGHT,BOTTOM,VERTICAL,HORIZONTAL,LEFTTOP,LEFTRIGHT,RIGHTTOP,RIGHTBOTTOM,BESIDES_LEFT,BESIDES_TOP,BESIDES_RIGHT,BESIDES_BOTTOM
    }


    public int getmStrokeWidth() {
        return mStrokeWidth;
    }

    public void setmStrokeWidth(int mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
    }

    public int getmStrokeColors() {
        return mStrokeColors;
    }

    public void setmStrokeColors(int mStrokeColors) {
        this.mStrokeColors = mStrokeColors;
        //设置边框的厚度以及边框的颜色
        setStroke(mStrokeWidth, mStrokeColors);
    }

    public int getBackGroundColor() {
        return backGroundColor;
    }

    public void setBackGroundColor(int backGroundColor) {
        this.backGroundColor = backGroundColor;
        //设置背景色
        setColor(backGroundColor);
    }

    public List<Gravity> getGravityList() {
        return gravityList;
    }

    public void setGravityList(List<Gravity> gravityList) {
        this.gravityList = gravityList;
    }

    public boolean isRadiusAuto() {
        return isRadiusAuto;
    }

    public void setRadiusAuto(boolean radiusAuto) {
        isRadiusAuto = radiusAuto;
    }
}
