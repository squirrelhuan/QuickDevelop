package cn.demomaster.huan.quickdevelop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.didichuxing.doraemonkit.DoraemonKit;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;
import cn.demomaster.huan.quickdeveloplibrary.QDApplication;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDSharedPreferences;
import cn.demomaster.huan.quickdeveloplibrary.helper.SoundHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.cache.QuickCache;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdlogger_library.config.ConfigBuilder;
import cn.demomaster.quickdatabaselibrary.QuickDbHelper;

import static cn.demomaster.qdlogger_library.QDLogger.TAG;

public class Application extends QDApplication {
    QuickDbHelper quickDb;

    @Override
    public void onCreate() {
        super.onCreate();
        ConfigBuilder configBuilder = new ConfigBuilder(this);
        configBuilder.setSaveExternalStorageBeforeAndroidQ(false);
        configBuilder.setSaveInternalSoragePath("/log/");
        configBuilder.setSaveExternalStoragePath(new File(Environment.getExternalStorageDirectory(),"qdlogger/log"));
        QDLogger.init(this, configBuilder.build());
        quickDb = new QuickDbHelper(this, "quickdev.db", null, 10);

        SoundHelper.init(this, true, R.raw.class);//自动加载raw下的音频文件
        //初始化友盟分享
        //initUmengShare("5c79138f61f564e0380012fa");
       /* UMConfigure.init(this,"5c79138f61f564e0380012fa"
                ,"umeng", UMConfigure.DEVICE_TYPE_PHONE,"");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0

        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
        PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
        PlatformConfig.setAlipay("2015111700822536");
        PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
        PlatformConfig.setPinterest("1439206");
        PlatformConfig.setKakao("e4f60e065048eb031e235c806b31c70f");
        PlatformConfig.setDing("dingoalmlnohc0wggfedpk");
        PlatformConfig.setVKontakte("5764965","5My6SNliAaLxEm3Lyd9J");
        PlatformConfig.setDropbox("oz8v5apet3arcdy","h7p2pjbzkkxt02a");*/

        DoraemonKit.install(this);
        //初始化缓存目录
        //QuickCache.init(this, Environment.getExternalStorageDirectory().getAbsolutePath()+"/qdlogger/cache/");
        QuickCache.getInstance().init(this, QDFileUtil.getDiskCacheDir(getInstance()) +"/cache/");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.i(TAG, "attachBaseContext");
        MultiDex.install(base);
    }

    @Override
    public QuickDbHelper getDbHelper() {
        if (dbHelper == null) {
            dbHelper = new QuickDbHelper(this, "quick_db.db", "quick_db.db", null, 10, this);
            //quick_db1.db 在data/data/下生成对应的db文件
            //quick_db2.db 在assets下的db文件
            /*try {
                dbHelper.createTable(KsnetPayModel.class);
                dbHelper.updateTable(KsnetPayModel.class);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
        return dbHelper;
    }

    public final SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    private SerialPort mSerialPort = null;

    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            /* Read serial port parameters */
            //SharedPreferences sp = getSharedPreferences("android_serialport_api.sample_preferences", MODE_PRIVATE);//"cn_demomaster_huan_quickdevelop_fragment_helper_serialport_sample_SerialPortPreferences"
            SharedPreferences sp = QDSharedPreferences.getInstance().getSharedPreferences();
            String path = sp.getString("DEVICE", "");
            int baudrate = Integer.decode(sp.getString("BAUDRATE", "-1"));

            /* Check parameters */
            if ((path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }

            Log.d("getSerialPort", path + ":" + baudrate);
            /* Open the serial port */
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
        }
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }
}
