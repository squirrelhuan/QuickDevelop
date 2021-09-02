package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import cn.demomaster.huan.quickdeveloplibrary.R;

/**
 * Created by huan on 2017/11/14.
 */

public class CircleImageView extends AppCompatImageView {

    public CircleImageView(Context context) {
        super(context);
        init(null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    int circle_background_padding;//是否对背景圆角处理
    int circle_background_color;
    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircleImageView);
            circle_background_padding = a.getDimensionPixelSize(R.styleable.CircleImageView_circle_background_padding, 0);
            circle_background_color = a.getColor(R.styleable.CircleImageView_circle_background_color, Color.TRANSPARENT);
            a.recycle();
        }
    }

    private boolean isRound = true;
    @Override
    protected void onDraw(Canvas canvas) {
        if (isRound) {
            Path path = new Path();
            float raduis = (Math.min(getHeight(), getWidth())-circle_background_padding)/2f;
            int centerX = (int) (getWidth() / 2f);
            int centerY = (int) (getHeight() / 2f);
            //Log.e("CGQ", "raduis=" + raduis + ",Height = "+getHeight()+",Width="+getWidth());
            //按照逆时针方向添加一个圆
            path.addCircle(centerX, centerY, raduis, Path.Direction.CCW);
            if (circle_background_color != Color.TRANSPARENT) {
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(circle_background_color);
                canvas.drawCircle(centerX, centerY, raduis, paint);
            }
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
