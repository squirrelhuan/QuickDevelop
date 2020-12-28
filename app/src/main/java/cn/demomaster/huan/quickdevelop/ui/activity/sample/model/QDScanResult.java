package cn.demomaster.huan.quickdevelop.ui.activity.sample.model;

import android.net.wifi.ScanResult;

import cn.demomaster.huan.quickdevelop.ui.activity.sample.utils.WifiUtil;

public class QDScanResult {
    private ScanResult scanResult;
    WifiUtil.WifiCipherType passwordType;

    public ScanResult getScanResult() {
        return scanResult;
    }

    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public WifiUtil.WifiCipherType getPasswordType() {
        passwordType = getPasswordType(scanResult);
        return passwordType;
    }

    public void setPasswordType(WifiUtil.WifiCipherType passwordType) {
        this.passwordType = passwordType;
    }

    /**
     *获取热点的加密类型
     */
    private WifiUtil.WifiCipherType getPasswordType(ScanResult scanResult){
        WifiUtil.WifiCipherType type;
        if (scanResult.capabilities.contains("WPA"))
            type = WifiUtil.WifiCipherType.WIFICIPHER_WPA;
        else if (scanResult.capabilities.contains("WEP"))
            type = WifiUtil.WifiCipherType.WIFICIPHER_WEP;
        else
            type = WifiUtil.WifiCipherType.WIFICIPHER_NOPASS;
        return type;
    }
}
