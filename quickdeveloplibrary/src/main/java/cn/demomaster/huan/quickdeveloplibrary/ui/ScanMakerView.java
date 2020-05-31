/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.demomaster.huan.quickdeveloplibrary.ui;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.animation.QDValueAnimator;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.qdzxinglibrary.ScanHelper;

import static cn.demomaster.qdzxinglibrary.ScanSurfaceView.TAG;


/**
 * 扫描控件maker层，用于处理扫描框  扫描特效光
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ScanMakerView extends View implements ResultPointCallback {

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private static final int CURRENT_POINT_OPACITY = 0xA0;
    private static final int MAX_RESULT_POINTS = 20;
    private static final int POINT_SIZE = 6;

    private Paint paint;
    private final int laserColor;
    private final int resultPointColor;
    private List<ResultPoint> possibleResultPoints;
    private List<ResultPoint> lastPossibleResultPoints;

    //闪烁的疑似点
    private int pointColor = Color.GREEN;
    //扫描线
    private int scanLineColor = Color.RED;
    private int maskColor = Color.parseColor("#33000000");

    // This constructor is used when the class is built from an XML resource.
    public ScanMakerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize these once for performance rather than calling them every time in onDraw().
        laserColor = scanLineColor;
        resultPointColor = pointColor;
        possibleResultPoints = new ArrayList<>(5);
        lastPossibleResultPoints = null;

        Display defaultDisplay = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
        Log.i(TAG, "screenWidth = " + screenWidth + ",screenHeight = " + screenHeight);
        //x = 1440,y = 2768
        windowWidth = 300;
        windowHeight = 300;
    }
    int screenWidth;
    int screenHeight;
    int windowWidth;
    int windowHeight;

    float roundRadius = 5f;
    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        drawMark(canvas);
        if (animator==null||(!animator.isRunning()&&!animator.isStarted())) {
            startAnimation();
        }
    }

    int markerWidth = 200;
    int markerHeight = 200;

    /**
     * 设置marker宽度
     * @param markerWidth
     */
    public void setMarkerWidth(int markerWidth) {
        this.markerWidth = markerWidth;
    }
    /**
     * 设置marker高度
     * @param markerHeight
     */
    public void setMarkerHeight(int markerHeight) {
        this.markerHeight = markerHeight;
    }

    /**
     * marker颜色
     * @param maskColor
     */
    public void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
    }

    /**
     * 绘制遮罩层
     * @param canvas
     */
    private void drawMark(Canvas canvas) {
        if (ScanHelper.getInstance().getCameraManager(getContext()) == null) {
            return; // not ready yet, early draw before done configuring
        }

        //TODO 这里是获取可识别区域，即扫码框区域，因为修改了源码中的可识别区域为整个可见区域。所以显示扫码区域可以随意设置了，这里就注销了。
        /*Rect frame = ScanHelper.getInstance().getCameraManager(getContext()).getFramingRect();
        Rect previewFrame = ScanHelper.getInstance().getCameraManager(getContext()).getFramingRectInPreview();
        if (frame == null || previewFrame == null) {
            return;
        }*/

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        float centerX = width/2;
        float centerY = height/2;

        // Draw the exterior (i.e. outside the framing rect) darkened
        canvas.drawColor(maskColor);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        paint.setColor(Color.BLACK);
        RectF rectF = new RectF(centerX-markerWidth/2, centerY-markerHeight/2, centerX+markerWidth/2, centerY+markerHeight/2);
        Rect frame = new Rect((int) rectF.left,(int)rectF.top,(int)rectF.right,(int)rectF.bottom);
        Rect previewFrame = frame;
        canvas.drawRoundRect(rectF, roundRadius, roundRadius, paint);
        paint.setXfermode(null);

        // Draw a red "laser scanner" line through the middle to show decoding is active
        paint.setColor(laserColor);
        /*paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
        scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
        int middle = (int) ((rectF.top+rectF.bottom)/2f);
        canvas.drawRect(rectF.left + 2, middle - 1, rectF.right - 1, middle + 2, paint);
        */

        int al = 50;
        int aw = DisplayUtil.dip2px(getContext(),3);
        paint.setColor(laserColor);
        paint.setStrokeWidth(aw);
        paint.setStyle(Paint.Style.STROKE);
       // canvas.drawLines(new float[]{rectF.left,rectF.top+al,rectF.left,rectF.top,rectF.left,rectF.top,rectF.left+al,rectF.top},paint);

        canvas.drawLine(rectF.left,rectF.top+al,rectF.left,rectF.top-aw/2,paint);
        canvas.drawLine(rectF.left,rectF.top,rectF.left+al,rectF.top,paint);

        canvas.drawLine(rectF.right,rectF.top,rectF.right-al,rectF.top,paint);
        canvas.drawLine(rectF.right,rectF.top+al,rectF.right,rectF.top-aw/2,paint);

        canvas.drawLine(rectF.left,rectF.bottom+aw/2,rectF.left,rectF.bottom-al,paint);
        canvas.drawLine(rectF.left,rectF.bottom,rectF.left+al,rectF.bottom,paint);

        canvas.drawLine(rectF.right,rectF.bottom,rectF.right-al,rectF.bottom,paint);
        canvas.drawLine(rectF.right,rectF.bottom-al,rectF.right,rectF.bottom+aw/2,paint);

        paint.setAntiAlias(true);//使用抗锯齿功能
        //paint.setColor(Color.RED);    //设置画笔的颜色为绿色
        paint.setStyle(Paint.Style.FILL);//设置画笔类型为填充
        /***********绘制圆弧*************/
        canvas.save();
        canvas.clipRect(rectF);
        RectF rectf_head;//确定外切矩形范围
        int lineHeight=100;
        Shader mShader;
        if(isForward){
            canvas.translate(0,progress*(markerHeight+lineHeight));
            rectf_head=new RectF(centerX-markerWidth/3*2, rectF.top-lineHeight, centerX+markerWidth/3*2, rectF.top);//确定外切矩形范围
            rectf_head.offset(0, 0);//使rectf_head所确定的矩形向右偏移0像素，向下偏移0像素

            mShader = new LinearGradient(centerX,rectf_head.top,centerX,(rectf_head.top+rectf_head.bottom)/2,new int[] {0x1300ff00,0xaa00ff00},null, Shader.TileMode.REPEAT);
//新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变

        }else {
            canvas.translate(0,-(1-progress)*(markerHeight+lineHeight));
            rectf_head=new RectF(centerX-markerWidth/3*2, rectF.bottom-lineHeight, centerX+markerWidth/3*2, rectF.bottom);//确定外切矩形范围
            mShader = new LinearGradient(centerX,rectf_head.top,centerX,(rectf_head.top+rectf_head.bottom)/2,new int[] {0xaa00ff00,0x1300ff00},null, Shader.TileMode.REPEAT);
//新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
        }

           //mShader = new RadialGradient(centerX,centerY,markerHeight,new int[] {0xaa00ff00,0x1100ff00},null, Shader.TileMode.REPEAT);
        paint.setShader(mShader);
        if(isForward) {
            canvas.drawArc(rectf_head, 0, -180, false, paint);//绘制圆弧，不含圆心
        }else {
            canvas.drawArc(rectf_head, 0, 180, false, paint);//绘制圆弧，不含圆心
        }
        canvas.restore();
        float scaleX = frame.width() / (float) previewFrame.width();
        float scaleY = frame.height() / (float) previewFrame.height();

        List<ResultPoint> currentPossible = possibleResultPoints;
        List<ResultPoint> currentLast = lastPossibleResultPoints;
        int frameLeft = frame.left;
        int frameTop = frame.top;
        if (currentPossible.isEmpty()) {
            lastPossibleResultPoints = null;
        } else {
            possibleResultPoints = new ArrayList<>(5);
            lastPossibleResultPoints = currentPossible;
            paint.setAlpha(CURRENT_POINT_OPACITY);
            paint.setColor(resultPointColor);
            synchronized (currentPossible) {
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                            frameTop + (int) (point.getY() * scaleY),
                            POINT_SIZE, paint);
                }
            }
        }
        if (currentLast != null) {
            paint.setAlpha(CURRENT_POINT_OPACITY / 2);
            paint.setColor(resultPointColor);
            synchronized (currentLast) {
                float radius = POINT_SIZE / 2.0f;
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                            frameTop + (int) (point.getY() * scaleY),
                            radius, paint);
                }
            }
        }

        // Request another update at the animation interval, but only repaint the laser line,
        // not the entire viewfinder mask.
        /*postInvalidateDelayed(80L,
                frame.left - POINT_SIZE,
                frame.top - POINT_SIZE,
                frame.right + POINT_SIZE,
                frame.bottom + POINT_SIZE);*/
    }

    /**
     * 添加疑似点
     *
     * @param point
     */
    public void addPossibleResultPoint(ResultPoint point) {
        List<ResultPoint> points = possibleResultPoints;
        synchronized (points) {
            points.add(point);
            int size = points.size();
            if (size > MAX_RESULT_POINTS) {
                // trim it
                points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
            }
        }
    }

    @Override
    public void foundPossibleResultPoint(ResultPoint resultPoint) {
        addPossibleResultPoint(resultPoint);
    }

    private int centerX, centerY, width, height;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        QDLogger.e("sw="+w+",sh="+h);
        centerX = width / 2;
        centerY = height/2;
    }

    private float progress;
    private boolean isForward = true;
    QDValueAnimator animator;
    public void startAnimation() {
        final float start = 0f;
        final float end = 1f;
        animator = QDValueAnimator.ofFloat(start, end);
        animator.setDuration(2400);
        animator.setAnimationListener(new QDValueAnimator.AnimationListener() {
            @Override
            public void onStartOpen(Object value) {
                isForward = true;
            }

            @Override
            public void onOpening(Object value) {
                isForward = true;
                if (getVisibility() == VISIBLE) {
                    progress = (float) value;
                    //QDLogger.d( "progress=" + progress);
                    invalidate();
                }
            }

            @Override
            public void onEndOpen(Object value) {

            }

            @Override
            public void onStartClose(Object value) {
                isForward = false;
            }

            @Override
            public void onClosing(Object value) {
                isForward = false;
                if (getVisibility() == VISIBLE) {
                    progress =(float) value;
                    QDLogger.d( "progress=" + progress+",value="+value);
                    invalidate();
                }
            }

            @Override
            public void onEndClose(Object value) {

            }
        });
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(-1);//ValueAnimator.INFINITE
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(animator!=null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                animator.cancel();
            }
        }
    }
}
