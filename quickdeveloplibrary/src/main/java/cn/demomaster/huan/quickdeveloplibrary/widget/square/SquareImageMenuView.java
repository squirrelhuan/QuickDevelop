package cn.demomaster.huan.quickdeveloplibrary.widget.square;

/**
 * @author squirrel桓
 * @date 2018/11/29.
 * description：
 */

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.hardware.input.InputManager;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AccelerateInterpolator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.service.QDAccessibilityService;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.qdlogger_library.QDLogger;

import static android.content.Context.WINDOW_SERVICE;
import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * 正方形的ImageView
 */
public class SquareImageMenuView extends View {

    public SquareImageMenuView(Context context) {
        super(context);
    }

    public SquareImageMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init();
        /*maxWith = w;
        maxHeight = h;*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        init();
    }

    private boolean isExpanded = false;

    private void init() {
        if (maxWith != -1) {
            return;
        }
        //QDLogger.i("init.......");
        //QDAccessibilityService.addPackage("com.huan.quanmintoutiao");
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QDLogger.i("onClick=stopAnimation" + clickX + "," + clickY);
                //stopAnimation();
                if (isExpanded) {
                    for (Map.Entry entry : pointMap.entrySet()) {
                        Point point = (Point) entry.getValue();
                        if (Math.abs(point.x - clickX) < button_width && Math.abs(point.y - clickY) < button_width) {
                            QDLogger.i("clicked=" + entry.getKey());
                            int id = (int) entry.getKey();
                            doAction(id);
                            stopAnimation();
                        }
                    }
                } else {
                    stopAnimation();
                }
            }
        });
        setOnTouchListener(new OnTouchListener(getContext(), (ViewGroup) getParent(), new OnClickListener() {
            @Override
            public void onClick(MotionEvent event) {
                QDLogger.i("event=" + event);
            }
        }));
    }

    /**
     * 执行动作
     * @param type
     */
    public void doAction(int type) {
        if (type == 0 && !QDAccessibilityService.isAccessibilityServiceRunning(getContext(),QDAccessibilityService.class.getName())) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            getContext().startActivity(intent);
            return;
        }
        if (!QDAccessibilityService.isAccessibilityServiceRunning(getContext(),QDAccessibilityService.class.getName())) {
            //跳转系统自带界面 辅助功能界面
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            return;
        }
        if (!QDAccessibilityService.isStart()) {
            Log.i(TAG, "isStart.........");
            //QDAccessibilityService.addPackage("com.huan.quanmintoutiao");
            //getContext().startService(new Intent(getContext(), QDAccessibilityService.class));

            AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();
            serviceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
            serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
            serviceInfo.packageNames = new String[]{getContext().getPackageName()};
            serviceInfo.notificationTimeout = 100;
            //QDAccessibilityService.getService().setServiceInfo(serviceInfo);
            return;
        }
        switch (type) {
            case 0:
                QDAccessibilityService.recentApps(QDAccessibilityService.getService(), AccessibilityService.GLOBAL_ACTION_HOME);
                break;
            case 1:
                QDAccessibilityService.recentApps(QDAccessibilityService.getService(), AccessibilityService.GLOBAL_ACTION_RECENTS);
                break;
            case 2:
                QDAccessibilityService.recentApps(QDAccessibilityService.getService(), AccessibilityService.GLOBAL_ACTION_BACK);
                break;
        }
    }

    private void onback() {
        InputManager inputManager = (InputManager) getContext().getSystemService(Context.INPUT_SERVICE);

        long now = SystemClock.uptimeMillis();
        KeyEvent down = new KeyEvent(now, now, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_1, 0);
        //inputManager.injectInputEvent(down, InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
        invokeInjectInputEventMethod(inputManager, down, 0);
        KeyEvent up = new KeyEvent(now, now, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_1, 0);
        //inputManager.injectInputEvent(up, InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
        invokeInjectInputEventMethod(inputManager, up, 0);
    }

    private void invokeInjectInputEventMethod(InputManager inputManager, InputEvent event, int mode) {
        Class<?> clazz = null;
        Method injectInputEventMethod = null;
        Method recycleMethod = null;
        try {
            clazz = Class.forName("android.hardware.input.InputManager");
        } catch (ClassNotFoundException e) {
            QDLogger.e(e);
        }
        try {
            injectInputEventMethod = clazz.getMethod("injectInputEvent", InputEvent.class, int.class);
        } catch (NoSuchMethodException e) {
            QDLogger.e(e);
        }
        try {
            injectInputEventMethod.invoke(inputManager, event, mode);
            // 准备回收event的方法
            recycleMethod = event.getClass().getMethod("recycle");
            //执行event的recycle方法
            recycleMethod.invoke(event);
        } catch (IllegalAccessException e) {
            QDLogger.e(e);
        } catch (InvocationTargetException e) {
            QDLogger.e(e);
        } catch (NoSuchMethodException e) {
            QDLogger.e(e);
        }

    }

    float clickX = 0;
    float clickY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 3000);
        clickX = event.getX();
        clickY = event.getY();
        return super.onTouchEvent(event);
    }

    public interface OnClickListener {
        void onClick(MotionEvent event);
    }

    Map<Integer, Point> pointMap = new HashMap<>();

    public class OnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        private long startTime = 0;
        private long endTime = 0;
        private boolean isclick;

        View targetView;
        WindowManager windowManager;
        OnClickListener onClickListener;

        public OnTouchListener(Context context, View targetView, OnClickListener onClickListener) {
            this.targetView = targetView;
            windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
            this.onClickListener = onClickListener;
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    isclick = false;//当按下的时候设置isclick为false
                    startTime = System.currentTimeMillis();
                    //System.out.println("执行顺序down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    isclick = true;//当按钮被移动的时候设置isclick为true,就拦截掉了点击事件

                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;

                    WindowManager.LayoutParams layoutParams;
                    if (targetView != null) {
                        if (targetView.getLayoutParams() instanceof WindowManager.LayoutParams) {
                            layoutParams = (WindowManager.LayoutParams) targetView.getLayoutParams();
                            layoutParams.x = layoutParams.x + movedX;
                            layoutParams.y = layoutParams.y + movedY;
                            windowManager.updateViewLayout(targetView, layoutParams);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    endTime = System.currentTimeMillis();
                    //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                    if ((endTime - startTime) > 0.15 * 1000L) {
                        isclick = true;
                        // onClickListener.onClick(event);
                    } else {
                        isclick = false;
                    }
                    break;
                default:
                    break;
            }
            return isclick;
        }
    }

    int maxWith = -1;
    int maxHeight = -1;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (maxWith == -1) {
            maxHeight = getHeight();
            maxWith = getWidth();
        }
        drawView(canvas);
        if (!isPlaying) {

        }
    }

    private boolean isPlaying = false;
    private int button_width = 0;
    int[] images = {R.drawable.ic_home_black_24dp, R.drawable.ic_view_carousel_black_24dp, R.drawable.ic_trending_flat_black_24dp};

    private void drawView(Canvas canvas) {
        Paint paintb = new Paint();
        paintb.setAntiAlias(true);
        paintb.setColor(Color.BLACK);
        paintb.setAlpha(80);
        RectF rectFb = new RectF(0, 0, getWidth(), getHeight());

        canvas.drawRoundRect(rectFb, DisplayUtil.dip2px(getContext(), 15), DisplayUtil.dip2px(getContext(), 15), paintb);

        button_width = maxWith / 3 * 2;
        int centX = getWidth() / 2;
        int centY = getHeight() / 2;
        if (isExpanded) {
            int count = 3;
            for (int i = 0; i < count; i++) {
                int r = getWidth() / 2 - button_width / 3 * 2;
                double a = Math.toRadians(i * 360 / count - 90);
                int x1 = (int) (centX + r * Math.cos(a));
                int y1 = (int) (centY + r * Math.sin(a));
                pointMap.put(i, new Point(x1, y1));
            }
            for (int i = 0; i < pointMap.entrySet().size(); i++) {
                Point point = pointMap.get(i);
                RectF rectF = new RectF(point.x - button_width / 2, point.y - button_width / 2, point.x + button_width / 2, point.y + button_width / 2);
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setAlpha(200);
                paint.setAntiAlias(true);
                //canvas.rotate(360 / count, centX, centY);
                canvas.drawRoundRect(rectF, button_width / 2, button_width / 2, paint);
                Bitmap bitmap = getBitmap(getContext(), images[i]);
                Rect bitmapSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                canvas.drawBitmap(bitmap, bitmapSrcRect, rectF, paint);
                //canvas.drawBitmap(bitmap,0,0,paint);
            }
        } else {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setAlpha(200);
            paint.setAntiAlias(true);
            RectF rectF = new RectF(centX - maxWith / 3, centY - maxWith / 3, centX + maxWith / 3, centY + maxWith / 3);
            canvas.drawRoundRect(rectF, maxWith / 2, maxWith / 2, paint);
        }
    }

    private static Bitmap getBitmap(Context context, int vectorDrawableId) {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
        }
        return bitmap;
    }


    public void setSize() {
        ViewGroup.LayoutParams lp = getLayoutParams();
        int w = ((ViewGroup) getParent()).getMinimumWidth();//DisplayUtil.dip2px(getContext(),60);
        //QDLogger.i("w=" + w);
        lp.width = (int) ((maxWith) * (progress / 360) + w);
        lp.height = lp.width;
        requestLayout();
        //mLayoutParams = lp;
        //setLayoutParams(lp);
    }

    private float progress;
    ValueAnimator animator;

    public void startAnimation() {
        isPlaying = true;
        final int start = 0;
        final int end = 360;
        if (isExpanded) {
            animator = ValueAnimator.ofInt(start, end);
        } else {
            animator = ValueAnimator.ofInt(end, start);
        }
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (getVisibility() == VISIBLE) {
                    int value = (int) animation.getAnimatedValue();
                    progress = value;
                    //Log.d(TAG, "progress=" + progress);
                    setSize();
                }
            }
        });
        // animator.setRepeatMode(ValueAnimator.RESTART);
        // animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    public void stopAnimation() {
        isExpanded = !isExpanded;
        startAnimation();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 3000);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isExpanded) {
                isExpanded = false;
                startAnimation();
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null)
            animator.cancel();
    }
}