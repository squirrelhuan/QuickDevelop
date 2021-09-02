package cn.demomaster.huan.quickdeveloplibrary.view.banner;

/**
 * 广告资源类型
 */
public enum BannerFileType {
    remote(0),
    local(1);

    private int value = 0;

    public int value() {
        return this.value;
    }

    BannerFileType(int value) {
        this.value = value;
    }

    public static BannerFileType getEnum(int value) {
        BannerFileType resultEnum = null;
        BannerFileType[] enumArray = BannerFileType.values();
        for (BannerFileType bannerFileType : enumArray) {
            if (bannerFileType.value() == value) {
                resultEnum = bannerFileType;
                break;
            }
        }
        return resultEnum;
    }
}
