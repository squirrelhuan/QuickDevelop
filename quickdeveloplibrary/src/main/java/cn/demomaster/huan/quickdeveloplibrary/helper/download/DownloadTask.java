package cn.demomaster.huan.quickdeveloplibrary.helper.download;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import cn.demomaster.huan.quickdeveloplibrary.constant.AppConfig;

/**
 * 下载任务
 */
public class DownloadTask {

    public static enum  DownloadType{
        DownloadManager,
        Okhttp
    }

    // 权限
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private long downloadId;
    private Context context;
    private String fileName;//文件名带格式名
    private String downloadUrl;//下载路径
    private Uri downloadUri;
    private String downUriStr;
    private DownloadType downloadType;
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

    public Uri getDownloadUri() {
        if(downloadUri==null&& !TextUtils.isEmpty(downUriStr)){
            downloadUri= Uri.parse(downUriStr);
        }
        return downloadUri;
    }

    public void setDownloadUri(Uri downloadUri) {
        this.downloadUri = downloadUri;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getDownUriStr() {
        if(downloadUri!=null&& TextUtils.isEmpty(downUriStr)){
            downUriStr = downloadUri.getPath();
        }
        return downUriStr;
    }

    public void setDownUriStr(String downUriStr) {
        this.downUriStr = downUriStr;
        downloadUri= Uri.parse(downUriStr);
    }

    public DownloadType getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(DownloadType downloadType) {
        this.downloadType = downloadType;
    }
}
