package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.network.NetWorkBroadcastReceiver;
import cn.demomaster.huan.quickdeveloplibrary.network.NetworkHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.QDDeviceHelper;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;
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

@ActivityPager(name = "DeviceHelper", preViewClass = TextView.class, resType = ResType.Custome)
public class DeviceFragment extends QuickFragment {

    @BindView(R.id.sb_MusicVolume)
    SeekBar sb_MusicVolume;
    @BindView(R.id.sb_RingVolume)
    SeekBar sb_RingVolume;
    @BindView(R.id.sb_AlarmVolume)
    SeekBar sb_AlarmVolume;
    @BindView(R.id.sb_VioceVolume)
    SeekBar sb_VioceVolume;

    @BindView(R.id.tv_MusicVolume)
    TextView tv_MusicVolume;
    @BindView(R.id.tv_RingVolume)
    TextView tv_RingVolume;
    @BindView(R.id.tv_AlarmVolume)
    TextView tv_AlarmVolume;
    @BindView(R.id.tv_VioceVolume)
    TextView tv_VioceVolume;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_device, null);
        return mView;
    }
    NetworkHelper networkHelper;
    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        QDDeviceHelper.setFlagDef(AudioManager.FLAG_PLAY_SOUND);
        //媒体音量
        tv_MusicVolume.setText("媒体" + QDDeviceHelper.getMusicVolumeCurrent(getContext()) + "/" + QDDeviceHelper.getMusicVolumeMax(getContext()));
        sb_MusicVolume.setMax(QDDeviceHelper.getMusicVolumeMax(getContext()));
        sb_MusicVolume.setProgress(QDDeviceHelper.getMusicVolumeCurrent(getContext()));
        sb_MusicVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                QDDeviceHelper.setMusicVolume(getContext(), progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //铃声音量
        tv_RingVolume.setText("铃声" + QDDeviceHelper.getRingVolumeCurrent(getContext()) + "/" + QDDeviceHelper.getRingVolumeMax(getContext()));
        sb_RingVolume.setMax(QDDeviceHelper.getRingVolumeMax(getContext()));
        sb_RingVolume.setProgress(QDDeviceHelper.getRingVolumeCurrent(getContext()));
        sb_RingVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                QDDeviceHelper.setRingVolume(getContext(), progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //闹钟音量
        tv_AlarmVolume.setText("闹钟" + QDDeviceHelper.getAlarmVolumeCurrent(getContext()) + "/" + QDDeviceHelper.getAlarmVolumeMax(getContext()));
        sb_AlarmVolume.setMax(QDDeviceHelper.getAlarmVolumeMax(getContext()));
        sb_AlarmVolume.setProgress(QDDeviceHelper.getAlarmVolumeCurrent(getContext()));
        sb_AlarmVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                QDDeviceHelper.setAlarmVolume(getContext(), progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //通话音量
        tv_VioceVolume.setText("通话" + QDDeviceHelper.getVioceVolumeCurrent(getContext()) + "/" + QDDeviceHelper.getVioceVolumeMax(getContext()));
        sb_VioceVolume.setMax(QDDeviceHelper.getVioceVolumeMax(getContext()));
        sb_VioceVolume.setProgress(QDDeviceHelper.getVioceVolumeCurrent(getContext()));
        sb_VioceVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                QDDeviceHelper.setVioceVolume(getContext(), progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
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
        networkHelper = new NetworkHelper();
        onNetStateChangedListener = new NetWorkBroadcastReceiver() {
            @Override
            public void onConnected(Context context, Intent intent) {
                QDLogger.i("wifi onConnected");
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
                QDLogger.e("網絡 斷開");
            }
        };

        IntentFilter mWifiConnectIntentFilter = new IntentFilter();
        mWifiConnectIntentFilter.addAction(WifiManager.ACTION_PICK_WIFI_NETWORK);
        mWifiConnectIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mWifiConnectIntentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        mWifiConnectIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        getContext().registerReceiver(onNetStateChangedListener,mWifiConnectIntentFilter);
        registerPermission();
    }
    NetWorkBroadcastReceiver onNetStateChangedListener;

    private void print() {
        List<ScanResult> scanResultList = getWifiList();
        if (scanResultList != null) {
            for (int i = 0; i < scanResultList.size(); i++) {
                QDLogger.i(scanResultList.get(i).toString());
            }
        } else {
            QDLogger.e("wifi kong");
        }
    }

    private void registerPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                PermissionHelper.requestPermission(mContext, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, new PermissionHelper.PermissionListener() {
                    @Override
                    public void onPassed() {
                        print();
                    }

                    @Override
                    public void onRefused() {

                    }
                });
        } else {
            print();
        }
    }

    public List<ScanResult> getWifiList() {
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> scanWifiList = wifiManager.getScanResults();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(onNetStateChangedListener!=null) {
            getContext().unregisterReceiver(onNetStateChangedListener);
        }
    }
}