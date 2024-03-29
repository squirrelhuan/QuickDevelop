package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.Serializable;

import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;

/**
 * 图片实体类
 */
public class Image implements Serializable {

    private String path;
    private long time;
    private String name;
    private UrlType urlType = UrlType.file;
    private Uri uri;

    public Image(String path, UrlType urlType) {
        this.path = path;
        this.urlType = urlType;
    }

    public Image(String path, long time, String name) {
        this.path = path;
        this.time = time;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public UrlType getUrlType() {
        return urlType;
    }

    public void setUrlType(UrlType urlType) {
        this.urlType = urlType;
    }

    public Uri getUri(Context context) {
        if(uri==null){
            //uri = Uri.parse(path);
            uri = QDFileUtil.fileToUri(context,new File(path));
        }
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
