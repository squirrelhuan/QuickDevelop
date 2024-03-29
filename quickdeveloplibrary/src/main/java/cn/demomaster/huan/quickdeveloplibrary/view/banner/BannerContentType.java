package cn.demomaster.huan.quickdeveloplibrary.view.banner;

/**
 * 广告内容类型
 */
public enum BannerContentType {
    img(0),
    video(1),
    html(2),
    text(3);

    private int value = 0;

    public int value() {
        return this.value;
    }

    BannerContentType(int value) {
        this.value = value;
    }

    public static BannerContentType getEnum(int value) {
        BannerContentType resultEnum = null;
        BannerContentType[] enumArray = BannerContentType.values();
        for (BannerContentType bannerContentType : enumArray) {
            if (bannerContentType.value() == value) {
                resultEnum = bannerContentType;
                break;
            }
        }
        return resultEnum;
    }
}
