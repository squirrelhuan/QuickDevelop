package cn.demomaster.huan.quickdeveloplibrary.util.system;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class QDAppInfoUtil {

    public static String getVersionName(Context context) {
        String ver_data;
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ver_data = pi.versionName;
        if (ver_data == null || ver_data.length() <= 0) {
            ver_data = "NA";
        }
        return ver_data;
    }

    // 获取本地的版本号
    public static int getVersionCode(Context context) {
        int versionCode = -1;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
