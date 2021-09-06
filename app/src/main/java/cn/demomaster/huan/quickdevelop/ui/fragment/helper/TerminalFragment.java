package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.util.terminal.ADBHelper;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;


/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "终端", iconRes = R.drawable.ic_terminal, resType = ResType.Resource)
public class TerminalFragment extends BaseFragment {

    @BindView(R.id.btn_send)
    QDButton btn_send;

    @BindView(R.id.et_console)
    EditText et_console;

    @BindView(R.id.scroll_01)
    ScrollView scroll_01;
    @BindView(R.id.tv_console)
    TextView tv_console;

    @BindView(R.id.tv_current_path)
    TextView tv_current_path;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_terminal, null);
        return mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        getActionBarTool().setTitle("command");

        btn_send.setOnClickListener(v -> send());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            et_console.setOnEditorActionListener((v, actionId, event) -> {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    send();
                }
                return false;
            });
        }
        //fab_clear.setOnClickListener(v -> tv_console.setText(""));
        /*btn_exe_02.setOnClickListener(v -> {
            //String str = "ps |grep " + getActivity().getPackageName();
            QDLogger.i(str);
            //exeCommand(str);
        });*/

       /* btn_exe_03.setOnClickListener(v -> {
            String str = "adb shell settings put global policy_control immersive.full=*";
            QDLogger.i(str);
            //exeCommand(str);
        });*/

        /*btn_clear.setOnClickListener(v -> {
            //tv_console.setText("");
            et_console.setText("");
        });*/

        /*TextWatcher textWatcher2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("MainActivity", "onTextChanged: "+s);
               *//* if (!TextUtils.isEmpty(et_console.getText().toString())&&s.toString().endsWith("\n")) {
                    String[] strings = et_console.getText().toString().split("\n");
                    exeCommand(strings[strings.length-1]);
                }*//*
                String str=s.toString();
                if (str.indexOf("\r")>=0 || str.indexOf("\n")>=0){//发现输入回车符或换行符
                    *//*et_console.setText(str.replace("\r","").replace("\n",""));//去掉回车符和换行符
                    //et_console.requestFocus();//让editText2获取焦点
                    et_console.setSelection(et_console.getText().length());//将光标移动到文本末尾*//*
                    Log.e("MainActivity", "发现输入回车符或换行符 ");
                    String[] strings = str.split("\n");
                    //exeCommand(strings[strings.length-1]);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("MainActivity", "afterTextChanged: "+s);

            }
        };
        et_console.addTextChangedListener(textWatcher2);*/

        /*et_console.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.e("MainActivity", "onKey: 按下回车键");
                if (keyCode == KeyEvent.KEYCODE_ENTER) {// 监听到回车键，会执行2次该方法。按下与松开
                    if (!TextUtils.isEmpty(et_console.getText().toString())) {
                       String[] strings = et_console.getText().toString().split("\n");
                        exeCommand(strings[strings.length-1]);
                    }
                    return true;
                }
                return false;
            }
        });*/

        /*QDRuntimeHelper.getInstance().addReceiver(new QDRuntimeHelper.RuntimeReceiver() {
            @Override
            public void onReceive(String data) {
               QDLogger.println("data="+data);
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        et_console.removeTextChangedListener(textWatcher2);
                        et_console.setText(et_console.getText()+"\n" + data);
                        et_console.addTextChangedListener(textWatcher2);
                        content = et_console.getText().toString();
                    }
                });
            }
        });*/

        //getActionBarTool().setActionBarType(ActionBarInterface.ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);
    }

    private void send() {
        if (!TextUtils.isEmpty(et_console.getText()) && et_console.getText().toString().contains("clear")) {
            et_console.setText("");
            tv_console.setText("");
            return;
        }
        ADBHelper.getInstance().execute(et_console.getText().toString(), result -> {
            tv_console.append("\n"+et_console.getText().toString());
            if (result.getCode() == 0) {
                tv_console.append("\n"+result.getResult());
            } else {
                tv_console.append("\n"+result.getError());
            }
            et_console.setText("");
            //scroll_01.scrollTo(0, tv_console.getHeight());
            tv_current_path.setText(">_" + ADBHelper.getInstance().currentPath);
            scroll_01.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
        });
    }

    public static String content;

    @Override
    public void onDestroy() {
        super.onDestroy();
        //QDRuntimeHelper.getInstance().removeReceiver();
    }
}