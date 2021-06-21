package cn.demomaster.huan.quickdeveloplibrary.model;

import java.io.File;
import java.net.URI;

public class QDFile extends File {
    private String creatTimeStr;
    private long creatTime;
    private long modifyTime;

    public QDFile(String pathname) {
        super(pathname);
    }

    public QDFile(String parent, String child) {
        super(parent, child);
    }

    public QDFile(File parent, String child) {
        super(parent, child);
    }

    public QDFile(URI uri) {
        super(uri);
    }

    public QDFile(File file) {
        super(file.toURI());
    }


    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    public String getCreatTimeStr() {
        return creatTimeStr;
    }

    public void setCreatTimeStr(String creatTimeStr) {
        this.creatTimeStr = creatTimeStr;
    }

    /**
     * 得到单位为毫秒
     *
     * @return
     */
    public long getModifyTime() {
        return modifyTime * 1000;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }
}
