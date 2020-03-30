package cn.demomaster.huan.quickdeveloplibrary.util.system;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

public class QDAppInfoUtil {

    public static String getVersionName(Context context) {
        if (context == null) return null;
        return getVersionName(context,context.getPackageName());
    }

    public static String getVersionName(Context context, String packageName) {
        String ver_data = null;
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(packageName, 0);
            if(pi!=null) {
                ver_data = pi.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return ver_data;
    }

    // 获取本地的版本号
    public static int getVersionCode(Context context) {
        if (context == null) return -1;
        return getVersionCode(context, context.getPackageName());
    }

    // 获取本地的版本号
    public static int getVersionCode(Context context, String packageName) {
        int versionCode = -1;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    packageName, 0);
            if(packageInfo!=null) {
                versionCode = packageInfo.versionCode;
            }
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
