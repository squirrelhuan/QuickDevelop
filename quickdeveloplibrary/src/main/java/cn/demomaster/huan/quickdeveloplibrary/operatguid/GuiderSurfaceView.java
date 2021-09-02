package cn.demomaster.huan.quickdeveloplibrary.operatguid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * Created by huan on 2017/9/14.
 */
public class GuiderSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    //用于控制SurfaceView
    private final SurfaceHolder sfh;
    private final Handler handler = new Handler();
    private final ImageRunnable imageRunnable = new ImageRunnable();
    private final Paint paint;
    private Canvas canvas;
    private Matrix matrix;

    /**
     * 图片的坐标
     */
    private float imageX, imageY;
    /**
     * 获取的图片
     */
    private Bitmap bmp;
    /**
     * 图片宽高
     */
    private float bmpW, bmpH;
    /**
     * 屏幕大小
     */
    private int screenW, screenH;

    /**
     * SurfaceView初始化函数
     */
    public GuiderSurfaceView(Context context) {
        super(context);
        sfh = this.getHolder();
        sfh.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        setFocusable(true);
    }

    /**
     * SurfaceView初始化函数
     */
    public GuiderSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sfh = this.getHolder();
        sfh.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        setFocusable(true);
    }

    private GuiderModel guiderModel;

    public GuiderSurfaceView(Context context, GuiderModel guiderModel) {
        super(context);

        this.guiderModel = guiderModel;
        sfh = this.getHolder();
        sfh.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        setFocusable(true);
        setBackgroundResource(android.R.color.transparent);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        setBackgroundResource(android.R.color.transparent);

    }

    /**
     * SurfaceView视图创建，响应此函数
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        QDLogger.println("ImageSurfaceView is surfaceCreated");
        screenH = this.getHeight();
        screenW = this.getWidth();
        handler.post(imageRunnable);
    }

    /**
     * 游戏绘图
     */
    public void draw() {
        try {
            canvas = sfh.lockCanvas();
            //canvas.drawRGB(0, 0, 0);
            //canvas.save();
//绘制
            // paint = new Paint();
            paint.setTextSize(10);
            paint.setColor(Color.YELLOW);
            //this.setBackgroundColor(getResources().getColor(R.color.red));
            RectF rectF = getViewRectF(guiderModel.getTargetView().get());
            canvas.drawRect(rectF, paint);
            //canvas.drawText("nihao",10,10,paint);
            //canvas.drawBitmap(bmp, matrix, paint);
            QDLogger.println("绘制图像了吗？");
            //canvas.restore();
        } catch (Exception e) {
            QDLogger.e(e);
        } finally {
            if (canvas != null)
                sfh.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * 触屏事件监听
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    /**
     * 图片的线程运行
     */
    class ImageRunnable implements Runnable {
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();
            if (end - start < 500) {
                handler.postDelayed(this, 200 - (end - start));
            } else {
                handler.post(this);
            }
        }
    }

    /**
     * SurfaceView视图状态发生改变，响应此函数
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        QDLogger.println("ImageSurfaceView is surfaceChanged");
    }

    /**
     * SurfaceView视图消亡时，响应此函数
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        QDLogger.println("ImageSurfaceView is surfaceDestroyed");
    }


    private RectF getViewRectF(View view) {
        int[] location;
        location = new int[2];
        view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
        view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
        QDLogger.println("view--->x坐标:" + location[0] + "view--->y坐标:" + location[1]);

        float l = location[0];
        float t = location[1];
        float r = location[0] + view.getWidth();
        float b = location[1] + view.getHeight();
        return new RectF(l, t, r, b);
    }
}

