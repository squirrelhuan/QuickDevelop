package cn.demomaster.huan.quickdevelop.activity.sample.actionbar;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.base.QDBaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarState;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;

@ActivityPager(name = "ActionBarTip",preViewClass = StateView.class,resType = ResType.Custome)
public class ActionBarTipActivity extends QDBaseActivity {


    private Button btn_complete;
    private Button btn_loading;
    private Button btn_warning;
    private Button btn_error;
    private SeekBar sb_background;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar_tip);

        btn_complete = findViewById(R.id.btn_complete);
        btn_loading = findViewById(R.id.btn_loading);
        btn_warning = findViewById(R.id.btn_warning);
        btn_error = findViewById(R.id.btn_error);

        getActionBarLayoutView().setHeaderBackgroundColor(Color.TRANSPARENT);
        getActionBarLayoutView().getActionBarTip().setLoadingStateListener(new ActionBarState.OnLoadingStateListener() {
            @Override
            public void onLoading(final ActionBarState.Loading loading) {
                //TODO 处理状态
                loading.setText("处理状态");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //loading.hide();
                loading.success("加载success");
            }

        });

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActionBarLayoutView().getActionBarTip().showComplete("完成");
            }
        });
        btn_loading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActionBarLayoutView().getActionBarTip().showLoading("加载");
            }
        });
        btn_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActionBarLayoutView().getActionBarTip().showWarning("警告");
            }
        });
        btn_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActionBarLayoutView().getActionBarTip().showError("错误");
            }
        });


        sb_background = findViewById(R.id.sb_background);
        sb_background.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int max = 0xff000000+0xffffff* progress/100;
                //int w = DisplayUtil.dip2px(mContext,max* progress/100);
                //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(w,w);
                getActionBarLayoutView().getActionBarTip().setBackgroundColor(max);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_background.setProgress(50);

    }
}
