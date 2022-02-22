package cn.demomaster.huan.quickdeveloplibrary.view.webview;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import cn.demomaster.huan.quickdeveloplibrary.helper.QdThreadHelper;
import cn.demomaster.qdlogger_library.QDLogger;

public class QuickWebChromeClient extends WebChromeClient {
    QDWebView qdWebView;

    @Override
    public void onPermissionRequest(PermissionRequest request) {
        //super.onPermissionRequest(request);
        QdThreadHelper.runOnUiThread(new Runnable(){
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                request.grant(request.getResources());
            }// run
        });// MainActivity
    }

    public QuickWebChromeClient(QDWebView qdWebView) {
        this.qdWebView = qdWebView;
        mOnProgressChanged = new OnProgressChanged() {
            @Override
            public void onProgress(int progress) {
                if(qdWebView!=null){
                    qdWebView.onProgress(progress);
                }
                if(onProgressChanged!=null){
                    onProgressChanged.onProgress(progress);
                }
            }

            @Override
            public void onFinish() {
                if(qdWebView!=null){
                    qdWebView.onFinish();
                }
                if(onProgressChanged!=null){
                    onProgressChanged.onFinish();
                }
            }
        };
    }

    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        new AlertDialog.Builder(view.getContext())
                .setTitle("AlertDialog")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> result.confirm())
                .setCancelable(false)
                .create()
                .show();
        return true;
    }

    public interface OnStateChangedListener {
        void onProgress(int progress);

        void onFinish();

        void onReceivedTitle(WebView view, String title);

      //  boolean onNewTab(WebView view, String url);
    }

    public interface OnProgressChanged {
        void onProgress(int progress);

        void onFinish();
    }

    private OnProgressChanged onProgressChanged;
    private OnProgressChanged mOnProgressChanged;

    public void setOnProgressChanged(OnProgressChanged onProgressChanged) {
        this.onProgressChanged = onProgressChanged;
    }


    /*android 低版本 Desperate*/
    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        QDLogger.i("console1", message + "(" +sourceID  + ":" + lineNumber+")");
        super.onConsoleMessage(message, lineNumber, sourceID);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        QDLogger.i("console2", "["+consoleMessage.messageLevel()+"] "+ consoleMessage.message() + "(" +consoleMessage.sourceId()  + ":" + consoleMessage.lineNumber()+")");
        return super.onConsoleMessage(consoleMessage);
    }
    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        super.onShowCustomView(view, callback);
        QDLogger.e("onShowCustomView:" + callback);
    }

    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        super.onShowCustomView(view, requestedOrientation, callback);
        QDLogger.e("onShowCustomView2:" + callback);
    }

    /**
     * getSettings().setSupportMultipleWindows(true);必须开启时此函数才会启用
     * 当网页里a标签target="_blank"，打开新窗口时，这里会调用
     */
    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        QDLogger.e("onCreateWindow: resultMsg:" + resultMsg);
        boolean b = qdWebView.onCreateWindow(view,isDialog,isUserGesture,resultMsg);
        if(b){
            return true;
        }
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    @Override
    public void onCloseWindow(WebView window) {
        QDLogger.e("onCloseWindow: window:" + window);
        boolean b = false;
        if(qdWebView!=null){
            b=qdWebView.onCloseWindow(window);
        }
        if(!b){
            super.onCloseWindow(window);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if(qdWebView!=null){
            qdWebView.onReceivedTitle(view,title);
        }
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        if(qdWebView!=null){
           return qdWebView.onShowFileChooser(this,webView, filePathCallback, fileChooserParams);
        }
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        //更新进度
        if (mOnProgressChanged != null) {
            mOnProgressChanged.onProgress(newProgress);
            if (newProgress == 100) {
                mOnProgressChanged.onFinish();
            }
        }
        super.onProgressChanged(view, newProgress);
    }
}
