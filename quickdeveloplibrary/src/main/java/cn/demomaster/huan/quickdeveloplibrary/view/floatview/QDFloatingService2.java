package cn.demomaster.huan.quickdeveloplibrary.view.floatview;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * 悬浮按钮
 * Created
 */
public abstract class QDFloatingService2 extends Service implements QdFloatingServiceInterFace {
    public WindowManager windowManager;
    public WindowManager.LayoutParams layoutParams;

    public Service getInstance() {
        return this;
    }

   /* @Override
    public boolean isShowing() {
        return false;
    }*/

    /**
     * 窗口消失
     */
    @Override
    public void onDismiss() {
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        QDLogger.println("服务启动" + this.getClass().getName());
        ServiceHelper.onCreateService(this);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        onCreateView(getApplicationContext(), windowManager);
        //contentView.setBackgroundColor(getResources().getColor(R.color.transparent_dark_33));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public abstract void onCreateView(Context context, WindowManager windowManager);

    public void updateViewLayout(View view, int width, int height) {
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        windowManager.updateViewLayout(view, layoutParams);
    }

    /**
     * 获取窗口左上角坐标
     *
     * @return
     */
    public abstract PointF getOriginPoint();

    /**
     * 悬浮窗是否可以触摸
     *
     * @return
     */
    public boolean getTouchAble() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QDLogger.e("onDestroyService,this:"+this);
        ServiceHelper.onDestroyService(this);
    }
    
    public void removeView(View view) {
        windowManager.removeView(view);
    }

    public void dissmissWindow() {
        getApplicationContext().stopService(new Intent(getApplicationContext(), this.getClass()));
    }

    private PopupWindow.OnDismissListener onDismissListener;

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

}
