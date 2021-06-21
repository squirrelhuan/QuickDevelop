package cn.demomaster.huan.quickdeveloplibrary.helper.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import cn.demomaster.huan.quickdeveloplibrary.helper.download.DownloadHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.download.DownloadTask;
import cn.demomaster.huan.quickdeveloplibrary.helper.download.OnDownloadProgressListener;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.qdlogger_library.QDLogger;

import static cn.demomaster.huan.quickdeveloplibrary.helper.cache.CacheMap.stringToMD5;

public class QuickCache {
    private static CacheMap quickCacheMap;
    private static String cacheFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/qdlogger/cache/";
    static Context applicationContext;

    public static void init(Context context, String cacheFolder) {
        cacheFolderPath = cacheFolder;
        applicationContext = context.getApplicationContext();
        if (quickCacheMap == null) {
            quickCacheMap = new CacheMap();
        }
        quickCacheMap.loadData(cacheFolderPath);
    }

    /**
     * 是否启用了缓存
     *
     * @return
     */
    public static boolean enable() {
        return quickCacheMap != null;
    }

    /**
     * 添加文件到缓存
     *
     * @param url
     * @param fileType
     * @param filePath
     */
    public static void addFile(String url, String fileType, String filePath) {
        CacheInfo cacheInfo = getCacheInfoByUrl(url);
        if (cacheInfo == null) {
            cacheInfo = new CacheInfo();
        }
        cacheInfo.setFileType(fileType);
        if (!TextUtils.isEmpty(filePath)) {
            cacheInfo.setFilePath(filePath);
        }
        quickCacheMap.put(url, cacheInfo);
    }

    /**
     * 缓存图片
     *
     * @param url
     * @param bitmap
     */
    public static void saveBitmap(String url, Bitmap bitmap) {
        String key = stringToMD5(url) + ".png";
        String folderPath = cacheFolderPath + "img/";
        String filePath = folderPath + key;
        QDFileUtil.createDir(folderPath);
        QDFileUtil.saveBitmap(bitmap, filePath);
        updateFilePath(url, filePath);
    }

    /**
     * 是否包含url的文件
     *
     * @param url
     * @return
     */
    public static boolean containsUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return quickCacheMap.containsURL(url);
    }

    public static CacheInfo getCacheInfoByUrl(String url) {
        if (quickCacheMap.containsURL(url)) {
            return quickCacheMap.getCacheInfoByUrl(url);
        }
        return null;
    }

    public static void updateFilePath(String url, String filePath) {
        CacheInfo cacheInfo = getCacheInfoByUrl(url);
        if (cacheInfo != null) {
            cacheInfo.setFilePath(filePath);
            System.out.println("添加到缓存url=" + url + ",filePath=" + filePath);
            quickCacheMap.put(url, cacheInfo);
        }
    }

    public static void downCacheFile(String url) {
        //判断文件类型
        UrlHelper.analyUrl(url, new UrlHelper.AnalyResult() {
            @Override
            public void success(String url, String fileType, int fileLength) {
                downCacheFile(url, fileType);
            }

            @Override
            public void error() {

            }
        });
    }

    public static void downCacheFile(String url, String fileType) {
        String fileTypeStr = fileType;
        if (fileType.contains("/")) {
            String[] strings = fileType.split("/");
            fileTypeStr = strings[strings.length - 1];
        }
        String key = stringToMD5(url) + "." + fileTypeStr;
        String fileName = key;
        downCacheFile2(url, fileName);
    }

    static int downloadState = 0;

    private static void downCacheFile2(String url, String fileName) {
        if (containsUrl(url)) {
            CacheInfo cacheInfo = getCacheInfoByUrl(url);
            if (cacheInfo != null) {
                if (!TextUtils.isEmpty(cacheInfo.getFilePath())) {
                    return;
                }
                System.out.println("缓存文件不存在，重新下载");
            }
        }
        if (downloadState == 0) {
            new DownloadHelper.DownloadBuilder(applicationContext).setUrl(url)
                    .setFileName(fileName)
                    .setDirectoryPath(cacheFolderPath)
                    .setDownloadType(DownloadTask.DownloadType.Okhttp)
                    .setOnProgressListener(
                            new OnDownloadProgressListener() {
                                @Override
                                public void onDownloadRunning(long downloadId, String name, float progress) {
                                    downloadState = 1;
                                    QDLogger.d("下载状态：" + downloadId + "," + name + "," + progress);
                                }

                                @Override
                                public void onDownloadSuccess(DownloadTask downloadTask) {
                                    downloadState = 0;
                                    QDLogger.i("1下载完成" + downloadTask.getSavePath());
                                    updateFilePath(url, downloadTask.getSavePath());
                                }

                                @Override
                                public void onDownloadFail() {
                                    downloadState = 2;
                                }

                                @Override
                                public void onDownloadPaused() {

                                }
                            }).start();
        }
    }
}
