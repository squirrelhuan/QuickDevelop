package cn.demomaster.huan.quickdeveloplibrary.lifecycle;

import android.app.Activity;

import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;

public class LifecycleRecorder {
    public static void record(LifecycleType lifecycleType, Activity activity) {
        LifecycleBean lifecycleBean = new LifecycleBean(lifecycleType, activity.getClass() + "", activity.hashCode());
        addRecord(activity, lifecycleBean);
    }

    public static void addRecord(Activity activity, LifecycleBean lifecycleBean) {
        // QDLogger.i(activity.getFilesDir().getAbsolutePath()+"/LifecycleRecorder.txt");
        if (isRecord) {
            QDFileUtil.writeFileSdcardFile(activity.getFilesDir().getAbsolutePath() + "/LifecycleRecorder.txt", lifecycleBean.toString() + "\n", true);
        }
    }

    static boolean isRecord;

    public static void startRecord(Activity activity) {
        isRecord = true;
        QDFileUtil.writeFileSdcardFile(activity.getFilesDir().getAbsolutePath() + "/LifecycleRecorder.txt", "", false);
    }

    public static void stopRecord() {
        isRecord = false;
    }
}
