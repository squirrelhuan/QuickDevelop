package cn.demomaster.huan.quickdevelop.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.Application;

/**
 * Created by Squirrel桓 on 2019/1/26.
 */
public class UploadFilesIntentService extends IntentService {
    private static final String UPLOAD_FILE = "com.nuoyuan.statistic.action.UPLOAD_FILE";
    private static String loadUrlPath = "";
    public static final String CHANNEL_ID_STRING = "nyd001";
    @Override
    public void onCreate() {
        super.onCreate();
        //适配8.0service
        NotificationManager notificationManager = (NotificationManager) Application.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID_STRING, "诺秒贷", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID_STRING).build();
            startForeground(1, notification);
        }
    }

    @Override
    protected void onHandleIntent( @Nullable Intent intent) {

    }

    public UploadFilesIntentService() {
        super("UploadFilesIntentService");
    }

    public static void startActionFoo(Context context, String loadPath) {
        Intent intent = new Intent(context, UploadFilesIntentService.class);
        intent.setAction(UPLOAD_FILE);
        loadUrlPath = loadPath;
//开启服务兼容
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

}
