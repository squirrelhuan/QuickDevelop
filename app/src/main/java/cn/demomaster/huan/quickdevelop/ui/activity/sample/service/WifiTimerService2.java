package cn.demomaster.huan.quickdevelop.ui.activity.sample.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.demomaster.huan.quickdevelop.ui.activity.sample.utils.NetWorkUtils;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.utils.WifiUtil;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class WifiTimerService2 extends Service {

    public static final String WIFI_SETTING_KEY = "WIFI_SETTING";
    public static final String WIFI_NAME_KEY = "WIFI_NAME";
    public static final String WIFI_PWD_KEY = "WIFI_PWD";

    private static String TAG = WifiTimerService2.class.getName();
    private static final long LOOP_TIME = 10; //循环时间
    private static ScheduledExecutorService mExecutorService;

    List<ScanResult> mScanResultList = new ArrayList<>();

    //WifiTestActivity2.ConnectAsyncTask mConnectAsyncTask = null;
    private String currentWifi = "hyc";
    private String password = "";//"hyc888888";
    boolean isLinked = false;
    String ssid;

    private IntentFilter mWifiSearchIntentFilter;
    private IntentFilter mWifiConnectIntentFilter;
    private int count = 0;
    WifiUtil.WifiCipherType type = WifiUtil.WifiCipherType.WIFICIPHER_NOPASS;
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        //初始化wifi工具
        WifiUtil.getInstance().init(getApplicationContext());
        WifiUtil.getInstance().getWifiInfo();
        WifiUtil.getInstance().openWifi();

        mExecutorService = Executors.newScheduledThreadPool(2);
        mExecutorService.scheduleAtFixedRate(mRunnable, LOOP_TIME, LOOP_TIME, TimeUnit.SECONDS);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();

        mExecutorService.shutdown();
        mExecutorService = null;
        mRunnable = null;
    }

    private Runnable mRunnable = () -> {
        count++;
        Log.d(TAG, "============ count:" + count);
        checkWifi();
    };

    private ScanResult findWifi() {
        Log.d(TAG, "====== mScanResultList=" + mScanResultList);
        for (ScanResult sr : mScanResultList) {
            Log.d(TAG, "====== currentWifi.equals(sr.SSID)=" + currentWifi.equals(sr.SSID));
            if (currentWifi.equals(sr.SSID)) {
                return sr;
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void checkWifi() {
        try {
            WifiUtil.getInstance().openWifi();
            boolean isNetwork = NetWorkUtils.isNetWork();
            Log.d(TAG, "====== isNetwork=" + isNetwork);
            if (!isNetwork) {
                WifiUtil.getInstance().getWifiInfo();
                //重连wifi
                Log.d(TAG, "=== checkWifi:无网络....");
                Log.d(TAG, "=currentWifi=" + currentWifi);
                Log.d(TAG, "=checkWifi=password=" + password);

                ScanResult srResult = findWifi();
                if (srResult == null) {
                    Log.d(TAG, "== checkWifi,ScanResult=null ==");
                    return;
                }

                ssid = srResult.SSID;
                Log.d(TAG, "checkWifi:ssid=" + ssid);
                type = WifiUtil.getInstance().getCipherType(ssid);
                Log.d(TAG, "findWifi:ssid=" + ssid);
                /*if (mConnectAsyncTask != null) {
                    mConnectAsyncTask.cancel(true);
                    mConnectAsyncTask = null;
                }*/
                //mConnectAsyncTask = new WifiTestActivity2.ConnectAsyncTask(ssid, password, type);
                //mConnectAsyncTask.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示wifi状态
     *
     * @param state
     */
    @SuppressLint("NewApi")
    public void setWifiState(final NetworkInfo.DetailedState state) {
        if (state == NetworkInfo.DetailedState.AUTHENTICATING) {

        } else if (state == NetworkInfo.DetailedState.BLOCKED) {

        } else if (state == NetworkInfo.DetailedState.CONNECTED) {
            isLinked = true;
        } else if (state == NetworkInfo.DetailedState.CONNECTING) {
            isLinked = false;
        } else if (state == NetworkInfo.DetailedState.DISCONNECTED) {
            isLinked = false;
        } else if (state == NetworkInfo.DetailedState.DISCONNECTING) {
            isLinked = false;
        } else if (state == NetworkInfo.DetailedState.FAILED) {
            isLinked = false;
        } else if (state == NetworkInfo.DetailedState.IDLE) {

        } else if (state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {

        } else if (state == NetworkInfo.DetailedState.SCANNING) {

        } else if (state == NetworkInfo.DetailedState.SUSPENDED) {

        }
    }

}
