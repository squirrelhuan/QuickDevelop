package cn.demomaster.huan.quickdeveloplibrary.model;

import java.io.Serializable;

/**
 * @author squirrel桓
 * @date 2019/1/3.
 * description：
 */
public class Version implements Serializable {

    /**
     * versionName : 2.1
     * versionCode : 16
     * description : 本次更新版本号：1.0.9 ，改版学习能力部分。
     * downloadUrl : http://www.geeppies.com/app/app-pokegeno.apk
     * isShow : 1
     * isMust : 1
     * versionCode_ios : 1.0.7
     * ver : 1.0.7
     * filename : http://www.geeppies.com/app/app-pokegeno.apk
     * des : 本次更新版本号：1.0.9 ，改版学习能力部分。
     */

    private String versionName;
    private int versionCode;
    private String description;
    private String downloadUrl;
    private String isShow;
    private boolean isMust;
    private String fileName;
    private String dateTime;
    private String showType;//html,dialog,

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public boolean isMust() {
        return isMust;
    }

    public void setMust(boolean must) {
        isMust = must;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
