package cn.demomaster.huan.quickdeveloplibrary;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.text.TextUtils;

import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.constant.AppConfig;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDSharedPreferences;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.ui.error.ErrorCrashActivity;
import cn.demomaster.huan.quickdeveloplibrary.util.CrashHandler;
import cn.demomaster.huan.quickdeveloplibrary.util.system.QDAppInfoUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.xml.QDSaxXml;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.manager.QDActivityManager;
import cn.demomaster.quickdatabaselibrary.QuickDbHelper;
import cn.demomaster.quickdatabaselibrary.listener.UpgradeInterface;

import static cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity.TAG;

public class QDApplication extends Application implements UpgradeInterface {

    private static QDApplication instance = null;
    public Handler handler;

    public static QDApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        handler = new Handler();
        QdToast.setContext(this);
        //AppConfig.getInstance().load(this, "config/project.conf");
        //String logPath = (String) AppConfig.getInstance().getConfigMap().get("LogFilePath");
        /*if(!TextUtils.isEmpty(logPath)) {
            QDLogger.init(this, logPath);
        }*/
        QDLogger.init(this,null);
        //QDLogger.i(TAG, "包名：" + getPackageName() + ",myPid=" + android.os.Process.myPid()+",uid="+QDAppInfoUtil.getUidByPackageName(this,getPackageName()));
        
        //初始化全局SharedPreferences
        QDSharedPreferences.init(this);
        //activity管理监听
        QDActivityManager.getInstance().init(this);
        //LifecycleManager.getInstance().init(this);

      /*  ActivityManager.RunningAppProcessInfo processInfo = QDProcessUtil.getProcessInfo(getApplicationContext(), android.os.Process.myPid());
        if (TextUtils.isEmpty(processInfo.processName) || !getPackageName().equals(processInfo.processName)) {
            QDLogger.e(TAG, getPackageName() + "进程[" + android.os.Process.myPid() + "]已存在无需重复初始化");
            return;
        }*/

        //NotifycationHelper.getInstance().init(this);

        //QDSaxXml.parseXmlAssets(this, "config/test.xml", cn.demomaster.huan.quickdeveloplibrary.util.xml.Article.class, null);
        //QDSaxXml.parseXmlAssets(this, "config/test2.xml", cn.demomaster.huan.quickdeveloplibrary.util.xml.AppConfig.class, null);
        QDSaxXml.parseXmlAssets(this, "config/project.xml", cn.demomaster.huan.quickdeveloplibrary.util.xml.AppConfig.class, result -> QDLogger.println("配置文件初始化完成" + result));
        
        //处理崩溃日志
        initCrash();
        //DoraemonKit.install(this);
        /*if (isDebug(this)) {
           ServiceHelper.showWindow(this,DebugFloatingService.class);
        }*/
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(base);
    }

    public QuickDbHelper dbHelper;
    
    /**
     * 初始化本地数据库
     * @return
     */
    public QuickDbHelper getDbHelper() {
        if (dbHelper == null) {
            String dbpath = null;
            if (AppConfig.getInstance().getConfigMap().containsKey("dbpath")) {
                dbpath = (String) AppConfig.getInstance().getConfigMap().get("dbpath");
            }

            if (!TextUtils.isEmpty(dbpath)) {
                dbHelper = new QuickDbHelper(this, dbpath, null, 7, this);
                // dbHelper = new QuickDb(this, dbpath, null, 7, this);
            }
        }
        return dbHelper;
    }
    
    /**
     * 处理异常捕获
     */
    public void initCrash() {
        if (BuildConfig.DEBUG) {
            /*Class errorReportActivity = AppConfig.getInstance().getClassFromClassMap("errorReportActivity");
            QDLogger.println("errorReportActivity="+errorReportActivity);*/
            CrashHandler.getInstance().init(this, ErrorCrashActivity.class);
        }else {
            CrashHandler.getInstance().init(this, null);
            CrashHandler.getInstance().setCrashDealType(CrashHandler.CrashDealType.savelog);
        }
    }

    /**
     * 数据库更新触发
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
