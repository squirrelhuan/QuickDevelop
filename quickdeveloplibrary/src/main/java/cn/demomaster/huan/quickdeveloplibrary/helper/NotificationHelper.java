package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.constant.TAG;
import cn.demomaster.huan.quickdeveloplibrary.util.WakeLockUtil;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Squirrel桓 on 2019/1/20.
 */
public class NotificationHelper {

    private static Context context;
    NotificationManager manager;
    private static NotificationHelper instance;

    public static NotificationHelper getInstance() {
        if (instance == null) {
            instance = new NotificationHelper();
        }
        return instance;
    }

    String channelId = "channel_1";
    String channelName = "聊天消息";
    String channelId2 = "subscribe";
    String channelName2 = "订阅消息";

    public void init(Context mcontext) {
        context = mcontext.getApplicationContext();
        manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /**
             * Oreo不用Priority了，用importance
             * IMPORTANCE_NONE 关闭通知
             * IMPORTANCE_MIN 开启通知，不会弹出，但没有提示音，状态栏中无显示
             * IMPORTANCE_LOW 开启通知，不会弹出，不发出提示音，状态栏中显示
             * IMPORTANCE_DEFAULT 开启通知，不会弹出，发出提示音，状态栏中显示
             * IMPORTANCE_HIGH 开启通知，会弹出，发出提示音，状态栏中显示
             */
            createNotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            createNotificationChannel(channelId2, channelName2, NotificationManager.IMPORTANCE_DEFAULT);
        }
    }

    /**
     * 创建消息通道
     *
     * @param channelId
     * @param channelName
     * @param importance
     */
    @TargetApi(Build.VERSION_CODES.O)
    public void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        manager.createNotificationChannel(channel);
    }

    public void sendNotification(String title, String content, Class<? extends Activity> activityClass) {
        PendingIntent contentIntent = null;
        if (activityClass != null) {
            Intent intent = new Intent(context, activityClass);
            contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.quickdevelop_ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.quickdevelop_ic_launcher))
                .setAutoCancel(true);

        // 向通知添加声音、闪灯和振动效果
        //.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_ALL | Notification.DEFAULT_SOUND)
        //.setVisibility(VISIBILITY_PUBLIC)
        if (contentIntent != null) {
            builder.setContentIntent(contentIntent);
        }
        Notification notification = builder.build();
        manager.notify(1, notification);
    }


    public void sendFullScreenNotification(String title, String content, Class<? extends Activity> activityClass, Bundle bundle) {
        Intent fullScreenIntent = new Intent(context, activityClass);
        fullScreenIntent.putExtras(bundle);
        //fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        fullScreenIntent.putExtra("action", "callfromdevice");
        fullScreenIntent.putExtra("type", "3");
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.mipmap.quickdevelop_ic_launcher)
                        //.setTicker(content)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setAutoCancel(true)
                        //.setDefaults(Notification.DEFAULT_ALL)
                        //.setPriority(NotificationCompat.PRIORITY_MAX)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(Notification.CATEGORY_CALL)
                        .setFullScreenIntent(fullScreenPendingIntent, true);

        Notification incomingCallNotification = notificationBuilder.build();
        manager.notify(1, incomingCallNotification);
    }

    /**
     * 检查锁屏状态，如果锁屏先点亮屏幕
     *
     * @param context
     */
    public void checkLockAndShowNotification(Context context, String message, Class targetClazz) {
        if (isNotificationEnabled(context)) {
            //唤醒屏幕
            WakeLockUtil.acquireWakeLock(context);
            sendNotification("", message, targetClazz);
        } else {
            Log.e("Notification", "通知权限不足");
            checkNotification(context);
        }
    }

    private void sendNotification(Context context, String message, String targetClazzName) {
        Class clazz = null;
        try {
            clazz = Class.forName(targetClazzName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        sendNotification("", message, clazz);
    }

    /**
     * 检测是否开启通知
     *
     * @param context
     */
    private static void checkNotification(final Context context) {
        if (!isNotificationEnabled(context)) {
            new AlertDialog.Builder(context).setTitle("温馨提示")
                    .setMessage("你还未开启系统通知，将影响消息的接收，要去开启吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setNotification(context);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
    }

    /**
     * 如果没有开启通知，跳转至设置界面
     *
     * @param context
     */
    private static void setNotification(Context context) {
        Intent localIntent = new Intent();
        //直接跳转到应用通知设置的代码：
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            localIntent.putExtra("app_package", context.getPackageName());
            localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localIntent.setData(Uri.parse("package:" + context.getPackageName()));
        } else {
            //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
            }
        }
        context.startActivity(localIntent);
    }

    /**
     * 获取通知权限,检测是否开启了系统通知
     *
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean isNotificationEnabled(Context context) {
        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 清除所有通知消息
     */
    public void clearAllNotifiication() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
