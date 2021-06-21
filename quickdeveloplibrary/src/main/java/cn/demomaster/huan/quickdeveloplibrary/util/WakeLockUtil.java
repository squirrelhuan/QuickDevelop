package cn.demomaster.huan.quickdeveloplibrary.util;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WakeLockUtil {

    /**
     * 点亮屏幕
     *
     * @param timeout The timeout after which to release the wake lock, in milliseconds.
     */
    @Nullable
    public static PowerManager.WakeLock acquireWakeLock(@NonNull Context context, long timeout) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm == null)
            return null;
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.FULL_WAKE_LOCK |
                        PowerManager.ON_AFTER_RELEASE,
                context.getClass().getName());
        wakeLock.acquire(timeout);
        return wakeLock;
    }

    /**
     * 唤醒屏幕
     *
     * @param context
     */
    public static void acquireWakeLock(@NonNull Context context) {
        //管理锁屏的一个服务
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (!pm.isScreenOn()) {
                @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
                wakeLock.acquire();  //点亮屏幕
                wakeLock.release();  //任务结束后释放
            }
        }
    }

    public static void release(@Nullable PowerManager.WakeLock wakeLock) {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }
}