package cn.demomaster.huan.quickdeveloplibrary.view.floatview;


import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.lifecycle.LifeCycleClassInfo;
import cn.demomaster.huan.quickdeveloplibrary.lifecycle.LifeCycleEvent;
import cn.demomaster.huan.quickdeveloplibrary.lifecycle.LifecycleView;
import cn.demomaster.huan.quickdeveloplibrary.util.lifecycle.LifecycleManager;
import cn.demomaster.huan.quickdeveloplibrary.util.system.QDAppInfoUtil;

import static cn.demomaster.qdlogger_library.QDLogger.isDebug;

/**
 * 生命周期悬浮窗
 * Created
 */
public class LifecycleFloatingService extends QDFloatingService2 {

    static Context mcontext;
    static ImageView iv_drag;
    static ImageView iv_logo;
    static TextView tv_title;
    static LifecycleView timeDomainPlotView;
    View view;

    @Override
    public void onCreateView(Context context, WindowManager windowManager) {
        mcontext = context;
        view = LayoutInflater.from(context).inflate(R.layout.layout_debug_lifecycle, null, false);

        timeDomainPlotView = view.findViewById(R.id.timeDomainPlotView);
        LinkedHashMap<LifeCycleClassInfo, List<LifeCycleEvent>> listLinkedHashMap = LifecycleManager.getInstance().getLifecycleTimerData().getLinePointsMap();
        timeDomainPlotView.setLinePoints(listLinkedHashMap);
        iv_drag = view.findViewById(R.id.iv_drag);
        onTouchListener = new QDFloatingService.FloatingOnTouchListener(iv_drag);
        iv_drag.setOnTouchListener(onTouchListener);
        iv_logo = view.findViewById(R.id.iv_logo);
        iv_logo.setImageDrawable(QDAppInfoUtil.getAppIconDrawable(mcontext));

        tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText("" + QDAppInfoUtil.getVersionName(context));
        TextView tv_close = view.findViewById(R.id.tv_close);
        tv_close.setOnClickListener(v -> dissmissWindow());
        handler.postDelayed(runnable, 1000);
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
        layoutParams.width = 800;
        layoutParams.height = 500;
        this.windowManager = windowManager;
        windowManager.addView(view, layoutParams);
        onTouchListener1 = new QDFloatingService.FloatingOnTouchListener(view);
        view.setOnTouchListener(onTouchListener1);
    }

    private static final Handler handler = new Handler();
    static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (timeDomainPlotView != null) {
                LinkedHashMap<LifeCycleClassInfo, List<LifeCycleEvent>> listLinkedHashMap = LifecycleManager.getInstance().getLifecycleTimerData().getLinePointsMap();
                timeDomainPlotView.setLinePoints(listLinkedHashMap);
                handler.postDelayed(runnable, 1000);
            }
        }
    };

    public QDFloatingService.FloatingOnTouchListener onTouchListener1;
    public QDFloatingService.FloatingOnTouchListener onTouchListener;
    /*int drag_X;
    int drag_Y;
    public View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drag_X = (int) event.getRawX();
                    drag_Y = (int) event.getRawY();
                    //isclick = false;//当按下的时候设置isclick为false
                    //startTime = System.currentTimeMillis();
                    //System.out.println("执行顺序down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    //isclick = true;//当按钮被移动的时候设置isclick为true,就拦截掉了点击事件
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - drag_X;
                    int movedY = nowY - drag_Y;
                    drag_X = nowX;
                    drag_Y = nowY;

                    WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) view.getLayoutParams();
                    int height = layoutParams.height + movedY;
                    height = Math.min(DisplayUtil.getScreenHeight(v.getContext()), height);
                    int width = layoutParams.width + movedX;
                    updateViewLayout(view, width, height);
                    break;
                case MotionEvent.ACTION_UP:
                    *//*endTime = System.currentTimeMillis();
                    //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                    if ((endTime - startTime) > 0.15 * 1000L) {
                        isclick = true;
                    } else {
                        isclick = false;
                    }*//*
                    break;
                default:
                    break;
            }
            return true;
        }
    };*/

    @Override
    public PointF getOriginPoint() {
        return new PointF(100, 100);
    }

/*    static DebugFloating2 debugFloating2;
    public void showConsole(Activity context) {
        if (isDebug(context)) {
            if (PermissionHelper.getPermissionStatus(context.getApplicationContext(), Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                //showWindow(context.getApplicationContext(), DebugFloatingService.class);
            } else {
                QDLogger.e(FloatHelper.Tag, "showConsole context= " + context);
                if (debugFloating2 == null) {
                    debugFloating2 = new DebugFloating2();
                }
                debugFloating2.show(context);
            }
        }
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        if(iv_logo!=null){
            iv_logo.setOnTouchListener(null);
        }
        if(iv_drag!=null){
            iv_drag.setOnTouchListener(null);
        }
        if(view!=null) {
            removeView(view);
            view.setOnTouchListener(null);
        }
        if(onTouchListener!=null){
            onTouchListener.onRelease(null);
        }
        if(onTouchListener1!=null){
            onTouchListener1.onRelease(null);
        }
    }
}
