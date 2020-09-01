package cn.demomaster.huan.quickdevelop.service;

import android.content.Context;
import android.graphics.PointF;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.base.QDHandler;
import cn.demomaster.huan.quickdeveloplibrary.helper.NetworkStatsHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.TrafficHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.QDFloatingService;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * 流量悬浮
 * Created
 */
public class TrafficFloatingService extends QDFloatingService {
    TextView tv_up;
    TextView tv_down;
    TextView tv_today;
    NetworkStatsHelper networkStatsHelper;
    Context mContext;

    @Override
    public View setContentView(Context context) {
        mContext = context.getApplicationContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_floating_traffic, null);
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissmissWindow(mContext, TrafficFloatingService.this.getClass());
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            networkStatsHelper = new NetworkStatsHelper(mContext);
        }
        tv_up = view.findViewById(R.id.tv_up);
        tv_down = view.findViewById(R.id.tv_down);
        tv_today = view.findViewById(R.id.tv_today);
        handler.postDelayed(runnable, 0);
        return view;
    }

    int uid;
    Handler handler = new QDHandler();
    public static class CRunnable implements Runnable{

        WeakReference<TrafficFloatingService> trafficFloatingServiceWeakReference ;
        public CRunnable(TrafficFloatingService trafficFloatingService) {
            trafficFloatingServiceWeakReference = new WeakReference<>(trafficFloatingService);
        }

        @Override
        public void run() {
            if(trafficFloatingServiceWeakReference!=null&&trafficFloatingServiceWeakReference.get()!=null) {
                String str1 = "";
                long value;
                if (trafficFloatingServiceWeakReference.get().tv_up != null) {
                    value = TrafficHelper.getInstant().getUpdateSpeed();
                    str1 = trafficFloatingServiceWeakReference.get().fomatString(value);
                    trafficFloatingServiceWeakReference.get().tv_up.setText(str1);
                }
                if (trafficFloatingServiceWeakReference.get().tv_down != null) {
                    value = TrafficHelper.getInstant().getDownloadSpeed();
                    str1 = trafficFloatingServiceWeakReference.get().fomatString(value);
                    trafficFloatingServiceWeakReference.get().tv_down.setText(str1);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    trafficFloatingServiceWeakReference.get().uid = NetworkStatsHelper.getUidByPackageName(trafficFloatingServiceWeakReference.get().mContext, trafficFloatingServiceWeakReference.get().mContext.getPackageName());
                    QDLogger.d("uid1=" + trafficFloatingServiceWeakReference.get().uid);
                    //value = networkStatsHelper.getAllBytesWifi(mContext);
                    value = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
                    str1 = "total:" + trafficFloatingServiceWeakReference.get().fomatString(value);
                    trafficFloatingServiceWeakReference.get().tv_today.setText(str1);
                }

                trafficFloatingServiceWeakReference.get().handler.postDelayed(trafficFloatingServiceWeakReference.get().runnable, 1000);
            }
        }
    }
    Runnable runnable = new CRunnable(this);
    private String fomatString(long value) {
        String str1 = "";
        if (value > 1073741824) {
            str1 = value / 1073741824 + "gb";
        } else if (value > 1048576) {
            str1 = value / 1048576 + "mb";
        } else if (value > 1024) {
            str1 = value / 1024 + "kb";
        } else if (value < 1024) {
            str1 = value + "bit";
        }
        return str1;
    }

    @Override
    public PointF getOriginPoint() {
        //设置默认原点坐标
        return new PointF(0, 120);
    }

    @Override
    public void init() {

    }

}
