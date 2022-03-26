package cn.demomaster.huan.quickdeveloplibrary.widget.layout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.qdrouter_library.view.ImageTextView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class EmptyLayoutLoader implements LoadViewInterface {
    ViewGroup rootLayout;
    ViewGroup ll_content;
    QDButton btn_retry;
    ImageTextView it_empty_icon, it_empty_title, it_empty_description;
    ImageView iv_empty,it_error;
    View loadingView;
    LinearLayout ll_custom;

    @Override
    public View onCreateLoadView(@NonNull @NotNull LayoutInflater mInflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,View.OnClickListener onClickListener) {
        rootLayout = (ViewGroup) mInflater.inflate(R.layout.layout_emptyview, null, false);
        ll_content = rootLayout.findViewById(R.id.ll_content);
        it_error = rootLayout.findViewById(R.id.it_error);
        btn_retry = rootLayout.findViewById(R.id.btn_retry);
        btn_retry.setOnClickListener(onClickListener);
        iv_empty = rootLayout.findViewById(R.id.iv_empty);

        it_empty_icon = rootLayout.findViewById(R.id.it_empty_icon);
        it_empty_title = rootLayout.findViewById(R.id.it_empty_title);
        it_empty_description = rootLayout.findViewById(R.id.it_empty_description);
        loadingView = rootLayout.findViewById(R.id.qd_loading);
        ll_custom = rootLayout.findViewById(R.id.ll_custom);
        //addView(layout_emptyview);
        return rootLayout;
    }

    @Override
    public ViewGroup.LayoutParams getLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
    public void hideAll() {
        int count = ll_content.getChildCount();
        for (int i = 0; i < count; i++) {
            ll_content.getChildAt(i).setVisibility(GONE);
        }
    }

    @Override
    public void onStateChanged(LoadLayout.LoadViewBuilder loadViewBuilder, int errorCode) {
        hideAll();
        if (loadViewBuilder.getLoadStateType() != LoadLayout.LoadStateType.LoadSuccess) {
            rootLayout.setVisibility(VISIBLE);
        }
        if (loadViewBuilder.isShowLoadingView()) {
            loadingView.setVisibility(VISIBLE);
        }
        if (loadViewBuilder.isShowEmptyImage()) {
            iv_empty.setVisibility(VISIBLE);
        }
        if (loadViewBuilder.isShowErrorImage()) {
            it_error.setVisibility(VISIBLE);
        }
        if (loadViewBuilder.isShowButton()) {
            btn_retry.setVisibility(VISIBLE);
            btn_retry.setText(loadViewBuilder.getButtonText());
        }
        if (loadViewBuilder.isShowTextView()) {
            it_empty_description.setVisibility(VISIBLE);
            it_empty_description.setText(loadViewBuilder.getMsgText());
        }
    }
}
