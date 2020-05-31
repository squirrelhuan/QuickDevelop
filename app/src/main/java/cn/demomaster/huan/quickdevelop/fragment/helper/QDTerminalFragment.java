package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBar;
import cn.demomaster.huan.quickdeveloplibrary.util.terminal.ADBHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;


/**
 * Squirrel桓
 * 2018/8/25 QDTerminal
 */
@ActivityPager(name = "Terminal", preViewClass = StateView.class, resType = ResType.Custome)
public class QDTerminalFragment extends QDFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    //Components
    @BindView(R.id.btn_execute)
    QDButton btn_execute;
    @BindView(R.id.tv_execute_content)
    TextView tv_execute_content;

    @BindView(R.id.et_execute_cmd)
    EditText et_execute_cmd;

    @BindView(R.id.btn_test_01)
    QDButton btn_test_01;

    View mView;

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_terminal, null);
        }
        ButterKnife.bind(this, mView);
        return (ViewGroup) mView;
    }
    ADBHelper adbHelper = ADBHelper.getInstance();

    @Override
    public void initView(View rootView, ActionBar actionBarLayoutOld) {
        actionBarLayoutOld.setTitle("终端命令");

        btn_test_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adbHelper.execute("adb shell settings put global policy_control immersive.full=*");
            }
        });

    }

}