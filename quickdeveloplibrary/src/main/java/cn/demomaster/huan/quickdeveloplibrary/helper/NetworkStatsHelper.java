package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.annotation.SuppressLint;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.RemoteException;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresApi;

import cn.demomaster.qdlogger_library.QDLogger;

import static android.content.Context.NETWORK_STATS_SERVICE;

public class NetworkStatsHelper {
    //以上的方法封装在了NetworkStatsHelper类里

    @RequiresApi(api = Build.VERSION_CODES.M)
    public NetworkStatsHelper(Context context) {
        //初始化这个工具类
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(NETWORK_STATS_SERVICE);
    }


    /**
     * 本机使用的 wifi 总流量
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public long getAllBytesWifi(Context context) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(NETWORK_STATS_SERVICE);
        NetworkStats.Bucket bucket = null;
        for (int i = 0; i < 17; i++) {
            try {
                bucket = networkStatsManager.querySummaryForDevice(i,
                        "",
                        0,
                        System.currentTimeMillis());

                if(bucket!=null){
                    QDLogger.d("bucket="+i+","+bucket.getTxBytes() + bucket.getRxBytes());
                }
            } catch (RemoteException e) {
                QDLogger.e(e);
                return -1;
            }
        }
        if(bucket==null){
            QDLogger.d("bucket=null");
            return 0;
        }
        //这里可以区分发送和接收
        return bucket.getTxBytes() + bucket.getRxBytes();
    }

    /**
     * 本机使用的 mobile 总流量
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public long getAllBytesMobile(Context context) {
        NetworkStats.Bucket bucket;
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(NETWORK_STATS_SERVICE);
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        //这里可以区分发送和接收
        return bucket.getTxBytes() + bucket.getRxBytes();
    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    private String getSubscriberId(Context context, int networkType) {
        if (ConnectivityManager.TYPE_MOBILE == networkType) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSubscriberId();
        }
        return "";
    }

    /**
     * 获取指定应用 wifi 发送的当天总流量
     *
     * @param packageUid 应用的uid
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public long getPackageTxDayBytesWifi(Context context, int packageUid) {
        NetworkStats networkStats = null;
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(NETWORK_STATS_SERVICE);
        long timesmorning = getTimesmorning();
        QDLogger.d("timesmorning=" + timesmorning);
        networkStats = networkStatsManager.queryDetailsForUid(
                ConnectivityManager.TYPE_MOBILE,
                "",
                timesmorning,
                System.currentTimeMillis(),
                packageUid);
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        networkStats.getNextBucket(bucket);
        return bucket.getTxBytes();
    }

    /**
     * 获取当天的零点时间
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static long getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis());
    }

    //获得本月第一天0点时间
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int getTimesMonthmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return (int) (cal.getTimeInMillis());
    }

    /**
     * 根据包名获取uid
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public static int getUidByPackageName(Context context, String packageName) {
        int uid = -1;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            uid = packageInfo.applicationInfo.uid;
        } catch (PackageManager.NameNotFoundException e) {
            QDLogger.e(e);
        }
        return uid;
    }

}
