package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.appcompat.widget.AppCompatImageView;

import android.util.AttributeSet;

import cn.demomaster.huan.quickdeveloplibrary.util.AttributeHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;

/**
 * @author squirrel桓
 * @date 2018/11/20.
 * description：
 */
public class ImageTextView extends AppCompatImageView {

    public ImageTextView(Context context) {
        super(context);
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ImageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public int textColor = -1;
    public int textSize = -1;

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        postInvalidate();
    }

    public void setTextSize(int textSize) {
        this.textSize = DisplayUtil.sp2px(getContext(), textSize);
        requestLayout();
        postInvalidate();
    }

    AttributeHelper attributeHelper;

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            attributeHelper = new AttributeHelper(context, attrs);
            //attributeHelper.hasAttr("textSize")

          /*  int count = attrs.getAttributeCount();
            for (int i = 0; i < count; i++) {
                String attrName = attrs.getAttributeName(i);
                String attrVal = attrs.getAttributeValue(i);
                if (attrName.equals("textColor")) {
                    int colorId = attrs.getAttributeResourceValue(i, -1);
                    if (colorId != -1) {
                        textColor = getResources().getColor(colorId);
                    }
                }
                Log.e(TAG, "attrName = " + attrName + " , attrVal = " + attrVal);
            }*/
            if (textColor == -1 && attributeHelper.hasAttr("textColor")) {
                String colorArr = attributeHelper.getValue("textColor");
                if (colorArr.startsWith("@")) {
                    int colorId = Integer.valueOf(colorArr.replace("@", ""));
                    textColor = getResources().getColor(colorId);
                } else {
                    textColor = Color.parseColor(colorArr);
                    //Log.e(TAG, "colorArr ===================================== " + textColor);
                }
            }

            if (textSize == -1 && attributeHelper.hasAttr("textSize")) {
                String textSizeArr = attributeHelper.getValue("textSize");
                if (textSizeArr.startsWith("@")) {
                    int textSizeId = Integer.valueOf(textSizeArr.replace("@", ""));
                    textSize = getResources().getDimensionPixelOffset(textSizeId);
                } else {
                    textSize = (int) DisplayUtil.getDimension(context, textSizeArr);
                    // Log.e(TAG, "textSizeId1 ===================================== " + textSize);
                }
            }
        }
        attributeHelper = null;
    }

    private float textWidth;
    private float baseLineY;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //QDLogger.e(TAG.TAG,"onMeasure");
        if (text != null) {
            initPaint();
            // 文字宽
            textWidth = mPaint.measureText(text);
            // 文字baseline在y轴方向的位置
            baseLineY = Math.abs(mPaint.ascent() + mPaint.descent()) / 2;

            int minimumWidth = getSuggestedMinimumWidth();
            int minimumHeight = getSuggestedMinimumHeight();
            int width = measureWidth(minimumWidth, widthMeasureSpec);
            int height = measureHeight(minimumHeight, heightMeasureSpec);
            //QDLogger.e(TAG.TAG,"width="+width+",height="+height);
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) textWidth;//super.getSuggestedMinimumWidth();
    }

    private int measureWidth(int defaultWidth, int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //QDLogger.e("YViewWidth", "---speSize = " + specSize + ",specMode="+specMode);

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                defaultWidth = (int) mPaint.measureText(text) + getPaddingLeft() + getPaddingRight();
                break;
            case MeasureSpec.EXACTLY:
                defaultWidth = specSize;// (int) mPaint.measureText(text);
                break;
            case MeasureSpec.UNSPECIFIED:
                //defaultWidth = Math.max(defaultWidth, specSize);
        }
        return defaultWidth;
    }

    private int measureHeight(int defaultHeight, int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //Log.e("YViewHeight", "---speSize = " + specSize + "");

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                defaultHeight = (int) (-mPaint.ascent() + mPaint.descent()) + getPaddingTop() + getPaddingBottom();
                //Log.e("YViewHeight", "---speMode = AT_MOST");
                break;
            case MeasureSpec.EXACTLY:
                defaultHeight = specSize;
                //Log.e("YViewHeight", "---speSize = EXACTLY");
                break;
            case MeasureSpec.UNSPECIFIED:
                defaultHeight = Math.max(defaultHeight, specSize);
                //Log.e("YViewHeight", "---speSize = UNSPECIFIED");
//        1.基准点是baseline
//        2.ascent：是baseline之上至字符最高处的距离
//        3.descent：是baseline之下至字符最低处的距离
//        4.leading：是上一行字符的descent到下一行的ascent之间的距离,也就是相邻行间的空白距离
//        5.top：是指的是最高字符到baseline的值,即ascent的最大值
//        6.bottom：是指最低字符到baseline的值,即descent的最大值
                break;
        }
        return defaultHeight;
    }

    public int center_x, center_y, mwidth, width, height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //QDLogger.e(TAG.TAG,"onSizeChanged"+"w="+w+",h="+h);
        width = w;
        height = h;
        center_x = width / 2;
    }

    boolean showImage = true;
    public void setShowImage(boolean showImage) {
        this.showImage = showImage;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //QDLogger.e(TAG.TAG,"onDraw");
        if (showImage) {
            super.onDraw(canvas);
        }
        if (text != null) {
            initPaint();
            drawText(canvas);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //QDLogger.e(TAG.TAG,"onLayout");
    }

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        requestLayout();
        invalidate();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    Paint mPaint;

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
    }

    private void drawText(Canvas canvas) {
        int h = getHeight();
       /* // 计算Baseline绘制的起点X轴坐标
        int baseX = (int) (canvas.getWidth() / 2 - mPaint.measureText(text) / 2);
        // 计算Baseline绘制的Y坐标(有点难理解，记住)
        int baseY = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));*/
        canvas.drawText(text, width / 2 - textWidth / 2, height / 2 + baseLineY, mPaint);
        //canvas.drawLine( 0,  height/2,  width,  height/2, mPaint);
    }

    private int showType;

    public void asTextView() {

    }
}
