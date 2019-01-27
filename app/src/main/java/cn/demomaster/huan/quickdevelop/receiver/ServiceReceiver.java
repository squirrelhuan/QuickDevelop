package cn.demomaster.huan.quickdevelop.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import cn.demomaster.huan.quickdevelop.service.GuardService;
import cn.demomaster.huan.quickdevelop.service.MessageService;

import static cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityRoot.TAG;

/**
 * Created by Squirrel桓 on 2019/1/26.
 */
public class ServiceReceiver extends BroadcastReceiver {


    public ServiceReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        Intent mIntent = new Intent();
        mIntent.setClass( context , MessageService.class );
        //context.startService( mIntent );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(mIntent);
        } else {
            context.startService(mIntent);
        }
        Log.d(TAG, "start MessageService");

        Intent intent1 = new Intent();
        intent1.setClass( context , GuardService.class);
        context.startService( intent1 );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent1);
        } else {
            context.startService(intent1);
        }
        Log.d(TAG, "start GuardService");
    }
}