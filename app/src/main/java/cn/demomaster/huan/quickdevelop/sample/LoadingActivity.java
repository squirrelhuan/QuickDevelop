package cn.demomaster.huan.quickdevelop.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;

public class LoadingActivity extends BaseActivityParent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        getActionBarLayout().setTitle("加载动画");
    }
}
