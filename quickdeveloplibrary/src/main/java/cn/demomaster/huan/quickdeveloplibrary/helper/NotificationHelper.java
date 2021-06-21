package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.WakeLockUtil;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by Squirrel桓 on 2019/1/20.
 */
public class NotificationHelper {

    public static void sendNotification(Context context, String title, String content, Class<? extends Activity> activityClass) {
        if (activityClass != null) {
            Intent intent = new Intent(context, activityClass);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }
        // 向通知添加声音、闪灯和振动效果
        //.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_ALL | Notification.DEFAULT_SOUND)
        //.setVisibility(VISIBILITY_PUBLIC)
    }

    public static void sendFullScreenNotification(Context context, String title, String content, Class<? extends Activity> activityClass, Bundle bundle) {
        Intent fullScreenIntent = new Intent(context, activityClass);
        fullScreenIntent.putExtras(bundle);
        //fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        fullScreenIntent.putExtra("action", "callfromdevice");
        fullScreenIntent.putExtra("type", "3");
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, "")
                        .setSmallIcon(R.mipmap.quickdevelop_ic_launcher)
                        //.setTicker(content)
                        //.setDefaults(Notification.DEFAULT_ALL)
                        //.setPriority(NotificationCompat.PRIORITY_MAX)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(Notification.CATEGORY_CALL)
                        .setFullScreenIntent(fullScreenPendingIntent, true);

        NotificationHelper.Builer builer = new NotificationHelper.Builer(context);
        builer.setTitle("測試消息").setContentText("震动加响铃").send();
    }

    /**
     * 检查锁屏状态，如果锁屏先点亮屏幕
     *
     * @param context
     */
    public void checkLockAndShowNotification(Context context, String message, Class targetClazz) {
        if (isNotificationEnabled(context)) {//唤醒屏幕
            WakeLockUtil.acquireWakeLock(context);
            sendNotification(context, "", message, targetClazz);
        } else {
            Log.e("Notification", "通知权限不足");
            checkNotification(context);
        }
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
                            startNotificationSetting(context);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

    /**
     * 如果没有开启通知，跳转至设置界面
     *
     * @param context
     */
    private static void startNotificationSetting(Context context) {
        Intent localIntent = new Intent();
        //直接跳转到应用通知设置的代码：
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            localIntent.putExtra("app_package", context.getPackageName());
            localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
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
        if (SDK_INT < Build.VERSION_CODES.KITKAT) {
            return true;
        } else if (SDK_INT < Build.VERSION_CODES.O) {
            return isEnableV19(context);
        } else {
            return isEnableV26(context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static boolean isEnableV19(Context context) {
        final String CHECK_OP_NO_THROW = "checkOpNoThrow";
        final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass = null; /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (int) opPostNotificationValue.get(Integer.class);
            return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
        } catch (NoSuchFieldException e) {
        } catch (InvocationTargetException e) {
        } catch (IllegalAccessException e) {
        } catch (Exception e) {
        }
        return false;
    }

    private static boolean isEnableV26(Context context) {
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        try {
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            Method sServiceField = notificationManager.getClass().getDeclaredMethod("getService");
            sServiceField.setAccessible(true);
            Object sService = sServiceField.invoke(notificationManager);

            Method method = sService.getClass().getDeclaredMethod("areNotificationsEnabledForPackage"
                    , String.class, Integer.TYPE);
            method.setAccessible(true);
            return (boolean) method.invoke(sService, pkg, uid);
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 清除所有通知消息
     */
    public static void clearAllNotifiication(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void sendNotifiication(Builer builder) {
        NotificationManager manager = (NotificationManager) builder.context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuiler = null;
        PendingIntent pendingIntent = null;
        Class clazz = null;
        if (!TextUtils.isEmpty(builder.acitvityName)) {
            try {
                Intent intent = new Intent();
                clazz = Class.forName(builder.acitvityName);
                intent.setClass(builder.context, clazz);
                pendingIntent = PendingIntent.getActivity(builder.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        String channelId = builder.getChannelId();
        notificationBuiler = new NotificationCompat.Builder(builder.context, channelId)
                .setContentTitle(builder.title)
                .setContentText(builder.contentText)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(builder.smallIcon)
                //.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.quickdevelop_ic_launcher))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        if (builder.pattern != null) {
            notificationBuiler.setVibrate(builder.pattern);
        }

        if (builder.enableSound) {
            if (builder.soundUri != null) {
                notificationBuiler.setSound(builder.soundUri);
            }
        } else {//静音
            notificationBuiler.setSound(null);
            //notificationBuiler.setDefaults(Notification.DEFAULT_VIBRATE);
            notificationBuiler.setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE);//消除声音和震动
        }
        if (SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes.Builder attrs = new AudioAttributes.Builder();
            attrs.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
            attrs.setUsage(AudioAttributes.USAGE_ALARM);
            //manager.deleteNotificationChannel(channelId);
            /**
             * Oreo不用Priority了，用importance
             * IMPORTANCE_NONE 关闭通知
             * IMPORTANCE_MIN 开启通知，不会弹出，但没有提示音，状态栏中无显示
             * IMPORTANCE_LOW 开启通知，不会弹出，不发出提示音，状态栏中显示
             * IMPORTANCE_DEFAULT 开启通知，不会弹出，发出提示音，状态栏中显示
             * IMPORTANCE_HIGH 开启通知，会弹出，发出提示音，状态栏中显示
             */
            NotificationChannel channel = new NotificationChannel(channelId, "消息",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(builder.enableLights);//闪光
            channel.enableVibration(builder.enableVibration);
            if (builder.pattern != null) {
                channel.setVibrationPattern(builder.pattern);
            }
            if (builder.enableSound) {
                if (builder.soundUri != null) {
                    channel.setSound(builder.soundUri, null);//Uri.parse("file:///android_asset/"+soundResourceName)
                }
            } else {
                channel.setSound(null, null);
            }
            manager.createNotificationChannel(channel);
            notificationBuiler.setChannelId(channelId);
        }

        Notification notification = notificationBuiler.build();
        manager.notify(builder.id, notification);
    }

    public static class Builer {
        Context context;
        String acitvityName;
        boolean enableLights = false;
        boolean enableVibration = true;
        boolean enableSound = true;
        Uri soundUri = null;
        int id = 1;
        long[] pattern = null;//{0, 200, 200,200};
        String title;
        String contentText;
        int smallIcon;

        public Builer(Context context) {
            this.context = context;
            smallIcon = context.getApplicationInfo().icon;
        }

        public Builer setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builer setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builer setAcitvityName(String acitvityName) {
            this.acitvityName = acitvityName;
            return this;
        }

        public Builer setEnableLights(boolean enableLights) {
            this.enableLights = enableLights;
            return this;
        }

        public Builer setEnableVibration(boolean enableVibration) {
            this.enableVibration = enableVibration;
            return this;
        }

        public Builer setEnableSound(boolean enableSound) {
            this.enableSound = enableSound;
            return this;
        }

        public Builer setSoundUri(Uri soundUri) {
            this.soundUri = soundUri;
            return this;
        }

        public Builer setId(int id) {
            this.id = id;
            return this;
        }

        public Builer setPattern(long[] pattern) {
            this.pattern = pattern;
            return this;
        }

        public Builer setContentText(String contentText) {
            this.contentText = contentText;
            return this;
        }

        public Builer setSmallIcon(int smallIcon) {
            this.smallIcon = smallIcon;
            return this;
        }

        public void send() {
            NotificationHelper.sendNotifiication(this);
        }

        public String getChannelId() {
            return enableLights + "_" + enableVibration + "_" + enableSound + "_" + soundUri;
        }
    }

    //从resource中获取音频
    public static Uri getUriFromResource(Context context, String soundResourceName) {
        int soundResourceId = context.getResources().getIdentifier(soundResourceName, "raw", context.getPackageName());
        if (soundResourceId != 0) {
            String path = "android.resource://" + context.getPackageName() + "/" + soundResourceId;
            return Uri.parse(path);
        }
        return null;
    }

    //从Assets中获取音频
    public static Uri getUriFromAssets(Context context, String soundResourceName) {
        if (!TextUtils.isEmpty(soundResourceName)) {
            try {
                //文件保存到本地再用uri引用
                String audioFileName = context.getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS).getAbsolutePath() + File.separator + soundResourceName;
                //audioFileName = context.getFilesDir().getPath() + File.separator+ soundResourceName;
                File file = new File(audioFileName);
                if (!file.exists()) {
                    AssetFileDescriptor afd = context.getAssets().openFd(soundResourceName);
                    InputStream is = afd.createInputStream();
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(audioFileName);
                    byte[] b = new byte[1024];
                    int length;
                    while ((length = is.read(b)) > 0) {
                        fos.write(b, 0, length);
                    }
                    is.close();
                    afd.close();
                    fos.close();
                }//参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                return FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
