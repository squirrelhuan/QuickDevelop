package cn.demomaster.huan.quickdevelop.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;

public class LoadingActivity extends BaseActivityParent {

    private StateView sv_loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        getActionBarLayout().setTitle("加载动画");

        sv_loading = findViewById(R.id.sv_loading);
        sv_loading.setStateType(StateView.StateType.LOADING);
    }
}
