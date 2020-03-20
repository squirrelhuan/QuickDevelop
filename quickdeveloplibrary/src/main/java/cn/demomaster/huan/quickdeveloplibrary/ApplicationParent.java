package cn.demomaster.huan.quickdeveloplibrary;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

//import com.umeng.commonsdk.UMConfigure;
//import com.umeng.socialize.PlatformConfig;

//import com.didichuxing.doraemonkit.DoraemonKit;

import com.tencent.bugly.Bugly;

import cn.demomaster.huan.quickdeveloplibrary.constant.AppConfig;
import cn.demomaster.huan.quickdeveloplibrary.db.CBHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDActivityManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.NotifycationHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.SharedPreferencesHelper;
import cn.demomaster.huan.quickdeveloplibrary.lifecycle.QDActivityLifecycleCallbacks;
import cn.demomaster.huan.quickdeveloplibrary.util.CrashHandler;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.util.StateObserver;
import cn.demomaster.huan.quickdeveloplibrary.util.xml.QDSaxHandler;
import cn.demomaster.huan.quickdeveloplibrary.util.xml.QDSaxXml;

import static cn.demomaster.huan.quickdeveloplibrary.util.xml.QDAppStateUtil.isApkInDebug;

public class ApplicationParent extends Application {

    //public static String TAG = "CGQ";
    private static ApplicationParent instance = null;
    public static ApplicationParent getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        QDLogger.setApplicationContext(this);
        //初始化全局SharedPreferences
        SharedPreferencesHelper.init(this);
        QDActivityManager.getInstance().init(this);
        NotifycationHelper.getInstance().init(this);

        AppConfig.getInstance().init(this, "config/project.conf");
        if (AppConfig.getInstance().getConfigMap().containsKey("dbpath")) {
            String dbpath = (String) AppConfig.getInstance().getConfigMap().get("dbpath");
            initDB(dbpath);
        }

        //处理崩溃日志
        initCrash();

        //QDSaxXml.parseXmlAssets(this, "config/test.xml", cn.demomaster.huan.quickdeveloplibrary.util.xml.Article.class, null);
        //QDSaxXml.parseXmlAssets(this, "config/test2.xml", cn.demomaster.huan.quickdeveloplibrary.util.xml.AppConfig.class, null);
        QDSaxXml.parseXmlAssets(this, "config/project.xml", cn.demomaster.huan.quickdeveloplibrary.util.xml.AppConfig.class, new QDSaxHandler.OnParseCompleteListener() {
            @Override
            public void onComplete(Object result) {
                QDLogger.d("配置文件初始化完成" + result);
            }
        });

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

    /**
     * 初始化友盟分享
     *
     * @param appkey
     */
    public void initUmengShare(String appkey) {
        /*UMConfigure.init(this, appkey
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0

        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
        PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
        PlatformConfig.setAlipay("2015111700822536");
        PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
        PlatformConfig.setPinterest("1439206");
        PlatformConfig.setKakao("e4f60e065048eb031e235c806b31c70f");
        PlatformConfig.setDing("dingoalmlnohc0wggfedpk");
        PlatformConfig.setVKontakte("5764965", "5My6SNliAaLxEm3Lyd9J");
        PlatformConfig.setDropbox("oz8v5apet3arcdy", "h7p2pjbzkkxt02a");*/
        //PlatformConfig.setYnote("9c82bf470cba7bd2f1819b0ee26f86c6ce670e9b");
    }

    public CBHelper dbHelper;
    public SQLiteDatabase db;

    /**
     * 初始化数据库
     * @param dbpath
     */
    private void initDB(String dbpath) {
        dbHelper = new CBHelper(this, dbpath, null, 1);
        //得到一个可读的SQLiteDatabase对象
        db = dbHelper.getReadableDatabase();
    }

    /**
     * 设置buggly appID
     * @return
     */
    public String getBugglyAppID(){
        return "7d6d33c554";
    }
    public ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new QDActivityLifecycleCallbacks();
    /**
     * 处理异常捕获
     */
    public void initCrash() {
        QDLogger.i("initCrash");
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        //CrashReport.initCrashReport(getApplicationContext(), getBugglyAppID(), false);
        Bugly.init(getApplicationContext(), getBugglyAppID(), false);
        if (BuildConfig.DEBUG) {
            Class errorReportActivity = AppConfig.getInstance().getClassFromClassMap("errorReportActivity");
            if (errorReportActivity == null) {
                QDLogger.e("未定义错误异常捕获页面");
                return;
            }
            Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this, errorReportActivity));
            StateObserver stateObserver = new StateObserver(PreferenceManager.getDefaultSharedPreferences(this));
        }
    }

}
