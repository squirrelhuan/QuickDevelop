package cn.demomaster.huan.quickdevelop.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

import cn.demomaster.huan.quickdevelop.receiver.ServiceReceiver;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * Created by Squirrel桓 on 2019/1/26.
 */
public class MessageService extends Service {

    public final static ServiceReceiver conncetReceiver = new ServiceReceiver() ;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        QDLogger.d("onCreate");
        super.onCreate();
        startGuardService();
        //DeviceEngine.getInst().init(this.getApplicationContext());

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction("android.intent.action.USER_PRESENT");
        //registerReceiver(conncetReceiver, filter);

        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), "chat").build();
       // Notification notification = new Notification(,"chat");
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
        startForeground(1, notification);//如何让通知不显示呢？只需要将id设为0即可。

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
        QDLogger.d( "ServiceDemo onStartCommand");
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        QDLogger.d( "onDestroy");
        super.onDestroy();
        Intent intent = new Intent(ACTION);
        intent.setAction("cn.demomaster.huan.quickdevelop.receiver.ServiceReceiver");
        intent.putExtra("type", "normal");
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        // intent.addFlags(Intent.FLAG_RECEIVER_INCLUDE_BACKGROUND)
        intent.setClassName(this.getClass().getPackage().getName(), "cn.demomaster.huan.quickdevelop.receiver.ServiceReceiver");
        intent.setPackage(this.getClass().getPackage().getName());
        Bundle bundle = new Bundle();
        bundle.putString("message", "您有新的消息");
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= 23) {
            ComponentName componentName = new ComponentName(getApplicationContext(), "cn.demomaster.huan.quickdevelop.receiver.ServiceReceiver");//参数1-包名 参数2-广播接收者所在的路径名
            intent.setComponent(componentName);
        }
               /* if(Build.VERSION.SDK_INT >= 26) {
                    intent.addFlags(0x01000000);//加上这句话，可以解决在android8.0系统以上2个module之间发送广播接收不到的问题}
                }*/
        sendBroadcast(intent);
       // unregisterReceiver(conncetReceiver);
        QDLogger.d( "sendBroadcast[" + ACTION + "]");
    }


    public void startGuardService() {
        Intent intent = new Intent();
        intent.setClass(this, GuardService.class);
       startService(intent);
    }

    public final static String ACTION = "cn.demomaster.huan.quickdevelop.service";
}