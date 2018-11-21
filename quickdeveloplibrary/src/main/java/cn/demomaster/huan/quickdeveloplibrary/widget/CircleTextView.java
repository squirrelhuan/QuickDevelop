package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author squirrel桓
 * @date 2018/11/21.
 * description：
 */
@SuppressLint("AppCompatCustomView")
public class CircleTextView extends TextView {
    public CircleTextView(Context context) {
        super(context);
    }

    public CircleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    private boolean isRound = true;
    private int backgroundColor = Color.GREEN;
    //private int textColor = Color.WHITE;


    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //this.setTextColor(textColor);
        if (isRound) {
            Path path = new Path();
            double raduis = Math.min(getHeight(),getWidth())/2;
            //Log.e("CGQ", "raduis=" + raduis + ",Height = "+getHeight()+",Width="+getWidth());
            //按照逆时针方向添加一个圆
            //path.addCircle(getWidth()/2, getHeight()/2, (float) (raduis ), Path.Direction.CCW);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(backgroundColor);
            RectF rectF = new RectF(0,0,width,height);
            canvas.drawRoundRect(rectF, height/2, height/2, paint);
            path.addRoundRect(rectF,height/2, height/2, Path.Direction.CCW);
            //先将canvas保存
            canvas.save();
            //设置为在圆形区域内绘制
            canvas.clipPath(path);
            //绘制Bitmap
            //canvas.drawBitmap(targetView.mBitmap, 0, 0, paint);
            super.onDraw(canvas); //恢复Canvas
            canvas.restore();
        } else {
            super.onDraw(canvas);
        }
    }

}
