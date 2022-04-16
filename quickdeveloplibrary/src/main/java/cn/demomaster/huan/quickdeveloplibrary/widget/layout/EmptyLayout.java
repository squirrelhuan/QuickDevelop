package cn.demomaster.huan.quickdeveloplibrary.widget.layout;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.QdLoadingView;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.qdrouter_library.view.ImageTextView;

public class EmptyLayout extends FrameLayout {
    public EmptyLayout(@NonNull Context context) {
        super(context);
        init(null);
    }

    public EmptyLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmptyLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EmptyLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private View targetView;
    public void setTargetView(View targetView) {
        this.targetView = targetView;
    }

    public QDButton btn_retry;
    ImageTextView it_empty_icon, it_empty_title, it_empty_description;
    QdLoadingView qd_loading;
    ViewGroup layout_emptyview;
    ViewGroup ll_content;
    private void init(AttributeSet attrs) {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        layout_emptyview = (ViewGroup) mInflater.inflate(R.layout.layout_emptyview, this, false);
        ll_content = layout_emptyview.findViewById(R.id.ll_content);
        btn_retry = layout_emptyview.findViewById(R.id.btn_retry);
        it_empty_icon = layout_emptyview.findViewById(R.id.it_empty_icon);
        it_empty_title = layout_emptyview.findViewById(R.id.it_empty_title);
        it_empty_description = layout_emptyview.findViewById(R.id.it_empty_description);
        qd_loading = layout_emptyview.findViewById(R.id.qd_loading);
        addView(layout_emptyview);
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(btn_retry!=null){
            btn_retry.setOnClickListener(null);
        }
        btn_retry = null;
        it_empty_icon = null;
        it_empty_title = null;
        it_empty_description = null;
        qd_loading = null;
        layout_emptyview = null;
        ll_content = null;
        targetView = null;
    }
    
    public void hideAll() {
        int count = ll_content.getChildCount();
        for (int i = 0; i < count; i++) {
            ll_content.getChildAt(i).setVisibility(GONE);
        }
    }

    public void showRetry(String str) {
        btn_retry.setText(str);
        showRetry();
    }

    public void showRetry() {
        btn_retry.setVisibility(VISIBLE);
    }
    String mRetryText;//重试按钮文字
    public void setRetryText(String retryText) {
        this.mRetryText = retryText;
        if(btn_retry!=null) {
            btn_retry.setText(mRetryText);
        }
    }

    String mTitle;
    public void showTitle(String title) {
        this.mTitle = title;
        it_empty_title.setText(mTitle);
        it_empty_title.setVisibility(VISIBLE);
    }

    public void showTitle() {
        it_empty_title.setVisibility(VISIBLE);
    }

    public void showMessage(String message) {
        it_empty_description.setText(message);
        it_empty_description.setVisibility(VISIBLE);
    }

    public void showMessage() {
        it_empty_description.setVisibility(VISIBLE);
    }

    public void showLodding() {
        qd_loading.setVisibility(VISIBLE);
    }

    public void loadSuccess() {
        hideAll();
        if (targetView != null) {
            targetView.setVisibility(VISIBLE);
        }
    }

    public void loadFail() {
        hideAll();
        if (targetView != null) {
            targetView.setVisibility(INVISIBLE);
        }
    }
}
