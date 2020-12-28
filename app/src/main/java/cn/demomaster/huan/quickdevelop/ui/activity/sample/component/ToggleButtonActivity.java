package cn.demomaster.huan.quickdevelop.ui.activity.sample.component;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.activity.BaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.ToggleButton;
import cn.demomaster.quicksticker_annotations.BindView;
import cn.demomaster.quicksticker_annotations.QuickStickerBinder;

@ActivityPager(name = "开关按钮",preViewClass = ToggleButton.class,resType = ResType.Custome)
public class ToggleButtonActivity extends BaseActivity {

    @BindView(R.id.sb_weight)
    SeekBar sb_weight;
    @BindView(R.id.tooglebutton)
    ToggleButton tooglebutton;
    @BindView(R.id.sb_progress_color)
    SeekBar sb_progress_color;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toggle_button);
        QuickStickerBinder.getInstance().bind(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QuickStickerBinder.getInstance().unBind(this);
    }
}
