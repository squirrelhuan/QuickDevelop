package cn.demomaster.huan.quickdevelop.ui.activity.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.activity.BaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.qdrouter_library.actionbar.ACTIONBAR_TYPE;
import cn.demomaster.quicksticker_annotations.BindView;

@ActivityPager(name = "导航栏", preViewClass = TextView.class, resType = ResType.Custome)
public class ActionBarActivity extends BaseActivity implements View.OnClickListener {

    private RadioGroup rg_action, rg_action_color;
    private SeekBar sb_color;

    @BindView(R.id.btn_ac_01)
    Button btn_ac_01;
    @BindView(R.id.btn_ac_02)
    Button btn_ac_02;
    @BindView(R.id.btn_ac_03)
    Button btn_ac_03;
    @BindView(R.id.btn_ac_04)
    Button btn_ac_04;
    @BindView(R.id.btn_ac_05)
    Button btn_ac_05;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        rg_action = findViewById(R.id.rg_action);
        rg_action.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_01:
                        ((RadioButton) findViewById(R.id.rb_01)).setText(ACTIONBAR_TYPE.NORMAL+"");
                        getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NORMAL);
                        break;
                    case R.id.rb_02:
                        ((RadioButton) findViewById(checkedId)).setText(ACTIONBAR_TYPE.NO_ACTION_BAR+"");
                        getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NO_ACTION_BAR);
                        break;
                    case R.id.rb_03:
                        ((RadioButton) findViewById(checkedId)).setText(ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS+"");
                        getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);
                        break;
                    case R.id.rb_04:
                        ((RadioButton) findViewById(checkedId)).setText(ACTIONBAR_TYPE.ACTION_STACK+"");
                        getActionBarTool().setActionBarType(ACTIONBAR_TYPE.ACTION_STACK);
                        break;
                    case R.id.rb_05:
                        ((RadioButton) findViewById(checkedId)).setText(ACTIONBAR_TYPE.ACTION_STACK_NO_STATUS+"");
                        getActionBarTool().setActionBarType(ACTIONBAR_TYPE.ACTION_STACK_NO_STATUS);
                        break;
                    case R.id.rb_07:
                        ((RadioButton) findViewById(checkedId)).setText(ACTIONBAR_TYPE.NO_STATUS+"");
                        getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NO_STATUS);
                        break;
                }
            }
        });

        rg_action_color = findViewById(R.id.rg_action_color);
        rg_action_color.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_color_01:
                        getActionBarTool().setHeaderBackgroundColor(Color.WHITE);
                        break;
                    case R.id.rb_color_02:
                        getActionBarTool().setHeaderBackgroundColor(Color.GREEN);
                        break;
                    case R.id.rb_color_03:
                        getActionBarTool().setHeaderBackgroundColor(Color.YELLOW);
                        break;
                    case R.id.rb_color_04:
                        getActionBarTool().setHeaderBackgroundColor(Color.RED);
                        break;
                    case R.id.rb_color_05:
                        getActionBarTool().setHeaderBackgroundColor(Color.GRAY);
                        break;
                    case R.id.rb_color_06:
                        getActionBarTool().setHeaderBackgroundColor(Color.BLACK);
                        break;
                    case R.id.rb_color_07:
                        getActionBarTool().setHeaderBackgroundColor(Color.TRANSPARENT);
                        break;
                }
            }
        });
        sb_color = findViewById(R.id.sb_color);
        sb_color.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int max = 0xff000000 + 0xffffff * progress / 100;
                getActionBarTool().setHeaderBackgroundColor(max);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btn_ac_01.setOnClickListener(this);

        btn_ac_02.setOnClickListener(this);

        btn_ac_03.setOnClickListener(this);
        btn_ac_04.setOnClickListener(this);
        btn_ac_05.setOnClickListener(this);

        findViewById(R.id.btn_color_black).setOnClickListener(this);
        findViewById(R.id.btn_color_red).setOnClickListener(this);
        findViewById(R.id.btn_color_gray).setOnClickListener(this);
        findViewById(R.id.btn_color_yellow).setOnClickListener(this);
        findViewById(R.id.btn_color_green).setOnClickListener(this);
        findViewById(R.id.btn_color_white).setOnClickListener(this);

        CheckBox cb_use_background = findViewById(R.id.cb_use_background);
        cb_use_background.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               // getActionBarTool().setHasContainBackground(isChecked);
            }
        });
        //getActionBarTool().getActionBarTip().setActionBarState();
        setTitle("导航栏");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ac_01:
                getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NORMAL);
                break;
            case R.id.btn_ac_02:
                getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NO_ACTION_BAR);
                break;
            case R.id.btn_ac_03:
                getActionBarTool().setActionBarType(ACTIONBAR_TYPE.ACTION_STACK);
                break;
            case R.id.btn_ac_04:
                getActionBarTool().setActionBarType(ACTIONBAR_TYPE.ACTION_STACK_NO_STATUS);
                break;
            case R.id.btn_ac_05:
                getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);
                break;
            case R.id.btn_color_black:
                /*getActionBarToolOld().setBackGroundColor(getResources().getColor(R.color.black));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.black), getResources().getColor(R.color.white));
                showMessage("黑色主题");*/
                break;
            case R.id.btn_color_white:
               /* getActionBarToolOld().setBackGroundColor(getResources().getColor(R.color.white));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.white), getResources().getColor(R.color.black));
                showMessage("白色主题");*/
                break;
            case R.id.btn_color_red:
                /*getActionBarToolOld().setBackGroundColor(getResources().getColor(R.color.red));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.red), getResources().getColor(R.color.white));
                PopToastUtil.ShowToast(this, "红色主题");*/
                break;
            case R.id.btn_color_gray:
               /* getActionBarToolOld().setBackGroundColor(getResources().getColor(R.color.gray));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.gray), getResources().getColor(R.color.white));
                PopToastUtil.ShowToast(this, "灰色主题");*/
                break;
            case R.id.btn_color_green:
                /*getActionBarToolOld().setBackGroundColor(getResources().getColor(R.color.green));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.green), getResources().getColor(R.color.white));
                PopToastUtil.ShowToast(this, "绿色主题");*/
                break;
            case R.id.btn_color_yellow:
                /*getActionBarToolOld().setBackGroundColor(getResources().getColor(R.color.yellow));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.yellow), getResources().getColor(R.color.black));
                PopToastUtil.ShowToast(this, "黄色主题");*/
                break;
        }
    }
}
