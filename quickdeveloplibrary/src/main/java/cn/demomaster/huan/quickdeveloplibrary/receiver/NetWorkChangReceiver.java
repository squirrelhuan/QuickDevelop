package cn.demomaster.huan.quickdeveloplibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

/**
 * Created by Squirrel桓 on 2018/12/12.
 */
public class NetWorkChangReceiver extends BroadcastReceiver {
    private OnNetStateChangedListener onNetStateChangedListener;

    public void setOnNetStateChangedListener(OnNetStateChangedListener onNetStateChangedListener) {
        this.onNetStateChangedListener = onNetStateChangedListener;
    }

    public NetWorkChangReceiver(OnNetStateChangedListener onNetStateChangedListener) {
        this.onNetStateChangedListener = onNetStateChangedListener;
    }

    private int netState=-400;//0不可用，1可用，-400未初始化

    public int getNetState() {//0不可用，1可用，-400未初始化
        return netState;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        //如果无网络连接activeInfo为null

        //也可获取网络的类型
        if (activeInfo != null) { //网络连接
            switch (activeInfo.getType()) {
                case TYPE_MOBILE:
                    QDLogger.i("CGQ", "正在使用2G/3G/4G网络");
                    break;
                case TYPE_WIFI:
                    QDLogger.i("CGQ", "正在使用wifi上网");
                    break;
                default:
                    break;
            }
            QDLogger.i("CGQ", "ko_network_connection_is_successful");
            if(netState!=1&&onNetStateChangedListener!=null){
                netState=1;
                QDLogger.i("CGQ", "网络状态发生改变，已连接");
                onNetStateChangedListener.onConnected(activeInfo);
            }
        } else { //网络断开
            Log.i("CGQ", "网络断开");
            if(netState!=0&&onNetStateChangedListener!=null){
                netState=0;
                QDLogger.i("CGQ", "网络状态发生改变，已断开");
                onNetStateChangedListener.onDisConnected();
            }
        }
    }

    public static interface OnNetStateChangedListener{
        void onConnected(NetworkInfo networkInfo);
        void onDisConnected();
    }
}
