package cn.demomaster.huan.quickdeveloplibrary.view.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;

import androidx.annotation.RequiresApi;

import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.demomaster.huan.quickdeveloplibrary.constant.AppConfig;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;
import cn.demomaster.qdlogger_library.QDLogger;

public class QDWebView extends WebView {
    private Context mContext;
    private String mUrl = "http://www.demomaster.cn";

    public QDWebView(Context context) {
        super(context);
        init();
    }

    public QDWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QDWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private float mProgress;
    /*private boolean isSupportZoom = true;
    private boolean isSupportZoomTool = true;*/

    private void init() {
        mContext = getContext();
        progressHeight = DisplayUtil.dip2px(mContext, 3);//进度条默认高度
        QDWebCromeClient qdWebCromeClient = new QDWebCromeClient();
        qdWebCromeClient.setOnProgressChanged(new QDWebCromeClient.OnProgressChanged() {
            @Override
            public void onProgress(int progress) {
                mProgress = progress / 100f;
                postInvalidate();
            }

            @Override
            public void onFinish() {
                mProgress = 0;
                postInvalidate();
            }
        });
        setWebChromeClient(qdWebCromeClient);
        // 设置WebView属性，能够执行JavaScript脚本
        getSettings().setJavaScriptEnabled(true);
        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        addJavascriptInterface(new AndroidtoJs(mContext), "app");//AndroidtoJS类对象映射到js的test对象

        // 设置可以支持缩放
        getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        getSettings().setBuiltInZoomControls(true);
        // 为图片添加放大缩小功能
        getSettings().setUseWideViewPort(true);
        setInitialScale(100);   //100代表不缩放
        setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                QDLogger.e("url1 ---  =", request.getUrl().toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (request.getUrl().toString().contains("geeppies.com")) {
                        view.loadUrl(request.getUrl().toString());
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                QDLogger.e("url2 ---  =", url);
                if (url.toString().contains("geeppies.com")) {
                    view.loadUrl(url);
                    return true;
                }
                return false;
            }
        });

        //loadUrl(mUrl);
    }


    // 继承自Object类
    public class AndroidtoJs extends Object {
        Context context;

        public AndroidtoJs(Context context) {
            this.context = context;
        }

        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void share(String msg) {
           QDLogger.println("JS调用了Android的share方法");
            //GeneralUtils.share((Activity) context, 2, null);
        }

        @JavascriptInterface
        public void back(String msg) {
           QDLogger.println(msg);
            ((Activity) context).finish();

        }

        @JavascriptInterface
        public void showMessage(String message) {
            //ToastHelper.showToast(mContext,message);
            // PopToastUtil.ShowToast((Activity) getc,message);
        }

        @JavascriptInterface
        public void newPage(String url) {
           QDLogger.println(url);
            Class clazz = AppConfig.getClassByClassName("");
            Intent intent = new Intent(context, clazz);
            //String url = ((ArrayList) params).get(0).toString();
            intent.putExtra("url", url);
            context.startActivity(intent);
        }

        @JavascriptInterface
        public void MoveUp(String msg) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StatusBarUtil.setStatusBarMode((Activity) context, false);
                    //StatusBarUtil.transparencyBar((Activity) context);
                }
            });
        }

        @JavascriptInterface
        public void MoveDown(String msg) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StatusBarUtil.setStatusBarMode((Activity) context, true);
                    //StatusBarUtil.transparencyBar((Activity) context);
                }
            });
        }

    }

    private int progressHeight = 10;
    private boolean showProgressBar = true;//是否显示进度条
    private int progressBarColor = Color.RED;//进度条颜色
    private int progressBarBackgroundColor = Color.BLACK;//进度条背景色

    public void setProgressHeight(int progressHeight) {
        this.progressHeight = progressHeight;
    }

    public void setShowProgressBar(boolean showProgressBar) {
        this.showProgressBar = showProgressBar;
    }

    public void setProgressBarColor(int progressBarColor) {
        this.progressBarColor = progressBarColor;
    }

    public void setProgressBarBackgroundColor(int progressBarBackgroundColor) {
        this.progressBarBackgroundColor = progressBarBackgroundColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showProgressBar&&mProgress!=0) {
            RectF rectF = new RectF(getLeft(), 0, getRight(), progressHeight);
            Paint mpaint = new Paint();
            mpaint.setColor(progressBarBackgroundColor);
            canvas.drawRoundRect(rectF, 0, 0, mpaint);
            RectF rectF2 = new RectF(getLeft(), 0, getRight() * mProgress, progressHeight);
            Paint mpaint2 = new Paint();
            mpaint2.setColor(progressBarColor);
            canvas.drawRoundRect(rectF2, 0, 0, mpaint2);
        }
    }
}
