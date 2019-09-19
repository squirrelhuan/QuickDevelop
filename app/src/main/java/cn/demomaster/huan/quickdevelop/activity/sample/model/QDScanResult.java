package cn.demomaster.huan.quickdevelop.activity.sample.model;

import android.net.wifi.ScanResult;

public class QDScanResult {
    private ScanResult scanResult;
    int passwordType;

    public ScanResult getScanResult() {
        return scanResult;
    }

    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public int getPasswordType() {
        passwordType = getPasswordType(scanResult);
        return passwordType;
    }

    public void setPasswordType(int passwordType) {
        this.passwordType = passwordType;
    }

    /**
     *获取热点的加密类型
     */
    private int getPasswordType(ScanResult scanResult){
        int type;
        if (scanResult.capabilities.contains("WPA"))
            type = 2;
        else if (scanResult.capabilities.contains("WEP"))
            type = 1;
        else
            type = 0;
        return type;
    }
}
