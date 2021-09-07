package cn.demomaster.huan.quickdeveloplibrary.network;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.OnReleaseListener;

import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_UNKNOWN;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_CAR;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

public class NetworkHelper {
    public NetworkHelper() {

    }

    /**
     * 检查网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    public static String int2ip(int ipInt) {
        String sb = (ipInt & 0xFF) + "." +
                ((ipInt >> 8) & 0xFF) + "." +
                ((ipInt >> 16) & 0xFF) + "." +
                ((ipInt >> 24) & 0xFF);
        return sb;
    }

    /**
     * 获取当前ip地址
     *
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return int2ip(i);
        } catch (Exception ex) {
            QDLogger.e(ex);
        }
        return null;
    }

    //GPRS连接下的ip
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("getLocalIpAddress", ex.toString());
        }
        return null;
    }

    public static String getWifiStateDes(int wifiState) {
        switch (wifiState) {
            case WIFI_STATE_DISABLING://WIFI网卡正在关闭  0
                return "WIFI网卡正在关闭";
            case WIFI_STATE_DISABLED:// WIFI网卡不可用  1
                return "WIFI网卡不可用";
            case WIFI_STATE_ENABLING://WIFI网卡正在打开  2
                return "WIFI网卡正在打开";
            case WIFI_STATE_ENABLED://WIFI网卡可用  3
                return "WIFI网卡可用";
            case WIFI_STATE_UNKNOWN://WIFI网卡状态不可知 4
                return "WIFI网卡状态不可知";
            default:
                return "WIFI default";
        }
    }

    public static String getActionDescription(String action) {
        String des = "";
        switch (action) {
            case WifiManager.WIFI_STATE_CHANGED_ACTION://数据流量状态变化
                des = "2G/3G/4G网络";
                break;
            case WifiManager.NETWORK_STATE_CHANGED_ACTION://wifi状态变化
                des = "wifi";
                break;
            case ConnectivityManager.CONNECTIVITY_ACTION://连接状态改变
                des = "连接状态改变";
                break;
        }
        return des;
    }

    public static String getNetworkTypeDescription(int type) {
        switch (type) {
            case TYPE_MOBILE:
                return "正在使用2G/3G/4G网络";
            case TYPE_WIFI:
                return "正在使用wifi上网";
            case TYPE_CAR:
                return "正在使用car上网";
            default:
                return "正在使用" + type + "上网";
        }
    }

    /*public void registerListener(Context context, NetWorkBroadcastReceiver netWorkBroadcastReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(netWorkBroadcastReceiver, filter);
    }*/
}
