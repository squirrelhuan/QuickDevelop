package cn.demomaster.huan.quickdeveloplibrary.helper.download;

import android.content.BroadcastReceiver;

public abstract class OnDownloadProgressListener implements OnDownloadProgressInterface {

    public abstract void  onDownloadFail();

    public abstract void onDownloadPaused();
}
