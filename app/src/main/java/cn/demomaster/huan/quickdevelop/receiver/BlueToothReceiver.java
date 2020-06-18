package cn.demomaster.huan.quickdevelop.receiver;


import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;

/**
 * 监听蓝牙的连接状态
 */
public class BlueToothReceiver extends BroadcastReceiver {

    private String btMessage = "";
    //监听蓝牙状态

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        //连接
        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            btMessage = device.getName();//蓝牙的名字
            //已经连接，开始你的操作，我是用了播放音乐的方式提醒客户
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            Log.e("断开", "onReceive: " + device.getName());//蓝牙的名字
            btMessage = device.getName() + "蓝牙连接已断开！！";
            //已经断开，开始你的操作，我是用了播放音乐的方式提醒客户，然后在activity里写了一个方法，继续连接。
        }
    }
}