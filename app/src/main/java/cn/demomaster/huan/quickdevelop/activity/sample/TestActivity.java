package cn.demomaster.huan.quickdevelop.activity.sample;

import androidx.appcompat.app.AppCompatActivity;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.QDBaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayoutView;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarState;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.ToggleButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

@ActivityPager(name = "ActionBar",preViewClass = ToggleButton.class,resType = ResType.Custome)
public class TestActivity extends QDBaseActivity implements View.OnClickListener {

    private ActionBarLayoutView action_bar;
    private CheckBox cb_use_background;
    private RadioGroup rg_action,rg_action_color;
    private SeekBar sb_color;

    Button btn_ac_01, btn_ac_02, btn_ac_03, btn_ac_04, btn_ac_05, btn_ac_06;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        rg_action = findViewById(R.id.rg_action);
        rg_action.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_01:
                        action_bar.setActionbarType(ActionBarLayout.ACTIONBAR_TYPE.NORMAL);
                        break;
                    case R.id.rb_02:
                        action_bar.setActionbarType(ActionBarLayout.ACTIONBAR_TYPE.NO_ACTION_BAR);
                        break;
                    case R.id.rb_03:
                        action_bar.setActionbarType(ActionBarLayout.ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);
                        break;
                    case R.id.rb_04:
                        action_bar.setActionbarType(ActionBarLayout.ACTIONBAR_TYPE.ACTION_STACK);
                        break;
                    case R.id.rb_05:
                        action_bar.setActionbarType(ActionBarLayout.ACTIONBAR_TYPE.ACTION_STACK_NO_STATUS);
                        break;
                    case R.id.rb_06:
                        action_bar.setActionbarType(ActionBarLayout.ACTIONBAR_TYPE.ACTION_TRANSPARENT);
                        break;
                }
            }
        });

        rg_action_color = findViewById(R.id.rg_action_color);
        rg_action_color.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_color_01:
                        action_bar.setHeaderBackgroundColor(Color.WHITE);
                        break;
                    case R.id.rb_color_02:
                        action_bar.setHeaderBackgroundColor(Color.GREEN);
                        break;
                    case R.id.rb_color_03:
                        action_bar.setHeaderBackgroundColor(Color.YELLOW);
                        break;
                    case R.id.rb_color_04:
                        action_bar.setHeaderBackgroundColor(Color.RED);
                        break;
                    case R.id.rb_color_05:
                        action_bar.setHeaderBackgroundColor(Color.GRAY);
                        break;
                    case R.id.rb_color_06:
                        action_bar.setHeaderBackgroundColor(Color.BLACK);
                        break;
                    case R.id.rb_color_07:
                        action_bar.setHeaderBackgroundColor(Color.TRANSPARENT);
                        break;
                }
            }
        });
        sb_color = findViewById(R.id.sb_color);
        sb_color.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int max = 0xff000000+0xffffff* progress/100;
                action_bar.setHeaderBackgroundColor(max);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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
        btn_ac_06 = findViewById(R.id.btn_ac_06);
        btn_ac_06.setOnClickListener(this);

        findViewById(R.id.btn_color_black).setOnClickListener(this);
        findViewById(R.id.btn_color_red).setOnClickListener(this);
        findViewById(R.id.btn_color_gray).setOnClickListener(this);
        findViewById(R.id.btn_color_yellow).setOnClickListener(this);
        findViewById(R.id.btn_color_green).setOnClickListener(this);
        findViewById(R.id.btn_color_white).setOnClickListener(this);

        action_bar = actionBarLayoutView;
        cb_use_background = findViewById(R.id.cb_use_background);
        cb_use_background.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    actionBarLayoutView.setHasContainBackground(isChecked);
            }
        });
        //actionBarLayoutView.getActionBarTip().setActionBarState();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_ac_01:
                action_bar.setActionbarType(ActionBarLayout.ACTIONBAR_TYPE.NORMAL);
                break;
            case R.id.btn_ac_02:
                action_bar.setActionbarType(ActionBarLayout.ACTIONBAR_TYPE.NO_ACTION_BAR);
                break;
            case R.id.btn_ac_03:
                action_bar.setActionbarType(ActionBarLayout.ACTIONBAR_TYPE.ACTION_STACK);
                break;
            case R.id.btn_ac_04:
                action_bar.setActionbarType(ActionBarLayout.ACTIONBAR_TYPE.ACTION_STACK_NO_STATUS);
                break;
            case R.id.btn_ac_05:
                action_bar.setActionbarType(ActionBarLayout.ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);
                break;
            case R.id.btn_ac_06:
                action_bar.setActionbarType(ActionBarLayout.ACTIONBAR_TYPE.ACTION_TRANSPARENT);
                break;
            case R.id.btn_color_black:
                /*getActionBarLayout().setBackGroundColor(getResources().getColor(R.color.black));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.black), getResources().getColor(R.color.white));
                showMessage("黑色主题");*/
                break;
            case R.id.btn_color_white:
               /* getActionBarLayout().setBackGroundColor(getResources().getColor(R.color.white));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.white), getResources().getColor(R.color.black));
                showMessage("白色主题");*/
                break;
            case R.id.btn_color_red:
                /*getActionBarLayout().setBackGroundColor(getResources().getColor(R.color.red));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.red), getResources().getColor(R.color.white));
                PopToastUtil.ShowToast(this, "红色主题");*/
                break;
            case R.id.btn_color_gray:
               /* getActionBarLayout().setBackGroundColor(getResources().getColor(R.color.gray));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.gray), getResources().getColor(R.color.white));
                PopToastUtil.ShowToast(this, "灰色主题");*/
                break;
            case R.id.btn_color_green:
                /*getActionBarLayout().setBackGroundColor(getResources().getColor(R.color.green));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.green), getResources().getColor(R.color.white));
                PopToastUtil.ShowToast(this, "绿色主题");*/
                break;
            case R.id.btn_color_yellow:
                /*getActionBarLayout().setBackGroundColor(getResources().getColor(R.color.yellow));
                PopToastUtil.setColorStyle(getResources().getColor(R.color.yellow), getResources().getColor(R.color.black));
                PopToastUtil.ShowToast(this, "黄色主题");*/
                break;
            case R.id.btn_change_bg:
               /* if (position == 0) {
                    ll_layout.setBackgroundResource(R.mipmap.meizi);
                    position = 1;
                } else {
                    ll_layout.setBackgroundResource(R.mipmap.gudaimeizi);
                    position = 0;
                }
                getActionBarLayout().changeChildView(ll_layout);*/
                break;
        }
    }
}
