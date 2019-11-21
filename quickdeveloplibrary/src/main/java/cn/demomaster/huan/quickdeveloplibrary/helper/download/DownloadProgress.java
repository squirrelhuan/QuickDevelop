package cn.demomaster.huan.quickdeveloplibrary.helper.download;

public class DownloadProgress {
    private int hasLoadSize;//已经下载的数据量
    private int totalSize;//总大小
    private int status;//下载状态
    private long downloadId;
    private String fileName;//文件名带格式名
    private String downloadUrl;//下载路径
    private String downloadUri;//下载路径



    public DownloadProgress(long downloadId, int status, int hasLoadSize, int totalSize) {
        this.hasLoadSize = hasLoadSize;
        this.totalSize = totalSize;
        this.status = status;
        this.downloadId = downloadId;
    }

    public int getHasLoadSize() {
        return hasLoadSize;
    }

    public void setHasLoadSize(int hasLoadSize) {
        this.hasLoadSize = hasLoadSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
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

    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }
}
