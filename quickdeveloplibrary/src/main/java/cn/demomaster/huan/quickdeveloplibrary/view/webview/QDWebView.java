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
import android.os.Bundle;
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

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.constant.AppConfig;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDSheetDialog;
import cn.demomaster.qdlogger_library.QDLogger;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

public class QDWebView extends WebView implements QuickWebViewInterface,QuickWebViewClientInterface{
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
        setInitialScale(100);//100代表不缩放
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
                    QdToast.showToast(getContext(), "选中的文字类型");
                    break;
                case HitTestResult.PHONE_TYPE: // 处理拨号
                    QdToast.showToast(getContext(), "处理拨号");
                    break;
                case HitTestResult.EMAIL_TYPE: // 处理Email
                    QdToast.showToast(getContext(), "处理Email");
                    break;
                case HitTestResult.GEO_TYPE: // 　地图类型
                    QdToast.showToast(getContext(), "地图类型");
                    break;
                case HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                    QdToast.showToast(getContext(), "超链接");
                    break;
                case HitTestResult.SRC_IMAGE_ANCHOR_TYPE: // 带有链接的图片类型
                    QdToast.showToast(getContext(), "带有链接的图片类型");
                    break;
                case HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
                    String url = result.getExtra();//获取图片
                    QdToast.showToast(getContext(), "处理长按图片的菜单项");
                    break;
                case HitTestResult.UNKNOWN_TYPE: //未知
                    QdToast.showToast(getContext(), "未知");
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
    private static ValueCallback<Uri[]> mFilePathCallback;
    private void openFileChooseProcess(String[] acceptTypes) {
        String acceptType = "*/*";
        if (acceptTypes.length > 0) {
            for (String type : acceptTypes) {
                acceptType = type;
                QDLogger.println("type="+type);
            }
        }

      /*  Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType(acceptType);
        ((Activity)getContext()).startActivityForResult(Intent.createChooser(i, "上传文件"), 0);
        */
        selectPic();
    }
    public void selectPic(){
            String[] menus = getResources().getStringArray(R.array.select_picture_items);
            new QDSheetDialog.MenuBuilder(getContext()).setData(menus).setOnDialogActionListener((dialog, position, data) -> {
                dialog.dismiss();
                if (position == 0) {
                    ((QDActivity) getContext()).getPhotoHelper().takePhoto(null, new PhotoHelper.OnTakePhotoResult() {
                        @Override
                        public void onSuccess(Intent data, String path) {
                            if (data == null) {
                                /*imageList.add(new Image(path, UrlType.file));
                                addImageToView();
                                if (onPictureChangeListener != null) {
                                    onPictureChangeListener.onChanged(imageList);
                                }*/
                            } else {
                                Bundle extras = data.getExtras();
                                if (extras != null) {
                                    /*Bitmap bitmap = extras.getParcelable("data");
                                    String filePath = QDBitmapUtil.savePhoto(bitmap, getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()+ File.separator+"temp", "header");//String.valueOf(System.currentTimeMillis())
                                    imageList.add(new Image(filePath, UrlType.file));
                                    addImageToView();
                                    if (onPictureChangeListener != null) {
                                        onPictureChangeListener.onChanged(imageList);
                                    }*/
                                }
                            }
                        }

                        @Override
                        public void onFailure(String error) {

                        }
                    });
                } else {//从相册选择
                    ((QDActivity) getContext()).getPhotoHelper().selectPhotoFromMyGallery(new PhotoHelper.OnSelectPictureResult() {
                        @Override
                        public void onSuccess(Intent data, ArrayList<Image> images) {
                            Uri[] uris = new Uri[]{images.get(0).getUri(getContext())};
                            //Uri[] uris = new Uri[]{data.getData()};
                            QDLogger.println("uris=" + JSON.toJSONString(uris));
                            /*if(data!=null) {
                                Uri uri = data.getData();
                                if (uri != null) {
                                    uris[0] = uri;
                                }
                            }*/
                            mFilePathCallback.onReceiveValue(uris);
                            /*imageList.addAll(images);
                            addImageToView();
                            if (onPictureChangeListener != null) {
                                onPictureChangeListener.onChanged(imageList);
                            }*/
                        }

                        @Override
                        public void onFailure(String error) {

                        }

                        @Override
                        public int getImageCount() {
                            return 1;
                        }
                    });
                }
            }).create().show();
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
                if (null != mFilePathCallback) {
                    Uri result = data == null ? null
                            : data.getData();
                    mFilePathCallback.onReceiveValue(new Uri[]{result});
                    mFilePathCallback = null;
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
        return false;
//        该方法会在webview加载一个url之前，拦截请求；
//        如果未使用自定义的WebViewClient，系统会要求Activity Manager去处理当前URL，通常使用系统浏览器加载；
//        如果使用了自定义的WebViewClient，返回true表示当前webview中止加载url（效果就像比如点击一个a标签链接没反应），返回false表示webview正常继续加载url；
//        不要调用loadUrl方法并且返回true，就像下面代码一样是错误的：
//————————————————
//        版权声明：本文为CSDN博主「ruiurrui」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//        原文链接：https://blog.csdn.net/u010982507/article/details/125037839
//        view.loadUrl(url);
//        return true;
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
            WebView newWebView = onWindowViewInflate.onWindowOpen(view,isDialog,isUserGesture,resultMsg);
            if(newWebView==null){
                return false;
            }
//            QDWebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
//            transport.setWebView(newWebView);
//            resultMsg.sendToTarget();
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
        WebView onWindowOpen(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg);
        void onCloseWindow(WebView window);
    }

    private void dealUrlLoading(WebView view, String url) {
        final WebView.HitTestResult result = getHitTestResult();
        if (null == result)
            return;
        int type = result.getType();
        switch (type) {
            case WebView.HitTestResult.EDIT_TEXT_TYPE: // 选中的文字类型
                QdToast.showToast(getContext(), "选中的文字类型");
                break;
            case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
                QdToast.showToast(getContext(), "处理拨号");
                break;
            case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
                QdToast.showToast(getContext(), "处理Email");
                break;
            case WebView.HitTestResult.GEO_TYPE: // 　地图类型
                QdToast.showToast(getContext(), "地图类型");
                break;
            case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                QdToast.showToast(getContext(), "超链接");
                break;
            case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE: // 带有链接的图片类型
                QdToast.showToast(getContext(), "带有链接的图片类型");
                break;
            case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
                //String url = result.getExtra();//获取图片
                QdToast.showToast(getContext(), "处理长按图片的菜单项");
                break;
            case WebView.HitTestResult.UNKNOWN_TYPE: //未知
                QdToast.showToast(getContext(), "未知");
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
        mFilePathCallback = filePathCallback;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            openFileChooseProcess(fileChooserParams.getAcceptTypes());
            return true;
        }else {
            return false;
        }
        // qdWebChromeClient.onShowFileChooser(webView, filePathCallback, fileChooserParams);
    }

    @Override
    public void onLoading(int progress) {
        displayProgressBar = true;
        mProgress = progress / 100f;
        postInvalidate();
    }

    @Override
    public void onLoadComplete() {
        mProgress = 0;
        displayProgressBar = false;
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

    private int progressHeight = 10;//进度条高度
    private boolean showProgressBar = true;//是否显示进度条
    private boolean displayProgressBar;//显示过程中才显示进度条否则不显示
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
        if (displayProgressBar && showProgressBar && mProgress != 0) {
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
