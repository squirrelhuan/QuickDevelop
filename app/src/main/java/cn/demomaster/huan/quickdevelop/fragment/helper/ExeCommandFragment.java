package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


    @BindView(R.id.btn_exe_03)
    QDButton btn_exe_03;

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
                exeCommand(((TextView)v).getText().toString());
            }
        });
        btn_exe_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "ps |grep "+getActivity().getPackageName();
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
    }

    public void initActionBarLayout(ActionBarLayout2 actionBarLayoutOld) {
        int i = (int) (Math.random() * 10 % 4);
        actionBarLayoutOld.setTitle("audio play");
        actionBarLayoutOld.setHeaderBackgroundColor(Color.RED);
    }

    private void exeCommand(String command) {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}