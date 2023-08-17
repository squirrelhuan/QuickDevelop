package cn.demomaster.huan.quickdeveloplibrary.helper.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import cn.demomaster.huan.quickdeveloplibrary.helper.download.DownloadHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.download.DownloadTask;
import cn.demomaster.huan.quickdeveloplibrary.helper.download.OnDownloadProgressListener;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.qdlogger_library.QDLogger;

import static cn.demomaster.huan.quickdeveloplibrary.helper.cache.CacheMap.stringToMD5;

import java.io.File;

public class QuickCache {
    private static CacheMap quickCacheMap;
    private boolean hasInited;
    private static String cacheFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() ;
    private static String cacheChildFolderPath =  "/qdlogger/cache/";
    static Context applicationContext;
    private static QuickCache instance;

    public static QuickCache getInstance() {
        if(instance ==null){
            instance = new QuickCache();
        }
        return instance;
    }

    public void init(Context context, String cacheFolder) {
        this.cacheChildFolderPath = cacheFolder;
        String dirPath = getCacheFolderPath(context) + this.cacheChildFolderPath;
        cacheFolderPath= dirPath;
        QDLogger.i("缓存路径："+dirPath);
        applicationContext = context.getApplicationContext();
        QDFileUtil.createDir(dirPath);
        if (quickCacheMap == null) {
            quickCacheMap = new CacheMap();
        }
        quickCacheMap.loadData(cacheFolderPath);
    }

    static String diskCacheDir;
    static String saveExternalStoragePath;//存储在外部存储时的路径
    static String saveInternalSoragePath;//保存在内部存储缓存目录中的位置
    static public boolean saveExternalStorageAfterAndroidQ;//强制使用外置存储 针对Android Q以上的版本，但是需要 MANAGE_EXTERNAL_STORAGE。 true 保存在指定文件夹，,false则保存在缓存目录
    static public boolean saveExternalStorageBeforeAndroidQ;//强制使用外置存储 Android Q以下的版本。true 保存在指定文件夹，false则保存在缓存目录

    public static String getCacheFolderPath(Context context) {
        diskCacheDir = cn.demomaster.qdlogger_library.util.QDFileUtil.getDiskCacheDir(context);
        saveInternalSoragePath = cacheChildFolderPath;

        File file;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            if (saveExternalStorageAfterAndroidQ) {//强制使用外置存储 针对Android Q以上的版本，但是需要 MANAGE_EXTERNAL_STORAGE
                file = new File(saveExternalStoragePath);
            } else {
                file = new File(diskCacheDir, saveInternalSoragePath);
            }
        } else {
            if (saveExternalStorageBeforeAndroidQ) {//使用外置存储
                file = new File(saveExternalStoragePath);
            } else {
                file = new File(diskCacheDir, saveInternalSoragePath);
            }
        }
        //System.out.println("sdk_int:" + Build.VERSION.SDK_INT + ",存储路径:" + file.getAbsolutePath());
        String DIR_PATH = file.getAbsolutePath();
        if (!TextUtils.isEmpty(DIR_PATH)) {
            if (!DIR_PATH.trim().endsWith(File.separator)) {
                DIR_PATH += File.separator;
            }
        }
        return DIR_PATH;
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveBitmap(Context context, String url, Bitmap bitmap) {
        String key = stringToMD5(url) + ".png";
        String dirPath = getCacheFolderPath(context) + this.cacheChildFolderPath;
        String folderPath = dirPath + "img/";
        String filePath = folderPath + key;
        QDFileUtil.createDir(folderPath);
        String path = QDFileUtil.saveBitmap(bitmap, filePath);
        if (!TextUtils.isEmpty(path)) {
            updateFilePath(url, filePath);
        }
    }

    /**
     * 是否包含url的文件
     *
     * @param url
     * @return
     */
    public static boolean containsUrl(String url) {
        if (quickCacheMap == null || TextUtils.isEmpty(url)) {
            return false;
        }
        return quickCacheMap.containsURL(url);
    }

    public static CacheInfo getCacheInfoByUrl(String url) {
        if (quickCacheMap != null && quickCacheMap.containsURL(url)) {
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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void success(String url, String fileType, int fileLength) {
                downCacheFile(url, fileType);
            }

            @Override
            public void error() {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void downCacheFile(String url, String fileType) {
        String fileTypeStr = fileType;
        if (fileType.contains("/")) {
            String[] strings = fileType.split("/");
            fileTypeStr = strings[strings.length - 1];
        }
        String key = stringToMD5(url) + "." + fileTypeStr;
        downCacheFile2(url, key);
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
