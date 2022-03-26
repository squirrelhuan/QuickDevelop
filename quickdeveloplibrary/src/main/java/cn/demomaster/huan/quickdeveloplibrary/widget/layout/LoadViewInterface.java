package cn.demomaster.huan.quickdeveloplibrary.widget.layout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface LoadViewInterface {
    View onCreateLoadView(@NonNull LayoutInflater inflater, @Nullable ViewGroup containerView, View.OnClickListener onClickListener);
    ViewGroup.LayoutParams getLayoutParams();
    void onStateChanged(LoadLayout.LoadViewBuilder loadViewBuilder, int errorCode);
}
