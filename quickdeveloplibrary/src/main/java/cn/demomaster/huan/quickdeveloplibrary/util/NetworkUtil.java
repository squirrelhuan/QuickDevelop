package cn.demomaster.huan.quickdeveloplibrary.util;

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

import cn.demomaster.huan.quickdeveloplibrary.receiver.NetWorkChangReceiver;
import cn.demomaster.qdlogger_library.QDLogger;

import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_UNKNOWN;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_CAR;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

public class NetworkUtil {
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
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
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
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("getLocalIpAddress", ex.toString());
        }
        return null;
    }

    static NetWorkChangReceiver mNetWorkChangReceiver;
    static Map<NetWorkChangReceiver.OnNetStateChangedListener, Context> mNetStateChangedListenerMap;
    private static class MyNetStateChangedListener implements NetWorkChangReceiver.NetStateChangedReceiver {
        public int netState = -400;//0不可用，1可用，-400未初始化

        @Override
        public void onReceive(Context context, Intent intent) {
            dispatchReceivedEvent(context, intent);
            String action = intent.getAction();
            //QDLogger.e("action=" + action);
            if (!TextUtils.isEmpty(action)) {
                String des =getActionDescription(action);

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

                if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
                    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                    String wifiDesc = "";
                    switch (wifiState) {
                        case WIFI_STATE_DISABLING://WIFI网卡正在关闭  0
                            wifiDesc = "WIFI网卡正在关闭";
                            break;
                        case WIFI_STATE_DISABLED:// WIFI网卡不可用  1
                            wifiDesc = "WIFI网卡不可用";
                            break;
                        case WIFI_STATE_ENABLING://WIFI网卡正在打开  2
                            wifiDesc = "WIFI网卡正在打开";
                            break;
                        case WIFI_STATE_ENABLED://WIFI网卡可用  3
                            wifiDesc = "WIFI网卡可用";
                            break;
                        case WIFI_STATE_UNKNOWN://WIFI网卡状态不可知 4
                            wifiDesc = "WIFI网卡状态不可知";
                            break;
                        default:
                            wifiDesc = "WIFI default";
                            break;
                    }

                    QDLogger.println("网络状态变化,hashCode:" + hashCode() + ",action=" + action+","+wifiDesc);
                }else if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
                    //int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                }else if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                    QDLogger.println("网络状态变化,hashCode:" + hashCode() + ",action=" + action+","+des);
                    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                    if (networkInfo != null) { //如果无网络连接networkInfo为null
                        dispatchConnectedEvent(context, intent);
                    } else { //网络连接
                        dispatchDisConnectedEvent(context, intent);
                    }
                }
            }
        }

        private String getActionDescription(String action) {
            String des ="";
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

        public void dispatchReceivedEvent(Context context, Intent intent) {
                if (mNetStateChangedListenerMap != null) {
                    for (Map.Entry entry : mNetStateChangedListenerMap.entrySet()) {
                        NetWorkChangReceiver.OnNetStateChangedListener listener = (NetWorkChangReceiver.OnNetStateChangedListener) entry.getKey();
                        listener.onReceive(context, intent);
                    }
                }
        }

        public void dispatchConnectedEvent(Context context, Intent intent) {
            if (netState != 1) {
                netState = 1;
                String des = "";
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                if (networkInfo != null) { //如果无网络连接activeInfo为null
                    switch (networkInfo.getType()) {
                        case TYPE_MOBILE:
                            des = "正在使用2G/3G/4G网络";
                            break;
                        case TYPE_WIFI:
                            des = "正在使用wifi上网";
                            break;
                        case TYPE_CAR:
                            des = "正在使用car上网";
                            break;
                        default:
                            des = "正在使用" + networkInfo.getType() + "上网";
                            break;
                    }
                    QDLogger.println("网络[连接]," + des);
                    if (mNetStateChangedListenerMap != null) {
                        for (Map.Entry entry : mNetStateChangedListenerMap.entrySet()) {
                            NetWorkChangReceiver.OnNetStateChangedListener listener = (NetWorkChangReceiver.OnNetStateChangedListener) entry.getKey();
                            listener.onConnected(context, intent);
                        }
                    }
                }
            }
        }

        public void dispatchDisConnectedEvent(Context context, Intent intent) {
            if (netState != 0) {
                netState = 0;
                String des =getActionDescription(intent.getAction());
                QDLogger.println("网络[断开],"+des);
                if (mNetStateChangedListenerMap != null) {
                    for (Map.Entry entry : mNetStateChangedListenerMap.entrySet()) {
                        NetWorkChangReceiver.OnNetStateChangedListener listener = (NetWorkChangReceiver.OnNetStateChangedListener) entry.getKey();
                        listener.onDisConnected(context, intent);
                    }
                }
            }
        }
    }

    public static void registerNetworkStatusChangedListener(Context context, NetWorkChangReceiver.OnNetStateChangedListener onNetStateChangedListener) {
        if (mNetWorkChangReceiver == null) {
            mNetWorkChangReceiver = new NetWorkChangReceiver(new MyNetStateChangedListener());
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mNetStateChangedListenerMap = new HashMap<>();
            context.registerReceiver(mNetWorkChangReceiver, filter);
        }
        mNetStateChangedListenerMap.put(onNetStateChangedListener, context);
        QDLogger.println("add监听,hashcode:" + onNetStateChangedListener.hashCode());
    }

    public static void unRegisterNetworkStatusChangedListener(NetWorkChangReceiver.OnNetStateChangedListener listener) {
        //context.unregisterReceiver(mNetWorkChangReceiver);
        if (mNetStateChangedListenerMap != null) {
            mNetStateChangedListenerMap.remove(listener);
        }
    }

    public static void unRegisterNetworkStatusChangedListener(Context context) {
        //context.unregisterReceiver(mNetWorkChangReceiver);
        if (mNetStateChangedListenerMap != null) {
            for (Map.Entry entry : mNetStateChangedListenerMap.entrySet()) {
                NetWorkChangReceiver.OnNetStateChangedListener listener = (NetWorkChangReceiver.OnNetStateChangedListener) entry.getKey();
                Context context1 = (Context) entry.getValue();
                if (context == context1) {
                    mNetStateChangedListenerMap.remove(listener);
                }
            }
        }
    }
}
