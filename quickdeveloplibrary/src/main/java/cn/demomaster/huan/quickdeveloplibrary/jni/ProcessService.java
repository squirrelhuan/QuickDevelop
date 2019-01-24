package cn.demomaster.huan.quickdeveloplibrary.jni;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import static cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityRoot.TAG;

/**
 * @author squirrel桓
 * @date 2019/1/24.
 * description：
 */
public class ProcessService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int index;

    @Override
    public void onCreate() {
        super.onCreate();
        String packageName = getApplicationContext().getPackageName();
        String serviceName = this.getClass().getName();
        Log.i(TAG, "packageName=" + packageName+",serviceName="+serviceName);
        Watcher watcher = new Watcher();
        watcher.createWatcher(String.valueOf(Process.myUid()),packageName,serviceName);
        watcher.connectMonitor();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, "run: 客户端进程 " + index);
                index++;
            }
        }, 0, 5000);

    }


}
