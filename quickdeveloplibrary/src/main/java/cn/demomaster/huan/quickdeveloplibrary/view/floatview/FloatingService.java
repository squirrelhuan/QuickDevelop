package cn.demomaster.huan.quickdeveloplibrary.view.floatview;


import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import cn.demomaster.huan.quickdeveloplibrary.helper.QDActivityManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.QdThreadHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.FPSMonitor;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.widget.FlowLayout;


/**
 * Created
 */
public class FloatingService extends QDFloatingService {
    Button button;
    @Override
    public View setContentView(final Context context) {
        LinearLayout linearLayout = new LinearLayout(context.getApplicationContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        button = new Button(context);
        button.setText("hello");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissmissWindow(context,FloatingService.this.getClass());
            }
        });
        //linearLayout.addView(button);
        return button;
    }

    @Override
    public PointF getOriginPoint() {
        return new PointF(100,100);
    }

    @Override
    public void init() {
        QDLogger.e("FloatingService init");
        handler.postDelayed(runnable,1000);
        fpsMonitor.setOnFramChangedListener(new FPSMonitor.OnFramChangedListener() {
            @Override
            public void onChanged(float mframe) {
                frame = mframe;
            }
        });
        fpsMonitor.start();
    }
    float frame = 0;
    FPSMonitor fpsMonitor = new FPSMonitor();
    Handler handler = new Handler(Looper.getMainLooper());
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(button!=null){
                if(QDActivityManager.getInstance().getCurrentActivity()!=null) {
                    Display display = QDActivityManager.getInstance().getCurrentActivity().getWindowManager().getDefaultDisplay();
                    float refreshRate = display.getRefreshRate();
                    QdThreadHelper.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            button.setText("frame:"+frame);
                            ///QDLogger.i("rate:"+refreshRate);
                        }
                    });
                }else {
                    QDLogger.e("getCurrentActivity == null");
                }
            }else {
                QDLogger.e("button == null");
            }
            handler.postDelayed(runnable,1000);
        }
    };
}
