package cn.demomaster.huan.quickdeveloplibrary.helper.download;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

public class DownloadUtil {

    /**
     * 可能会出错Cannot update URI: content://downloads/my_downloads/-1
     * 检查下载管理器是否被禁用
     */
    public static boolean checkDownloadManagerEnable(Context mContext) {
        try {
            int state = mContext.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                String packageName = "com.android.providers.downloads";
                try {
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:$packageName"));
                    mContext.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Intent intent = new  Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                    mContext.startActivity(intent);
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
