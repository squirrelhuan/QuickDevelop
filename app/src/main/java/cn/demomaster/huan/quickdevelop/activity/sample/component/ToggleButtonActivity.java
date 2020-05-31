package cn.demomaster.huan.quickdevelop.activity.sample.component;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.ToggleButton;

@ActivityPager(name = "ToggleButton",preViewClass = ToggleButton.class,resType = ResType.Custome)
public class ToggleButtonActivity extends QDActivity {

    private SeekBar sb_weight;
    private ToggleButton tooglebutton;
    private SeekBar sb_progress_color;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toggle_button);

        tooglebutton = findViewById(R.id.tooglebutton);
        sb_weight = findViewById(R.id.sb_weight);
        sb_weight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int max = 200;
                int w = DisplayUtil.dip2px(mContext,max* progress/100);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(w,w/2);
                tooglebutton.setLayoutParams(layoutParams);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_weight.setProgress(50);

        tooglebutton.setToogleColor(Color.YELLOW);
        sb_progress_color = findViewById(R.id.sb_progress_color);
        sb_progress_color.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                int max = 0xff000000+0xffffff* progress/100;
                //int w = DisplayUtil.dip2px(mContext,max* progress/100);
                //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(w,w);
                tooglebutton.setToogleColor(max);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_progress_color.setProgress(50);
    }
}
