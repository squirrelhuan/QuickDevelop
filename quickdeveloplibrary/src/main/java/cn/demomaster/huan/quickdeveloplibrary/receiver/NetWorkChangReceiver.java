package cn.demomaster.huan.quickdeveloplibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import cn.demomaster.qdlogger_library.QDLogger;

import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

/**
 * Created by Squirrel桓 on 2018/12/12.
 */
public class NetWorkChangReceiver extends BroadcastReceiver {
    private NetStateChangedReceiver netStateChangedReceiver;

    public void setOnNetStateChangedListener(NetStateChangedReceiver netStateChangedReceiver) {
        this.netStateChangedReceiver = netStateChangedReceiver;
    }

    public NetWorkChangReceiver(NetStateChangedReceiver netStateChangedReceiver) {
        this.netStateChangedReceiver = netStateChangedReceiver;
    }

    private int netState = -400;//0不可用，1可用，-400未初始化

    public int getNetState() {//0不可用，1可用，-400未初始化
        return netState;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (netStateChangedReceiver != null) {
            netStateChangedReceiver.onReceive(context, intent);
        }
    }

    public static interface NetStateChangedReceiver{
        void onReceive(Context context, Intent intent);
    }

    public static interface OnNetStateChangedListenerInterFace extends NetStateChangedReceiver {
        void onConnected(Context context, Intent intent);

        void onDisConnected(Context context, Intent intent);
    }

    public static abstract class OnNetStateChangedListener implements NetWorkChangReceiver.OnNetStateChangedListenerInterFace {
        @Override
        public void onReceive(Context context, Intent intent) {

        }

    }


}
