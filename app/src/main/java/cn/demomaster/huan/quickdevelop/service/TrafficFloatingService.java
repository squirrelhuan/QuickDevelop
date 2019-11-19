package cn.demomaster.huan.quickdevelop.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.NetworkStatsHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.TrafficHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.QDFloatingService;

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
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String str1 = "";
            long value;
            if (tv_up != null) {
                value = TrafficHelper.getInstant().getUpdateSpeed();
                str1 = fomatString(value);
                tv_up.setText(str1);
            }
            if (tv_down != null) {
                value = TrafficHelper.getInstant().getDownloadSpeed();
                str1 = fomatString(value);
                tv_down.setText(str1);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uid = NetworkStatsHelper.getUidByPackageName(mContext, mContext.getPackageName());
                QDLogger.d("uid1=" + uid);
                //value = networkStatsHelper.getAllBytesWifi(mContext);
                value = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
                str1 = "total:"+fomatString(value);
                tv_today.setText(str1);
            }

            handler.postDelayed(runnable, 1000);
        }
    };

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

}
