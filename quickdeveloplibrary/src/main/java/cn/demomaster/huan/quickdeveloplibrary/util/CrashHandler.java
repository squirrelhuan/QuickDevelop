package cn.demomaster.huan.quickdeveloplibrary.util;

/**
 * Created by Stardust on 2017/2/2.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Process;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.WeakReference;

import cn.demomaster.huan.quickdeveloplibrary.R;

public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private Class<?> mErrorReportClass;
    private Context mContext;
    private CrashDealType mCrashDealType = CrashDealType.showError;
    private OnCrashListener mOnCrashListener;
    private static CrashHandler instance;

    public static CrashHandler getInstance() {
        if(instance==null){
            instance = new CrashHandler();
        }
        return instance;
    }

    private CrashHandler() {
    }
    public void init(Context context, Class<?> errorReportClass) {
        mContext = context.getApplicationContext();
        this.mErrorReportClass = errorReportClass;

        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void setCrashDealType(CrashDealType crashDealType) {
        this.mCrashDealType = crashDealType;
    }

    public void setmOnCrashListener(OnCrashListener mOnCrashListener) {
        this.mOnCrashListener = mOnCrashListener;
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            QDLogger.e("/******************* "+Process.myPid()+" Error start ********************/");
            QDLogger.e(TAG, throwableToString(ex));
            QDLogger.e("/******************* Error end ********************/");
            String t = mContext.getString(R.string.sorry_for_crash) + ex.toString();
            //处理异常，要么提示用户，要么关闭app，要么重启app
            if (mCrashDealType != null) {
                switch (mCrashDealType) {
                    case shutdown:
                        System.exit(0);
                        break;
                    case showError:
                        if(mErrorReportClass!=null) {
                            Intent intent = new Intent(mContext, this.mErrorReportClass);
                            intent.putExtra("message", t);
                            intent.putExtra("error", throwableToString(ex));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                mContext.sendBroadcastAsUser(intent, android.os.Process.myUserHandle());//UserHandle.USER_CURRENT
                            }
                            mContext.startActivity(intent);
                        }
                        //UserHandle.CURRENT
                        break;
                    case reboot:
                        Process.killProcess(Process.myPid());
                        break;
                    case custome:
                        if (mOnCrashListener != null) {
                            mOnCrashListener.onUncaughtException(thread, ex);
                        }
                        break;
                }
            }else {
                QDLogger.e("mCrashDealType = null");
            }
        } catch (Throwable var6) {
            QDLogger.e("/******************* Error var6.printStackTrace() ********************/");
            var6.printStackTrace();
        }
    }

    public static String throwableToString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace();
        throwable.printStackTrace(pw);
        return sw.toString();
    }

    public static enum CrashDealType {
        reboot,//重启
        shutdown,//关闭
        showError,//显示错误
        custome//自定义
    }

    public static interface OnCrashListener {
        void onUncaughtException(Thread thread, Throwable ex);
    }
}
