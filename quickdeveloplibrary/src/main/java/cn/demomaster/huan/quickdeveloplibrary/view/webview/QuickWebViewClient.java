package cn.demomaster.huan.quickdeveloplibrary.view.webview;

import android.net.http.SslError;
import android.os.Build;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.demomaster.qdlogger_library.QDLogger;

public class QuickWebViewClient extends WebViewClient {
    WebView qdWebView;
    public QuickWebViewClient(WebView qdWebView) {
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
        QDLogger.println("onLoadResource","onLoadResource url="+url);
        super.onLoadResource(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        QDLogger.println("shouldOverrideUrlLoading2:" + url);
        boolean b = ((QuickWebViewClientInterface)qdWebView).shouldOverrideUrlLoading(view, url);
        return b;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
        QDLogger.println("onReceivedSslError: error:" + error);
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
        QDLogger.println("onReceivedHttpError: errorResponse:" + errorResponse);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            QDLogger.println("网页错误2: [" +error.getErrorCode()+"]"+ error.getDescription());
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        QDLogger.println("网页错误1:" + description+",failingUrl="+failingUrl);
    }
}
