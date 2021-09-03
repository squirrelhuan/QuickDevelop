package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.adapter.WifiAdapter;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.model.QDScanResult;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.utils.WifiUtil;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdevelop.util.PasswordGenarator;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.network.NetworkHelper;
import cn.demomaster.huan.quickdeveloplibrary.network.OnNetStateChangedListener;
import cn.demomaster.huan.quickdeveloplibrary.util.QDDeviceHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.decorator.GridDividerItemDecoration;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.ToggleButton;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.OnClickActionListener;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.layout.LoadLayout;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.quickpermission_library.PermissionHelper;

import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_UNKNOWN;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
import static cn.demomaster.huan.quickdevelop.ui.activity.sample.utils.WifiUtil.WIFI_CONNECTED;
import static cn.demomaster.huan.quickdevelop.ui.activity.sample.utils.WifiUtil.WIFI_CONNECTING;
import static cn.demomaster.huan.quickdevelop.ui.activity.sample.utils.WifiUtil.WIFI_CONNECT_FAILED;
import static cn.demomaster.huan.quickdevelop.ui.activity.sample.utils.WifiUtil.isWifiContected;
import static cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity.TAG;


/**
 * wifi解密 Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "wifi解密", preViewClass = TextView.class, resType = ResType.Custome)
public class WifiFragment2 extends BaseFragment {

    View mView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toggle)
    ToggleButton toggle;
    @BindView(R.id.tv_state)
    TextView tv_state;
    @BindView(R.id.tv_pass)
    TextView tv_pass;
    @BindView(R.id.loadlayout)
    LoadLayout loadlayout;
    private WifiAdapter wifiAdapter;
    List<QDScanResult> scanResultList = new ArrayList<>();

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_layout_wifi, null);
        }
        ButterKnife.bind(this, mView);
        return mView;
    }

    NetworkHelper networkHelper;
    public void initView(View rootView) {
        QDDeviceHelper.setFlagDef(AudioManager.FLAG_PLAY_SOUND);
        WifiUtil.getInstance().init(this.getContext());
        WifiUtil.getInstance().setOnWifiChangeListener(new WifiUtil.OnWifiChangeListener() {
            @Override
            public void onWifiStateChanged(NetworkInfo.DetailedState state) {
                setState(state);
            }

            @Override
            public void onScanResult(List<ScanResult> scanResults) {
                getWifiListSort(scanResults, WifiUtil.getInstance().getSSID());
            }
        });
        networkHelper = new NetworkHelper(getContext());
        onNetStateChangedListener = new OnNetStateChangedListener() {
            @Override
            public void onConnected(Context context, Intent intent) {
                QDLogger.e("wifi onConnected");
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                switch (networkInfo.getType()) {
                    case TYPE_MOBILE:
                        QDLogger.i("CGQ", "正在使用2G/3G/4G网络");
                        break;
                    case TYPE_WIFI:
                        QDLogger.i("CGQ", "正在使用wifi上网");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onDisConnected(Context context, Intent intent) {
                QDLogger.e("wifi disconnect");
            }
        };
        networkHelper.registerListener(onNetStateChangedListener);
        //registerPermission();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        //设置布局管理器
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        wifiAdapter = new WifiAdapter(mContext);
        wifiAdapter.setOnItemClickListener((view, position) -> {
            WifiUtil.WifiCipherType wifiCipherType = scanResultList.get(position).getPasswordType();
            if (wifiCipherType == WifiUtil.WifiCipherType.WIFICIPHER_NOPASS) {
            } else if (wifiCipherType == WifiUtil.WifiCipherType.WIFICIPHER_WEP) {
                showInputDialog(position);
            } else if (wifiCipherType == WifiUtil.WifiCipherType.WIFICIPHER_WPA) {
                showInputDialog(position);
            }
        });

        wifiAdapter.updateList(scanResultList);
        //设置Adapter
        recyclerView.setAdapter(wifiAdapter);
        //设置分隔线
        //recyclerView.addItemDecoration( new DividerGridItemDecoration(this ));
        //设置行级分割线使用的divider
        //recyclerView.addItemDecoration(new android.support.v7.widget.DividerItemDecoration(getContext(), android.support.v7.widget.DividerItemDecoration.VERTICAL));

        int spanCount = 1;
        //使用网格布局展示NanumSquareRoundr.ttf
        //recyclerView.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        recyclerView.setLayoutManager(layoutManager);
        //设置分隔线
        recyclerView.addItemDecoration(new GridDividerItemDecoration(mContext, spanCount));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        toggle.setChecked(WifiUtil.getInstance().isWifiEnabled());
        toggle.setOnToggleChanged(new ToggleButton.OnToggleChangeListener() {
            @Override
            public void onToggle(View view, boolean on) {
                if (on) {
                    WifiUtil.getInstance().openWifi();
                } else {
                    WifiUtil.getInstance().closeWifi();
                }
            }
        });
        loadlayout.setOnLoadListener(new LoadLayout.OnLoadListener() {
            @Override
            public void loadData() {
                initData();
            }

            @Override
            public void loadSuccess() {

            }

            @Override
            public void loadFail() {

            }
        });
        loadlayout.loadData();

        initWifi();
    }

    OnNetStateChangedListener onNetStateChangedListener;
    private IntentFilter mWifiSearchIntentFilter;
    private BroadcastReceiver mWifiSearchBroadcastReceiver;

    private void initWifi() {
        mWifiSearchBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(WifiManager.RSSI_CHANGED_ACTION)) {
                    //有可能是正在获取，或者已经获取了
                    if (isWifiContected(mContext) == WIFI_CONNECTED) {
                        QDLogger.e("连接success 密码：" + passWordStr);
                    } else if (isWifiContected(mContext) == WIFI_CONNECT_FAILED) {
                        QDLogger.e("连接失败 密码错误：" + passWordStr);
                        i--;
                        tryConnectWifi(mSsid,mPasswordType);
                    } else if (isWifiContected(mContext) == WIFI_CONNECTING) {
                        Log.d(TAG, "====== 连接ing onReceive ======");
                    } else {
                        Log.d(TAG, " intent is " + WifiManager.RSSI_CHANGED_ACTION);
                    }
                }
            }

        };
        mWifiSearchIntentFilter = new IntentFilter();
        mWifiSearchIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mWifiSearchIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mWifiSearchIntentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        mContext.registerReceiver(mWifiSearchBroadcastReceiver, mWifiSearchIntentFilter);
        Log.e("wifidemo", "监听");
    }

    /**
     * 状态变更
     *
     * @param state
     */
    private void setState(NetworkInfo.DetailedState state) {
        int wifiState = wifiManager.getWifiState();
        switch (wifiState) {
            case WIFI_STATE_DISABLING://WIFI网卡正在关闭  0
                tv_state.setText("WIFI网卡正在关闭");
                break;
            case WIFI_STATE_DISABLED:// WIFI网卡不可用  1
                tv_state.setText("WIFI网卡不可用");
                break;
            case WIFI_STATE_ENABLING://WIFI网卡正在打开  2
                tv_state.setText("WIFI网卡正在打开");
                break;
            case WIFI_STATE_ENABLED://WIFI网卡可用  3
                tv_state.setText("WIFI网卡可用");
                break;
            case WIFI_STATE_UNKNOWN://WIFI网卡状态不可知 4
                tv_state.setText("WIFI网卡状态不可知");
                break;
            default:
                tv_state.setText("WIFI default");
                break;
        }

        String ssid = WifiUtil.getInstance().getSSID();
        wifiAdapter.setCurrentWifiName(ssid);
        toggle.setChecked(WifiUtil.getInstance().isWifiEnabled());
    }

    /**
     * 密码输入框
     *
     * @param position
     */
    private void showInputDialog(int position) {
        new QDDialog.Builder(mContext).setTitle("标题")
                .setMessage("尝试连接" + scanResultList.get(position).getScanResult().SSID + "？")
                .addAction("连接", new OnClickActionListener() {
                    @Override
                    public void onClick(Dialog dialog, View view, Object tag) {
                        tryConnectWifi(scanResultList.get(position).getScanResult().SSID, scanResultList.get(position).getPasswordType());
                        dialog.dismiss();
                    }
                }).addAction("取消").create().show();
    }

    int i = 100;
    String mSsid;
    WifiUtil.WifiCipherType mPasswordType;
    String passWordStr = "";
    PasswordGenarator passwordGenarator;
    private void tryConnectWifi(String ssid, WifiUtil.WifiCipherType passwordType) {
        if(passwordGenarator==null){
            passwordGenarator = new PasswordGenarator();
        }

        mSsid = ssid;
        mPasswordType = passwordType;
        passWordStr = "qia&xin" + i;
        connect(ssid,passWordStr,passwordType);
    }

    private void connect(String ssid, String password, WifiUtil.WifiCipherType passwordType) {
        QDLogger.e("tryConnectWifi：" + password);
        boolean b = connectWifi(ssid, passWordStr, passwordType);
        QDLogger.e("b：" +b);
        if (b) {
            tv_pass.setText(passWordStr + "," + ssid);
            QDLogger.e(ssid+"密码su：" + passWordStr);
            Toast.makeText(mContext, "连接成功：" + passWordStr, Toast.LENGTH_SHORT).show();
        } else {
            QDLogger.e(ssid+"密码错误：" + passWordStr);
            passWordStr = passwordGenarator.genaratPass();
            connect(ssid,passWordStr,passwordType);
            //Toast.makeText(mContext, "密码错误：" + passWordStr, Toast.LENGTH_SHORT).show();
        }
    }
    private boolean connectWifi(String ssid, String password, WifiUtil.WifiCipherType passwordType) {
        WifiConfiguration configuration = WifiUtil.getInstance().createWifiInfo(ssid, password, passwordType);//tag.toString()
        int netId = configuration.networkId;
        if (netId == -1) {
            netId = wifiManager.addNetwork(configuration);
        }
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.enableNetwork(netId, true);
    }

    String[] permissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE};
    WifiManager wifiManager;

    private void initData() {
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        PermissionHelper.requestPermission(mContext, permissions, new PermissionHelper.PermissionListener() {
            @Override
            public void onPassed() {
                scanResultList = WifiUtil.getInstance().getWifiList();
                loadlayout.loadSuccess();
                if (scanResultList != null) {
                    wifiAdapter.updateList(scanResultList);
                    for (int i = 0; i < scanResultList.size(); i++) {
                        QDLogger.i(scanResultList.get(i).getScanResult().toString());
                    }
                } else {
                    QDLogger.e("wifi kong");
                }
            }

            @Override
            public void onRefused() {
                QDLogger.i("未通过");
            }
        });
    }

    public List<ScanResult> getWifiListSort(List<ScanResult> scanWifiList, String ssid) {
        List<ScanResult> wifiList = new ArrayList<>();
        if (scanWifiList != null && scanWifiList.size() > 0) {
            HashMap<String, Integer> signalStrength = new HashMap<>();
            for (int i = 0; i < scanWifiList.size(); i++) {
                ScanResult scanResult = scanWifiList.get(i);
                if (!scanResult.SSID.isEmpty()) {
                    String key = scanResult.SSID + " " + scanResult.capabilities;
                    if (!signalStrength.containsKey(key)) {
                        signalStrength.put(key, i);
                        wifiList.add(scanResult);
                    }
                }
            }
        }
        return wifiList;
    }

    static WifiManager mWifiManager;

    public interface OnScanListener {
        void onScanResultAvailable();

        void onNetWorkStateChanged(NetworkInfo.DetailedState state, int mSSID);

        void onWiFiStateChanged(int wifiState);

        void onWifiPasswordFault();
    }

    OnScanListener listener;
    List<ScanResult> mWifiList = new ArrayList<>();

    public void refreshLocalWifiListData() {
//逻辑说明：
        /*1.从扫描结果中将已经连接的wifi添加到wifi列表中
        2.从所有WiFilist中将已经添加过的已经连接的WiFi移除
        3.将剩余的WiFi添加到WiFilist中
                实现了已经连接的WiFi处于wifi列表的第一位*/
        //得到扫描结果
        mWifiList.clear();
        List<ScanResult> tmpList = mWifiManager.getScanResults();
        for (ScanResult tmp : tmpList) {
            if (isGivenWifiConnect(tmp.SSID)) {
                mWifiList.add(tmp);
            }
        }
        //从wifi列表中删除已经连接的wifi
        for (ScanResult tmp : mWifiList) {
            tmpList.remove(tmp);
        }
        mWifiList.addAll(tmpList);
    }

    public void connect(WifiConfiguration config) {
        int wcgID = mWifiManager.addNetwork(config);
        mWifiManager.enableNetwork(wcgID, true);
    }

    //判断当前是否已经连接
    public boolean isGivenWifiConnect(String SSID) {
        return isWifiConnected() && getCurentWifiSSID().equals(SSID);
    }

    //得到当前连接的WiFi  SSID
    public String getCurentWifiSSID() {
        String ssid = "";
        ssid = mWifiManager.getConnectionInfo().getSSID();
        if (ssid.charAt(0) == '"'
                && ssid.endsWith("\"")) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        return ssid;
    }

    ConnectivityManager connectivityManager;

    /**
     * 是否处于wifi连接的状态
     */
    public boolean isWifiConnected() {
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return (wifiNetworkInfo.isConnected()||wifiNetworkInfo.isAvailable());
    }

    //断开指定ID的网络
    public void disConnectionWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(networkHelper!=null){
            networkHelper.unRegisterListener(onNetStateChangedListener);
        }
    }
}