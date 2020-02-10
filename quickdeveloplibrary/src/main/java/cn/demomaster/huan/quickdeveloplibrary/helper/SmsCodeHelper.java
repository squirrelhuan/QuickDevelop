package cn.demomaster.huan.quickdeveloplibrary.helper;


import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.demomaster.huan.quickdeveloplibrary.constant.TAG;
import cn.demomaster.huan.quickdeveloplibrary.helper.SharedPreferencesHelper;

import static cn.demomaster.huan.quickdeveloplibrary.helper.SharedPreferencesHelper.Message_Code_Last_Time;

/**
 * @author squirrel桓
 * @date 2018/11/8.
 * description：
 */
public class SmsCodeHelper  {

    //1.手机号，2button,3默认text,4等待text,5,onReciveSmsCode,6time

    private String telephone;
    private TextView btn_getSmsCode;
    private int time = 60;
    private SmsCodeHelper.OnSmsCodeListener onReceiveSmsCodeListener;
    private Context context;
    private String btn_str;

    public SmsCodeHelper(String telephone, TextView btn_getSmsCode, SmsCodeHelper.OnSmsCodeListener onReceiveSmsCodeListener, int time) {
        this.telephone = telephone;
        this.btn_getSmsCode = btn_getSmsCode;
        this.onReceiveSmsCodeListener = onReceiveSmsCodeListener;
        this.time = time;
        this.context = btn_getSmsCode.getContext();
        this.btn_str = btn_getSmsCode.getText().toString();
    }

    public void start(){
        //初始化
        handler.removeCallbacks(runnable);
        handler.post(runnable);
    }
    //@TODO must use this method when you get SmsCode success
    public void onReceiveSmsCodeSuccess(String tip){
        onVerifyCodeSuccess();
        onReceiveSmsCodeListener.onReceiveSuccess(tip);
    }
    //@TODO must use this method when you get SmsCode failure
    public void onReceiveSmsCodeFailure(String error){
        onVerifyCodeFailure(error);
        onReceiveSmsCodeListener.onReceiveFailure(error);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            validateCode();
        }
    };
    private void validateCode() {
        long last = SharedPreferencesHelper.getInstance().getLong(Message_Code_Last_Time, 0);
        long now = System.currentTimeMillis();
        long diff = getSecond(now - last);
        if (diff > time) {
            btn_getSmsCode.setText(btn_str);
            btn_getSmsCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //返回校验结果
                    if(onReceiveSmsCodeListener.onNextHttpGet()){
                        //onReceiveSmsCodeListener.onNextHttpGet();
                        btn_getSmsCode.setOnClickListener(null);
                    }else {
                        Log.i(TAG.APP,"手机号不合法");
                        onReceiveSmsCodeListener.onReceiveFailure("手机号不合法");
                    }
                }
            });
            handler.removeCallbacks(runnable);
        } else {
            btn_getSmsCode.setOnClickListener(null);
            onReceiveSmsCodeListener.onTimeChange(time - diff );
            handler.postDelayed(runnable, 1000);
        }
    }


    public void onVerifyCodeSuccess() {
        //ToastHelper.showToast(RegisterActivity.this, "验证码发送成功，请等待", Toast.LENGTH_LONG);
        SharedPreferencesHelper.getInstance().setLong(Message_Code_Last_Time, System.currentTimeMillis());
        handler.postDelayed(runnable, 1000);
    }

    public void onVerifyCodeFailure(String message) {
        btn_getSmsCode.setOnClickListener(null);
        validateCode();
        //ToastHelper.showToast(this, "验证码获取失败" + message, Toast.LENGTH_LONG);
    }


    //时间戳转字符串
    public static long getSecond(long diff) {
        //以秒为单位
        Long second = (diff / 1000);
        return second;
    }

    public static interface OnSmsCodeListener{
        //读秒状态不可点击
        void onTimeChange(long time);
        //可以发起网络请求获取短信验证码 返回是否发起请求了
        boolean onNextHttpGet();// btn_getSmsCode.setOnClickListener(null);
        //成功接收到短信验证码 （网络请求）
        void onReceiveSuccess(String tip);
        //接收到短信验证码失败 （网络请求）
        void onReceiveFailure(String error);
    }
    public static interface OnHttpResult{
       void onSuccess();
        void onFail();
    }

    public static class Builder{
        private String telephone;
        private TextView btn_getSmsCode;
        private SmsCodeHelper.OnSmsCodeListener onReceiveSmsCodeListener;
        private int time = 60;
        private SmsCodeHelper smsCodeHelper;

        public Builder(String telephone, TextView btn_getSmsCode, SmsCodeHelper.OnSmsCodeListener onReceiveSmsCodeListener) {
            this.telephone = telephone;
            this.btn_getSmsCode = btn_getSmsCode;
            this.onReceiveSmsCodeListener = onReceiveSmsCodeListener;
        }
        public SmsCodeHelper.Builder setTime(int time){
            this.time = time;
            return this;
        }
        public SmsCodeHelper create(){
            smsCodeHelper = new SmsCodeHelper(telephone,btn_getSmsCode,onReceiveSmsCodeListener,time);
            return smsCodeHelper;
        }
    }

    /**
     * 每次都默认可以点击
     */
    public void firstStart(){
        if(handler!=null){
            handler.removeCallbacks(runnable);
        }
        SharedPreferencesHelper.getInstance().setLong(Message_Code_Last_Time, 0);
        start();
    }

    public void unRegisterSmsListener(){
        onReceiveSmsCodeListener=null;
        this.context = null;
        if(handler!=null){
            handler.removeCallbacks(runnable);
        }
        handler=null;
    }

}