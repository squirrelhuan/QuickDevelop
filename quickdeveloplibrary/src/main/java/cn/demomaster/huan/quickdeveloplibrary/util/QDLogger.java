package cn.demomaster.huan.quickdeveloplibrary.util;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.demomaster.huan.quickdeveloplibrary.constant.AppConfig;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;

import static android.content.Context.DOWNLOAD_SERVICE;
import static cn.demomaster.huan.quickdeveloplibrary.helper.download.DownloadHelper.PERMISSIONS_STORAGE;

public class QDLogger {

    public static boolean enable = true;
    public static String TAG = "QDLogger";
    private static Context applicationContext;

    public static void setApplicationContext(Context applicationContext) {
        QDLogger.applicationContext = applicationContext;
    }

    public static void setEnable(boolean enable) {
        QDLogger.enable = enable;
    }

    public static void setTAG(String TAG) {
        QDLogger.TAG = TAG;
    }

    /**********  i ***********/
    public static void i(Object obj) {
        Log(Log.INFO, TAG, obj);
    }

    public static void i(String tag, Object obj) {
        Log(Log.INFO, tag, obj.toString());
    }

    public static void i(Context context, Object obj) {
        Log(context, Log.INFO, TAG, obj.toString());
    }

    public static void i(Context context, String tag, Object obj) {
        Log(context, Log.INFO, tag, obj.toString());
    }

    /**********  d ***********/
    public static void d(String tag, Object obj) {
        Log(Log.DEBUG, tag, obj.toString());
    }

    public static void d(Object obj) {
        Log(Log.DEBUG, TAG, obj);
    }

    public static void d(Context context, Object obj) {
        Log(context, Log.DEBUG, TAG, obj.toString());
    }

    public static void d(Context context, String tag, Object obj) {
        Log(context, Log.DEBUG, tag, obj);
    }

    /**********  e ***********/
    public static void e(String tag, Object obj) {
        Log(Log.ERROR, tag, obj.toString());
    }

    public static void e(String tag, Object obj, Throwable tr) {
        Log(null, Log.ERROR, tag, obj, tr);
    }

    public static void e(Object obj) {
        Log(Log.ERROR, TAG, obj.toString());
    }

    public static void e(Context context, Object obj) {
        Log(context, Log.ERROR, TAG, obj.toString());
    }

    public static void e(Context context, String tag, Object obj) {
        Log(context, Log.ERROR, tag, obj);
    }

    public static void e(Context context, String tag, Object obj, Throwable tr) {
        Log(context, Log.ERROR, tag, obj, tr);
    }

    /**********  v ***********/
    public static void v(String tag, Object obj) {
        Log(Log.VERBOSE, tag, obj.toString());
    }

    public static void v(Object obj) {
        Log(Log.VERBOSE, TAG, obj);
    }

    public static void v(Context context, Object obj) {
        Log(context, Log.VERBOSE, TAG, obj.toString());
    }

    public static void v(Context context, String tag, Object obj) {
        Log(context, Log.VERBOSE, tag, obj);
    }

    /**********  w ***********/
    public static void w(String tag, Object obj) {
        Log(Log.WARN, tag, obj.toString());
    }

    public static void w(Object obj) {
        Log(Log.WARN, TAG, obj);
    }

    public static void w(Context context, Object obj) {
        Log(context, Log.WARN, TAG, obj);
    }

    private static void Log(int logType, String tag, Object obj) {
        Log(null, logType, tag, obj);
    }

    private static void Log(Context context, int logType, String tag, Object obj) {
        Log(null, logType, tag, obj, null);
    }

    private static String message;
    private static String Tag;

    private static void Log(Context context, int logType, String tag, Object obj, Throwable tr) {
        message = (context == null ? (obj == null ? "NULL" : obj.toString()) : (context.getClass().getName() + ":"));
        Tag = tag;
        try {
            switch (logType) {
                case Log.VERBOSE:// = 2;
                    Log.v(Tag, message);
                    break;
                case Log.INFO://INFO = 4;
                    Log.i(Tag, message);
                    break;
                case Log.WARN:// = 5;
                    Log.w(Tag, message);
                    break;
                case Log.DEBUG://= 3;
                    Log.d(Tag, message);
                    break;
                case Log.ERROR://= 6;
                    message = tr == null ? message : message + tr.getMessage() + tr.getCause();
                    Log.e(Tag, message);
                    break;
            }
        } catch (Exception e) {
            System.out.println(message);
        }

        if (applicationContext == null && context != null) {
            applicationContext = context.getApplicationContext();
        }

        //如果配置了日志目录，则打印log到指定目录
        if (applicationContext != null && AppConfig.getInstance().getConfigMap() != null && AppConfig.getInstance().getConfigMap().containsKey("LogFilePath")) {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            // 检查该权限是否已经获取
            boolean useful = PermissionManager.getPermissionStatus(applicationContext, permissions);
            if (useful) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
                Date date = new Date(System.currentTimeMillis());
                String LogFilePath  = AppConfig.getInstance().getConfigMap().get("LogFilePath").toString();
                String fileName = AppConfig.getInstance().getConfigMap().get("LogFilePath").toString();
                if (!fileName.startsWith(File.separator)) {
                    fileName = File.separator + fileName;
                }
                if (!fileName.contains(".")) {
                    fileName = fileName + File.separator + simpleDateFormat2.format(date) + ".txt";
                }

                String path = Environment.getExternalStorageDirectory() + "";
                if (TextUtils.isEmpty(path)) {
                    path = applicationContext.getFilesDir().getAbsolutePath();
                    System.out.println("外置内存卡不存在,log存储路径已改为：" + path);
                } else {
                    //  System.out.println("外置内存卡存在");
                }
                File dir = new File(path+(LogFilePath.startsWith(File.separator)?LogFilePath:File.separator+LogFilePath));
                if(!dir.exists()){
                    dir.mkdir();
                }
                FileUtil.writeFileSdcardFile(path + File.separator + fileName, simpleDateFormat.format(date) + " " + applicationContext.getPackageName() + "-" + "[" + Tag + "]" + message + "\n", true);
            } else {
                Log.e("qdlog error","本地日志写入失败，请打开存储权限");
                // throw new IllegalArgumentException("log 打印失败，请打开存储权限");
            }
        }
    }
}
