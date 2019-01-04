package cn.demomaster.huan.quickdevelop.activity.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.DialogHelper;

public class QDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qdialog);
        findViewById(R.id.btn_common_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessagePositiveDialog();
            }
        });
    }

    private void showMessagePositiveDialog() {
        DialogHelper.showDialog(this,"title","message");
    }



}
