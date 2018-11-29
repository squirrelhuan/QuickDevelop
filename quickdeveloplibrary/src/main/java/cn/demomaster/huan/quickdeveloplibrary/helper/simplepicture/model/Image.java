package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model;

/**
 * Created by Squirrel桓 on 2018/11/28.
 */
/**
 *图片实体类
 */
public class Image {

    private String path;
    private long time;
    private String name;
    private int position;

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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
