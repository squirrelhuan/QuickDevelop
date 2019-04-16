package cn.demomaster.huan.quickdevelop.activity.sample.component;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.RatingBar;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.ToggleButton;

@ActivityPager(name = "RatingBar",preViewClass = RatingBar.class,resType = ResType.Custome)
public class RatingBarActivity extends BaseActivityParent {

    private SeekBar sb_weight,sb_progress;
    private RatingBar ratingBar;
    private ToggleButton tooglebutton,tooglebutton_datatype,tooglebutton_customdrable,tooglebutton_minValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_bar);

        ratingBar = findViewById(R.id.ratingBar);
        sb_weight = findViewById(R.id.sb_weight);
        sb_weight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int max = 200;
                int w = DisplayUtil.dip2px(mContext,max* progress/100);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(w,w);
                ratingBar.setLayoutParams(layoutParams);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_weight.setProgress(50);
        sb_progress = findViewById(R.id.sb_progress);
        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ratingBar.setProgress((float)progress/100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_progress.setProgress(50);

        tooglebutton = findViewById(R.id.tooglebutton);
        tooglebutton.setOnToggleChanged(new ToggleButton.OnToggleChangeListener() {
            @Override
            public void onToggle(boolean on) {
                ratingBar.setCanTouch(on);
            }
        });
        tooglebutton_datatype = findViewById(R.id.tooglebutton_datatype);
        tooglebutton_datatype.setChecked(true);
        ratingBar.setFloat(true);
        tooglebutton_datatype.setToogleColor(Color.RED);
        tooglebutton_datatype.setOnToggleChanged(new ToggleButton.OnToggleChangeListener() {
            @Override
            public void onToggle(boolean on) {
                ratingBar.setFloat(on);
            }
        });

        tooglebutton_customdrable = findViewById(R.id.tooglebutton_customdrable);
        tooglebutton_customdrable.setToogleColor(Color.BLUE);
        tooglebutton_customdrable.setOnToggleChanged(new ToggleButton.OnToggleChangeListener() {
            @Override
            public void onToggle(boolean on) {
                //使用默认背景前要把自定义的资源设置好
                ratingBar.setBackResourceId(R.mipmap.meizi);
                ratingBar.setFrontResourceId(R.mipmap.gudaimeizi);
                ratingBar.setUseCustomDrable(on);
            }
        });


        tooglebutton_minValue = findViewById(R.id.tooglebutton_minValue);
        tooglebutton_minValue.setToogleColor(Color.BLUE);
        tooglebutton_minValue.setOnToggleChanged(new ToggleButton.OnToggleChangeListener() {
            @Override
            public void onToggle(boolean on) {
                //使用默认背景前要把自定义的资源设置好
                ratingBar.setCountMni(on?3:0);//int类型
                ratingBar.setProgressMin(on?.6f:0f);//int类型
            }
        });
    }


}
