package cn.demomaster.huan.quickdeveloplibrary.view.webview;

import android.net.Uri;
import android.os.Message;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public interface QuickWebViewInterface {
    void onReceivedTitle(WebView view, String title);
    boolean onShowFileChooser(QuickWebChromeClient qdWebChromeClient, WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams);
    void onLoading(int progress);
    void onLoadComplete();
    boolean shouldOverrideUrlLoading(WebView view, String url);
    boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg);
    boolean onCloseWindow(WebView window);
    boolean onInterceptLoadResource(WebView view, String url);
}
