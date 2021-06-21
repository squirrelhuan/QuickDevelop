package cn.demomaster.huan.quickdeveloplibrary.helper.download;

public abstract class OnDownloadProgressListener implements OnDownloadProgressInterface {
    public long frequency = 1000;//回调频率
    long lastCallBackTime;//上一次回调时间

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

    public abstract void onDownloadFail();

    public abstract void onDownloadPaused();

    @Override
    public void onDownloadRunning(long downloadId, String name, float progress) {
        if (Math.abs(System.currentTimeMillis() - lastCallBackTime) > frequency) {
            lastCallBackTime = System.currentTimeMillis();
        }
    }

    /**
     * 是否可以回调操作，有些操作不需要太频繁，有频率限制
     *
     * @return
     */
    public boolean canCallBack() {
        return Math.abs(System.currentTimeMillis() - lastCallBackTime) > frequency;
    }

    //public abstract void onDownloadReady(int downloadId);
}
