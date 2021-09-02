package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
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
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.receiver.NetWorkChangReceiver;
import cn.demomaster.huan.quickdeveloplibrary.util.NetworkHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.QDDeviceHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.decorator.GridDividerItemDecoration;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.ToggleButton;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.OnClickActionListener;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDInputDialog;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.quickpermission_library.PermissionHelper;

import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_UNKNOWN;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;


/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "wifi", preViewClass = TextView.class, resType = ResType.Custome)
public class WifiFragment extends BaseFragment {

    View mView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toggle)
    ToggleButton toggle;
    @BindView(R.id.tv_state)
    TextView tv_state;
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
        networkHelper.registerListener(new NetWorkChangReceiver.OnNetStateChangedListener() {
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
        });
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

        initData();
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
        new QDInputDialog.Builder(mContext).setTitle("连接" + scanResultList.get(position).getScanResult().SSID)
                .setMessage("")
                .setHint("请输入密码")
                .setBackgroundRadius(30)
                .setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                .addAction("连接", new OnClickActionListener() {
                    @Override
                    public void onClick(Dialog dialog, View view, Object tag) {
                        //Toast.makeText(mContext, "input = " + value, Toast.LENGTH_SHORT).show();
                        //连接返回editview的value
                        WifiConfiguration configuration = WifiUtil.getInstance().createWifiInfo(scanResultList.get(position).getScanResult().SSID, tag.toString(), scanResultList.get(position).getPasswordType());
                        int netId = configuration.networkId;
                        if (netId == -1) {
                            netId = wifiManager.addNetwork(configuration);
                        }
                        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        boolean b = wifiManager.enableNetwork(netId, true);
                        if (b) {
                            Toast.makeText(mContext, "连接成功：" + tag, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "密码错误：" + tag, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                }).addAction("取消").setGravity_foot(Gravity.RIGHT).create().show();
    }


    String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION};
    WifiManager wifiManager;

    private void initData() {
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        PermissionHelper.requestPermission(mContext, permissions, new PermissionHelper.PermissionListener() {
            @Override
            public void onPassed() {
                scanResultList = WifiUtil.getInstance().getWifiList();
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
        String ssid = mWifiManager.getConnectionInfo().getSSID();
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
            networkHelper.onRelease();
        }
    }
}