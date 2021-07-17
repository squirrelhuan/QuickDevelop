package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;


/**
 * Squirrel桓
 * 2018/8/25 QDTerminal
 */
@ActivityPager(name = "终端", preViewClass = StateView.class, resType = ResType.Custome)
public class QDTerminalFragment extends BaseFragment {

    @BindView(R.id.btn_execute)
    QDButton btn_execute;
    @BindView(R.id.tv_execute_content)
    TextView tv_execute_content;

    @BindView(R.id.et_execute_cmd)
    EditText et_execute_cmd;

    @BindView(R.id.btn_test_01)
    QDButton btn_test_01;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_terminal, null);
        return (ViewGroup) mView;
    }

    ADBHelper adbHelper = ADBHelper.getInstance();

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        getActionBarTool().setTitle("终端命令");

        btn_test_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adbHelper.execute("adb shell settings put global policy_control immersive.full=*");
            }
        });

    }

}