package cn.demomaster.huan.quickdeveloplibrary.view.floatview;


import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.os.Build;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDActivityManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.QdThreadHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.FPSMonitor;

/**
 * Created
 */
public class FpsFloatingService extends QDFloatingService2 {
    LinearLayout linearLayout;

    @Override
    public void onCreateView(Context context, WindowManager windowManager) {
        linearLayout = new LinearLayout(context.getApplicationContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView button = new TextView(context);
        button.setText("fps");
        button.setPadding(10, 10, 10, 10);
        button.setBackgroundColor(context.getResources().getColor(R.color.transparent_dark_99));
        button.setTextColor(context.getResources().getColor(R.color.white));
        fpsMonitor.setOnFramChangedListener(new FPSMonitor.OnFramChangedListener() {
            @Override
            public void onChanged(float mframe) {
                frame = mframe;
                if (QDActivityManager.getInstance().getCurrentActivity() != null) {
                    Display display = QDActivityManager.getInstance().getCurrentActivity().getWindowManager().getDefaultDisplay();
                    float refreshRate = display.getRefreshRate();
                    QdThreadHelper.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            button.setText("frame:" + frame);
                            ///QDLogger.i("rate:"+refreshRate);
                        }
                    });
                }
            }
        });
        fpsMonitor.start();

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
        linearLayout.addView(button);
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        windowManager.addView(linearLayout, layoutParams);
        linearLayout.setOnTouchListener(new QDFloatingService.FloatingOnTouchListener(linearLayout));
    }

    @Override
    public PointF getOriginPoint() {
        return new PointF(100, 100);
    }

    float frame = 0;
    FPSMonitor fpsMonitor = new FPSMonitor();

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeView(linearLayout);
        if (fpsMonitor != null)
            fpsMonitor.stop();
    }
}
