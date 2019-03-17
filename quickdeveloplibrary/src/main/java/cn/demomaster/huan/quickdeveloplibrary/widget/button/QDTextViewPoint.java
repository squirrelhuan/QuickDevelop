package cn.demomaster.huan.quickdeveloplibrary.widget.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

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
        init(context, attrs);
    }

    public QDTextViewPoint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QDTextViewPoint);

        pointMargin = array.getDimension(R.styleable.QDTextViewPoint_pointMargin, 0);

        if (pointMargin == 0) {
            pointMarginLeft = array.getDimension(R.styleable.QDTextViewPoint_pointMarginLeft, 0);
            pointMarginRight = array.getDimension(R.styleable.QDTextViewPoint_pointMarginRight, 0);
            pointMarginTop = array.getDimension(R.styleable.QDTextViewPoint_pointMarginTop, 0);
            pointMarginBottom = array.getDimension(R.styleable.QDTextViewPoint_pointMarginBottom, 0);
        } else {
            setPointMarginLeft(pointMargin);
            setPointMarginTop(pointMargin);
            setPointMarginRight(pointMargin);
            setPointMarginBottom(pointMargin);
        }

        pointPadding = array.getDimension(R.styleable.QDTextViewPoint_pointPadding, 0);
        if (pointPadding != 0) {
            setPointPaddingLeft(pointPadding);
            setPointPaddingTop(pointPadding);
            setPointPaddingRight(pointPadding);
            setPointPaddingBottom(pointPadding);
        }
        float pointPaddingLeft_value = array.getDimension(R.styleable.QDTextViewPoint_pointPaddingLeft, 0);
        if (pointPaddingLeft_value != 0) {
            pointPaddingLeft = pointPaddingLeft_value;
        }
        float pointPaddingRight_value = array.getDimension(R.styleable.QDTextViewPoint_pointPaddingRight, 0);
        if (pointPaddingRight_value != 0) {
            pointPaddingRight = pointPaddingRight_value;
        }
        float pointPaddingTop_value = array.getDimension(R.styleable.QDTextViewPoint_pointPaddingTop, 0);
        if (pointPaddingTop_value != 0) {
            pointPaddingTop = pointPaddingTop_value;
        }
        float pointPaddingBottom_value = array.getDimension(R.styleable.QDTextViewPoint_pointPaddingBottom, 0);
        if (pointPaddingBottom_value != 0) {
            pointPaddingBottom = pointPaddingBottom_value;
        }


        pointText = array.getString(R.styleable.QDTextViewPoint_pointText);
        pointBackgroundColor = array.getColor(R.styleable.QDTextViewPoint_pointBackgroundColor, 0);
        pointTextSize = array.getDimension(R.styleable.QDTextViewPoint_pointTextSize, 0);
        pointTextColor = array.getColor(R.styleable.QDTextViewPoint_pointTextColor, 0);
        pointGravity = array.getInt(R.styleable.QDTextViewPoint_pointGravity, 0);
        showPoint = array.getBoolean(R.styleable.QDTextViewPoint_showPoint, true);
        array.recycle();

        // Log.i(TAG, "pointMargin: " + pointMargin);
        QDLogger.i(TAG, "width: " + width);
        QDLogger.i(TAG, "pointText: " + pointText);
        QDLogger.i(TAG, "pointGravity: " + pointGravity);
        QDLogger.i(TAG, "showPoint: " + showPoint);
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
    private boolean showPoint = true;
    private String pointText = "";
    private float pointMargin, pointMarginLeft, pointMarginRight, pointMarginTop, pointMarginBottom;
    private int pointGravity = 0;
    private float pointTextSize = 0;
    private float pointPadding, pointPaddingLeft, pointPaddingRight, pointPaddingTop, pointPaddingBottom;
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

        // 文字宽
        textWidth = paint.measureText(pointText);
        // 文字baseline在y轴方向的位置
        baseLineY = Math.abs(paint.ascent() + paint.descent()) / 2;
        // 文字baseline在y轴方向的位置
        float descent = Math.abs(paint.descent()) / 2;

        textWidth_draw = pointPaddingLeft + pointPaddingRight + textWidth;
        baseLineY_draw = pointPaddingTop + pointPaddingBottom + baseLineY + descent;

        pointRadius = baseLineY_draw / 2;
        int x = 0, y = 0;
        if (pointGravity == 0) {//leftTop
            x = (int) (textWidth_draw / 2);
            y = (int) baseLineY_draw / 2;
        }
        if (pointGravity == 1) {//leftBottom
            x = (int) (textWidth_draw / 2);
            y = (int) (height - baseLineY_draw / 2);
        }
        if (pointGravity == 2) {//rightTop
            x = (int) (width - textWidth_draw / 2);
            y = (int) baseLineY_draw / 2;
        }
        if (pointGravity == 3) {//rightBottom
            x = (int) (width - textWidth_draw / 2);
            y = (int) (height - baseLineY_draw / 2);
        }

        point = new Point((int) (x + pointMarginLeft - pointMarginRight), (int) (y + pointMarginTop - pointMarginBottom));
    }

    float textWidth;
    float baseLineY;
    float textWidth_draw;
    float baseLineY_draw;

    private void drawPoint(Canvas canvas) {

        paint.setColor(pointBackgroundColor);
        //canvas.drawCircle(point.x, point.y, pointRadius, paint);
        RectF rect = new RectF(point.x - textWidth_draw / 2, point.y - baseLineY_draw / 2, point.x + textWidth_draw / 2, point.y + baseLineY_draw / 2);
        canvas.drawRoundRect(rect, pointRadius, pointRadius, paint);
        paint.setColor(pointTextColor);
        canvas.drawText(pointText, point.x - textWidth / 2, point.y + baseLineY, paint);
    }


    public boolean isShowPoint() {
        return showPoint;
    }

    public void setShowPoint(boolean showPoint) {
        this.showPoint = showPoint;
        postInvalidate();
    }

    public String getPointText() {
        return pointText;
    }

    public void setPointText(String pointText) {
        this.pointText = pointText;
        postInvalidate();
    }

    public float getPointMarginLeft() {
        return pointMarginLeft;
    }

    public void setPointMarginLeft(float pointMarginLeft) {
        this.pointMarginLeft = pointMarginLeft;
        postInvalidate();
    }

    public float getPointMarginRight() {
        return pointMarginRight;
    }

    public void setPointMarginRight(float pointMarginRight) {
        this.pointMarginRight = pointMarginRight;
        postInvalidate();
    }

    public float getPointMarginTop() {
        return pointMarginTop;
    }

    public void setPointMarginTop(float pointMarginTop) {
        this.pointMarginTop = pointMarginTop;
        postInvalidate();
    }

    public float getPointMarginBottom() {
        return pointMarginBottom;
    }

    public void setPointMarginBottom(float pointMarginBottom) {
        this.pointMarginBottom = pointMarginBottom;
        postInvalidate();
    }

    public int getPointGravity() {
        return pointGravity;
    }

    public void setPointGravity(int pointGravity) {
        this.pointGravity = pointGravity;
        postInvalidate();
    }

    public float getPointTextSize() {
        return pointTextSize;
    }

    public void setPointTextSize(float pointTextSize) {
        this.pointTextSize = pointTextSize;
        postInvalidate();
    }

    public int getPointBackgroundColor() {
        return pointBackgroundColor;
    }

    public void setPointBackgroundColor(int pointBackgroundColor) {
        this.pointBackgroundColor = pointBackgroundColor;
        postInvalidate();
    }

    public int getPointTextColor() {
        return pointTextColor;
    }

    public void setPointTextColor(int pointTextColor) {
        this.pointTextColor = pointTextColor;
        postInvalidate();
    }

    public float getPointRadius() {
        return pointRadius;
    }

    public void setPointRadius(float pointRadius) {
        this.pointRadius = pointRadius;
        postInvalidate();
    }

    public float getPointMargin() {
        return pointMargin;
    }

    public void setPointMargin(float pointMargin) {
        this.pointMargin = pointMargin;
        postInvalidate();
    }

    public float getPointPadding() {
        return pointPadding;
    }

    public void setPointPadding(float pointPadding) {
        this.pointPadding = pointPadding;
        postInvalidate();
    }

    public float getPointPaddingLeft() {
        return pointPaddingLeft;
    }

    public void setPointPaddingLeft(float pointPaddingLeft) {
        this.pointPaddingLeft = pointPaddingLeft;
        postInvalidate();
    }

    public float getPointPaddingRight() {
        return pointPaddingRight;
    }

    public void setPointPaddingRight(float pointPaddingRight) {
        this.pointPaddingRight = pointPaddingRight;
        postInvalidate();
    }

    public float getPointPaddingTop() {
        return pointPaddingTop;
    }

    public void setPointPaddingTop(float pointPaddingTop) {
        this.pointPaddingTop = pointPaddingTop;
        postInvalidate();
    }

    public float getPointPaddingBottom() {
        return pointPaddingBottom;
    }

    public void setPointPaddingBottom(float pointPaddingBottom) {
        this.pointPaddingBottom = pointPaddingBottom;
        postInvalidate();
    }
}
