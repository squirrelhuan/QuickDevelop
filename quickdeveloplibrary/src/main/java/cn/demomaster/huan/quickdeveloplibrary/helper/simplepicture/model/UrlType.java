package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model;

/**
 * @author squirrel桓
 * @date 2019/1/15.
 * description：
 */
public enum UrlType {
    url(0), file(1);

    private int value = 0;
    private String desc = "";
    UrlType(int value) {//必须是private的，否则编译错误
        this.value = value;
    }
    public int value() {
        return this.value;
    }

    public String getDesc() {
        return desc;
    }

    public static UrlType getEnum(int value) {
        UrlType resultEnum = null;
        UrlType[] enumArray = UrlType.values();
        for (UrlType urlType : enumArray) {
            if (urlType.value() == value) {
                resultEnum = urlType;
                break;
            }
        }
        return resultEnum;
    }
}
