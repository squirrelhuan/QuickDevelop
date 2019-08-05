package cn.demomaster.huan.quickdeveloplibrary.helper.download;

public interface OnDownloadProgressInterface {
    /**
     * 下载进度
     *
     * @param fraction 已下载/总大小
     */
    void onProgress(long downloadId, String name, float fraction);
}
