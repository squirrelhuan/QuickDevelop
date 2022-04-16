package cn.demomaster.huan.quickdeveloplibrary.ui.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.view.webview.QDWebView;
import cn.demomaster.huan.quickdeveloplibrary.view.webview.QuickWebChromeClient;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.fragment.AppletsFragment;
import cn.demomaster.qdrouter_library.view.ImageTextView;


/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "Web", preViewClass = StateView.class, resType = ResType.Custome)
public class WebViewFragment extends AppletsFragment {
    QDWebView webView;

    @Nullable
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View mView = inflater.inflate(R.layout.fragment_layout_webview, null);
        return mView;
    }
    String url;
    public void initView(View rootView) {
        webView = findViewById(R.id.webView);
        ImageTextView rightView = findViewById(R.id.it_actionbar_menu);
        if(rightView!=null){
            rightView.setOnClickListener(v -> webView.loadUrl(url));
        }
        Bundle bundle = getArguments();
        if (bundle!=null&&bundle.containsKey("URL")) {
            webView.setOnStateChangedListener(new QuickWebChromeClient.OnStateChangedListener() {
                @Override
                public void onProgress(int progress) {

                }

                @Override
                public void onFinish() {

                }

                @Override
                public void onReceivedTitle(WebView view, String title) {
                    setTitle(title);
                }

            });
            url = (String) bundle.get("URL");
            webView.loadUrl(url);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//默认只处理回退事件
            if (webView!=null&&webView.canGoBack()){
                webView.goBack();
                return true;
            }
            //当返回true时，表示已经完整地处理了这个事件，并不希望其他的回调方法再次进行处理，而当返回false时，表示并没有完全处理完该事件，更希望其他回调方法继续对其进行处理
        }
        QDLogger.e("onKeyDown false");
        return super.onKeyDown(keyCode,event);
    }
}