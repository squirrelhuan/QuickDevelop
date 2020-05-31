package cn.demomaster.huan.quickdevelop.fragment.designer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBar;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;


/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "WebViewFragment",preViewClass = StateView.class,resType = ResType.Custome)
public class WebViewFragment extends QDFragment {

    @BindView(R.id.webView)
    WebView webView;
    View mView;
    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_webview, null);
        }
        //绑定fragment
        ButterKnife.bind( this , mView ) ;
        return (ViewGroup) mView;
    }

    @Override
    public void initView(View rootView, ActionBar actionBarLayout) {
        actionBarLayout.setTitle("web");

        Bundle bundle = getArguments();
        if(bundle.containsKey("URL")){
            webView.loadUrl((String) bundle.get("URL"));
        }
    }

}