package cn.demomaster.huan.quickdeveloplibrary.helper.download;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * 下载任务
 */
public class DownloadTask {

    public enum DownloadType {
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
    private String downloadPath;
    private String downFilePath;
    private DownloadType downloadType;
    private String savePath;

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    //
    private OnDownloadProgressListener onProgressListener;//下载进度监听器

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
    }

    //默认下载路径
    private String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();//AppConfig.getInstance().getConfig("DownloadFilePath").toString()

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

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
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

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
        downloadUri = Uri.parse(downloadPath);
    }

    public void setDownloadFilePath(String downFilePath) {
        this.downFilePath = downFilePath;
        downloadUri = Uri.fromFile(new File(downFilePath));
    }

    public DownloadType getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(DownloadType downloadType) {
        this.downloadType = downloadType;
    }
}
