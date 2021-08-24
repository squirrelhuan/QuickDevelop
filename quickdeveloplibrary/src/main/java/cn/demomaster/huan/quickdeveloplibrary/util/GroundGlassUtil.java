package cn.demomaster.huan.quickdeveloplibrary.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.OnReleaseListener;

import static cn.demomaster.huan.quickdeveloplibrary.util.QDBitmapUtil.generateColorBitmap;

/**
 * 毛玻璃特效帮助类
 */
public class GroundGlassUtil implements OnReleaseListener {
    private View targetView;
    private View backgroundView;
    //private View backgroundViewParent;

    Context context;

    public GroundGlassUtil(Context context) {
        this.context = context;
    }

    private int backgroundColor = 0xabffffff;
    private boolean useBackgroundColor = true;

    public void setUseBackgroundColor(boolean useBackgroundColor) {
        this.useBackgroundColor = useBackgroundColor;
        invalidate();
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        invalidate();
    }

    public View getTargetView() {
        return targetView;
    }

    public void setTargetView(View targetView) {
        this.targetView = targetView;
        //QDLogger.println("targetViewId=" + targetView.getId());
        //if (backgroundView != null && targetView.getId() != View.NO_ID && autoFindBackgroundView) {
            backgroundView = targetView.getRootView();//findBackgroundViewParent(backgroundView);
       // }
        targetView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                targetView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //QDLogger.println("OnGlobalLayoutListener targetView");
                invalidate();
            }
        });

        targetOnDrawListener = new ViewTreeObserver.OnDrawListener () {
            @Override
            public void onDraw() {
                //targetView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //QDLogger.println("OnGlobalLayoutListener " + targetView.getWidth() + "," + targetView.getHeight());
                isReady = true;
                if(!isSelfDraw) {
                   // QDLogger.println("isSelfDraw1 =" +isSelfDraw);
                    invalidate();
                }else {
                    isSelfDraw = false;
                }

                long timeMillis = (long) targetView.getTag();
                if(timeMillis==refreshTime){

                }else {
                    invalidate();
                }
                targetView.setTag(System.currentTimeMillis());
               // QDLogger.println("isSelfDraw2 =" +isSelfDraw);
            }
        };

        targetView.getViewTreeObserver().addOnDrawListener(targetOnDrawListener);
    }
    boolean isSelfDraw;
    ViewTreeObserver.OnDrawListener targetOnDrawListener;
    public View getBackgroundView() {
        //View view = backgroundViewParent == null ? backgroundView : backgroundViewParent;
        //if(view==null){
           return targetView.getRootView();
       // }
       // return view;
    }

    boolean autoFindBackgroundView;//自动寻找待截图对象

    public void setBackgroundView(View backgroundView, boolean autoFind) {
        this.backgroundView = backgroundView;
        autoFindBackgroundView = autoFind;

        if (backgroundView != null && targetView.getId() != View.NO_ID && autoFindBackgroundView) {
          //  backgroundViewParent = findBackgroundViewParent(backgroundView);
        }
        addViewTreeObserver(backgroundView);

    }

    ViewTreeObserver.OnDrawListener onDrawListener;
    View lastBackgroundView;

    private void addViewTreeObserver(View view) {
        if (lastBackgroundView != null) {
            if (onDrawListener != null) {
                lastBackgroundView.getViewTreeObserver().removeOnDrawListener(onDrawListener);
                onDrawListener = null;
            }
        }
        onDrawListener = new ViewTreeObserver.OnDrawListener() {
            @Override
            public void onDraw() {
                //QDLogger.d("QDScrollView", "view重绘重新模糊处理");
                invalidate();
            }
        };
        lastBackgroundView = view;
        //view重绘时回调
        view.getViewTreeObserver().addOnDrawListener(onDrawListener);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //QDLogger.println("OnGlobalLayoutListener backgroundView");
                invalidate();
            }
        });
    }

    /**
     * 获取非targetView父类
     *
     * @param v
     * @return
     */
    public View findBackgroundViewParent(Object v) {
        if (v != null && v instanceof ViewGroup && targetView != null) {
            ViewGroup view = ((ViewGroup) v);
            if (!containsView(view, targetView)) {
                Object object = view.getParent();
                if (object != null && object instanceof ViewGroup) {
                    if (containsView(((ViewGroup) object), targetView)) {
                        return view;
                    } else {
                        return findBackgroundViewParent(object);
                    }
                } else if (object == null || !(object instanceof ViewGroup)) {
                    return view;
                }
            } else {
                return view;
            }
        }
        return null;
    }

    public boolean containsView(ViewGroup viewGroup, View view) {
        if (view == null || viewGroup == null) {
            return false;
        }
        if (view.getId() != View.NO_ID) {
            return null != viewGroup.findViewById(view.getId());
        }
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                containsView((ViewGroup) child, view);
            } else if (child == view) {
                return true;
            }
        }
        return false;
    }
    long refreshTime;
    boolean isReady;
    public void invalidate() {
        if(!isReady){
            return;
        }
        Bitmap bitmap = generateBackgroundBitmap();
        if (bitmap == null) {
            return;
        }
        if (getBackgroundView() != null && targetView != null) {
            //模糊背景
            long t1 = System.currentTimeMillis();
            //QDLogger.println("bitmap w1=：" + bitmap.getWidth());
            bitmap = QDBitmapUtil.zoomImage(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3);
            //QDLogger.println("bitmap w2=：" + bitmap.getWidth());
            bitmap = BlurUtil.doBlur(bitmap, radius, 0.2f);
            if (bitmap == null) {
                return;
            }
            long t2 = System.currentTimeMillis();
            QDLogger.println("模糊用时：" + (t2 - t1));
            //添加背景色
            if (useBackgroundColor) {
                Bitmap backgroundBitmap = generateColorBitmap(bitmap.getWidth(), bitmap.getHeight(), backgroundColor);
                //添加背景色
                bitmap = mergeBitmap(bitmap, backgroundBitmap);
                //long t3 = System.currentTimeMillis();
                //QDLogger.println("添加背景色用时：" + (t3 - t2));
            }
            //bitmap = getAlplaBitmap(bitmap, 80);
            if (bitmap != null) {
                refreshTime = System.currentTimeMillis();
                targetView.setTag(refreshTime);
                targetView.setBackground(new BitmapDrawable(context.getResources(),bitmap));
                isSelfDraw = true;
                //targetView.setBackground(new BitmapDrawable(context.getResources(),getBackgroundBitmap()));
            }
        }
    }

    boolean compareBitmap = false;//对比图片是否有变化
    /**
     * 获取待模糊的图片
     *
     * @return
     */
    private Bitmap generateBackgroundBitmap() {
        if(!compareBitmap){
            mBackgroundBitmap = getBackgroundBitmap();
            return mBackgroundBitmap;
        }
        //截取背景色
        Bitmap backgroundBitmap = getBackgroundBitmap();
        if (backgroundBitmap == null) {
            mBackgroundBitmap = backgroundBitmap;
            return null;
        }
        long t1 = System.currentTimeMillis();
        boolean b = false;
        if (backgroundBitmap != null && mBackgroundBitmap != null) {
            //对比两张图片是否一致
            b = compare2Image(backgroundBitmap, mBackgroundBitmap);
        }
        mBackgroundBitmap = backgroundBitmap;
        long t2 = System.currentTimeMillis();
        QDLogger.println("图片对比：" + b + ",用时" + (t2 - t1));
        return b ? null : backgroundBitmap;
    }

    /**
     * 获取背景图片
     *
     * @return
     */
    private Bitmap getBackgroundBitmap() {
        //获取覆盖区域
        int[] location_background = new int[2];
        //tabGroup.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
        getBackgroundView().getLocationOnScreen(location_background);//获取在整个屏幕内的绝对坐标
        Rect rect_background = new Rect(location_background[0], location_background[1], location_background[0] + getBackgroundView().getWidth(), location_background[1] + getBackgroundView().getHeight());
        //QDLogger.println("x=" + location_background[0] + ",y=" + location_background[1]);

        int[] location_target = new int[2];
        targetView.getLocationOnScreen(location_target);//获取在整个屏幕内的绝对坐标
        Rect rect_target = new Rect(location_target[0], location_target[1], location_target[0] + targetView.getWidth(), location_target[1] + targetView.getHeight());
        //QDLogger.println("x2=" + location_target[0] + ",y2=" + location_target[1]);

        //获取绝对坐标的交集
        Rect rect3 = getBothArea(rect_background, rect_target);
        if (rect3.width() <= 0 || rect3.height() <= 0) {
            return null;
        }

        //QDLogger.println("left=" + rect3.left + ",top=" + rect3.top + ",right=" + rect3.right + ",bottom=" + rect3.bottom+",width="+rect3.width()+",height="+rect3.height());
        //绝对位置改为以backgroundView的内部的相对位置
        Rect rect4 = new Rect(rect3.left - location_background[0], rect3.top - location_background[1], rect3.right - location_background[0], rect3.bottom - location_background[1]);
        boolean isVisible = false;
        if(targetView.getVisibility()==View.VISIBLE) {
            isVisible = true;
            targetView.setVisibility(View.INVISIBLE);
        }
        Bitmap bitmap = shot(getBackgroundView(), rect4.left, rect4.top, rect4.width(), rect4.height());
        if(isVisible) {
            targetView.setVisibility(View.VISIBLE);
        }
        return bitmap;
    }

    /**
     * 获取交集
     *
     * @param r1
     * @param r2
     * @return
     */
    public static Rect getBothArea(Rect r1, Rect r2) {
        Rect both = new Rect();
        both.left = Math.max(r1.left, r2.left);
        both.right = Math.min(r1.right, r2.right);
        both.top = Math.max(r1.top, r2.top);
        both.bottom = Math.min(r1.bottom, r2.bottom);
        return both;
    }

    public int radius = 20;

    public void setRadius(int radius) {
        this.radius = radius;
    }

    Bitmap mBackgroundBitmap;

    /**
     * 对比两张图片是否一致
     *
     * @param bmp1
     * @param bmp2
     * @return
     */
    private boolean compare2Image(Bitmap bmp1, Bitmap bmp2) {
        if (bmp1 == null && bmp2 == null) {
            return true;
        } else if (bmp1 == null || bmp2 == null) {
            return false;
        }
        int width = bmp1.getWidth();
        int height = bmp1.getHeight();
        if (width != bmp2.getWidth() || height != bmp2.getHeight()) {
            return false;
        }
/*
        for (int i = 0; i < width; i = i + width/10) {
            for (int j = 0; j < height; j = j + height/10) {
                if (bmp1.getPixel(i, j) != bmp2.getPixel(i, j)) return false;
            }
        }*/
        long t1 = System.currentTimeMillis();
        int pixelCount = width * height;
        int[] pixels1 = new int[pixelCount];
        int[] pixels2 = new int[pixelCount];
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        int setp = (int) Math.max(density, 1)*3;
        //QDLogger.e("density=" + density + ",setp=" + setp);

        bmp1.getPixels(pixels1, 0, width, 0, 0, width, height);
        bmp2.getPixels(pixels2, 0, width, 0, 0, width, height);
        boolean result1 = true;
        for (int i = 0; i < pixelCount; i = i + setp * setp) {
            if (pixels1[i] != pixels2[i]) {
                result1 = false;
                break;
            }
        }
       /*long t2 = System.currentTimeMillis();
        IntBuffer buffer1 = IntBuffer.allocate(pixelCount);
        IntBuffer buffer2 = IntBuffer.allocate(pixelCount);
        bmp1.copyPixelsToBuffer(buffer1);
        bmp2.copyPixelsToBuffer(buffer2);
        buffer1.position(0);
        buffer2.position(0);
        int result = buffer1.compareTo(buffer2);
        long t3 = System.currentTimeMillis();
        QDLogger.e("compare2Image width="+width+",height="+height+",t1="+(t2-t1)+",r="+result1+",t2="+(t3-t2) +",r="+(result==0));
*/
        return result1;
    }

    public static Bitmap drawBitmapBg(Bitmap bt, int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        Canvas canvas = new Canvas(bt);
        canvas.drawRect(0, 0, bt.getWidth(), bt.getHeight(), paint);
        canvas.drawBitmap(bt, 0, 0, paint);
        return bt;
    }

    /**
     * 合并图层
     *
     * @param background
     * @param foreground
     * @return
     */
    private Bitmap mergeBitmap(Bitmap background, Bitmap foreground) {
        if (background == null) {
            return null;
        }

        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        //int fgWidth = foreground.getWidth();
        //int fgHeight = foreground.getHeight();
        //create the new blank bitmap 创建一个新的和SRC长度宽度一样的位图
        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newbmp);
        //draw bg into
        cv.drawBitmap(background, 0, 0, null);//在 0，0坐标开始画入bg
        //draw fg into
        cv.drawBitmap(foreground, 0, 0, null);//在 0，0坐标开始画入fg ，可以从任意位置画入
        //save all clip
        //cv.save(Canvas.ALL_SAVE_FLAG);//保存
        //store
        //cv.restore();//存储
        return newbmp;
    }

    /**
     * 替换透明色为
     *
     * @param sourceImg
     * @param color
     * @return
     */
    public static Bitmap fillPixBitmap(Bitmap sourceImg, int color) {
        Bitmap imageWithBG = Bitmap.createBitmap(sourceImg.getWidth(), sourceImg.getHeight(), sourceImg.getConfig());  // Create another image the same size
        imageWithBG.eraseColor(color);  // set its background to white, or whatever color you want
        Canvas canvas = new Canvas(imageWithBG);  // create a canvas to draw on the new image
        canvas.drawBitmap(sourceImg, 0f, 0f, null); // draw old image on the background
        sourceImg.recycle();
        return imageWithBG;
    }

    public static Bitmap setAlplaBitmap(Bitmap sourceImg, int number) {
        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg.getWidth(), sourceImg.getHeight());
        number = number * 255 / 100;
        for (int i = 0; i < argb.length; i++) {
            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);
        }
        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg.getHeight(), Bitmap.Config.ARGB_8888);
        return sourceImg;
    }

    public Bitmap shot(View view, int x, int y, int width, int height) {
        if (width <= 0 || height <= 0) {
            return null;
        }
        try {
            // 获取windows中最顶层的view
            view.buildDrawingCache();
            // 允许当前窗口保存缓存信息
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = view.getDrawingCache();
            // QDLogger.e("bitmap寬度=" + bitmap.getWidth() + ",高度=" + bitmap.getHeight());
            if (x < 0 || x > bitmap.getWidth()) {
                return null;
            } else if (y < 0 || y > bitmap.getHeight()) {
                return null;
            } else if ((x + width) > bitmap.getWidth()) {
                return null;
            } else if ((y + height) > bitmap.getHeight()) {
                return null;
            }
            Bitmap bmp = Bitmap.createBitmap(bitmap, x, y, width, height);
            // 销毁缓存信息
            view.destroyDrawingCache();
            return bmp;
        } catch (Exception e) {
            QDLogger.e(e);
        }
        return null;
    }

    @Override
    public void onRelease() {
        if (lastBackgroundView != null) {
            if (onDrawListener != null) {
                lastBackgroundView.getViewTreeObserver().removeOnDrawListener(onDrawListener);
                onDrawListener = null;
            }
        }
    }
}
