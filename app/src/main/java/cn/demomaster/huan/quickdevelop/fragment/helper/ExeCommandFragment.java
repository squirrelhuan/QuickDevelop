package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout2;
import cn.demomaster.huan.quickdeveloplibrary.jni.Watcher;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;

import static cn.demomaster.huan.quickdeveloplibrary.constant.EventBusConstant.EVENT_REFRESH_LANGUAGE;
import static cn.demomaster.huan.quickdeveloplibrary.util.system.QDLanguageUtil.changeAppLanguage;
import static cn.demomaster.huan.quickdeveloplibrary.util.system.QDLanguageUtil.setLanguageLocal;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "ExeCommand", preViewClass = StateView.class, resType = ResType.Custome)
public class ExeCommandFragment extends QDBaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    //Components
    @BindView(R.id.btn_exe_01)
    QDButton btn_exe_01;
    @BindView(R.id.btn_exe_02)
    QDButton btn_exe_02;
    View mView;

    @BindView(R.id.btn_clear)
    QDButton btn_clear;
/*
    @BindView(R.id.et_command)
    EditText et_command;*/

    @BindView(R.id.btn_exe_03)
    QDButton btn_exe_03;


    //@BindView(R.id.ll_console)
    //LinearLayout ll_console;
    /*@BindView(R.id.tv_console)
    TextView tv_console;*/

    @BindView(R.id.et_console)
    EditText et_console;


    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_execommand, null);
        }
        ButterKnife.bind(this, mView);
        return (ViewGroup) mView;
    }

    @Override
    public void initView(View rootView, ActionBarInterface actionBarLayoutOld) {
        actionBarLayoutOld.setTitle("command");

        btn_exe_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exeCommand(((TextView) v).getText().toString());
            }
        });
        btn_exe_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "ps |grep " + getActivity().getPackageName();
                QDLogger.i(str);
                exeCommand(str);
            }
        });

        btn_exe_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "adb shell settings put global policy_control immersive.full=*";
                QDLogger.i(str);
                exeCommand(str);
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tv_console.setText("");
                et_console.setText("");
            }
        });
       /* tv_console.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });*/

        TextWatcher textWatcher2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(et_console.getText().toString())&&s.toString().endsWith("\n")) {
                    String[] strings = et_console.getText().toString().split("\n");
                    exeCommand(strings[strings.length-1]);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        et_console.addTextChangedListener(textWatcher2);

        et_console.setOnKeyListener(new View.OnKeyListener() {
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
        });
    }


    public void initActionBarLayout(ActionBarLayout2 actionBarLayoutOld) {
        int i = (int) (Math.random() * 10 % 4);
        actionBarLayoutOld.setTitle("audio play");
        actionBarLayoutOld.setHeaderBackgroundColor(Color.RED);
    }

    private void exeCommand(String command) {
        et_console.setText(et_console.getText()+"\n" + command);
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                //System.out.println(line);
                et_console.setText(et_console.getText()+"\n" + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}