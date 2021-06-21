package cn.demomaster.huan.quickdeveloplibrary.helper.download;

public interface OnDownloadProgressInterface {
    /**
     * 下载进度
     *
     * @param progress 已下载/总大小
     */
    void onDownloadRunning(long downloadId, String name, float progress);

    void onDownloadSuccess(DownloadTask downloadTask);
}
