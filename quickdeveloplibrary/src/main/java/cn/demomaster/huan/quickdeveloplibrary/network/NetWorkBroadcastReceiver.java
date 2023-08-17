package cn.demomaster.huan.quickdeveloplibrary.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * Created by Squirrel桓 on 2018/12/12.
 */
public abstract class NetWorkBroadcastReceiver extends BroadcastReceiver implements NetStateChangedListener{

    public NetWorkBroadcastReceiver(){
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
        String action = intent.getAction();
        //QDLogger.e("action=" + action);
        if (!TextUtils.isEmpty(action)) {
            String des = NetworkHelper.getActionDescription(action);
            switch (action) {
                case WifiManager.WIFI_STATE_CHANGED_ACTION:
                    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                    String wifiDesc = NetworkHelper.getWifiStateDes(wifiState);
                    //QDLogger.println("网络状态变化,hash:" + hashCode() + ",action=" + action + "," + wifiDesc);
                    break;
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    //int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                    break;
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    //QDLogger.println("网络状态变化,hash:" + hashCode() + ",action=" + action + "," + des);
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

    @Override
    public void onConnected(Context context, Intent intent) {

    }

    @Override
    public void onDisConnected(Context context, Intent intent) {

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
                onConnected(context,intent);
            }
        }
    }

    public void dispatchDisConnectedEvent(Context context, Intent intent) {
        if (netState != 0) {
            netState = 0;
            String des = NetworkHelper.getActionDescription(intent.getAction());
            QDLogger.println("网络[断开]:" + des);
            onDisConnected(context,intent);
        }
    }


}
