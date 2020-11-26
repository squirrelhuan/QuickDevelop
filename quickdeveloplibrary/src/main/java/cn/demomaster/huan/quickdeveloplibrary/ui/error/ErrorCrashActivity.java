package cn.demomaster.huan.quickdeveloplibrary.ui.error;

import android.os.Bundle;
import android.widget.TextView;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;

public class ErrorCrashActivity extends QDActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_crash);
        String error = getIntent().getStringExtra("error");
        TextView tv_content = findViewById(R.id.tv_content);
        tv_content.setText(""+error);
    }
}
