package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.os.Handler;
import android.os.Looper;

public class QdThreadHelper {

    /**
     * 运行在主线程即ui线程
     *
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            new Handler(Looper.getMainLooper()).post(runnable);
        }
    }

    /**
     * 运行在子线程
     *
     * @param runnable
     */
    public static void runOnSubThread(Runnable runnable) {
        if (isMainThread()) {
            new Thread(runnable).start();
        } else {
            runnable.run();
        }
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
