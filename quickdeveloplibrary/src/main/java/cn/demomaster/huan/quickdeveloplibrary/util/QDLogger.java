package cn.demomaster.huan.quickdeveloplibrary.util;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import cn.demomaster.huan.quickdeveloplibrary.BuildConfig;
import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.constant.AppConfig;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;

/**
 * 日志打印帮助类
 */
public class QDLogger {
    public static boolean enable = true;//是否启用
    public static String TAG = "QDLogger";
    private static Context applicationContext;

    public static enum Logger{
        ALL(-2),
        INFO(Log.INFO),
        DEBUG(Log.DEBUG),
        ERROR(Log.ERROR),
        VERBOSE(Log.VERBOSE),
        WARN(Log.WARN),
        PRINTLN(-3);

        private int value = 0;
        Logger(int value) {//必须是private的，否则编译错误
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }

    public static void setApplicationContext(Context context) {
        applicationContext = context.getApplicationContext();
    }

    public static void setEnable(boolean enable) {
        QDLogger.enable = enable;
    }

    public static void setTAG(String tag) {
        TAG = tag;
    }

    /**********  i ***********/
    public static void i(Object obj) {
        Log(Logger.INFO, TAG, obj);
    }

    public static void i(String tag, Object obj) {
        Log(Logger.INFO, tag, obj.toString());
    }

    public static void i(Context context, Object obj) {
        Log(context, Logger.INFO, TAG, obj.toString());
    }

    public static void i(Context context, String tag, Object obj) {
        Log(context, Logger.INFO, tag, obj.toString());
    }

    /**********  d ***********/
    public static void d(String tag, Object obj) {
        Log(Logger.DEBUG, tag, obj.toString());
    }

    public static void d(Object obj) {
        Log(Logger.DEBUG, TAG, obj);
    }

    public static void d(Context context, Object obj) {
        Log(context, Logger.DEBUG, TAG, obj.toString());
    }

    public static void d(Context context, String tag, Object obj) {
        Log(context, Logger.DEBUG, tag, obj);
    }

    /**********  e ***********/
    public static void e(String tag, Object obj) {
        Log(Logger.ERROR, tag, obj.toString());
    }

    public static void e(String tag, Object obj, Throwable tr) {
        doLog(null, Logger.ERROR, tag, obj, tr);
    }

    public static void e(Object obj) {
        Log(Logger.ERROR, TAG, obj.toString());
    }

    public static void e(Context context, Object obj) {
        Log(context, Logger.ERROR, TAG, obj.toString());
    }

    public static void e(Context context, String tag, Object obj) {
        Log(context, Logger.ERROR, tag, obj);
    }

    public static void e(Context context, String tag, Object obj, Throwable tr) {
        doLog(context, Logger.ERROR, tag, obj, tr);
    }

    /**********  v ***********/
    public static void v(String tag, Object obj) {
        Log(Logger.VERBOSE, tag, obj.toString());
    }

    public static void v(Object obj) {
        Log(Logger.VERBOSE, TAG, obj);
    }

    public static void v(Context context, Object obj) {
        Log(context, Logger.VERBOSE, TAG, obj.toString());
    }

    public static void v(Context context, String tag, Object obj) {
        Log(context, Logger.VERBOSE, tag, obj);
    }

    /**********  w ***********/
    public static void w(String tag, Object obj) {
        Log(Logger.WARN, tag, obj.toString());
    }

    public static void w(Object obj) {
        Log(Logger.WARN, TAG, obj);
    }

    public static void w(Context context, Object obj) {
        Log(context, Logger.WARN, TAG, obj);
    }

    private static void Log(Logger logType, String tag, Object obj) {
        Log(null, logType, tag, obj);
    }

    private static void Log(Context context, Logger logType, String tag, Object obj) {
        doLog(null, logType, tag, obj, null);
    }

    public static void println(String message) {
        println("System",message);
    }
    
    public static void println(String tag,String message) {
        System.out.println(tag+" "+message);
        LogBean logBean = new LogBean(Logger.PRINTLN, tag, message);
        if (mLogInterceptor != null) {
            mLogInterceptor.onLog(logBean);
        }
    }

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA);// HH:mm:ss
    public static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);// HH:mm:ss
    public static SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss:SSS", Locale.CHINA);// HH:mm:ss

   /* static Locale mLocale = Locale.CHINA;
    public static void setLocale(Locale locale) {
        mLocale = locale;
        simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", mLocale);// HH:mm:ss
        simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd", mLocale);// HH:mm:ss
    }*/

    /**
     * 设置时区
     *
     * @param timeZone
     */
    public static void setTimeZone(TimeZone timeZone) {
        simpleDateFormat.setTimeZone(timeZone);
        simpleDateFormat2.setTimeZone(timeZone);
    }

    static String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static void doLog(Context context, Logger logType, String tag, Object obj, Throwable throwable) {

        if (applicationContext == null && context != null) {
            applicationContext = context.getApplicationContext();
        }

        if (obj instanceof Exception && BuildConfig.DEBUG) {
            ((Exception) obj).printStackTrace();
        }

        String message = obj==null?"":obj.toString();
        if (mLogInterceptor != null) {
            LogBean logBean = new LogBean(logType, tag, message);
            mLogInterceptor.onLog(logBean);
        }

        if(logType==Logger.VERBOSE){
            Log.v(tag, message);
        }else if(logType==Logger.INFO){
            Log.i(tag, message);
        }else if(logType==Logger.WARN){
            Log.w(tag, message);
        }else if(logType==Logger.DEBUG){
            Log.d(tag, message);
        }else if(logType==Logger.ERROR){
            message = message + (throwable==null?"":("==>"+throwable.toString()));
            Log.e(tag, message);
        }else if(logType==Logger.PRINTLN){
            System.out.println(message);
        }
        //Log.d(TAG,new Exception().getStackTrace()[0].getMethodName()); //函数名
        //Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName()); //函数名
        //Log.d(TAG, +Thread.currentThread().getStackTrace()[2].getLineNumber()); //行号
        //Log.d(TAG, Thread.currentThread().getStackTrace()[2].getFileName()); //文件名


        //如果配置了日志目录，则打印log到指定目录
        if (enable && applicationContext != null && AppConfig.getInstance().getConfigMap() != null && AppConfig.getInstance().getConfigMap().containsKey("LogFilePath")) {
            // 检查该权限是否已经获取
            boolean useful = PermissionManager.getInstance().getPermissionStatus(applicationContext, permissions);
            if (useful) {
                Date date = new Date(System.currentTimeMillis());
                String LogFilePath = AppConfig.getInstance().getConfigMap().get("LogFilePath").toString();
                if (!LogFilePath.startsWith(File.separator)) {
                    LogFilePath = File.separator + LogFilePath;
                }
                LogFilePath = LogFilePath + File.separator + simpleDateFormat2.format(date) + ".txt";

                if (canWriteAble) {
                    File file = new File(Environment.getExternalStorageDirectory(), LogFilePath);
                    QDFileUtil.createFile(file);
                    QDFileUtil.writeFileSdcardFile(file, simpleDateFormat3.format(date) + " "  + message + "\n", true);
                }
            } else {
                println(message);
                if (!firstError) {
                    firstError = true;
                    String tip = "本地日志写入失败，请打开存储权限";
                    println(tip);
                    Log.e(TAG, tip);
                }
                // throw new IllegalArgumentException("log 打印失败，请打开存储权限");
            }
        }
    }

    public static boolean firstError;
    public static boolean canWriteAble = true;//是否可以读写日志

    public static void setCanWriteAble(boolean mcanWriteAble) {
        canWriteAble = mcanWriteAble;
        if (!mcanWriteAble) {
            System.err.println("暂停日志文件写入权限");
        } else {
            System.err.println("恢复日志写入文件权限");
        }
    }

    static int[] tagColors = new int[]{ Color.WHITE,Color.BLUE,Color.GREEN,Color.YELLOW,Color.RED,Color.RED};
    public static int getColor(int logType){
        int color = tagColors[1];
        switch (logType) {
            case Log.VERBOSE:// 2;
               color = tagColors[0];
                break;
            case Log.DEBUG:// 3;
                color = applicationContext.getResources().getColor(R.color.deepskyblue);
                break;
            case Log.INFO:// 4;
                color = tagColors[2];
                break;
            case Log.WARN:// 5;
                color = tagColors[3];
                break;
            case Log.ERROR:// 6;
                color = tagColors[4];
                break;
            case -2:// 6;
                color = tagColors[2];
                break;
        }
        return color;
    }
    static LogInterceptor mLogInterceptor;

    /**
     * 日志拦截器
     * @param logInterceptor
     */
    public static void setInterceptor(LogInterceptor logInterceptor) {
        mLogInterceptor = logInterceptor;
    }

    public static interface LogInterceptor {
        void onLog(LogBean msg);
    }

    public static class LogBean {
        private Logger type;
        private String message;
        private String tag;

        public LogBean(Logger type, String tag, String message) {
            this.type = type;
            this.message = message;
            this.tag = tag;
        }

        public Logger getType() {
            return type;
        }

        public void setType(Logger type) {
            this.type = type;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }
}
