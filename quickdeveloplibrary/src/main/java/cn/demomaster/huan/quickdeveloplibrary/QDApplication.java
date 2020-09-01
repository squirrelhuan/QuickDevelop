package cn.demomaster.huan.quickdeveloplibrary;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

//import com.umeng.commonsdk.UMConfigure;
//import com.umeng.socialize.PlatformConfig;

//import com.didichuxing.doraemonkit.DoraemonKit;

import cn.demomaster.huan.quickdeveloplibrary.constant.AppConfig;
import cn.demomaster.huan.quickdeveloplibrary.db.DbHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDActivityManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDSharedPreferences;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.util.CrashHandler;
import cn.demomaster.huan.quickdeveloplibrary.util.QDProcessUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.xml.QDSaxHandler;
import cn.demomaster.huan.quickdeveloplibrary.util.xml.QDSaxXml;
import cn.demomaster.qdlogger_library.QDLogger;

import static cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity.TAG;
import static cn.demomaster.huan.quickdeveloplibrary.util.system.QDAppInfoUtil.isApkInDebug;

public class QDApplication extends Application implements
        DbHelper.DbHelperInterface {

    private static QDApplication instance = null;
    public static QDApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        QdToast.setContext(this);
        AppConfig.getInstance().load(this, "config/project.conf");
        QDLogger.init(this);
        QDLogger.i(TAG,"包名："+getPackageName()+",myPid="+android.os.Process.myPid());

        //初始化全局SharedPreferences
        QDSharedPreferences.init(this);
        QDActivityManager.getInstance().init(this);

        ActivityManager.RunningAppProcessInfo processInfo = QDProcessUtil.getProcessInfo(getApplicationContext(), android.os.Process.myPid());
        // other process will create app instance.
        if (TextUtils.isEmpty(processInfo.processName)||!getPackageName().equals(processInfo.processName)) {
            QDLogger.e(TAG,getPackageName()+"进程["+android.os.Process.myPid()+"]已存在无需重复初始化");
            return;
        }

        //NotifycationHelper.getInstance().init(this);

        //QDSaxXml.parseXmlAssets(this, "config/test.xml", cn.demomaster.huan.quickdeveloplibrary.util.xml.Article.class, null);
        //QDSaxXml.parseXmlAssets(this, "config/test2.xml", cn.demomaster.huan.quickdeveloplibrary.util.xml.AppConfig.class, null);
        QDSaxXml.parseXmlAssets(this, "config/project.xml", cn.demomaster.huan.quickdeveloplibrary.util.xml.AppConfig.class, new QDSaxHandler.OnParseCompleteListener() {
            @Override
            public void onComplete(Object result) {
                QDLogger.println("配置文件初始化完成" + result);
            }
        });

        //处理崩溃日志
        initCrash();

        //DoraemonKit.install(this);
        if (isApkInDebug(this)) {
            // DebugFloatingService.showWindow(this.getApplicationContext(),DebugFloatingService.class);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(base);
    }

    private DbHelper dbHelper;
    public DbHelper getDbHelper() {
        if(dbHelper==null) {
            String dbpath = null;
            if (AppConfig.getInstance().getConfigMap().containsKey("dbpath")) {
                dbpath = (String) AppConfig.getInstance().getConfigMap().get("dbpath");
            }

            if (!TextUtils.isEmpty(dbpath)) {
                dbHelper = new DbHelper(this, dbpath, null, 7, this);
            }
        }
        return dbHelper;
    }

    /**
     * 处理异常捕获
     */
    public void initCrash() {
        if (BuildConfig.DEBUG) {
            QDLogger.println("初始化异常捕获");
            Class errorReportActivity = AppConfig.getInstance().getClassFromClassMap("errorReportActivity");
            if (errorReportActivity == null) {
                QDLogger.println("未定义错误异常捕获页面");
                return;
            }
            CrashHandler.getInstance().init(this,errorReportActivity);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
