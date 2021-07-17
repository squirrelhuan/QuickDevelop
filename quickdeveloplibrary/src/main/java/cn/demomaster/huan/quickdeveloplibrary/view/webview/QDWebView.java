package cn.demomaster.huan.quickdeveloplibrary.view.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

import cn.demomaster.huan.quickdeveloplibrary.constant.AppConfig;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;
import cn.demomaster.qdlogger_library.QDLogger;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class QDWebView extends WebView {
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

    boolean touchAble = true;

    public void setTouchAble(boolean touchAble) {
        this.touchAble = touchAble;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return touchAble ? super.onTouchEvent(ev) : false;
    }

    private float mProgress;
    /*private boolean isSupportZoom = true;
    private boolean isSupportZoomTool = true;*/
    QDWebChromeClient.OnStateChangedListener onStateChangedListener;

    public void setOnStateChangedListener(QDWebChromeClient.OnStateChangedListener onStateChangedListener) {
        this.onStateChangedListener = onStateChangedListener;
    }

    private void init() {
        progressHeight = DisplayUtil.dip2px(getContext(), 2);//进度条默认高度
        QDWebChromeClient qdWebCromeClient = new QDWebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (onStateChangedListener != null) {
                    onStateChangedListener.onReceivedTitle(view, title);
                }else {//缺省设置
                    QDLogger.e("网络请求异常L:"+title);
                    if (title.contains("404") || title.contains("500") || title.contains("Error")) {
                        //view.loadUrl("about:blank");//避免出现默认的错误界面
                        view.loadDataWithBaseURL(null, "^_^暂无内容", "text/html", "UTF-8", null);
                    }
                }
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                QdToast.show(getContext(),"选择图片");
                uploadFiles = filePathCallback;
                openFileChooseProcess();
                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }
        };
        qdWebCromeClient.setOnProgressChanged(new QDWebChromeClient.OnProgressChanged() {
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
        addJavascriptInterface(new AndroidtoJs(getContext()), "app");//AndroidtoJS类对象映射到js的test对象

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
                QDLogger.println("shouldOverrideUrlLoading1:" + request.getUrl().toString());
                String url = request.getUrl().toString();
                return shouldOverrideUrlLoading1(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                QDLogger.println("shouldOverrideUrlLoading2:" + url);
                return shouldOverrideUrlLoading1(view, url);
            }
        });
        //loadUrl(mUrl);
        setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //dealUrlLoading();
                final WebView.HitTestResult result = getHitTestResult();
                if (null == result) {
                    return false;
                }
                int type = result.getType();
                switch (type) {
                    case WebView.HitTestResult.EDIT_TEXT_TYPE: // 选中的文字类型
                        QdToast.show(getContext(), "选中的文字类型");
                        break;
                    case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
                        QdToast.show(getContext(), "处理拨号");
                        break;
                    case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
                        QdToast.show(getContext(), "处理Email");
                        break;
                    case WebView.HitTestResult.GEO_TYPE: // 　地图类型
                        QdToast.show(getContext(), "地图类型");
                        break;
                    case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                        QdToast.show(getContext(), "超链接");
                        break;
                    case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE: // 带有链接的图片类型
                        QdToast.show(getContext(), "带有链接的图片类型");
                        break;
                    case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
                        String url = result.getExtra();//获取图片
                        QdToast.show(getContext(), "处理长按图片的菜单项");
                        break;
                    case WebView.HitTestResult.UNKNOWN_TYPE: //未知
                        QdToast.show(getContext(), "未知");
                        break;
                }
                return false;
            }
        });
    }
    private ValueCallback<Uri> uploadFile;
    private ValueCallback<Uri[]> uploadFiles;
    private void openFileChooseProcess() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        ((Activity)getContext()).startActivityForResult(Intent.createChooser(i, "上传文件"), 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if (null != uploadFile) {
                    Uri result = data == null ? null
                            : data.getData();
                    uploadFile.onReceiveValue(result);
                    uploadFile = null;
                }
                if (null != uploadFiles) {
                    Uri result = data == null ? null
                            : data.getData();
                    uploadFiles.onReceiveValue(new Uri[]{result});
                    uploadFiles = null;
                }
            } else if (resultCode == RESULT_CANCELED) {
                if (null != uploadFile) {
                    uploadFile.onReceiveValue(null);
                    uploadFile = null;
                }
            }
        }
    }
    private boolean shouldOverrideUrlLoading1(WebView view, String url) {
        if (url.startsWith("newtab:")) {
            //对新的URL进行截取，去掉前面的newtab:
            /*String realUrl=url.substring(7,url.length());*/
            if (onStateChangedListener != null) {
                return onStateChangedListener.onNewTab(view, url);
            }
        } else {
            view.loadUrl(url);
            return true;
        }
        return false;
    }

    private void dealUrlLoading(WebView view, String url) {
        final WebView.HitTestResult result = getHitTestResult();
        if (null == result)
            return;
        int type = result.getType();
        switch (type) {
            case WebView.HitTestResult.EDIT_TEXT_TYPE: // 选中的文字类型
                QdToast.show(getContext(), "选中的文字类型");
                break;
            case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
                QdToast.show(getContext(), "处理拨号");
                break;
            case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
                QdToast.show(getContext(), "处理Email");
                break;
            case WebView.HitTestResult.GEO_TYPE: // 　地图类型
                QdToast.show(getContext(), "地图类型");
                break;
            case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                QdToast.show(getContext(), "超链接");
                break;
            case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE: // 带有链接的图片类型
                QdToast.show(getContext(), "带有链接的图片类型");
                break;
            case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
                //String url = result.getExtra();//获取图片
                QdToast.show(getContext(), "处理长按图片的菜单项");
                break;
            case WebView.HitTestResult.UNKNOWN_TYPE: //未知
                QdToast.show(getContext(), "未知");
                break;
        }
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
    }
    
    public class AndroidtoJs {
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
        if (showProgressBar && mProgress != 0) {
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
