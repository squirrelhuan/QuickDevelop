package cn.demomaster.huan.quickdevelop.ui.fragment.designer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.AppletsFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.view.webview.QDWebChromeClient;
import cn.demomaster.huan.quickdeveloplibrary.view.webview.QDWebView;
import cn.demomaster.qdrouter_library.base.activity.QuickActivity;
import cn.demomaster.qdrouter_library.view.ImageTextView;


/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "Web", preViewClass = StateView.class, resType = ResType.Custome)
public class WebViewFragment extends AppletsFragment {

    @BindView(R.id.webView)
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
        ButterKnife.bind(this,rootView);
        ImageTextView rightView = findViewById(R.id.it_actionbar_menu);
        if(rightView!=null){
            rightView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webView.loadUrl(url);
                }
            });
        }
        Bundle bundle = getArguments();
        if (bundle.containsKey("URL")) {
            webView.setOnStateChangedListener(new QDWebChromeClient.OnStateChangedListener() {
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

                @Override
                public boolean onNewTab(WebView view, String url) {
                    return false;
                }
            });
            url = (String) bundle.get("URL");
            webView.loadUrl(url);
        }
    }

}