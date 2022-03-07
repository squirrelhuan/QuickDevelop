package cn.demomaster.huan.quickdeveloplibrary.util;

import android.app.ActivityManager;
import android.content.Context;

public class ProcessUtils {

    /*
     * 当应用存在多个进程时，确保只在主进程进行，判断是不是处在主线程中
     * true 表示主线程
     * false 表示多线程中的其他线程
     */
    public boolean isMainProcess(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return context.getApplicationInfo().packageName.equals(appProcess.processName);
            }
        }
        return false;
    }
}
