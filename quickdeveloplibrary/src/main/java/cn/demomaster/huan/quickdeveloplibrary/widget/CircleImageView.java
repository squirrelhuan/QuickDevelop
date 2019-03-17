package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by huan on 2017/11/14.
 */

public class CircleImageView extends AppCompatImageView {

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean isRound = true;
    private float minWidth = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        if (isRound) {
            Path path = new Path();
            double raduis = Math.min(getHeight(),getWidth())/2;
            //Log.e("CGQ", "raduis=" + raduis + ",Height = "+getHeight()+",Width="+getWidth());
            //按照逆时针方向添加一个圆
            path.addCircle(getWidth()/2, getHeight()/2, (float) (raduis ), Path.Direction.CCW);
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
