package cn.demomaster.huan.quickdeveloplibrary.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import cn.demomaster.qdlogger_library.QDLogger;

import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_UNKNOWN;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_CAR;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

/**
 * Created by Squirrel桓 on 2018/12/12.
 */
public class NetWorkBroadcastReceiver extends BroadcastReceiver {
    /*public NetStateChangedListener netStateChangedListener;

    public void setOnNetStateChangedListener(NetStateChangedListener netStateChangedListener) {
        this.netStateChangedListener = netStateChangedListener;
    }

    public NetWorkBroadcastReceiver(NetStateChangedListener netStateChangedListener) {
        this.netStateChangedListener = netStateChangedListener;
    }*/
    
    public NetWorkBroadcastReceiver(){
        listenerMap = new HashMap<>();
    }

    private int netState = -400;//0不可用，1可用，-400未初始化
    public int getNetState() {//0不可用，1可用，-400未初始化
        return netState;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        /*if (netStateChangedListener != null) {
            netStateChangedListener.onReceive(context, intent);
        }*/
        dispatchReceivedEvent(context, intent);
        String action = intent.getAction();
        //QDLogger.e("action=" + action);
        if (!TextUtils.isEmpty(action)) {
            String des = NetworkHelper.getActionDescription(action);
            switch (action) {
                case WifiManager.WIFI_STATE_CHANGED_ACTION:
                    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                    String wifiDesc = NetworkHelper.getWifiStateDes(wifiState);
                    QDLogger.println("网络状态变化,hash:" + hashCode() + ",action=" + action + "," + wifiDesc);
                    break;
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    //int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                    break;
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    QDLogger.println("网络状态变化,hash:" + hashCode() + ",action=" + action + "," + des);
                    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                    if (networkInfo != null) { //如果无网络连接networkInfo为null
                        dispatchConnectedEvent(context, intent);
                    } else { //网络连接
                        dispatchDisConnectedEvent(context, intent);
                    }
                    break;
            }
        }
    }

    public void dispatchReceivedEvent(Context context, Intent intent) {
        if (listenerMap != null) {
            for (Map.Entry entry : listenerMap.entrySet()) {
                NetStateChangedListener listener = (NetStateChangedListener) entry.getKey();
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
                des = NetworkHelper.getNetworkTypeDescription(networkInfo.getType());
                QDLogger.println("网络[连接]:" + des);
                if (listenerMap != null) {
                    for (Map.Entry entry : listenerMap.entrySet()) {
                        NetStateChangedListener listener = (NetStateChangedListener) entry.getKey();
                        listener.onConnected(context, intent);
                    }
                }
            }
        }
    }

    public void dispatchDisConnectedEvent(Context context, Intent intent) {
        if (netState != 0) {
            netState = 0;
            String des = NetworkHelper.getActionDescription(intent.getAction());
            QDLogger.println("网络[断开]:" + des);
            if (listenerMap != null) {
                for (Map.Entry entry : listenerMap.entrySet()) {
                    NetStateChangedListener listener = (NetStateChangedListener) entry.getKey();
                    listener.onDisConnected(context, intent);
                }
            }
        }
    }

    Map<NetStateChangedListener, Integer> listenerMap;
    public void putListener(OnNetStateChangedListener listener, int hashCode) {
        listenerMap.put(listener, hashCode);
    }

    public void remove(NetStateChangedListener listener) {
        if(listenerMap!=null){
            listenerMap.remove(listener);
        }
    }

    public void removeAll() {
        if(listenerMap!=null){
            listenerMap.clear();
        }
    }

}
