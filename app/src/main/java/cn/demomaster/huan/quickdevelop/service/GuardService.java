package cn.demomaster.huan.quickdevelop.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * Created by Squirrel桓 on 2019/1/26.
 */
public class GuardService extends Service {

    public final static String ACTION = "cn.demomaster.huan.quickdevelop.service";

    public GuardService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        QDLogger.d( "守护服务 onCreate");
        super.onCreate();

    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
        QDLogger.d( "守护服务 onStartCommand");
        flags = Service.START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        QDLogger.d( "守护服务onDestroy");
        super.onDestroy();
        Intent intent = new Intent( ACTION );
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
        QDLogger.d( "守护服务 sendBroadcast[" + ACTION + "]");

    }

}