package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import static cn.demomaster.huan.quickdeveloplibrary.util.QMUIDeviceHelper.isHuawei;
import static cn.demomaster.huan.quickdeveloplibrary.util.QMUIDeviceHelper.isLeTV;
import static cn.demomaster.huan.quickdeveloplibrary.util.QMUIDeviceHelper.isMeizu;
import static cn.demomaster.huan.quickdeveloplibrary.util.QMUIDeviceHelper.isOppo;
import static cn.demomaster.huan.quickdeveloplibrary.util.QMUIDeviceHelper.isSamsung;
import static cn.demomaster.huan.quickdeveloplibrary.util.QMUIDeviceHelper.isSmartisan;
import static cn.demomaster.huan.quickdeveloplibrary.util.QMUIDeviceHelper.isVivo;
import static cn.demomaster.huan.quickdeveloplibrary.util.QMUIDeviceHelper.isXiaomi;

/**
 * 电量白名单
 */
public class BatteryOptimizationsHelper {
    static Context context;

    /*IgnoreBatteryOptimizationsHelper(Context context){
        this.context = context;
    }*/

    public static void request(Context context) {
        BatteryOptimizationsHelper.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isIgnoringBatteryOptimizations(context)) {
                requestIgnoreBatteryOptimizations(context);
            } else {
                requestAutoStartService(context);
            }
        }
    }

    /**
     * 跳转到app自启动设置页面
     */
    public static void requestAutoStartService(Context context){
        BatteryOptimizationsHelper.context = context;
        if (isXiaomi()) {
            goXiaomiSetting();
        } else if (isHuawei()) {
            goHuaweiSetting();
        } else if (isOppo()) {
            goOPPOSetting();
        } else if (isVivo()) {
            goVIVOSetting();
        } else if (isMeizu()) {
            goMeizuSetting();
        } else if (isSamsung()) {
            goSamsungSetting();
        } else if (isLeTV()) {
            goLetvSetting();
        } else if (isSmartisan()) {
            goSmartisanSetting();
        }
    }


    /**
     * 判断我们的应用是否在白名单中
     *
     * @param context
     * @return
     */
    public static boolean isIgnoringBatteryOptimizations(Context context) {
        boolean isIgnoring = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (powerManager != null) {
                isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
            }
        }
        return isIgnoring;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestIgnoreBatteryOptimizations(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到指定应用的首页
     */
    private static void showActivity(@NonNull String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    /**
     * 跳转到指定应用的指定页面
     */
    private static void showActivity(@NonNull String packageName, @NonNull String activityDir) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName(packageName, activityDir));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        context.startActivity(intent);
    }

    private static void goHuaweiSetting() {
        try {
            showActivity("com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
            Log.e("", "goHuaweiSetting 1");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                showActivity("com.huawei.systemmanager",
                        "com.huawei.systemmanager.optimize.bootstart.BootStartActivity");
                Log.e("", "goHuaweiSetting 2");
            } catch (Exception e1) {
                e1.printStackTrace();
                showActivity("com.huawei.systemmanager",
                        "com.huawei.systemmanager.mainscreen.MainActivity");
                Log.e("", "goHuaweiSetting 3");
            }
        }
    }

    private static void goXiaomiSetting() {
        showActivity("com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity");
    }

    private static void goOPPOSetting() {
        try {
            showActivity("com.coloros.phonemanager");
        } catch (Exception e1) {
            try {
                showActivity("com.oppo.safe");
            } catch (Exception e2) {
                try {
                    showActivity("com.coloros.oppoguardelf");
                } catch (Exception e3) {
                    showActivity("com.coloros.safecenter");
                }
            }
        }
    }

    private static void goVIVOSetting() {
        showActivity("com.iqoo.secure");
    }

    private static void goMeizuSetting() {
        showActivity("com.meizu.safe");
    }

    private static void goSamsungSetting() {
        try {
            showActivity("com.samsung.android.sm_cn");
        } catch (Exception e) {
            showActivity("com.samsung.android.sm");
        }
    }

    private static void goLetvSetting() {
        showActivity("com.letv.android.letvsafe",
                "com.letv.android.letvsafe.AutobootManageActivity");
    }

    private static void goSmartisanSetting() {
        showActivity("com.smartisanos.security");
    }
}
