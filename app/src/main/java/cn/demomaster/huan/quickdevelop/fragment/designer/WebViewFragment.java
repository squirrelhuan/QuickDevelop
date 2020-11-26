package cn.demomaster.huan.quickdevelop.fragment.designer;

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
import cn.demomaster.huan.quickdevelop.fragment.component.AppletsFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;


/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "WebViewFragment", preViewClass = StateView.class, resType = ResType.Custome)
public class WebViewFragment extends AppletsFragment {

    @BindView(R.id.webView)
    WebView webView;

    @Nullable
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View mView = inflater.inflate(R.layout.fragment_layout_webview, null);
        return mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this,rootView);
        setTitle("web");
        Bundle bundle = getArguments();
        if (bundle.containsKey("URL")) {
            webView.loadUrl((String) bundle.get("URL"));
        }
    }

}