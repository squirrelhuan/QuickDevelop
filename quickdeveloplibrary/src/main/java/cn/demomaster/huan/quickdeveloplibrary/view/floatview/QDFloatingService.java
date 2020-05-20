package cn.demomaster.huan.quickdeveloplibrary.view.floatview;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.sql.DatabaseMetaData;

import cn.demomaster.huan.quickdeveloplibrary.service.QDAccessibilityService;

/**
 * 悬浮按钮
 * Created
 */

public abstract class QDFloatingService extends Service {
    private static boolean isShowing = false;
    private static QDFloatingService instance;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    public static QDFloatingService getInstance() {
        return instance;
    }

    private View contentView;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setIsShowing(true);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        if(!getTouchAble()) {
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }

        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.x = 00;
        layoutParams.y = 00;
        PointF pointF = getOriginPoint();
        if (pointF != null) {
            layoutParams.x = (int) pointF.x;
            layoutParams.y = (int) pointF.y;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initContentView();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initContentView() {
        if (contentView != null && contentView.getParent() != null) {
            windowManager.removeView(contentView);
        }
        contentView = setContentView(getApplicationContext());
        if(getTouchAble()) {
            contentView.setOnTouchListener(new FloatingOnTouchListener(this, contentView));
        }
        if (contentView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    windowManager.addView(contentView, layoutParams);
                }
            } else {
                windowManager.addView(contentView, layoutParams);
            }
        }
        init();
    }

    public abstract View setContentView(Context context);

    public abstract PointF getOriginPoint();

    public abstract void init();

    public boolean getTouchAble(){
        return true;
    }

    public View getContentView() {
        return contentView;
    }

    public static class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        private long startTime = 0;
        private long endTime = 0;
        private boolean isclick;

        View targetView;
        WindowManager windowManager;

        public FloatingOnTouchListener(Context context, View targetView) {
            this.targetView = targetView;
            windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeView();
    }

    private void removeView() {
        setIsShowing(false);
        if (contentView.getParent() != null) {
            windowManager.removeView(contentView);
        }
    }

    public void setIsShowing(boolean isShowing) {
        if (isShowing != QDFloatingService.isShowing) {
            if (!isShowing && onDismissListener != null) {
                onDismissListener.onDismiss();
            }
            QDFloatingService.isShowing = isShowing;
        }
    }

    public static boolean isShowing() {
        return isShowing;
    }

    public static void showWindow(Context context,Class clazz) {
        if (!isShowing) {
            context.getApplicationContext().startService(new Intent(context.getApplicationContext(), clazz));
        }
    }

    public static void dissmissWindow(Context context,Class clazz) {
        if (isShowing) {
            context.getApplicationContext().stopService(new Intent(context.getApplicationContext(), clazz));
            if (onDismissListener != null) {
                onDismissListener.onDismiss();
            }
            QDAccessibilityService.setOnAccessibilityEventListener(null);
        }
    }

    private static PopupWindow.OnDismissListener onDismissListener;
    public static void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        onDismissListener = onDismissListener;
    }

}
