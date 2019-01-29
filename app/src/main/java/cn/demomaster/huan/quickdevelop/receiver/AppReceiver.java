package cn.demomaster.huan.quickdevelop.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import cn.demomaster.huan.quickdeveloplibrary.receiver.ApplicationReceiver;

/**
 * @author squirrel桓
 * @date 2019/1/24.
 * description：
 */
public class AppReceiver extends ApplicationReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Intent sintent=new Intent("cn.demomaster.huan.quickdevelop.service.SimpleService");
        //context.startService(sintent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(sintent);
        } else {
            context.startService(sintent);
        }
    }
}
