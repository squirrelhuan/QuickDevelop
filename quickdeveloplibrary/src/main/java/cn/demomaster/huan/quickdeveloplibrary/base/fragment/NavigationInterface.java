package cn.demomaster.huan.quickdeveloplibrary.base.fragment;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public interface NavigationInterface {
    void navigate(FragmentActivity context, QDFragment fragment, int containerViewId, Intent intent);
    void navigateForResult(FragmentActivity context, ViewLifecycle qdFragmentInterface, QDFragment fragment, int containerViewId, Intent intent, int requestCode);
}
