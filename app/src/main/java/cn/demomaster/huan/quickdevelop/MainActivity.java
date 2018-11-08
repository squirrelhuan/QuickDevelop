package cn.demomaster.huan.quickdevelop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.widget.loader.LoadingDialog;

public class MainActivity extends BaseActivityParent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
       LoadingDialog.Builder builder = new LoadingDialog.Builder(this);
       builder.setMessage("加载中...").setCanTouch(false).create().show();
    }
}
