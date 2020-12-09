package cn.demomaster.huan.quickdeveloplibrary.util;

/**
 * Created by Stardust on 2017/2/2.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.WeakReference;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDActivityManager;
import cn.demomaster.qdlogger_library.QDLogger;

import static android.os.Looper.getMainLooper;

public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private Class<?extends Activity> mErrorReportClass;
    private Context mContext;
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
    }

    public void init(Context context, Class<?extends Activity> errorReportClass) {
        QDLogger.println("初始化异常捕获");
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
    public void setmOnCrashListener(OnCrashListener mOnCrashListener) {
        this.mOnCrashListener = mOnCrashListener;
    }
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
            QDLogger.e(TAG, ex);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    dealError(thread,ex);
                    Looper.loop();
                }
            }).start();
    }

    /**
     * 处理异常
     * @param thread
     * @param ex
     */
    private void dealError(Thread thread, Throwable ex) {
        try {
            boolean isMainLooper = Looper.getMainLooper().getThread().getId() == thread.getId();
            String tipMessage = mContext.getString(R.string.sorry_for_crash) + ex.toString();
            //处理异常，要么提示用户，要么关闭app，要么重启app
            if (mCrashDealType != null) {
                switch (mCrashDealType) {
                    case shutdown:
                        break;
                    case showError:
                        if (mErrorReportClass != null &&!isMainLooper) {//子线程异常，可以展示异常详情
                            Intent intent = new Intent(mContext, mErrorReportClass);
                            intent.putExtra("message", tipMessage);
                            intent.putExtra("error", throwableToString(ex));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                mContext.sendBroadcastAsUser(intent, android.os.Process.myUserHandle());//UserHandle.USER_CURRENT
                            }
                            mContext.startActivity(intent);
                        }else {//主线程可以错误提示,并关闭app
                            Toast.makeText(mContext, tipMessage, Toast.LENGTH_LONG).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    System.exit(0);
                                }
                            },5000);
                        }
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
            }
        } catch (Throwable throwable) {
            QDLogger.e("异常捕获失败>", throwable);
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
