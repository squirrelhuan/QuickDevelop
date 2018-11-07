package cn.demomaster.huan.quickdevelop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;

public class MainActivity extends BaseActivityParent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
