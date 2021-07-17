package cn.demomaster.huan.quickdevelop.ui.activity.sample.actionbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.ToggleButton;
import cn.demomaster.qdrouter_library.actionbar.ACTIONBAR_TYPE;

@ActivityPager(name = "ActionBar",preViewClass = ToggleButton.class,resType = ResType.Custome)
public class ActionBarActivity extends QDActivity implements View.OnClickListener {

    private LinearLayout ll_root;
    Button btn_ac_01, btn_ac_02, btn_ac_03, btn_ac_04, btn_ac_05;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar);

        btn_ac_01 = findViewById(R.id.btn_ac_01);
        btn_ac_01.setOnClickListener(this);

        btn_ac_02 = findViewById(R.id.btn_ac_02);
        btn_ac_02.setOnClickListener(this);

        btn_ac_03 = findViewById(R.id.btn_ac_03);
        btn_ac_03.setOnClickListener(this);
        btn_ac_04 = findViewById(R.id.btn_ac_04);
        btn_ac_04.setOnClickListener(this);
        btn_ac_05 = findViewById(R.id.btn_ac_05);
        btn_ac_05.setOnClickListener(this);


        findViewById(R.id.btn_color_black).setOnClickListener(this);
        findViewById(R.id.btn_color_red).setOnClickListener(this);
        findViewById(R.id.btn_color_gray).setOnClickListener(this);
        findViewById(R.id.btn_color_yellow).setOnClickListener(this);
        findViewById(R.id.btn_color_green).setOnClickListener(this);
        findViewById(R.id.btn_color_white).setOnClickListener(this);
//        findViewById(R.id.btn_change_bg).setOnClickListener(this);


        ll_root = findViewById(R.id.ll_root);
        SeekBar sb_background = findViewById(R.id.sb_background);
        sb_background.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int max = 0xff000000+0xffffff* progress/100;
                //int w = DisplayUtil.dip2px(mContext,max* progress/100);
                //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(w,w);
                ll_root.setBackgroundColor(max);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_background.setProgress(50);

        getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);

        //getActionBarLayout2();
/*
        action_bar = findViewById(R.id.action_bar);
        findViewById(R.id.btn_NORMAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action_bar.setActionbarType(ActionBarLayoutView.ACTIONBAR_TYPE.NORMAL);
                PopToastUtil.ShowToast(mContext,"NORMAL");
            }
        });
        findViewById(R.id.btn_NO_ACTION_BAR).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action_bar.setActionbarType(ActionBarLayoutView.ACTIONBAR_TYPE.NO_ACTION_BAR);
                PopToastUtil.ShowToast(mContext,"NO_ACTION_BAR");
            }
        });
        findViewById(R.id.btn_ACTION_STACK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action_bar.setActionbarType(ActionBarLayoutView.ACTIONBAR_TYPE.ACTION_STACK);
                PopToastUtil.ShowToast(mContext,"ACTION_STACK");
            }
        });
        findViewById(R.id.btn_ACTION_STACK_NO_STATUS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action_bar.setActionbarType(ActionBarLayoutView.ACTIONBAR_TYPE.ACTION_STACK_NO_STATUS);
                PopToastUtil.ShowToast(mContext,"ACTION_STACK_NO_STATUS");
            }
        });*/
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ac_01:
                btn_ac_01.setText(ACTIONBAR_TYPE.NORMAL+"");
                getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NORMAL);
                break;

            case R.id.btn_ac_02:
                btn_ac_02.setText(ACTIONBAR_TYPE.NO_ACTION_BAR+"");
                getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NO_ACTION_BAR);
                break;

            case R.id.btn_ac_03:
                btn_ac_03.setText(ACTIONBAR_TYPE.ACTION_STACK+"");
                getActionBarTool().setActionBarType(ACTIONBAR_TYPE.ACTION_STACK);
                break;
            case R.id.btn_ac_04:
                btn_ac_04.setText(ACTIONBAR_TYPE.ACTION_STACK_NO_STATUS+"");
                getActionBarTool().setActionBarType(ACTIONBAR_TYPE.ACTION_STACK_NO_STATUS);
                break;
            case R.id.btn_ac_05:
                btn_ac_05.setText(ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS+"");
                getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);
                break;
            case R.id.btn_color_black:
                getActionBarTool().getActionBarTool().setBackgroundColor(getResources().getColor(R.color.black));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.black), getResources().getColor(R.color.white));
                showMessage("黑色主题");
                break;
            case R.id.btn_color_white:
                getActionBarTool().getActionBarTool().setBackgroundColor(getResources().getColor(R.color.white));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.white), getResources().getColor(R.color.black));
                showMessage("白色主题");
                break;
            case R.id.btn_color_red:
                getActionBarTool().getActionBarTool().setBackgroundColor(getResources().getColor(R.color.red));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.red), getResources().getColor(R.color.white));
                PopToastUtil.showToast(this, "红色主题");
                break;
            case R.id.btn_color_gray:
                getActionBarTool().getActionBarTool().setBackgroundColor(getResources().getColor(R.color.gray));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.gray), getResources().getColor(R.color.white));
                PopToastUtil.showToast(this, "灰色主题");
                break;
            case R.id.btn_color_green:
                getActionBarTool().getActionBarTool().setBackgroundColor(getResources().getColor(R.color.green));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.green), getResources().getColor(R.color.white));
                PopToastUtil.showToast(this, "绿色主题");
                break;
            case R.id.btn_color_yellow:
                getActionBarTool().getActionBarTool().setBackgroundColor(getResources().getColor(R.color.yellow));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.yellow), getResources().getColor(R.color.black));
                PopToastUtil.showToast(this, "黄色主题");
                break;
        }
    }

}
