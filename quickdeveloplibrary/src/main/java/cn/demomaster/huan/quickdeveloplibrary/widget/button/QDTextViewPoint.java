package cn.demomaster.huan.quickdeveloplibrary.widget.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDividerDrawable;

import static cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityRoot.TAG;

/**
 * @author squirrel桓
 * @date 2019/1/11.
 * description：
 */
public class QDTextViewPoint extends AppCompatTextView {
    public QDTextViewPoint(Context context) {
        super(context);
    }

    public QDTextViewPoint(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public QDTextViewPoint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QDTextViewPoint);
        pointWidth = array.getDimension(R.styleable.QDTextViewPoint_pointWidth, 0);
        pointMargin = array.getDimension(R.styleable.QDTextViewPoint_pointMargin, 0);
        pointText = array.getString(R.styleable.QDTextViewPoint_pointText);
        pointBackgroundColor = array.getColor(R.styleable.QDTextViewPoint_pointBackgroundColor, 0);
        pointTextSize = array.getDimension(R.styleable.QDTextViewPoint_pointTextSize, 0);
        pointTextColor = array.getColor(R.styleable.QDTextViewPoint_pointTextColor, 0);
        pointGravity = array.getInt(R.styleable.QDTextViewPoint_pointGravity, 0);
        showPoint = array.getBoolean(R.styleable.QDTextViewPoint_showPoint, true);
        array.recycle();

        Log.i(TAG, "pointWidth: " + pointWidth);
        Log.i(TAG, "pointMargin: " + pointMargin);
        Log.i(TAG, "width: " + width);
        Log.i(TAG, "pointText: " + pointText);
        Log.i(TAG, "pointGravity: " + pointGravity);
        Log.i(TAG, "showPoint: " + showPoint);
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
        if (showPoint) {
            initPaint();
            drawPoint(canvas);
        }
    }

    private Paint paint;
    private Point point;
    private boolean showPoint =true;
    private String pointText = "";
    private float pointMargin;
    private int pointGravity = 0;
    private float pointTextSize = 0;
    private float pointWidth = 50;
    private int pointBackgroundColor = Color.RED;
    private int pointTextColor = Color.WHITE;
    private float pointRadius = 0;

     /*<enum name="leftTop" value="0"/>
            <enum name="leftBottom" value="1"/>
            <enum name="rightTop" value="2"/>
            <enum name="rightBottom" value="3"/>*/
    private void initPaint() {
        paint = new Paint();
        paint.setTextSize(pointTextSize);
        pointRadius= pointWidth/2;
        int x = 0, y = 0;
        if (pointGravity == 0) {//leftTop
            x = (int) (pointWidth / 2);
            y = (int) pointWidth/2;
        }
        if (pointGravity == 1) {//leftBottom
            x = (int) (pointWidth / 2);
            y = (int) (height-pointWidth/2);
        }
        if (pointGravity == 2) {//rightTop
            x = (int) (width - pointWidth / 2);
            y = (int) pointWidth/2;
        }
        if (pointGravity == 3) {//rightBottom
            x = (int) (width - pointWidth / 2);
            y = (int) (height-pointWidth/2);
        }
        point = new Point(x, y);
    }

    private void drawPoint(Canvas canvas) {
        // 文字宽
        float textWidth = paint.measureText(pointText);
        // 文字baseline在y轴方向的位置
        float baseLineY = Math.abs(paint.ascent() + paint.descent()) / 2;

        paint.setColor(pointBackgroundColor);
        canvas.drawCircle(point.x, point.y, pointRadius, paint);
        paint.setColor(pointTextColor);
        canvas.drawText(pointText,point.x- textWidth / 2,point.y + baseLineY,paint );
    }


}
