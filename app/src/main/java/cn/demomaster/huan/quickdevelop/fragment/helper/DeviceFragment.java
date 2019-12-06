package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout2;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;
import cn.demomaster.huan.quickdeveloplibrary.receiver.NetWorkChangReceiver;
import cn.demomaster.huan.quickdeveloplibrary.util.QDDeviceHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;

import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_UNKNOWN;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
import static cn.demomaster.huan.quickdeveloplibrary.constant.EventBusConstant.EVENT_REFRESH_LANGUAGE;
import static cn.demomaster.huan.quickdeveloplibrary.util.system.QDLanguageUtil.changeAppLanguage;
import static cn.demomaster.huan.quickdeveloplibrary.util.system.QDLanguageUtil.setLanguageLocal;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "DeviceHelper", preViewClass = StateView.class, resType = ResType.Custome)
public class DeviceFragment extends QDBaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    //Components
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

    View mView;

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_device, null);
        }
        ButterKnife.bind(this, mView);
        return (ViewGroup) mView;
    }

    @Override
    public void initView(View rootView, ActionBarInterface actionBarLayoutOld) {
        QDDeviceHelper.init(getContext());
        QDDeviceHelper.setFlagDef(AudioManager.FLAG_PLAY_SOUND);
        //媒体音量
        tv_MusicVolume.setText("媒体" + QDDeviceHelper.getMusicVolumeCurrent() + "/" + QDDeviceHelper.getMusicVolumeMax());
        sb_MusicVolume.setMax(QDDeviceHelper.getMusicVolumeMax());
        sb_MusicVolume.setProgress(QDDeviceHelper.getMusicVolumeCurrent());
        sb_MusicVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                QDDeviceHelper.setMusicVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //铃声音量
        tv_RingVolume.setText("铃声" + QDDeviceHelper.getRingVolumeCurrent() + "/" + QDDeviceHelper.getRingVolumeMax());
        sb_RingVolume.setMax(QDDeviceHelper.getRingVolumeMax());
        sb_RingVolume.setProgress(QDDeviceHelper.getRingVolumeCurrent());
        sb_RingVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                QDDeviceHelper.setRingVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //闹钟音量
        tv_AlarmVolume.setText("闹钟" + QDDeviceHelper.getAlarmVolumeCurrent() + "/" + QDDeviceHelper.getAlarmVolumeMax());
        sb_AlarmVolume.setMax(QDDeviceHelper.getAlarmVolumeMax());
        sb_AlarmVolume.setProgress(QDDeviceHelper.getAlarmVolumeCurrent());
        sb_AlarmVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                QDDeviceHelper.setAlarmVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //通话音量
        tv_VioceVolume.setText("通话" + QDDeviceHelper.getVioceVolumeCurrent() + "/" + QDDeviceHelper.getVioceVolumeMax());
        sb_VioceVolume.setMax(QDDeviceHelper.getVioceVolumeMax());
        sb_VioceVolume.setProgress(QDDeviceHelper.getVioceVolumeCurrent());
        sb_VioceVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                QDDeviceHelper.setVioceVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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
        registerPermission();
    }

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
            PermissionManager.chekPermission(mContext, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, new PermissionManager.OnCheckPermissionListener() {
                @Override
                public void onPassed() {
                    print();
                }

                @Override
                public void onNoPassed() {

                }
            });
        } else {
            print();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION) {
            print();
        }*/
    }

    public List<ScanResult> getWifiList() {
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(getActivity().WIFI_SERVICE);
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
}