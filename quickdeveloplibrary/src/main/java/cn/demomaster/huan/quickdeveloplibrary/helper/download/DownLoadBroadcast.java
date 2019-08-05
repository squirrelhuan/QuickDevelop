package cn.demomaster.huan.quickdeveloplibrary.helper.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

import static android.content.Context.DOWNLOAD_SERVICE;

public class DownLoadBroadcast extends BroadcastReceiver {
    private DownloadManager downloadManager;
    private DownloadHelper.OnDownloadStateChangeListener onDownloadStateChangeListener;
    public DownLoadBroadcast(DownloadHelper.OnDownloadStateChangeListener onDownloadStateChangeListener) {
        this.onDownloadStateChangeListener = onDownloadStateChangeListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        switch (intent.getAction()) {
            case DownloadManager.ACTION_DOWNLOAD_COMPLETE:
                if (downId != -1 && downloadManager != null) {
                    Uri downIdUri = downloadManager.getUriForDownloadedFile(downId);
                    if (downIdUri != null) {
                        //QDLogger.i("下载完成，存储路径为 ：" + downIdUri.getPath());
                        onDownloadStateChangeListener.onComplete(downId,downIdUri);
                           /* if (listenerMap.containsKey(downId)) {
                                OnDownloadProgressListener listener = listenerMap.get(downId);
                                listener.onProgress(downId, listener.fileName, 1);
                                // listener.getDownloadChangeObserver().close();
                                listenerMap.remove(downId);
                                //TODO 遗留问题回家处理
                                unregisterBroadcast(context, listener.getBroadcastReceiver());
                                listener.getDownloadChangeObserver().close();
                                unregisterContentObserver(context, listener.getDownloadChangeObserver());
                            }*/
                    }
                }
                break;
            default:
                break;
        }
    }
}
