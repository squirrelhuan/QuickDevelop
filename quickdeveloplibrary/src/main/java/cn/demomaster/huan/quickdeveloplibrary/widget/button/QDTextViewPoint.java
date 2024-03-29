package cn.demomaster.huan.quickdeveloplibrary.widget.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.QDViewUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIViewHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.drawable.QDRoundButtonDrawable;

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
        if (getBackground() == null) {//注意如果设置了自定义的背景 就不用自动生成背景drawable了
            StateListDrawable drawable = new StateListDrawable();
            //如果要设置莫项为false，在前面加负号 ，比如android.R.attr.state_focesed标志true，-android.R.attr.state_focesed就标志false
            int defStyleAttr = R.attr.QDTextViewStyle;

            QDRoundButtonDrawable bg_normal = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
            QDRoundButtonDrawable bg_focused = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
            QDRoundButtonDrawable bg_pressed = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
            QDRoundButtonDrawable bg_selected = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
            QDRoundButtonDrawable bg_enabled = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int color = getResources().getColor(R.color.gray);
                int color_normal = color;// bg_normal.backgroundColors.getColorForState(bg_normal.getState(),Color.RED);
                int[] colors = new int[]{color_normal, color_normal, color, color_normal, color_normal, color_normal};//{ pressed, focused, normal, focused, unable, normal };
                ColorStateList colorBg = QDViewUtil.getColorStateList(colors);
                bg_enabled.setColor(colorBg);
            }
            drawable.addState(new int[]{android.R.attr.state_focused}, bg_focused);
            drawable.addState(new int[]{android.R.attr.state_pressed}, bg_pressed);
            drawable.addState(new int[]{android.R.attr.state_selected}, bg_selected);
            drawable.addState(new int[]{android.R.attr.state_enabled}, bg_selected);
            drawable.addState(new int[]{-android.R.attr.state_enabled}, bg_enabled);
            drawable.addState(new int[]{}, bg_normal);//默认
            //btn.setBackgroundDrawable(drawable);
            //QDRoundButtonDrawable bg = QDRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr);
            QMUIViewHelper.setBackgroundKeepingPadding(this, drawable);

            /*StateListDrawable bg = new StateListDrawable();
            Drawable normal = context.getResources().getDrawable(idNormal);
            Drawable pressed = context.getResources().getDrawable(idPressed);
            Drawable focused = context.getResources().getDrawable(idFocused);
            bg.addState(new int[]{android.R.attr.state_pressed,}, pressed);
            bg.addState(new int[]{android.R.attr.state_focused}, focused);
            bg.addState(new int[]{}, normal);*/
        }

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

        hollowOut = array.getBoolean(R.styleable.QDTextViewPoint_hollowOut, false);
        hollowColor = array.getColor(R.styleable.QDTextViewPoint_hollowColor, hollowColor);
        hollowRadius = array.getDimension(R.styleable.QDTextViewPoint_hollowRadius, hollowRadius);
        array.recycle();

        // Log.i(TAG, "pointMargin: " + pointMargin);
        /*QDLogger.println( "width: " + width);
        QDLogger.println( "pointText: " + pointText);
        QDLogger.println( "pointGravity: " + pointGravity);
        QDLogger.println( "showPoint: " + showPoint);*/
    }

    private int width, height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    boolean hollowOut;
    int hollowColor = Color.WHITE;
    float hollowRadius = 5;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (hollowOut) {
            //禁用硬件加速
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            RectF rectf = new RectF(0, 0, getWidth(), getHeight());
            paint = new Paint();
            paint.setColor(hollowColor);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
            canvas.drawRoundRect(rectf, hollowRadius, hollowRadius, paint);
            paint.setXfermode(null);
        }
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

        if (!TextUtils.isEmpty(pointText)) {
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
    }

    float textWidth;
    float baseLineY;
    float textWidth_draw;
    float baseLineY_draw;

    private void drawPoint(Canvas canvas) {
        if (point != null) {
            paint.setColor(pointBackgroundColor);
            //canvas.drawCircle(point.x, point.y, pointRadius, paint);
            RectF rect = new RectF(point.x - textWidth_draw / 2, point.y - baseLineY_draw / 2, point.x + textWidth_draw / 2, point.y + baseLineY_draw / 2);
            canvas.drawRoundRect(rect, pointRadius, pointRadius, paint);
            paint.setColor(pointTextColor);
            canvas.drawText(pointText, point.x - textWidth / 2, point.y + baseLineY, paint);
        }
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
