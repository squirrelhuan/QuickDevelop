package cn.demomaster.huan.quickdevelop.ui.activity.sample;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.activity.BaseActivity;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.utils.WifiUtil;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;

@ActivityPager(name = "Wifi管理2", preViewClass = TextView.class, resType = ResType.Custome)
public class WifiTestActivity2 extends BaseActivity implements View.OnClickListener {

    //EditText et_pwd;
    TextView mWifiState;//wifi状态
    TextView mWifiName;//Wi-Fi名称
    TextView mMac;//物理地址
    TextView mIP;//ip地址
    TextView mGateway;//网关地址
    ListView mListWifi;//Wi-Fi列表
    Button mBtnSearch;//搜索Wi-Fi
    //Button mBtnConnect;//连接Wi-Fi
    WifiListAdapter mWifiListAdapter;
    public static final int WIFI_SCAN_PERMISSION_CODE = 2;
    //ConnectAsyncTask mConnectAsyncTask = null;
    List<ScanResult> mScanResultList = new ArrayList<>();
    /* public static String ssid = "";
     public static String password = "";//"hyc888888";*/
    FrameLayout progressbar;

    String gateway = "";
    String mac = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_test);
        intiView();
        //初始化wifi工具
        WifiUtil.getInstance().init(getApplicationContext());
        WifiUtil.getInstance().setOnWifiChangeListener(new WifiUtil.OnWifiChangeListener() {
            @Override
            public void onWifiStateChanged(NetworkInfo.DetailedState state) {
                setWifiState(state);
            }

            @Override
            public void onScanResult(List<ScanResult> scanResults) {
                progressbar.setVisibility(View.GONE);
                mScanResultList.clear();
                if (scanResults != null) {
                    mScanResultList.addAll(scanResults);
                    mWifiListAdapter.notifyDataSetChanged();
                }
            }
        });

        WifiUtil.getInstance().searchWifi();

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // 获取wifi连接需要定位权限,没有获取权限
//            ActivityCompat.requestPermissions(this, new String[]{
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//            }, WIFI_SCAN_PERMISSION_CODE);
//            return;
//        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.search_wifi:
                WifiUtil.getInstance().searchWifi();
                break;
           /* case R.id.connect_wifi:
               *//* if (ssid.equals(WifiUtil.getInstance().getSSID())) {
                    return;
                }
                String pwdString = et_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwdString)) {
                        Toast.makeText(WifiTestActivity2.this, "请先填写wifi密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                password = pwdString;
                WifiUtil.getInstance().saveWifiInfo(ssid, password);
                WifiUtil.getInstance().connect(ssid, password);*//*
                break;*/
            default:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        WifiUtil.getInstance().init(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        WifiUtil.getInstance().unRegister();
    }

    /**
     * 显示wifi状态
     *
     * @param state
     */
    @SuppressLint("NewApi")
    public void setWifiState(final NetworkInfo.DetailedState state) {
        QdToast.show(state.toString());
        Log.e("wifidemo", "setWifiState " + state);
        if (state == NetworkInfo.DetailedState.AUTHENTICATING) {

        } else if (state == NetworkInfo.DetailedState.BLOCKED) {

        } else if (state == NetworkInfo.DetailedState.CONNECTED) {
            progressbar.setVisibility(View.GONE);
            mWifiState.setText("wifi state:连接成功");
            mWifiName.setText("wifi name:" + WifiUtil.getInstance().getSSID());
            mIP.setText("ip address:" + WifiUtil.getInstance().getIpAddress());
            gateway = WifiUtil.getInstance().getGateway();
            mGateway.setText("gateway:" + gateway);
            mac = WifiUtil.getInstance().getMacAddress();
            mMac.setText("mac:" + mac);
        } else if (state == NetworkInfo.DetailedState.CONNECTING) {
            mWifiState.setText("wifi state:连接中...");
            mWifiName.setText("wifi name:" + WifiUtil.getInstance().getSSID());
            mIP.setText("ip address");
            mGateway.setText("gateway");
        } else if (state == NetworkInfo.DetailedState.DISCONNECTED) {
            mWifiState.setText("wifi state:断开连接");
            mWifiName.setText("wifi name");
            mIP.setText("ip address");
            mGateway.setText("gateway");
        } else if (state == NetworkInfo.DetailedState.DISCONNECTING) {
            mWifiState.setText("wifi state:断开连接中...");
        } else if (state == NetworkInfo.DetailedState.FAILED) {
            mWifiState.setText("wifi state:连接失败");
        } else if (state == NetworkInfo.DetailedState.IDLE) {

        } else if (state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {

        } else if (state == NetworkInfo.DetailedState.SCANNING) {

        } else if (state == NetworkInfo.DetailedState.SUSPENDED) {

        }
    }

    private void intiView() {
        progressbar = findViewById(R.id.progressbar);
        mWifiState = findViewById(R.id.wifi_state);
        mWifiName = findViewById(R.id.wifi_name);
        mMac = findViewById(R.id.wifi_mac);
        mIP = findViewById(R.id.ip_address);
        mGateway = findViewById(R.id.ip_gateway);
        mListWifi = findViewById(R.id.list_wifi);
        mBtnSearch = findViewById(R.id.search_wifi);
        //mBtnConnect = (Button) findViewById(R.id.connect_wifi);
        //et_pwd = (EditText) findViewById(R.id.et_pwd);

        mBtnSearch.setOnClickListener(this);
        //mBtnConnect.setOnClickListener(this);
        mListWifi.setOnItemClickListener((adapterView, view, i, l) -> {
            mWifiListAdapter.setSelectItem(i);
            mWifiListAdapter.notifyDataSetChanged();
            ScanResult scanResult = mScanResultList.get(i);

            String currentSsid = WifiUtil.getInstance().getWifiInfo()[0];
            String ssid = scanResult.SSID;
            if (currentSsid.equals(ssid)) {
                WifiUtil.WifiCipherType type = WifiUtil.getInstance().getCipherType(ssid);

               /*WifiUtil.getInstance().saveWifiInfo(ssid, password);
               WifiUtil.getInstance().connect(ssid, password);*/
            }
        });
        mWifiListAdapter = new WifiListAdapter(mScanResultList, LayoutInflater.from(WifiTestActivity2.this));
        mListWifi.setAdapter(mWifiListAdapter);
    }

    /**
     * wifi列表适配器
     */
     static class WifiListAdapter extends BaseAdapter {
        private List<ScanResult> mWifiList;
        private LayoutInflater mLayoutInflater;

        public WifiListAdapter(List<ScanResult> wifiList, LayoutInflater layoutInflater) {
            this.mWifiList = wifiList;
            this.mLayoutInflater = layoutInflater;
        }

        @Override
        public int getCount() {
            return mWifiList.size();
        }

        @Override
        public Object getItem(int position) {
            return mWifiList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.fragment_wifi_list_item, null);
            }
            ScanResult sr = mWifiList.get(position);
            convertView.setTag(sr);
            TextView textView = convertView.findViewById(R.id.wifi_item_name);
            int numLevel = WifiUtil.getInstance().getSignalNumsLevel(sr.level, 5);
            String password = sr.capabilities;
            String text = "加密方式:";
            if (password.contains("WPA") || password.contains("wpa")) {
                password = "WPA";
            } else if (password.contains("WEP") || password.contains("wep")) {
                password = "WEP";
            } else {
                text = "";
                password = "";
            }
            textView.setText(sr.SSID + " " + text + password + "  信号强度：" + numLevel);
            convertView.setBackgroundColor(Color.WHITE);
            if (position == selectItem) {
                convertView.setBackgroundColor(Color.GRAY);
            }
            return convertView;
        }

        public void setSelectItem(int selectItem) {
            this.selectItem = selectItem;
        }

        private int selectItem = -1;

        public void setmWifiList(List<ScanResult> mWifiList) {
            this.mWifiList = mWifiList;
        }
    }

    /**
     * 连接指定的wifi
     */
   /* public class ConnectAsyncTask extends AsyncTask<Void, Void, Boolean> {
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
            progressbar.setVisibility(View.VISIBLE);
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
                if (!WifiUtil.getInstance().isLinked && type != WifiUtil.WifiCipherType.WIFICIPHER_NOPASS) {
                    try {
                        Thread.sleep(5000);//超过5s提示失败
                        if (!WifiUtil.getInstance().isLinked) {
                            Log.d("wifidemo", ssid + "连接失败！");
                            WifiUtil.getInstance().wifiManager.disableNetwork(tempConfig.networkId);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressbar.setVisibility(View.GONE);
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
                            });
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final EditText inputServer = new EditText(WifiTestActivity2.this);
                            new AlertDialog.Builder(WifiTestActivity2.this)
                                    .setTitle("请输入密码")
                                    .setView(inputServer)
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setPositiveButton("连接", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            password = inputServer.getText().toString();
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
                                    }).show();
                        }
                    });
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
    }*/
}
