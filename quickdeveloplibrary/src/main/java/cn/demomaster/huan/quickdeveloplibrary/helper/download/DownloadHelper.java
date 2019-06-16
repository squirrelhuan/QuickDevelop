package cn.demomaster.huan.quickdeveloplibrary.helper.download;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;

import static android.content.Context.DOWNLOAD_SERVICE;

public class DownloadHelper {

    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static String DOWNLOAD_APP_FOLDER_NAME;
    public static String  DOWNLOAD_APP_FILE_NAME;
    public static String app_name;
    //下载文件
    public static void downloadFile(final Context context, String urls, final String name) {
        final String url = urls;
        PermissionManager.chekPermission(context, PERMISSIONS_STORAGE, new PermissionManager.OnCheckPermissionListener() {
            @Override
            public void onPassed() {
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(uri);

                // 存储的目录
                request.setDestinationInExternalPublicDir(DOWNLOAD_APP_FOLDER_NAME, DOWNLOAD_APP_FILE_NAME);
                // 设置允许使用的网络类型，这里是移动网络和wifi都可以
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

                // 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
                request.setShowRunningNotification(true);
                request.setVisibleInDownloadsUi(true);
                request.setTitle(app_name + "正在下载" + name);
                // 不显示下载界面
                request.setVisibleInDownloadsUi(true);
                /*
                 * 设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件
                 * 在/mnt/sdcard
                 * /Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错
                 * ，不设置，下载后的文件在/cache这个 目录下面
                 */
                //request.setDestinationInExternalFilesDir(this, null, "tar.apk");
                // 下载完成后保留 下载的notification
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                long id = downloadManager.enqueue(request);
                // TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面
            }

            @Override
            public void onNoPassed() {
                Toast.makeText(context, "请打开相关权限！", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
