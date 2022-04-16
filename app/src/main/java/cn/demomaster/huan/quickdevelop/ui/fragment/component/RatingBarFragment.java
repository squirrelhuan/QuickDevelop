package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.RatingBar;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.ToggleButton;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;


/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "进度条", preViewClass = TextView.class, resType = ResType.Resource)
public class RatingBarFragment extends QuickFragment {

    private SeekBar sb_weight, sb_progress;
    private RatingBar ratingBar;
    private ToggleButton tooglebutton, tooglebutton_datatype, tooglebutton_customdrable, tooglebutton_minValue;

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_rating_bar, null);
        return view;
    }

    @Override
    public void initView(View rootView) {
        ButterKnife.bind(this,rootView);
        ratingBar = findViewById(R.id.ratingBar);
        sb_weight = findViewById(R.id.sb_weight);
        sb_weight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int max = 200;
                int w = DisplayUtil.dip2px(mContext, max * progress / 100f);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(w, w);
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
                ratingBar.setProgress((float) progress / 100f);
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
        tooglebutton.setOnToggleChanged((view, on) -> ratingBar.setCanTouch(on));
        tooglebutton_datatype = findViewById(R.id.tooglebutton_datatype);
        tooglebutton_datatype.setChecked(true);
        ratingBar.setFloat(true);
        tooglebutton_datatype.setToogleColor(Color.RED);
        tooglebutton_datatype.setOnToggleChanged((view, on) -> ratingBar.setFloat(on));

        tooglebutton_customdrable = findViewById(R.id.tooglebutton_customdrable);
        tooglebutton_customdrable.setToogleColor(Color.BLUE);
        tooglebutton_customdrable.setOnToggleChanged((view, on) -> {
            //使用默认背景前要把自定义的资源设置好
            ratingBar.setBackResourceId(R.mipmap.meizi);
            ratingBar.setFrontResourceId(R.mipmap.ic_launcher);
            ratingBar.setUseCustomDrable(on);
        });

        tooglebutton_minValue = findViewById(R.id.tooglebutton_minValue);
        tooglebutton_minValue.setToogleColor(Color.BLUE);
        tooglebutton_minValue.setOnToggleChanged((view, on) -> {
            //使用默认背景前要把自定义的资源设置好
            ratingBar.setCountMni(on ? 3 : 0);//int类型
            ratingBar.setProgressMin(on ? .6f : 0f);//int类型
        });
    }
}