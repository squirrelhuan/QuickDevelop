package cn.demomaster.huan.quickdeveloplibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

import cn.demomaster.qdlogger_library.QDLogger;
//系统崩溃异常捕获
public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final String Dir_KEY = "quick_crash_log";
    private Class<? extends Activity> mErrorReportClass;
    private Context mContext;
    private final Thread.UncaughtExceptionHandler systemHandler;
    private final Handler mHandler;
    private CrashDealType mCrashDealType = CrashDealType.showError;//异常结果处理方式
    private OnCrashListener mOnCrashListener;
    private static CrashHandler instance;

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    private CrashHandler() {
        systemHandler = Thread.getDefaultUncaughtExceptionHandler();
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
    }

    public void init(Context context, Class<? extends Activity> errorReportClass) {
        //QDLogger.println("初始化异常捕获");
        mContext = context.getApplicationContext();
        this.mErrorReportClass = errorReportClass;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 设置异常处理方式
     * @param crashDealType
     */
    public void setCrashDealType(CrashDealType crashDealType) {
        this.mCrashDealType = crashDealType;
    }

    /**
     * 设置异常监听
     * @param mOnCrashListener
     */
    public void setOnCrashListener(OnCrashListener mOnCrashListener) {
        this.mOnCrashListener = mOnCrashListener;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //保存崩溃信息
        QDFileUtil.writeFileSdcardFile(getCrashCacheFile(),Log.getStackTraceString(ex),false);
        QDLogger.e(TAG, ex);
        mHandler.post(()->{
           dealError(thread, ex);
        });
    }

    /**
     * 处理异常
     *
     * @param thread
     * @param ex
     */
    private void dealError(Thread thread, Throwable ex) {
        boolean hasDeal = false;
        String tipMessage = Log.getStackTraceString(ex);
        Toast.makeText(mContext, tipMessage, Toast.LENGTH_LONG).show();
        try {
            //处理异常，要么提示用户，要么关闭app，要么重启app
            if (mCrashDealType != null) {
                switch (mCrashDealType) {
                    case savelog:
                    case shutdown:
                        break;
                    case showError:
                        if (mErrorReportClass != null) {//子线程异常，可以展示异常详情
                            Intent intent = new Intent(mContext, mErrorReportClass);
                            intent.putExtra("error", tipMessage);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                mContext.sendBroadcastAsUser(intent, android.os.Process.myUserHandle());//UserHandle.USER_CURRENT
                            }
                            mContext.startActivity(intent);
                            hasDeal = true;
                        } else {//主线程可以错误提示,并关闭app
                            //System.exit(0);
                        }
                        break;
                    case reboot:
                        Process.killProcess(Process.myPid());
                        break;
                    case custome:
                        if (mOnCrashListener != null) {
                            mOnCrashListener.onUncaughtException(thread, ex);
                            hasDeal = true;
                        }
                        break;
                }
            }
        } catch (Throwable throwable) {
            QDLogger.e("异常捕获失败>", throwable);
        }
        if(!hasDeal) {
            mHandler.postDelayed(() -> systemHandler.uncaughtException(thread, ex), 2000);
        }
    }
    public enum CrashDealType {
        savelog,//只保存记录
        reboot,//重启
        shutdown,//关闭
        showError,//显示错误
        custome//自定义
    }

    public interface OnCrashListener {
        void onUncaughtException(Thread thread, Throwable ex);
    }
    public File getCrashCacheDir() {
        File dir = new File(mContext.getCacheDir() + File.separator + Dir_KEY);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }

    private File getCrashCacheFile() {
        String fileName = new Date().toString();
        return new File(getCrashCacheDir() + File.separator + fileName);
    }
}
