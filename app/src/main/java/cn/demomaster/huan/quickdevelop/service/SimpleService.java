package cn.demomaster.huan.quickdevelop.service;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import cn.demomaster.huan.quickdeveloplibrary.jni.BaseService;
import cn.demomaster.huan.quickdeveloplibrary.jni.Watcher;

import static cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityRoot.TAG;

/**
 * @author squirrel桓
 * @date 2019/1/24.
 * description：
 */
public class SimpleService extends BaseService {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public int index;

    @Override
    public void onCreate() {
        super.onCreate();
        baseBinder = new BaseBinder(this);
        String packageName = getApplicationContext().getPackageName();
        String serviceName = this.getClass().getName();
        Log.i(TAG, "packageName=" + packageName+",serviceName="+serviceName);
        Watcher watcher = new Watcher();
        watcher.createWatcher(String.valueOf(Process.myUid()),packageName,serviceName);
        watcher.connectMonitor();
        Log.i(TAG, "守护进程已启动"+str + index);
       final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, "run: 客户端进程 " + index);
                Intent intent = new Intent();
                intent.setAction("cn.demomaster.huan.quickdeveloplibrary.receiver");
                intent.putExtra("type", "normal");
                intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
               // intent.addFlags(Intent.FLAG_RECEIVER_INCLUDE_BACKGROUND)
                intent.setClassName(this.getClass().getPackage().getName(),"cn.demomaster.huan.quickdeveloplibrary.receiver.ApplicationReceiver");
                intent.setPackage(this.getClass().getPackage().getName());
                Bundle bundle = new Bundle();
                bundle.putString("message","run: 客户端进程 "+str + index);
                intent.putExtras(bundle);
                if(Build.VERSION.SDK_INT >= 23){
                    ComponentName componentName=new ComponentName(getApplicationContext(),"cn.demomaster.huan.quickdeveloplibrary.receiver.ApplicationReceiver");//参数1-包名 参数2-广播接收者所在的路径名
                    intent.setComponent(componentName);
                }
               /* if(Build.VERSION.SDK_INT >= 26) {
                    intent.addFlags(0x01000000);//加上这句话，可以解决在android8.0系统以上2个module之间发送广播接收不到的问题}
                }*/
                sendBroadcast(intent);

                index++;
            }
        }, 0, 5000);

    }

    private String str="";
    public void setText(String text) {
        this.str = text;
    }
}
