package cn.demomaster.huan.quickdeveloplibrary.util.system;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.util.List;

import cn.demomaster.qdlogger_library.QDLogger;

import static cn.demomaster.huan.quickdeveloplibrary.helper.install.InstallHelper.checkAppInstalled;

public class QDAppInfoUtil {

    /**
     * 根据包名获取版本信息
     * @param context
     * @param packageName
     * @return
     */
    public static PackageInfo getPackageInfoByPackageName(Context context, String packageName) {
        if (context == null) {
            return null;
        }
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            QDLogger.e("未找到安裝包："+packageName);
        }
        return null;
    }

    /**
     * 判断是否存在pckName包
     *
     * @param pckName
     * @return
     */
    public static boolean isPackageExist(Context context, String pckName) {
        return getPackageInfoByPackageName(context,pckName)!=null;
    }

    public static String getVersionName(Context context) {
        return getVersionName(context, context.getPackageName());
    }

    public static String getVersionName(Context context, String packageName) {
        PackageInfo pi = getPackageInfoByPackageName(context, packageName);
        return pi != null?pi.versionName:null;
    }

    // 获取本地的版本号
    public static int getVersionCode(Context context) {
        if (context == null) return -1;
        return getVersionCode(context, context.getPackageName());
    }

    // 获取本地的版本号
    public static int getVersionCode(Context context, String packageName) {
        PackageInfo packageInfo = getPackageInfoByPackageName(context, packageName);
        if (packageInfo != null) {
            return packageInfo.versionCode;
        }
        return -1;
    }

    /**
     * 根据包名判断应用是否已经安装
     * @param context
     * @param pkgName
     * @return
     */
    public static boolean checkAppInstalled(Context context, String pkgName) {
        if (TextUtils.isEmpty(pkgName)) {
            return false;
        }
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> info = packageManager.getInstalledPackages(0);
        if (info == null || info.isEmpty())
            return false;
        for (int i = 0; i < info.size(); i++) {
            if (pkgName.equals(info.get(i).packageName)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检测其他应用是否处于debug模式。
     */
    public static boolean isApkDebugable(Context context, String packageName) {
        try {
            @SuppressLint("WrongConstant") PackageInfo pkginfo = context.getPackageManager().getPackageInfo(packageName, 1);
            if (pkginfo != null) {
                ApplicationInfo info = pkginfo.applicationInfo;
                return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            }

        } catch (Exception e) {

        }
        return false;
    }


    /**
     * 获取图标 bitmap
     *
     * @param context
     */

    public static Bitmap getAppIconBitmap(Context context) {
        Drawable d = getAppIconDrawable(context); //xxx根据自己的情况获取drawable
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }
    public static  Drawable getAppIconDrawable(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        return d;
    }


    /**
     * 根据包名启动app
     * @param context
     * @param packageName
     */
    public static void startApp(Context context, String packageName) {
        if (checkAppInstalled(context,packageName)) {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                //intent.putExtra("type", "110");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }
}
