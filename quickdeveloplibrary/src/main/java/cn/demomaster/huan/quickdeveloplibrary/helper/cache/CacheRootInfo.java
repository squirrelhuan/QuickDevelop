package cn.demomaster.huan.quickdeveloplibrary.helper.cache;

public class CacheRootInfo {
    //int fileType;
    String filePaths;
    String filePath;
    String url;
    String md5;
    CacheMap cacheMap;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
