package cn.demomaster.huan.quickdevelop.receiver;


import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * 监听蓝牙的连接状态
 */
public class BlueToothReceiver extends BroadcastReceiver {

    //监听蓝牙状态
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        //连接
        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            device.getName();//蓝牙的名字
            QDLogger.println("蓝牙连接：" + device.getName());//蓝牙的名字
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            QDLogger.println("蓝牙断开：" + device.getName());//蓝牙的名字
        }
    }
}