package cn.demomaster.huan.quickdeveloplibrary.view.webview;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.demomaster.qdlogger_library.QDLogger;

public class QuickWebViewClient extends WebViewClient {
    QDWebView qdWebView;
    public QuickWebViewClient(QDWebView qdWebView) {
        this.qdWebView = qdWebView;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        /*String url;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            url = request.getUrl().toString();
        }else {
            url = request.toString();
        }
        QDLogger.println("shouldOverrideUrlLoading1:" + url);
        return qdWebView.shouldOverrideUrlLoading(view, url);*/
       return super.shouldOverrideUrlLoading(view,request);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        QDLogger.i("onLoadResource url="+url);
        super.onLoadResource(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        QDLogger.println("shouldOverrideUrlLoading2:" + url);
        return qdWebView.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
        QDLogger.e("onReceivedSslError: error:" + error);
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
        QDLogger.e("onReceivedHttpError: errorResponse:" + errorResponse);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        QDLogger.e("onReceivedError2: description:" + error);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        QDLogger.e("onReceivedError1: description:" + description+",failingUrl="+failingUrl);
    }
}
