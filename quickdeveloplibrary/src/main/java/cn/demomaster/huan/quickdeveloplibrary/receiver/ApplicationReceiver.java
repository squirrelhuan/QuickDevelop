package cn.demomaster.huan.quickdeveloplibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.demomaster.huan.quickdeveloplibrary.helper.NotificationHelper;

/**
 * @author squirrel桓
 * @date 2019/1/24.
 * description：
 */
public class ApplicationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey("message")) {
            String message = bundle.getString("message", "");
            NotificationHelper.sendNotification(context, "无标题", message, null);
        }
    }
}
