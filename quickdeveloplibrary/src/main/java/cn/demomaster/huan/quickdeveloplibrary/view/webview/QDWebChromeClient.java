package cn.demomaster.huan.quickdeveloplibrary.view.webview;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class QDWebChromeClient extends WebChromeClient {
    public interface OnStateChangedListener {
        void onProgress(int progress);

        void onFinish();

        void onReceivedTitle(WebView view, String title);

        boolean onNewTab(WebView view, String url);
        
    }

    public interface OnProgressChanged {
        void onProgress(int progress);

        void onFinish();
    }

    private OnProgressChanged onProgressChanged;

    public void setOnProgressChanged(OnProgressChanged onProgressChanged) {
        this.onProgressChanged = onProgressChanged;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        //更新进度
        if (onProgressChanged != null) {
            onProgressChanged.onProgress(newProgress);
            if (newProgress == 100) {
                onProgressChanged.onFinish();
            }
        }
        super.onProgressChanged(view, newProgress);
    }
}
