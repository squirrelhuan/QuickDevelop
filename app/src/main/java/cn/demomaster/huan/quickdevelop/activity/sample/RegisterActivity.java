package cn.demomaster.huan.quickdevelop.activity.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDTimer;
import cn.demomaster.huan.quickdeveloplibrary.util.StringVerifyUtil;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }
    QDTimer qdTimer;
    private void initView() {
        final EditText et_phone = findViewById(R.id.et_phone);
        final Button btn_get_smscode = findViewById(R.id.btn_get_smscode);
        qdTimer = new QDTimer(60,new QDTimer.OnTimerListener() {
            @Override
            public void onTimeChange(long time) {
                btn_get_smscode.setText("剩余"+time);
            }
        });
        btn_get_smscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean b = StringVerifyUtil.validateMobilePhone(et_phone.getText().toString());
                if(b){
                    Toast.makeText(RegisterActivity.this,"net获取验证码...",Toast.LENGTH_LONG).show();
                    qdTimer.stop();

                }else {
                    Toast.makeText(RegisterActivity.this,"手机号格式有误",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
