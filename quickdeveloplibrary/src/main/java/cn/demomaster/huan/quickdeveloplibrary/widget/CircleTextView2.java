package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;

/**
 * @author squirrel桓
 * @date 2018/11/21.
 * description：
 */
@SuppressLint("AppCompatCustomView")
public class CircleTextView2 extends TextView {
    public CircleTextView2(Context context) {
        super(context);
    }

    public CircleTextView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleTextView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int width, height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
    }

    private boolean isRound = true;

    public boolean isRound() {
        return isRound;
    }

    public void setRound(boolean round) {
        isRound = round;
    }

    private int backgroundColor = Color.GREEN;
    //private int textColor = Color.WHITE;
    private int line_width = 3;

    public int getLine_width() {
        return line_width;
    }

    public void setLine_width(int line_width) {
        this.line_width = line_width;
    }

    private boolean usePadding;

    public boolean isUsePadding() {
        return usePadding;
    }

    public void setUsePadding(boolean usePadding) {
        this.usePadding = usePadding;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    private Drawable drawable;

    public void setDrawable(Drawable background) {
        this.drawable = background;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //this.setTextColor(textColor);
        if (isRound) {
            int padding_l = usePadding ? getPaddingLeft() : 0;
            int padding_t = usePadding ? getPaddingTop() : 0;
            int padding_r = usePadding ? getPaddingRight() : 0;
            int padding_b = usePadding ? getPaddingBottom() : 0;

            int rel_height = height - padding_t - padding_b;
            int rel_width = height - padding_l - padding_r;
            int line_w = DisplayUtil.dp2px(getContext(), line_width);

            Path path = new Path();
            //按照逆时针方向添加一个圆
            //path.addCircle(getWidth()/2, getHeight()/2, (float) (raduis ), Path.Direction.CCW);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(getTextColors().getDefaultColor());

            paint.setStrokeWidth(line_width);
            paint.setStyle(Paint.Style.STROKE);
            RectF rectF = new RectF(padding_l, padding_t, rel_width + padding_l, rel_height + padding_t);
            rectF = new RectF(padding_l + line_w, padding_t + line_w, width - line_w - padding_r, height - line_w - padding_b);
            canvas.drawRoundRect(rectF, rel_height / 2f, rel_height / 2f, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(backgroundColor);
            RectF rectF2 = new RectF(padding_l + line_w, padding_t + line_w, width - line_w - padding_r, height - line_w - padding_b);
            canvas.drawRoundRect(rectF2, rel_height / 2f - line_w, rel_height / 2f - line_w, paint);


            path.addRoundRect(rectF, rel_height / 2f, rel_height / 2f, Path.Direction.CCW);
            //先将canvas保存
            canvas.save();
            //设置为在圆形区域内绘制
            canvas.clipPath(path);
            //绘制Bitmap
            //canvas.drawBitmap(targetView.mBitmap, 0, 0, paint);
            super.onDraw(canvas); //恢复Canvas
            canvas.restore();
        } else {
            //canvas.save();
            super.onDraw(canvas);
            //失效了
            //drawable.draw(canvas);
           /* BitmapDrawable bd = (BitmapDrawable) drawable;
            Bitmap bm= bd.getBitmap();*/
            int padding_l = usePadding ? getPaddingLeft() : 0;
            int padding_t = usePadding ? getPaddingTop() : 0;
            int padding_r = usePadding ? getPaddingRight() : 0;
            int padding_b = usePadding ? getPaddingBottom() : 0;
            Rect rect = new Rect(padding_l, padding_t, width - padding_r, height - padding_b);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.setTint(getTextColors().getDefaultColor());
            }
            drawable.setBounds(rect);
            drawable.draw(canvas);
            /*Paint paint = new Paint();
            canvas.drawBitmap(bm,rect,rect,paint);*/
            /*float sx = (float) (width-padding_l-padding_r)/width;
            float sy = (float) (height-padding_t-padding_b)/height;
            canvas.scale(sx,sy);
            canvas.translate(padding_l,padding_t);*/

        }
    }

}
