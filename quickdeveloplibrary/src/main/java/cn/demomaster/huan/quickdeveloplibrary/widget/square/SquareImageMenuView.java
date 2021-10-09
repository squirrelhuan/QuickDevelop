package cn.demomaster.huan.quickdeveloplibrary.widget.square;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.input.InputManager;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.animation.QDValueAnimator;
import cn.demomaster.huan.quickdeveloplibrary.model.EventMessage;
import cn.demomaster.huan.quickdeveloplibrary.service.AccessibilityHelper;
import cn.demomaster.huan.quickdeveloplibrary.service.QDAccessibilityService;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDBitmapUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.ServiceHelper;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.OnReleaseListener;
import cn.demomaster.quickevent_library.core.QuickEvent;

/**
 * 正方形的ImageView
 */
public class SquareImageMenuView extends View implements OnReleaseListener {

    public SquareImageMenuView(Context context) {
        super(context);
        init();
    }

    public SquareImageMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SquareImageMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (maxWith == -1) {
            maxHeight = getMeasuredHeight();
            maxWith = getMeasuredWidth();
            miniWidth = getMeasuredWidth();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    public static String SquareImageMenuView_X_SP = "SquareImageMenuView_X_SP";
    public static String SquareImageMenuView_Y_SP = "SquareImageMenuView_Y_SP";

    private void init() {
        onClickListener = v -> {
            //QDLogger.println("触发点击事件 onClick=" + animator);
            if (animator != null) {
                //QDLogger.println("animator=" + animator.getState() + ",isHasReversed=" + animator.isHasReversed());
                if (animator.getState() == QDValueAnimator.AnimationState.isOpened || animator.getState() == QDValueAnimator.AnimationState.isOpening) {
                    boolean canDoAction = false;
                    for (Map.Entry entry : pointMap.entrySet()) {
                        Point point = (Point) entry.getValue();
                        if (Math.abs(point.x - clickX) < button_width && Math.abs(point.y - clickY) < button_width) {
                            //QDLogger.i("clicked=" + entry.getKey());
                            int id = (int) entry.getKey();
                            doAction(id);
                            canDoAction = true;
                            break;
                        }
                    }
                    if (canDoAction) {
                        stopAnimation();
                    }
                } else {
                    startAnimation();
                }
            } else {
                startAnimation();
            }
        };
        setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener;

    /**
     * 执行动作
     *
     * @param type
     */
    public void doAction(int type) {
       /* Vibrator vibrator = (Vibrator) getContext().getSystemService(getContext().VIBRATOR_SERVICE);
        long[] patter = {50, 50};
        vibrator.vibrate(patter, -1);*/
        if (type == 0 && !AccessibilityHelper.isEnable(getContext(), QDAccessibilityService.class)) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            getContext().startActivity(intent);
            return;
        }
        if (!AccessibilityHelper.isEnable(getContext(), QDAccessibilityService.class)) {
            //跳转系统自带界面 辅助功能界面
            QDAccessibilityService.startSettintActivity(getContext());
            return;
        }
       /* if (!ServiceHelper.serverIsRunning(getContext(),QDAccessibilityService.class.getName())) {
            AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();
            serviceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
            serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
            serviceInfo.packageNames = new String[]{getContext().getPackageName()};
            serviceInfo.notificationTimeout = 100;
            //QDAccessibilityService.getService().setServiceInfo(serviceInfo);
            return;
        }*/
        //QDAccessibilityService qdAccessibilityService = AccessibilityHelper.getService();
        if (ServiceHelper.serverIsRunning(getContext(),QDAccessibilityService.class.getName())) {
            switch (type) {
                case 0:
                    EventMessage eventMessage = new EventMessage("performGlobalAction",AccessibilityService.GLOBAL_ACTION_HOME);
                    QuickEvent.getDefault().post(QDAccessibilityService.class,eventMessage);
                    //qdAccessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                    break;
                case 1:
                    EventMessage eventMessage1 = new EventMessage("performGlobalAction",AccessibilityService.GLOBAL_ACTION_RECENTS);
                    QuickEvent.getDefault().post(QDAccessibilityService.class,eventMessage1);
                    //qdAccessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
                    break;
                case 2:
                    EventMessage eventMessage2 = new EventMessage("performGlobalAction",AccessibilityService.GLOBAL_ACTION_BACK);
                    QuickEvent.getDefault().post(QDAccessibilityService.class,eventMessage2);
                    //qdAccessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    break;
            }
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
            injectInputEventMethod = clazz.getMethod("injectInputEvent", InputEvent.class, int.class);
            injectInputEventMethod.invoke(inputManager, event, mode);
            // 准备回收event的方法
            recycleMethod = event.getClass().getMethod("recycle");
            recycleMethod.invoke(event);
        } catch (Exception e) {
            QDLogger.e(e);
        }
    }

    float clickX = 0;
    float clickY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        clickX = event.getX();
        clickY = event.getY();
        return super.onTouchEvent(event);
    }

    WindowManager windowManager;

    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    @Override
    public void onRelease(Object self) {
        onDetachedFromWindow();
    }

    public interface OnClickListener {
        void onClick(MotionEvent event);
    }

    Map<Integer, Point> pointMap = new HashMap<>();
   /* public class OnTouchListener implements View.OnTouchListener {
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
                    break;
                case MotionEvent.ACTION_MOVE:
                    //System.out.println("触摸 滑动 down "+targetView.getLayoutParams());
                    isclick = true;//当按钮被移动的时候设置isclick为true,就拦截掉了点击事件
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;

                    WindowManager.LayoutParams layoutParams;
                    if (targetView.getLayoutParams() instanceof WindowManager.LayoutParams) {
                        layoutParams = (WindowManager.LayoutParams) targetView.getLayoutParams();
                        layoutParams.x = layoutParams.x + movedX;
                        layoutParams.y = layoutParams.y + movedY;
                        QDSharedPreferences.getInstance().putInt(SquareImageMenuView_X_SP, layoutParams.x);
                        QDSharedPreferences.getInstance().putInt(SquareImageMenuView_Y_SP, layoutParams.y);
                        windowManager.updateViewLayout(targetView, layoutParams);
                    }
                    delayedStopAnimation();
                    break;
                case MotionEvent.ACTION_UP:
                    endTime = System.currentTimeMillis();
                    //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                    isclick = ((endTime - startTime) > 0.15 * 1000L) ;
                    // onClickListener.onClick(event);
                    break;
                default:
                    break;
            }
            return isclick;
        }
    }*/

    int maxWith = -1;
    int maxHeight = -1;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);
    }

    private int miniWidth = -1;
    private int button_width = 0;
    int[] images = {R.drawable.ic_home_black_24dp, R.drawable.ic_view_carousel_black_24dp, R.drawable.ic_trending_flat_black_24dp};//R.drawable.ic_baseline_settings_24,

    private void drawView(Canvas canvas) {
        float progress2 = progress / 360;
        Paint paintb = new Paint();
        paintb.setAntiAlias(true);
        paintb.setColor(Color.BLACK);
        paintb.setAlpha((int) (100 * progress2) + 70);
        int rectWidth = (int) ((maxWith) * (progress2) + miniWidth);
        RectF rectFb = new RectF(0, 0, rectWidth, rectWidth);
        canvas.drawRoundRect(rectFb, DisplayUtil.dip2px(getContext(), 15), DisplayUtil.dip2px(getContext(), 15), paintb);

        button_width = maxWith / 3 * 2;
        int centX = (int) (rectFb.width() / 2);
        int centY = (int) (rectFb.width() / 2);
        if (animator != null && animator.getState() != QDValueAnimator.AnimationState.isClosed) {
            int count = images.length;
            for (int i = 0; i < count; i++) {
                int r = (int) (progress2 * (rectWidth / 2 - button_width / 3 * 2));
                double a = Math.toRadians(i * 360f / count - 90);
                int x1 = (int) (centX + r * Math.cos(a));
                int y1 = (int) (centY + r * Math.sin(a));
                pointMap.put(i, new Point(x1, y1));
            }
            for (int i = 0; i < pointMap.entrySet().size(); i++) {
                Point point = pointMap.get(i);
                RectF rectF = new RectF(point.x - button_width / 2f, point.y - button_width / 2f, point.x + button_width / 2f, point.y + button_width / 2f);
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setAlpha((int) (180 * (progress2)));
                paint.setAntiAlias(true);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                //canvas.rotate(360 / count, centX, centY);
                canvas.drawRoundRect(rectF, button_width / 2f, button_width / 2f, paint);
                paint.setAlpha((int) (10 + 200 * (progress2)));
                Bitmap bitmap = QDBitmapUtil.getBitmapByDrawableId(getContext(), images[i]);
                //bitmap = QDBitmapUtil.setBitmapLight(bitmap, (int) (100+progress2*150));
                Rect bitmapSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                canvas.drawBitmap(bitmap, bitmapSrcRect, rectF, paint);
                //canvas.drawBitmap(bitmap,0,0,paint);
                paint.setXfermode(null);
            }
        } else {
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            //paint.setColor(Color.WHITE);
            int color = 0xffffff;
            paint.setColor(color);
            paint.setAlpha((int) (100 + 125 * (1 - progress2)) / 7 * 6);
            paint.setAntiAlias(true);
            int r = (int) ((1 - progress2) * maxWith / 3);
            RectF rectF = new RectF(centX - r, centY - r, centX + r, centY + r);
            canvas.drawRoundRect(rectF, maxWith / 2f, maxWith / 2f, paint);
            paint.setXfermode(null);
        }
    }

    public void setSize() {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = (int) ((maxWith) * (progress / 360f) + miniWidth);
        lp.height = lp.width;
        //setLayoutParams(lp);
        //requestLayout();
        //View parentView = (View) this.getParent();
        if (windowManager != null) {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) getLayoutParams();
            layoutParams.width = lp.width;
            layoutParams.height = lp.height;
            //QDLogger.println("setSize w1=" + lp.width + ",w2=" + layoutParams.width);
            windowManager.updateViewLayout(this, layoutParams);
        }
    }

    private float progress;
    QDValueAnimator animator;
    QDValueAnimator.AnimationListener animationListener;
    public void startAnimation() {
        QDLogger.println("开启动画");
        if (animator == null) {
            final int start = 0;
            final int end = 360;
            animator = new QDValueAnimator(Integer.class);
            animator.setIntValues(start, end);
            animator.setDuration(200);
            if (animationListener == null) {
                animationListener = new QDValueAnimator.AnimationListener() {
                    @Override
                    public void onStartOpen(Object value) {
                        //Log.d(TAG, "onStartOpen");
                    }

                    @Override
                    public void onOpening(Object value) {
                        //Log.d(TAG, "onOpening");
                        if (getVisibility() == VISIBLE) {
                            progress = (int) value;
                            //Log.d(TAG, "progress=" + progress);
                            setSize();
                        }
                    }

                    @Override
                    public void onEndOpen(Object value) {
                       // Log.d(TAG, "onEndOpen");
                        delayedStopAnimation();
                    }

                    @Override
                    public void onStartClose(Object value) {
                        //Log.d(TAG, "onStartClose");
                    }

                    @Override
                    public void onClosing(Object value) {
                        //Log.d(TAG, "onClosing");
                        if (getVisibility() == VISIBLE) {
                            progress = (int) value;
                            //Log.d(TAG, "progress=" + progress);
                            setSize();
                        }
                    }

                    @Override
                    public void onEndClose(Object value) {
                        //Log.d(TAG, "onEndClose");
                        postInvalidate();
                    }
                };
            }
            animator.setAnimationListener(animationListener);
            // animator.setRepeatMode(ValueAnimator.RESTART);
            // animator.setRepeatCount(ValueAnimator.INFINITE);
            if (interpolator == null) {
                interpolator = new AccelerateInterpolator();
            }
            animator.setInterpolator(interpolator);
        }
        animator.start();
    }

    AccelerateInterpolator interpolator;

    public void stopAnimation() {
        //Log.d(TAG, "关闭动画");
        handler.removeCallbacks(runnable);
        if (animator != null && !animator.isHasReversed()) {
            animator.reverse();
        }
    }

    public void delayedStopAnimation() {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 3500);
    }

    Handler handler = new Handler();
    Runnable runnable = () -> stopAnimation();

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onClickListener = null;
        setOnClickListener(null);
        setOnTouchListener(null);
        if (animator != null) {
            animator.setAnimationListener(null);
            if (animator.isRunning()) {
                animator.cancel();
            }
        }
        animationListener = null;
        animator = null;
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        runnable = null;
        windowManager = null;
    }
}