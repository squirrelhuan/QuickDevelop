package cn.demomaster.huan.quickdeveloplibrary.view.webview;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class QDWebCromeClient extends WebChromeClient {

    public static interface OnProgressChanged {
        void onProgress(int progress);

        void onFinish();
    }

    private OnProgressChanged onProgressChanged;

    public void setOnProgressChanged(OnProgressChanged onProgressChanged) {
        this.onProgressChanged = onProgressChanged;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        //更新进度
        if (onProgressChanged != null){
                onProgressChanged.onProgress(newProgress);
            if(newProgress==100){
                onProgressChanged.onFinish();
            }
        }
        super.onProgressChanged(view, newProgress);
    }
}
