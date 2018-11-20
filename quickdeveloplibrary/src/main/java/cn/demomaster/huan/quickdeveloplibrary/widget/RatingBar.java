package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import java.util.List;

/**
 * @author squirrel桓
 * @date 2018/11/20.
 * description：
 */
public class RatingBar extends View {


    public RatingBar(Context context) {
        super(context);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
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

    private int startCount = 5;
    private int activateColor=Color.YELLOW,darkColor = Color.LTGRAY;
    private int activateCount = 0;

    public void setColor(int activateColor,int darkColor) {
        this.activateColor = activateColor;
        this.darkColor = darkColor;
        postInvalidate();
    }

    public void setActivateCount(int count){
        this.activateCount = count;
        postInvalidate();
    }

    private void drawView(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.YELLOW);
        //遍历绘制五角星
        for (int i = 0; i < startCount; i++) {
            //一个五角星需要10个定点

            //横向排列的五个星所在的坐标位置gravity：left
            int cell = Math.min(height, width / 5);//最大正方形

            int x1 = i * width / 5;
            int x2 = i * width / 5 + cell;
            int y1 = 0;
            int y2 = cell;

            //最上面的顶点开始
            PointF p1 = new PointF(x1 + cell / 2, y1);
            PointF p2 = new PointF(x1 + cell / 2 + cell * .14f, y1 + cell * .35f);
            PointF p3 = new PointF(x1 + cell / 2 + cell * .5f, y1 + cell * .35f);
            PointF p4 = new PointF(x1 + cell / 2 + cell * .22f, y1 + cell * .6f);
            PointF p5 = new PointF(x1 + cell / 2 + cell * .35f, y1 + cell * 1f);
            PointF p6 = new PointF(x1 + cell / 2, y1 + cell * .72f);
            PointF p7 = new PointF(x1 + cell / 2 - cell * .35f, y1 + cell * 1f);
            PointF p8 = new PointF(x1 + cell / 2 - cell * .22f, y1 + cell * .6f);
            PointF p9 = new PointF(x1 + cell / 2 - cell * .5f, y1 + cell * .35f);
            PointF p10 = new PointF(x1 + cell / 2 - cell * .14f, y1 + cell * .35f);

            Path path = null;
            PointF[] points = {p1, p2, p3, p4, p5, p6, p7, p8, p9, p10};
            for (PointF pointF : points) {
                if (path == null) {
                    path =new Path();
                    path.moveTo(pointF.x, pointF.y);
                } else {
                    path.lineTo(pointF.x, pointF.y);
                }
            }
            path.close();
            if(activateCount>i){
                mPaint.setColor(activateColor);
            }else {
                mPaint.setColor(darkColor);
            }
            canvas.drawPath(path,mPaint);
        }
    }

}
