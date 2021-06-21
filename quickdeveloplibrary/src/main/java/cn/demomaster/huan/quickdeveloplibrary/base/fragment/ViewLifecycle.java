package cn.demomaster.huan.quickdeveloplibrary.base.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDBaseInterface;

/**
 * @author squirrel桓
 * @date 2018/12/7.
 * description：
 */
public interface ViewLifecycle extends QDBaseInterface {
    View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    //   ViewGroup getContentView(LayoutInflater inflater);
    void initView(View rootView);

    boolean onKeyDown(int keyCode, KeyEvent event);

    //void back();
    void setFragmentHelper(FragmentHelper fragmentHelper);

    void setIntent(Intent intent);

    void setRequestCode(ViewLifecycle qdFragmentInterface, int requestCode);

    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

    void doResume();

    void onFragmentResume();//为了解决 多个fragment存在时，activity的resume事件，影响到所有fragment的resume触发

    void doPause();
}
