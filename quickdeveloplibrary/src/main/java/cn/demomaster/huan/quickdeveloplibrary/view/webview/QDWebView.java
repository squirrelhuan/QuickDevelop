package cn.demomaster.huan.quickdeveloplibrary.view.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdeveloplibrary.constant.AppConfig;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDDialog;
import cn.demomaster.qdlogger_library.QDLogger;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class QDWebView extends WebView implements QuickWebViewInterface{
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
        return touchAble && super.onTouchEvent(ev);
    }

    private float mProgress;
    /*private boolean isSupportZoom = true;
    private boolean isSupportZoomTool = true;*/
    QuickWebChromeClient.OnStateChangedListener onStateChangedListener;
    public void setOnStateChangedListener(QuickWebChromeClient.OnStateChangedListener onStateChangedListener) {
        this.onStateChangedListener = onStateChangedListener;
    }

    private void init() {
        progressHeight = DisplayUtil.dip2px(getContext(), 2);//进度条默认高度
        setWebChromeClient(new QuickWebChromeClient(this));
        // 修改ua使得web端正确判断
        /*String chrome_ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36";
        getSettings().setUserAgentString(chrome_ua);*/
        /*String ua = getSettings().getUserAgentString();
        QDLogger.i("webview agent:"+ua);*/
        // 设置WebView属性，能够执行JavaScript脚本
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDatabaseEnabled(true);
        getSettings().setDomStorageEnabled(true);
        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        addJavascriptInterface(new AndroidtoJs(getContext()), "app");//AndroidtoJS类对象映射到js的test对象

        super.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            if(downloadListener!=null) {
                downloadListener.onDownloadStart(url, userAgent, contentDisposition, mimetype, contentLength);
            }else {
                QDLogger.e("userAgent="+userAgent+",contentDisposition="+contentDisposition+",mimetype="+mimetype+",contentLength="+contentLength);
                showDownloadDialog(url,contentDisposition,contentLength);
            }
        });

        getSettings().setSupportMultipleWindows(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置可以支持缩放
        getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        getSettings().setBuiltInZoomControls(true);
        // 为图片添加放大缩小功能
        getSettings().setUseWideViewPort(true);
        setInitialScale(100);   //100代表不缩放
        setMyWebViewClient(new QuickWebViewClient(this));
        //loadUrl(mUrl);
        setOnLongClickListener(v -> {
            //dealUrlLoading();
            final HitTestResult result = getHitTestResult();
            if (null == result) {
                return false;
            }
            int type = result.getType();
            switch (type) {
                case HitTestResult.EDIT_TEXT_TYPE: // 选中的文字类型
                    QdToast.show(getContext(), "选中的文字类型");
                    break;
                case HitTestResult.PHONE_TYPE: // 处理拨号
                    QdToast.show(getContext(), "处理拨号");
                    break;
                case HitTestResult.EMAIL_TYPE: // 处理Email
                    QdToast.show(getContext(), "处理Email");
                    break;
                case HitTestResult.GEO_TYPE: // 　地图类型
                    QdToast.show(getContext(), "地图类型");
                    break;
                case HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                    QdToast.show(getContext(), "超链接");
                    break;
                case HitTestResult.SRC_IMAGE_ANCHOR_TYPE: // 带有链接的图片类型
                    QdToast.show(getContext(), "带有链接的图片类型");
                    break;
                case HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
                    String url = result.getExtra();//获取图片
                    QdToast.show(getContext(), "处理长按图片的菜单项");
                    break;
                case HitTestResult.UNKNOWN_TYPE: //未知
                    QdToast.show(getContext(), "未知");
                    break;
            }
            return false;
        });
    }

    @Override
    public void reload() {
        //super.reload();
        loadUrl(mUrl);
    }

    private void showDownloadDialog(String url, String contentDisposition, long contentLength) {
        QDDialog qdDialog = new QDDialog.Builder(getContext())
                .setTitle("确定要下载"+contentDisposition+"("+ QDFileUtil.formatFileSize(contentLength,false) +")"+"吗？")
                .setMessage(url)
                .addAction("取消")
                .addAction("确定", (dialog, view, tag) -> {
                    dialog.dismiss();
                    downloadByBrowser(url);
                })
                .create();
        qdDialog.show();
    }
    //跳转到系统浏览器下载
    private void downloadByBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        getContext().startActivity(intent);
    }

    DownloadListener downloadListener;
    public void setDownloadListener(DownloadListener listener) {
        this.downloadListener = listener;
    }
    public void setMyWebViewClient(@NonNull QuickWebViewClient myClient) {
        super.setWebViewClient(myClient);
    }
    public void setMyWebChromeClient(@Nullable QuickWebChromeClient client) {
        super.setWebChromeClient(client);
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
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
        /*if (url.startsWith("newtab:")) {
            //对新的URL进行截取，去掉前面的newtab:
            *//*String realUrl=url.substring(7,url.length());*//*
            if (onStateChangedListener != null) {
                return onStateChangedListener.onNewTab(view, url);
            }
        } else {
            view.loadUrl(url);
            return true;
        }
        return false;*/
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        if(onWindowViewInflate==null){
            return false;
        }else {
            WebView newWebView = onWindowViewInflate.onWindowOpen();
            if(newWebView==null){
                return false;
            }
            QDWebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(newWebView);
            resultMsg.sendToTarget();
            return true;
        }
    }

    @Override
    public boolean onCloseWindow(WebView window) {
        if(onWindowViewInflate==null||window==null||window.getParent()==null){
            return false;
        }else {
            onWindowViewInflate.onCloseWindow(window);
            return true;
        }
    }

    @Override
    public boolean onInterceptLoadResource(WebView view, String url) {
        //是否拦截资源加载
        return false;
    }

    WindowViewInflate onWindowViewInflate;
    public void setOnWindowViewInflate(WindowViewInflate onWindowViewInflate) {
        this.onWindowViewInflate = onWindowViewInflate;
    }

    public interface WindowViewInflate{
        WebView onWindowOpen();
        void onCloseWindow(WebView window);
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
        mUrl = url;
        super.loadUrl(url);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (onStateChangedListener != null) {
            onStateChangedListener.onReceivedTitle(view, title);
        }else {//缺省设置
            /*if (title.contains("404") || title.contains("500") || title.contains("Error")) {
                QDLogger.e("网络请求异常L:"+title);
                //view.loadUrl("about:blank");//避免出现默认的错误界面
                view.loadDataWithBaseURL(null, "^_^暂无内容", "text/html", "UTF-8", null);
            }*/
        }
    }

    @Override
    public boolean onShowFileChooser(QuickWebChromeClient qdWebChromeClient, WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            //QdToast.show(getContext(),"选择图片");
            uploadFiles = filePathCallback;
            openFileChooseProcess();
            return qdWebChromeClient.onShowFileChooser(webView, filePathCallback, fileChooserParams);
    }

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

    public static class AndroidtoJs {
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
            /*((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StatusBarUtil.setStatusBarMode((Activity) context, false);
                    //StatusBarUtil.transparencyBar((Activity) context);
                }
            });*/
        }

        @JavascriptInterface
        public void MoveDown(String msg) {
            
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

    @Override
    public void destroy() {
        removeAllViews();
        super.destroy();
    }
}
