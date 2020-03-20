package cn.demomaster.huan.quickdevelop.activity.sample.actionbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarState;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarTip;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.EmoticonView;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadStateType;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;


@ActivityPager(name = "ActionBarTip", preViewClass = TextView.class, resType = ResType.Custome)
public class ActionBarTipActivity extends QDActivity {

    private Button btn_complete;
    private Button btn_loading;
    private Button btn_warning;
    private Button btn_error;
    private SeekBar sb_background;
    private CheckBox cb_001;

    @BindView(R.id.ev_emtion)
    EmoticonView emoticonView;
    @BindView(R.id.btn_emui_halo)
    Button btn_emui_halo;
    @BindView(R.id.btn_emui_sad)
    Button btn_emui_sad;
    @BindView(R.id.btn_emui_sluggish)
    Button btn_emui_sluggish;
    @BindView(R.id.btn_emui_smile)
    Button btn_emui_smile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar_tip);
        ButterKnife.bind(this);
        btn_emui_halo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emoticonView.setStateType(LoadStateType.LOADING);
            }
        });
        btn_emui_sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emoticonView.setStateType(LoadStateType.ERROR);
            }
        });
        btn_emui_sluggish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emoticonView.setStateType(LoadStateType.WARNING);
            }
        });
        btn_emui_smile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emoticonView.setStateType(LoadStateType.COMPLETE);
            }
        });

        btn_complete = findViewById(R.id.btn_complete);
        btn_loading = findViewById(R.id.btn_loading);
        btn_warning = findViewById(R.id.btn_warning);
        btn_error = findViewById(R.id.btn_error);
        cb_001 = findViewById(R.id.cb_001);
        cb_001.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getActionBarLayout().setActionBarTipType(isChecked ? ActionBarTip.ACTIONBARTIP_TYPE.NORMAL : ActionBarTip.ACTIONBARTIP_TYPE.STACK);
            }
        });

        getActionBarLayout().setHeaderBackgroundColor(Color.TRANSPARENT);
        getActionBarLayout().getActionBarTip().setLoadingStateListener(new ActionBarState.OnLoadingStateListener() {
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
                getActionBarLayout().getActionBarTip().showComplete("完成");
            }
        });
        btn_loading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActionBarLayout().getActionBarTip().showLoading("加载");
            }
        });
        btn_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActionBarLayout().getActionBarTip().showWarning("警告");
            }
        });
        btn_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActionBarLayout().getActionBarTip().showError("错误");
            }
        });


        sb_background = findViewById(R.id.sb_background);
        sb_background.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int max = 0xff000000 + 0xffffff * progress / 100;
                //int w = DisplayUtil.dip2px(mContext,max* progress/100);
                //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(w,w);
                getActionBarLayout().getActionBarTip().setBackgroundColor(max);
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
