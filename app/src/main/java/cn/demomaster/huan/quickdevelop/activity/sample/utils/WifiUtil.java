package cn.demomaster.huan.quickdevelop.activity.sample.utils;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import cn.demomaster.huan.quickdevelop.activity.sample.WifiTestActivity2;
import cn.demomaster.huan.quickdevelop.activity.sample.model.QDScanResult;
import cn.demomaster.huan.quickdevelop.activity.sample.service.WifiTimerService2;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;

import static android.content.Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_UNKNOWN;
import static cn.demomaster.huan.quickdevelop.activity.sample.service.WifiTimerService2.WIFI_SETTING_KEY;

public class WifiUtil {

    private static final String TAG = WifiUtil.class
            .getSimpleName();

    public WifiManager wifiManager = null;
    private static WifiUtil mWifiUtil;

    public boolean isLinked = false;

    public int getWifiState() {
        int state = wifiManager.getWifiState();
        switch (state){
            case WIFI_STATE_DISABLING ://WIFI网卡正在关闭（0）
                break;
            case WIFI_STATE_DISABLED ://WIFI网卡不可用（1）
                break;
            case WIFI_STATE_ENABLING ://WIFI网正在打开（2） （WIFI启动需要一段时间）
                break;
            case WIFI_STATE_ENABLED ://WIFI网卡可用（3）
                break;
            case WIFI_STATE_UNKNOWN ://未知网卡状态
                break;
        }
        return state;
    }

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public boolean saveWifiInfo(String wifi, String pwd) {
        editor.putString(WifiTimerService2.WIFI_NAME_KEY, wifi);
        editor.putString(WifiTimerService2.WIFI_PWD_KEY, pwd);
        return editor.commit();
    }

    ConnectAsyncTask mConnectAsyncTask;
    WifiUtil.WifiCipherType type = WifiUtil.WifiCipherType.WIFICIPHER_NOPASS;

    public void connect(String ssid, String password) {
        if (mConnectAsyncTask != null) {
            mConnectAsyncTask.cancel(true);
            mConnectAsyncTask = null;
        }
        mConnectAsyncTask = new ConnectAsyncTask(ssid, password, type);
        mConnectAsyncTask.execute();
    }

    // 定义几种加密方式，一种是WEP，一种是WPA，还有没有密码的情况
    public static enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    // 构造函数
    private WifiUtil() {
    }

    public static WifiUtil getInstance() {
        if (mWifiUtil == null) {
            mWifiUtil = new WifiUtil();
        }
        return mWifiUtil;
    }

    private IntentFilter mWifiSearchIntentFilter;
    private IntentFilter mWifiConnectIntentFilter;
    private BroadcastReceiver mWifiSearchBroadcastReceiver;
    private BroadcastReceiver mWifiConnectBroadcastReceiver;
    private Context mContext;

    public void init(Context context) {
        mContext = context.getApplicationContext();
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        preferences = mContext.getSharedPreferences(WIFI_SETTING_KEY, MODE_ENABLE_WRITE_AHEAD_LOGGING);
        editor = preferences.edit();

        initSearchWifi();
        initConnectWifi();
    }

    /**
     * wifi 状态变化接收广播
     */
    private void initConnectWifi() {
        if (mWifiConnectBroadcastReceiver == null) {
            mWifiConnectBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    Log.e("wifidemo ", "action:" + action);
                    if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                        int wifState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                        Log.e("wifidemo ", "wifState:" + wifState);
                        if (wifState != WifiManager.WIFI_STATE_ENABLED) {
                            QdToast.show("没有wifi");
                        } else {
                            QdToast.show("wifi已开启");
                        }
                    } else if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
                        int linkWifiResult = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 123);
                        Log.e("wifidemo", ssid + "linkWifiResult:" + linkWifiResult);
                        if (linkWifiResult == WifiManager.ERROR_AUTHENTICATING) {
                            Log.e("wifidemo", ssid + "onReceive:密码错误");
                        }
                    } else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                        NetworkInfo.DetailedState state = ((NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO)).getDetailedState();
                        Log.e("wifidemo ", "state:" + state);
                        if (onWifiChangeListener != null) {
                            onWifiChangeListener.onWifiStateChanged(state);
                        }
                        setWifiState(state);
                    }
                }
            };
            mWifiConnectIntentFilter = new IntentFilter();
            mWifiConnectIntentFilter.addAction(WifiManager.ACTION_PICK_WIFI_NETWORK);
            mWifiConnectIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            mWifiConnectIntentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
            mWifiConnectIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

            mContext.registerReceiver(mWifiConnectBroadcastReceiver, mWifiConnectIntentFilter);
            Log.e("wifidemo", "连接监听");
        }
    }

    private void initSearchWifi() {
        if (mWifiSearchBroadcastReceiver == null) {
            //wifi 搜索结果接收广播
            mWifiSearchBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.d(TAG, "====== mWifiSearchBroadcastReceiver onReceive ======");
                    String action = intent.getAction();
                    if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {// 扫描结果改表
                        if (onWifiChangeListener != null) {
                            onWifiChangeListener.onScanResult(WifiUtil.getInstance().getScanResults());
                        }
                    }
                }

            };
            mWifiSearchIntentFilter = new IntentFilter();
            mWifiSearchIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            mWifiSearchIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            mWifiSearchIntentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
            mContext.registerReceiver(mWifiSearchBroadcastReceiver, mWifiSearchIntentFilter);
            Log.e("wifidemo", "扫描监听");
        }
    }

    public void unRegister() {
        mContext.unregisterReceiver(mWifiSearchBroadcastReceiver);
        mContext.unregisterReceiver(mWifiConnectBroadcastReceiver);
    }

    OnWifiChangeListener onWifiChangeListener;

    public void setOnWifiChangeListener(OnWifiChangeListener onWifiChangeListener) {
        this.onWifiChangeListener = onWifiChangeListener;
    }

    public static interface OnWifiChangeListener {
        void onWifiStateChanged(NetworkInfo.DetailedState state);

        void onScanResult(List<ScanResult> scanResults);
    }

    public void setWifiState(final NetworkInfo.DetailedState state) {
        QdToast.show(state.toString());
        Log.e(TAG, "setWifiState " + state);
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

    private String password = "";//"hyc888888";
    String ssid;

    public String[] getWifiInfo() {
        ssid = preferences.getString(WifiTimerService2.WIFI_NAME_KEY, "cgq");
        password = preferences.getString(WifiTimerService2.WIFI_PWD_KEY, "1234567890");
        return new String[]{ssid, password};
    }

    public List<QDScanResult> getWifiList() {
        List<ScanResult> scanWifiList = wifiManager.getScanResults();
        List<QDScanResult> wifiList = new ArrayList<>();
        if (scanWifiList != null && scanWifiList.size() > 0) {
            for (int i = 0; i < scanWifiList.size(); i++) {
                ScanResult scanResult = scanWifiList.get(i);
                if (!scanResult.SSID.isEmpty()) {
                    QDScanResult qdScanResult = new QDScanResult();
                    qdScanResult.setScanResult(scanResult);
                    wifiList.add(qdScanResult);
                }
            }
        }
        return wifiList;
    }

    // 查看以前是否也配置过这个网络
    public WifiConfiguration isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = wifiManager
                .getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    /**
     * 创建wifi配置文件
     *
     * @param SSID
     * @param Password
     * @param Type
     * @return
     */
    public WifiConfiguration createWifiInfo(String SSID, String Password, WifiCipherType Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        // config.SSID = SSID;
        // 分为三种情况：0没有密码1用wep加密2用wpa加密
        if (Type == WifiCipherType.WIFICIPHER_NOPASS) {
            // config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            // config.wepTxKeyIndex = 0;
        } else if (Type == WifiCipherType.WIFICIPHER_WEP) {// wep
            if (!TextUtils.isEmpty(Password)) {
                if (isHexWepKey(Password)) {
                    config.wepKeys[0] = Password;
                } else {
                    config.wepKeys[0] = "\"" + Password + "\"";
                }
            }
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else if (Type == WifiCipherType.WIFICIPHER_WPA) {// wpa
            config.preSharedKey = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    /**
     * wifi 是否已经开启
     * @return
     */
    public boolean isWifiEnabled(){
        if(wifiManager!=null){
            return wifiManager.isWifiEnabled();
        }
        return false;
    }
    // 打开wifi功能
    public boolean openWifi() {
        boolean bRet = true;
        if (!isWifiEnabled()) {
            bRet = wifiManager.setWifiEnabled(true);
        }
        return bRet;
    }

    // 关闭WIFI
    public void closeWifi() {
        if (isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    private static boolean isHexWepKey(String wepKey) {
        final int len = wepKey.length();

        // WEP-40, WEP-104, and some vendors using 256-bit WEP (WEP-232?)
        if (len != 10 && len != 26 && len != 58) {
            return false;
        }

        return isHex(wepKey);
    }

    private static boolean isHex(String key) {
        for (int i = key.length() - 1; i >= 0; i--) {
            final char c = key.charAt(i);
            if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a'
                    && c <= 'f')) {
                return false;
            }
        }

        return true;
    }

    /**
     * 根据给定的ssid信号量和总级别，判断当前信号量，在什么级别
     *
     * @param rssi
     * @param numLevels
     * @return
     */
    public int getSignalNumsLevel(int rssi, int numLevels) {
        if (wifiManager == null) {
            return -1;
        }
        return WifiManager.calculateSignalLevel(rssi, numLevels);
    }

    /**
     * 获取ssid的加密方式
     */
    public WifiCipherType getCipherType(String ssid) {
        if (wifiManager == null) {
            return null;
        }
        List<ScanResult> list = wifiManager.getScanResults();

        for (ScanResult scResult : list) {
            if (!TextUtils.isEmpty(scResult.SSID) && scResult.SSID.equals(ssid)) {
                String capabilities = scResult.capabilities;
                if (!TextUtils.isEmpty(capabilities)) {
                    if (capabilities.contains("WPA")
                            || capabilities.contains("wpa")) {
                        Log.e("wifidemo", "wpa");
                        return WifiCipherType.WIFICIPHER_WPA;
                    } else if (capabilities.contains("WEP")
                            || capabilities.contains("wep")) {
                        Log.e("wifidemo", "wep");
                        return WifiCipherType.WIFICIPHER_WEP;
                    } else {
                        Log.e("wifidemo", "no");
                        return WifiCipherType.WIFICIPHER_NOPASS;
                    }
                }
            }
        }
        return WifiCipherType.WIFICIPHER_INVALID;
    }

    /**
     * 获取 bssid 接入点的地址
     *
     * @return
     */
    public String getBSSID() {
        if (wifiManager == null) {
            return null;
        }
        WifiInfo info = wifiManager.getConnectionInfo();
        Log.e("wifidemo", "getBSSID" + info.getBSSID());
        if (info == null) {
            return null;
        }
        return info.getBSSID();
    }

    /**
     * 获取网关地址
     *
     * @return
     */
    public String getGateway() {
        if (wifiManager == null) {
            return "";
        }
        InetAddress inetAddress = NetWorkUtils.intToInetAddress(wifiManager.getDhcpInfo().gateway);
        if (inetAddress == null) {
            return "";
        }
        return inetAddress.getHostAddress();
    }

    /**
     * 获取ip地址
     * @return
     */
    public String getIpAddress() {
        if (wifiManager == null) {
            return "";
        }
        InetAddress inetAddress = NetWorkUtils.intToInetAddress(wifiManager.getConnectionInfo().getIpAddress());
        if (inetAddress == null) {
            return "";
        }
        return inetAddress.getHostAddress();
    }

    /**
     * 获取mac地址
     *
     * @return
     */
    public String getMacAddress() {
        if (wifiManager == null) {
            return "";
        }
        return wifiManager.getConnectionInfo().getMacAddress();
    }

    /**
     * 获取wifi名称
     *
     * @return
     */
    public String getSSID() {
        if (wifiManager == null) {
            return null;
        }
        WifiInfo info = wifiManager.getConnectionInfo();
        String ssid = info.getSSID();
        return ssid;
    }

    /**
     * 扫描WIFI AP
     */
    public boolean startStan() {
        if (wifiManager == null) {
            return false;
        }

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return wifiManager.startScan();
    }


    /**
     * 获取所有WIFI AP
     */
    public List<ScanResult> getScanResults() {
        List<ScanResult> srList = wifiManager.getScanResults();
        if (srList == null) {
            srList = new ArrayList<ScanResult>();
        }
        return srList;
    }

    SearchAsyncTask searchAsyncTask;

    public void searchWifi() {
        if (searchAsyncTask != null) {
            searchAsyncTask.cancel(true);
            searchAsyncTask = null;
        }
        searchAsyncTask = new SearchAsyncTask();
        searchAsyncTask.execute();
    }

    private class SearchAsyncTask extends AsyncTask<Void, Void, List<ScanResult>> {
        private List<ScanResult> mScanResult = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<ScanResult> doInBackground(Void... params) {
            if (WifiUtil.getInstance().startStan()) {
                mScanResult = WifiUtil.getInstance().getScanResults();
            }
            return mScanResult;
        }

        @Override
        protected void onPostExecute(final List<ScanResult> result) {
            super.onPostExecute(result);
            if (onWifiChangeListener != null) {
                onWifiChangeListener.onScanResult(mScanResult);
            }
        }
    }

    /**
     * 连接指定的wifi
     */
    public class ConnectAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private String ssid;
        private String password;
        private WifiUtil.WifiCipherType type;
        WifiConfiguration tempConfig;

        public ConnectAsyncTask(String ssid, String password, WifiUtil.WifiCipherType type) {
            this.ssid = ssid;
            this.password = password;
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            // 打开wifi
            WifiUtil.getInstance().openWifi();
            // 开启wifi功能需要一段时间(我在手机上测试一般需要1-3秒左右)，所以要等到wifi
            // 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
            while (WifiUtil.getInstance().wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
                try {
                    // 为了避免程序一直while循环，让它睡个100毫秒检测……
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    Log.e("wifidemo", ie.toString());
                }
            }

            tempConfig = WifiUtil.getInstance().isExsits(ssid);
            //禁掉所有wifi
            for (WifiConfiguration c : WifiUtil.getInstance().wifiManager.getConfiguredNetworks()) {
                WifiUtil.getInstance().wifiManager.disableNetwork(c.networkId);
            }

            if (tempConfig != null) {
                Log.d("wifidemo", ssid + "配置过！");
                boolean result = WifiUtil.getInstance().wifiManager.enableNetwork(tempConfig.networkId, true);
                if (!isLinked && type != WifiUtil.WifiCipherType.WIFICIPHER_NOPASS) {
                    try {
                        Thread.sleep(5000);//超过5s提示失败
                        if (!isLinked) {
                            Log.d("wifidemo", ssid + "连接失败！");
                            WifiUtil.getInstance().wifiManager.disableNetwork(tempConfig.networkId);
                            reConnect(tempConfig);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Log.d("wifidemo", "result=" + result);
                return result;
            } else {
                Log.d("wifidemo", ssid + "没有配置过！");
                if (type != WifiUtil.WifiCipherType.WIFICIPHER_NOPASS) {
                    requestPassword();
                } else {
                    WifiConfiguration wifiConfig = WifiUtil.getInstance().createWifiInfo(ssid, password, type);
                    if (wifiConfig == null) {
                        Log.d("wifidemo", "wifiConfig is null!");
                        return false;
                    }
                    Log.d("wifidemo", wifiConfig.SSID);
                    int netID = WifiUtil.getInstance().wifiManager.addNetwork(wifiConfig);
                    boolean enabled = WifiUtil.getInstance().wifiManager.enableNetwork(netID, true);
                    Log.d("wifidemo", "enableNetwork status enable=" + enabled);
//                    Log.d("wifidemo", "enableNetwork connected=" + mWifiAutoConnectManager.wifiManager.reconnect());
//                    return mWifiAutoConnectManager.wifiManager.reconnect();
                    return enabled;
                }
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mConnectAsyncTask = null;
        }
    }

    private void reConnect(WifiConfiguration tempConfig) {
        Log.d("wifidemo", "----------------" + ssid + "重新连接中.....");
        //如果连接失败  删除原来的networkId 重新假如
        WifiUtil.getInstance().wifiManager.removeNetwork(tempConfig.networkId);
        WifiConfiguration wifiConfig = WifiUtil.getInstance().createWifiInfo(ssid, password, type);
        int netID = WifiUtil.getInstance().wifiManager.addNetwork(wifiConfig);
        boolean enabled = WifiUtil.getInstance().wifiManager.enableNetwork(netID, true);
        Log.d("wifidemo", "enableNetwork status enable=" + enabled);

//                                    Toast.makeText(getApplicationContext(), "连接失败!请在系统里删除wifi连接，重新连接。", Toast.LENGTH_SHORT).show();
//                                    new AlertDialog.Builder(WifiTestActivity.this)
//                                            .setTitle("连接失败！")
//                                            .setMessage("请在系统里删除wifi连接，重新连接。")
//                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    dialog.dismiss();
//                                                }
//                                            })
//                                            .setPositiveButton("好的", new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    Intent intent = new Intent();
//                                                    intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
//                                                    startActivity(intent);
//                                                }
//                                            }).show();
//

    }

    private void requestPassword() {
        //password = inputServer.getText().toString();
        WifiUtil.getInstance().saveWifiInfo(ssid, password);
        new Thread(new Runnable() {
            @Override
            public void run() {
                WifiConfiguration wifiConfig = WifiUtil.getInstance().createWifiInfo(ssid, password,
                        type);
                if (wifiConfig == null) {
                    Log.d("wifidemo", "wifiConfig is null!");
                    return;
                }
                Log.d("wifidemo", wifiConfig.SSID);

                int netID = WifiUtil.getInstance().wifiManager.addNetwork(wifiConfig);
                boolean enabled = WifiUtil.getInstance().wifiManager.enableNetwork(netID, true);
                Log.d("wifidemo", "enableNetwork status enable=" + enabled);
//                                                    Log.d("wifidemo", "enableNetwork connected=" + mWifiAutoConnectManager.wifiManager.reconnect());
//                                                    mWifiAutoConnectManager.wifiManager.reconnect();
            }
        }).start();
    }

}
