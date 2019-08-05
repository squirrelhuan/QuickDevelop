package cn.demomaster.huan.quickdeveloplibrary.util;

/**
 * Created by Stardust on 2017/2/2.
 */


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.WeakReference;

import cn.demomaster.huan.quickdeveloplibrary.R;

public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private final Class<?> mErrorReportClass;
    private WeakReference<Context> applicationWeakReference;

    public CrashHandler(Context context, Class<?> errorReportClass) {
        applicationWeakReference = new WeakReference<>(context.getApplicationContext());
        this.mErrorReportClass = errorReportClass;
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            QDLogger.e("/******************* Error start ********************/");
            QDLogger.e(TAG, "Uncaught Exception", ex);
            QDLogger.e("/******************* Error end ********************/");
            String t = applicationWeakReference.get().getString(R.string.sorry_for_crash) + ex.toString();
            Intent intent = new Intent(applicationWeakReference.get(), this.mErrorReportClass);
            intent.putExtra("message", t);
            intent.putExtra("error", throwableToString(ex));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            applicationWeakReference.get().startActivity(intent);
            //System.exit(0);
        } catch (Throwable var6) {
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
}
