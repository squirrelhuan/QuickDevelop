package cn.demomaster.huan.quickdeveloplibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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

    public interface NetStateChangedReceiver {
        void onReceive(Context context, Intent intent);
    }

    public interface OnNetStateChangedListenerInterFace extends NetStateChangedReceiver {
        void onConnected(Context context, Intent intent);

        void onDisConnected(Context context, Intent intent);
    }

    public static abstract class OnNetStateChangedListener implements NetWorkChangReceiver.OnNetStateChangedListenerInterFace {
        @Override
        public void onReceive(Context context, Intent intent) {
        
        }

    }


}
