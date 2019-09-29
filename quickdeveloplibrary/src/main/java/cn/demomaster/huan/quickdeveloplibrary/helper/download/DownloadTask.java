package cn.demomaster.huan.quickdeveloplibrary.helper.download;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import cn.demomaster.huan.quickdeveloplibrary.constant.AppConfig;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * 下载任务
 */
public class DownloadTask {
    // 权限
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private long downloadId;
    private Context context;
    private String fileName;//文件名带格式名
    private String downloadUrl;//下载路径
    private Uri downIdUri;
    //
    private OnDownloadProgressListener onProgressListener;//下载进度监听器

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
    }

    //默认下载路径
    private String download_app_folder_name = AppConfig.getInstance().getConfig("DownloadFilePath").toString();

    public DownloadTask(Context context) {
        this.context = context;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownload_app_folder_name() {
        return download_app_folder_name;
    }

    public void setDownload_app_folder_name(String download_app_folder_name) {
        this.download_app_folder_name = download_app_folder_name;
    }

    public OnDownloadProgressListener getOnProgressListener() {
        return onProgressListener;
    }

    public void setOnProgressListener(OnDownloadProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public Uri getDownIdUri() {
        return downIdUri;
    }

    public void setDownIdUri(Uri downIdUri) {
        this.downIdUri = downIdUri;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
