package cn.demomaster.huan.quickdeveloplibrary.view.floatview;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import cn.demomaster.huan.quickdeveloplibrary.model.EventMessage;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.quickevent_library.core.QuickEvent;

/**
 * 悬浮按钮
 * Created
 */
public abstract class QDFloatingService2 extends Service {
    public WindowManager windowManager;
    public WindowManager.LayoutParams layoutParams;

    @Override
    public void onCreate() {
        super.onCreate();
        QDLogger.println("服务启动" + this.getClass().getName());
        QuickEvent.getDefault().post(new EventMessage(0,this.getClass(),0));
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
        QDLogger.println("onDestroyService,this:"+this);
        QuickEvent.getDefault().post(new EventMessage(0,this.getClass(),1));
    }
    
    public void removeView(View view) {
        if(windowManager!=null) {
            windowManager.removeView(view);
        }
    }

    public void dissmissWindow() {
        getApplicationContext().stopService(new Intent(getApplicationContext(), this.getClass()));
    }

}
