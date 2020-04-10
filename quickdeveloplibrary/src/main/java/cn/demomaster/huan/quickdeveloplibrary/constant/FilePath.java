package cn.demomaster.huan.quickdeveloplibrary.constant;

import android.os.Environment;

/**
 * Created by huan on 2017/10/25.
 */

public class FilePath {

    public final static String BASE_FILE_PATH = Environment.getExternalStorageDirectory() + "/geneg";

    public final static String APP_PATH_CACHE_DIR = BASE_FILE_PATH + "/cache";

    public final static String APP_PATH_PICTURE = BASE_FILE_PATH + "/picture";

    //app下载路径
    public static String DOWNLOAD_APP_FOLDER_NAME = BASE_FILE_PATH+"/update";
    //app下载名称
    public static String DOWNLOAD_APP_FILE_NAME = "geneg.apk";
}
