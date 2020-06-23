package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.activity.sample.model.QDScanResult;
import cn.demomaster.huan.quickdevelop.adapter.WifiAdapter;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBar;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;
import cn.demomaster.huan.quickdeveloplibrary.receiver.NetWorkChangReceiver;
import cn.demomaster.huan.quickdeveloplibrary.util.QDDeviceHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.view.decorator.GridDividerItemDecoration;
import cn.demomaster.huan.quickdeveloplibrary.view.tabmenu.TabMenuAdapter;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDInputDialog;

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
public class WifiFragment extends QDFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    //Components


    View mView;


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private WifiAdapter wifiAdapter;

    List<QDScanResult> scanResultList = new ArrayList<>();

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_wifi, null);
        }
        ButterKnife.bind(this, mView);
        return (ViewGroup) mView;
    }

    @Override
    public void initView(View rootView, ActionBar actionBarLayoutOld) {
        QDDeviceHelper.setFlagDef(AudioManager.FLAG_PLAY_SOUND);

        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(getActivity().WIFI_SERVICE);
        int wifiState = wifiManager.getWifiState();
        switch (wifiState) {
            case WIFI_STATE_DISABLING://WIFI网卡正在关闭  0
                QDLogger.e("WIFI网卡正在关闭");
                break;
            case WIFI_STATE_DISABLED:// WIFI网卡不可用  1
                QDLogger.e("WIFI网卡不可用");
                break;
            case WIFI_STATE_ENABLING://WIFI网卡正在打开  2
                QDLogger.e("WIFI网卡正在打开");
                break;
            case WIFI_STATE_ENABLED://WIFI网卡可用  3
                QDLogger.e("WIFI网卡可用");
                break;
            case WIFI_STATE_UNKNOWN://WIFI网卡状态不可知 4
                QDLogger.e("WIFI网卡状态不可知");
                break;
            default:
                QDLogger.e("WIFI default");
                break;
        }

        setOnNetStateChangedListener(new NetWorkChangReceiver.OnNetStateChangedListener() {
            @Override
            public void onConnected(NetworkInfo networkInfo) {
                QDLogger.e("wifi onConnected");
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
            public void onDisConnected() {
                QDLogger.e("wifi dis");
            }
        });
        //registerPermission();


        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        //设置布局管理器
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        wifiAdapter = new WifiAdapter(mContext);
        wifiAdapter.setOnItemClickListener(new TabMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (scanResultList.get(position).getPasswordType()){
                    case 0:
                        //wifiPassType="";
                        break;
                    case 1://WEP
                        //wifiPassType="WEP";
                        break;
                    case 2://WPA
                        //wifiPassType="WPA";
                        break;
                }
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

        initData();
        print();
    }

    private void showInputDialog(int position) {
        new QDInputDialog.Builder(mContext).setTitle("连接"+scanResultList.get(position).getScanResult().SSID)
                .setMessage("")
                .setHint("请输入密码")
                .setBackgroundRadius(30)
                .setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                .addAction("连接", new QDInputDialog.OnClickActionListener() {
                    @Override
                    public void onClick(QDInputDialog dialog, String value) {
                        Toast.makeText(mContext,"input = "+value,Toast.LENGTH_SHORT).show();
                        //连接返回editview的value
                        WifiConfiguration configuration = configWifiInfo(mContext.getApplicationContext(),scanResultList.get(position).getScanResult().SSID,value,scanResultList.get(position).getPasswordType());
                        int netId = configuration.networkId;
                        if (netId == -1) {
                            netId = wifiManager.addNetwork(configuration);
                        }
                        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(getActivity().WIFI_SERVICE);
                        wifiManager.enableNetwork(netId, true);
                    }
                }).addAction("取消").setGravity_foot(Gravity.RIGHT).create().show();
    }


    String[] permissions = { Manifest.permission.ACCESS_COARSE_LOCATION};
    WifiManager wifiManager;
    private void initData() {
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(getActivity().WIFI_SERVICE);
        PermissionManager.getInstance().chekPermission(mContext, permissions, new PermissionManager.PermissionListener() {
            @Override
            public void onPassed() {
                scanResultList = getQDWifiList();
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
            public void onRefused() {QDLogger.i("未通过");
            }
        });
    }

    private void print() {
        List<ScanResult> scanResultList = getWifiList();
        if (scanResultList != null) {
            for (int i = 0; i < scanResultList.size(); i++) {
                QDLogger.i(scanResultList.get(i).SSID+"==> "+scanResultList.get(i).capabilities);
            }
        }
    }


    public List<ScanResult> getWifiList() {
        List<ScanResult> scanWifiList = wifiManager.getScanResults();
        List<ScanResult> wifiList = new ArrayList<>();
        if (scanWifiList != null && scanWifiList.size() > 0) {
            HashMap<String, Integer> signalStrength = new HashMap<String, Integer>();
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

    public List<QDScanResult> getQDWifiList() {
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(getActivity().WIFI_SERVICE);
        List<ScanResult> scanWifiList = wifiManager.getScanResults();
        List<QDScanResult> wifiList = new ArrayList<>();
        if (scanWifiList != null && scanWifiList.size() > 0) {
            HashMap<String, Integer> signalStrength = new HashMap<String, Integer>();
            for (int i = 0; i < scanWifiList.size(); i++) {
                ScanResult scanResult = scanWifiList.get(i);
                if (!scanResult.SSID.isEmpty()) {
                    String key = scanResult.SSID + " " + scanResult.capabilities;
                    if (!signalStrength.containsKey(key)) {
                        signalStrength.put(key, i);
                        QDScanResult qdScanResult = new QDScanResult();
                        qdScanResult.setScanResult(scanResult);
                        wifiList.add(qdScanResult);
                    }
                }
            }
        }
        return wifiList;
    }

    public static WifiConfiguration configWifiInfo(Context context, String SSID, String password, int type) {
        WifiConfiguration config = null;
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager != null) {
            List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig == null) continue;
                String t = "\"" + SSID + "\"";
                if (existingConfig.SSID.equals(t)  /*&&  existingConfig.preSharedKey.equals("\""  +  password  +  "\"")*/) {
                    config = existingConfig;
                    break;
                }
            }
        }
        if (config == null) {
            config = new WifiConfiguration();
        }
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig=null;
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }
        // 分为三种情况：0没有密码1用wep加密2用wpa加密
        if (type == 0) {// WIFICIPHER_NOPASSwifiCong.hiddenSSID = false;
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        } else if (type == 1) {  //  WIFICIPHER_WEP
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + password + "\"";
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else if (type == 2) {   // WIFICIPHER_WPA
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }
}