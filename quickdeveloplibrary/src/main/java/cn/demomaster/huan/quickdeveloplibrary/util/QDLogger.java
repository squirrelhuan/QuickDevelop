package cn.demomaster.huan.quickdeveloplibrary.util;

import android.content.Context;
import android.util.Log;

public class QDLogger {

    public static boolean enable = true;
    public static String TAG = "QDLogger";

    public static void setEnable(boolean enable) {
        QDLogger.enable = enable;
    }

    public static void setTAG(String TAG) {
        QDLogger.TAG = TAG;
    }

    /**********  i ***********/
    public static void i(Object obj){
        Log(Log.INFO,TAG,obj);
    }
    public static void i(String tag,Object obj){
        Log(Log.INFO,tag,obj.toString());
    }
    public static void i(Context context , Object obj){
        Log(context,Log.INFO,TAG,obj.toString());
    }
    public static void i(Context context ,String tag, Object obj){
        Log(context,Log.INFO,tag,obj.toString());
    }

    /**********  d ***********/
    public static void d(String tag,Object obj){
        Log(Log.DEBUG,tag,obj.toString());
    }
    public static void d(Object obj){
        Log(Log.DEBUG,TAG,obj.toString());
    }
    public static void d(Context context ,Object obj){
        Log(context,Log.DEBUG,TAG,obj.toString());
    }
    public static void d(Context context ,String tag,Object obj){
        Log(context,Log.DEBUG,tag,obj.toString());
    }

    /**********  e ***********/
    public static void e(String tag,Object obj){
        Log(Log.ERROR,tag,obj.toString());
    }
    public static void e(String tag,Object obj,Throwable tr){
        Log(null,Log.ERROR,tag,obj.toString(),tr);
    }
    public static void e(Object obj){
        Log(Log.ERROR,TAG,obj.toString());
    }
    public static void e(Context context ,Object obj){
        Log(context,Log.ERROR,TAG,obj.toString());
    }
    public static void e(Context context ,String tag,Object obj){
        Log(context,Log.ERROR,tag,obj.toString());
    }
    public static void e(Context context ,String tag,Object obj,Throwable tr){
        Log(context,Log.ERROR,tag,obj.toString(),tr);
    }

    /**********  v ***********/
    public static void v(String tag,Object obj){
        Log(Log.VERBOSE,tag,obj.toString());
    }
    public static void v(Object obj){
        Log(Log.VERBOSE,TAG,obj.toString());
    }
    public static void v(Context context ,Object obj){
        Log(context,Log.VERBOSE,TAG,obj.toString());
    }
    public static void v(Context context ,String tag,Object obj){
        Log(context,Log.VERBOSE,tag,obj.toString());
    }

    /**********  w ***********/
    public static void w(String tag,Object obj){
        Log(Log.WARN,tag,obj.toString());
    }
    public static void w(Object obj){
        Log(Log.WARN,TAG,obj.toString());
    }
    public static void w(Context context,Object obj){
        Log(context,Log.WARN,TAG,obj.toString());
    }

    private static void Log(int logType, String tag, Object obj){
        Log(null,logType,tag,obj);
    }

    private static void Log(Context context,int logType, String tag, Object obj){
        Log(null,logType,tag,obj,null);
    }

    private static void Log(Context context,int logType, String tag, Object obj,Throwable tr){
        String message =(context==null?"":(context.getClass().getName()+":"));
        if(obj==null) message="null";
        switch (logType){
            case Log.VERBOSE:// = 2;
                Log.v(tag,message);
                break;
            case Log.INFO://INFO = 4;
                Log.i(tag,message);
                break;
            case Log.WARN:// = 5;
                Log.w(tag,message);
                break;
            case Log.DEBUG://= 3;
                Log.d(tag,message);
                break;
            case Log.ERROR://= 6;
                message = tr==null?message:message+tr.getMessage()+tr.getCause();
                Log.e(tag,message);
                break;
        }
    }
}
