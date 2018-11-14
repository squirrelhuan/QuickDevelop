package cn.demomaster.huan.quickdevelop.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import cn.demomaster.huan.quickdevelop.R;

/**
 * @author squirrel桓
 * @date 2018/11/14.
 * description：
 */
public class CameraCropView extends View {


    public CameraCropView(Context context) {
        super(context);
    }

    public CameraCropView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraCropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int center_x, center_y, mwidth, width, height;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        center_x = width / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);
    }

    private void drawView(Canvas canvas) {
        Paint mPaint=new Paint();  //定义一个Paint
        Shader mShader = new LinearGradient(0,0,40,60,new int[] {Color.RED,Color.GREEN,Color.BLUE},null,Shader.TileMode.REPEAT);
        //新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
        //mPaint.setShader(mShader);
        //mPaint.setShadowLayer(15,100,100,Color.YELLOW);
        //mPaint.setShaderLayer(15,10,10,Color.GRAY);  //第一个参数是阴影扩散半径，紧接着的2个参数是阴影在X和Y方向的偏移量，最后一个参数是颜色

        mPaint.setAntiAlias(true);
        mPaint.setColor(0x88000000);
        canvas.drawRect(0,0,width,height,mPaint);

        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        int panel_width = Math.min(width,height)*11/12;
        int panel_height = panel_width*9/15;
        //canvas.drawRect(((width-panel_width)/2),((height-panel_height)/2),(width-panel_width)/2 + panel_width,(height-panel_height)/2+panel_height,mPaint);
        realLeft = ((width-panel_width)/2);
        realRight = (width-panel_width)/2 + panel_width;
        realTop =((height-panel_height)/4);
        realBottom =(height-panel_height)/4+panel_height;
        RectF oval3 = new RectF(realLeft,realTop,realRight,realBottom);// 设置个新的长方形
        canvas.drawRoundRect(oval3, 20, 20, mPaint);//第二个参数是x半径，第三个参数是y半径

        mPaint.setXfermode(null);
        mPaint.setColor(getResources().getColor(R.color.white));
        mPaint.setTextSize(getContext().getResources().getDimensionPixelSize(R.dimen.quickdev_common_text_size));
        // 文字宽
        float textWidth = mPaint.measureText(tip);
        // 文字baseline在y轴方向的位置
        float baseLineY = Math.abs(mPaint.ascent() + mPaint.descent()) / 2;
        canvas.drawText(tip,getWidth()/2-textWidth/2,realBottom+4*baseLineY,mPaint);

    }

    private String tip="请将卡片边缘对齐方框以便扫描";//提示语

    public void setTip(String tip) {
        this.tip = tip;
        this.postInvalidate();
    }

    private int realTop;
    private int realLeft;
    private int realRight;
    private int realBottom;
    private int realWidth;
    private int realHeight;
    private float percentage_width;
    private float percentage_height;
    private float percentage_top;
    private float percentage_Left;

    public int getRealTop() {
        return realTop;
    }

    public int getRealLeft() {
        return realLeft;
    }

    public int getRealRight() {
        return realRight;
    }

    public int getRealBottom() {
        return realBottom;
    }

    public int getRealWidth() {
        realWidth = realRight- realLeft;
        return realWidth;
    }

    public int getRealHeight() {
        realHeight = realBottom- realTop;
        return realHeight;
    }

    public float getPercentage_width() {
        percentage_width = (float) getRealWidth()/getWidth();
        return percentage_width;
    }

    public float getPercentage_height() {
        percentage_height = (float) getRealHeight()/getHeight();
        return percentage_height;
    }

    public float getPercentage_top() {
        percentage_top=(float) realTop/getHeight();
        return percentage_top;
    }

    public float getPercentage_Left() {
        percentage_Left=(float) realLeft/getWidth();
        return percentage_Left;
    }
}