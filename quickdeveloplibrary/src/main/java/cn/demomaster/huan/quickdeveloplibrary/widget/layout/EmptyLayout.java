package cn.demomaster.huan.quickdeveloplibrary.widget.layout;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.QdLoadingView;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;

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
    LinearLayout ll_custom;
    private LayoutInflater mInflater;
    ViewGroup layout_emptyview;
    ViewGroup ll_content;
    private void init(AttributeSet attrs) {
        mInflater = LayoutInflater.from(getContext());
        layout_emptyview = (ViewGroup) mInflater.inflate(R.layout.layout_emptyview, this, false);
        ll_content = layout_emptyview.findViewById(R.id.ll_content);
        btn_retry = layout_emptyview.findViewById(R.id.btn_retry);
        it_empty_icon = layout_emptyview.findViewById(R.id.it_empty_icon);
        it_empty_title = layout_emptyview.findViewById(R.id.it_empty_title);
        it_empty_description = layout_emptyview.findViewById(R.id.it_empty_description);
        qd_loading = layout_emptyview.findViewById(R.id.qd_loading);
        ll_custom = layout_emptyview.findViewById(R.id.ll_custom);
        addView(layout_emptyview);
    }

    public void hideAll() {
        int count = ll_content.getChildCount();
        for(int i=0;i<count;i++){
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

    public void showTitle(String title) {
        it_empty_title.setText(title);
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
        if(targetView!=null) {
            targetView.setVisibility(VISIBLE);
        }
    }

    public void loadFail() {
        hideAll();
        if(targetView!=null) {
            targetView.setVisibility(INVISIBLE);
        }
    }
}
