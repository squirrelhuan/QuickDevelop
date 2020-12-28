package cn.demomaster.huan.quickdevelop.ui.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.base.dialog.QdDialogActivity;

public class DialogWindowActivity extends QdDialogActivity {

    @Override
    public void generateView(LayoutInflater layoutInflater, ViewGroup viewParent) {
        layoutInflater.inflate(R.layout.activity_dialog_window, viewParent);
        Button button = viewParent.findViewById(R.id.btn_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        // 参数1：MainActivity进场动画，参数2：SecondActivity出场动画
        overridePendingTransition(0, R.anim.fade_out);
    }
}