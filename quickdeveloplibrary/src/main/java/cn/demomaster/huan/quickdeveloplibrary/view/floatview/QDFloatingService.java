package cn.demomaster.huan.quickdeveloplibrary.view.floatview;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
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

import cn.demomaster.huan.quickdeveloplibrary.model.EventMessage;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.OnReleaseListener;
import cn.demomaster.quickevent_library.core.QuickEvent;

/**
 * 悬浮按钮
 * Created
 */
public abstract class QDFloatingService extends Service{
    private WindowManager windowManager;
    public WindowManager.LayoutParams layoutParams;
    public View contentView;
    public Service getInstance() {
        return this;
    }
    public WindowManager getWindowManager() {
        return windowManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        QDLogger.i("服务启动" + this.getClass().getName());
        QuickEvent.getDefault().post(new EventMessage(0,this.getClass(),0));
        //setIsShowing(true);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;//TYPE_APPLICATION_OVERLAY;
        } else {
            //layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        if (!getTouchAble()) {
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }

        PointF pointF = getOriginPoint();
        if (pointF != null) {
            layoutParams.x = (int) pointF.x;
            layoutParams.y = (int) pointF.y;
        }
        initContentView();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initContentView() {
        if (contentView != null && contentView.getParent() != null) {
            windowManager.removeView(contentView);
        }
        contentView = setContentView(getApplicationContext());
        //contentView.setBackgroundColor(getResources().getColor(R.color.transparent_dark_33));
        if (getTouchAble()) {
            contentView.setOnTouchListener(new FloatingOnTouchListener(contentView));
        }
        if (contentView != null) {
            updateLayoutParams();
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

    /**
     * 更新布局
     */
    public void updateLayoutParams() {
        if (layoutParams != null) {
            //QDLogger.e("updateLayoutParams width=" + mWidth + ",mHeight=" + mHeight);
            layoutParams.width = mWidth;
            layoutParams.height = mHeight;
            contentView.setLayoutParams(layoutParams);
        }
    }

    public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;

    /**
     * 设置窗口高度
     *
     * @param height
     */
    public void setHeight(int height) {
        mHeight = height;
        updateLayoutParams();
        if (contentView != null)
            windowManager.updateViewLayout(contentView, layoutParams);
    }

    /**
     * 设置窗口宽度
     *
     * @param width
     */
    public void setWidth(int width) {
        mWidth = width;
        updateLayoutParams();
        if (layoutParams != null) {
            windowManager.updateViewLayout(contentView, layoutParams);
        }
    }

    public abstract View setContentView(Context context);

    /**
     * 获取窗口左上角坐标
     *
     * @return
     */
    public abstract PointF getOriginPoint();

    public abstract void init();

    public Point getPosition() {
        layoutParams = (WindowManager.LayoutParams) contentView.getLayoutParams();
        return new Point(layoutParams.x, layoutParams.y);
    }

    public void setPosition(Point point) {
        layoutParams = (WindowManager.LayoutParams) contentView.getLayoutParams();
        layoutParams.x = point.x;
        layoutParams.y = point.y;
        contentView.setLayoutParams(layoutParams);
    }

    /**
     * 悬浮窗是否可以触摸
     *
     * @return
     */
    public boolean getTouchAble() {
        return true;
    }

    /**
     * 获取悬浮窗视图
     *
     * @return
     */
    public View getContentView() {
        return contentView;
    }

    public static class FloatingOnTouchListener implements View.OnTouchListener, OnReleaseListener {
        private int x;
        private int y;

        private long startTime = 0;
        private long endTime = 0;
        private boolean isclick;

        View targetView;//目标控件（位置要移动的控件）
        WindowManager windowManager;

        public FloatingOnTouchListener(View targetView) {
            this.targetView = targetView;
            windowManager = (WindowManager) targetView.getContext().getSystemService(WINDOW_SERVICE);
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            //System.out.println("执行顺序down");
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

                    if (targetView != null) {
                        if (targetView.getLayoutParams() instanceof WindowManager.LayoutParams) {
                            WindowManager.LayoutParams layoutParams;
                            layoutParams = (WindowManager.LayoutParams) targetView.getLayoutParams();
                            layoutParams.x = layoutParams.x + movedX;
                            layoutParams.y = layoutParams.y + movedY;
                            windowManager.updateViewLayout(targetView, layoutParams);
                        } else if (targetView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                            ViewGroup.MarginLayoutParams layoutParams;
                            layoutParams = (ViewGroup.MarginLayoutParams) targetView.getLayoutParams();
                            layoutParams.leftMargin = layoutParams.leftMargin + movedX;
                            layoutParams.topMargin = layoutParams.topMargin + movedY;
                            targetView.setLayoutParams(layoutParams);
                        } else {
                            //QDLogger.e("onTouch:" + targetView.getLayoutParams().getClass().getName());
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    endTime = System.currentTimeMillis();
                    //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                    isclick = ((endTime - startTime) > 0.15 * 1000L);
                    break;
                default:
                    break;
            }
            return isclick;
        }

        @Override
        public void onRelease(Object self) {
            targetView = null;
            windowManager = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeView();
        QDLogger.e("onDestroyService " + this.getClass().getName());
        QuickEvent.getDefault().post(new EventMessage(0,this.getClass(),1));
    }

    private void removeView() {
       // setIsShowing(false);
        if (contentView!=null&&contentView.getParent() != null) {
            if(windowManager!=null) {
                windowManager.removeView(contentView);
            }
        }
    }

}
