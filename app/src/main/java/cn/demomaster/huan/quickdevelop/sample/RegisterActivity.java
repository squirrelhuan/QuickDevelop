package cn.demomaster.huan.quickdevelop.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.util.StringVerifyUtil;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }
    SmsCodeHelper smsCodeHelper;
    private void initView() {
        final EditText et_phone = findViewById(R.id.et_phone);
        final Button btn_get_smscode = findViewById(R.id.btn_get_smscode);


         SmsCodeHelper.Builder builder = new SmsCodeHelper.Builder(et_phone.getText().toString(), btn_get_smscode, new SmsCodeHelper.OnSmsCodeListener() {
            @Override
            public void onTimeChange(long time) {
                btn_get_smscode.setText("剩余"+time);
            }

            @Override
            public boolean onNextHttpGet() {
                Boolean b = StringVerifyUtil.validateMobilePhone(et_phone.getText().toString());
                if(b){
                    Toast.makeText(RegisterActivity.this,"net获取验证码...",Toast.LENGTH_LONG).show();
                    smsCodeHelper.onReceiveSmsCodeSuccess("模拟获取成功");

                }else {
                    Toast.makeText(RegisterActivity.this,"手机号格式有误",Toast.LENGTH_LONG).show();
                }
                return b;
            }

            @Override
            public void onReceiveSuccess(String tip) {
                Toast.makeText(RegisterActivity.this,"success："+tip,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onReceiveFailure(String error) {
                Toast.makeText(RegisterActivity.this,"fail："+error,Toast.LENGTH_LONG).show();
            }
        });
        smsCodeHelper = builder.create();
        smsCodeHelper.start();
    }
}
