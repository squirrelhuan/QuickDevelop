package cn.demomaster.huan.quickdevelop.ui.activity.sample.component;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.activity.BaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QuickToggleButton;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;
import cn.demomaster.quicksticker_annotations.BindView;
import cn.demomaster.quicksticker_annotations.QuickStickerBinder;

@ActivityPager(name = "Switch 开关",preViewClass = QuickToggleButton.class,resType = ResType.Resource,iconRes = R.drawable.ic_switch_mul)
public class ToggleButtonActivity extends QuickFragment {

    @BindView(R.id.sb_weight)
    SeekBar sb_weight;
    @BindView(R.id.tooglebutton)
    QuickToggleButton tooglebutton;
    @BindView(R.id.sb_progress_color)
    SeekBar sb_progress_color;

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_toggle_button, null);
        return mView;
    }

    @Override
    public String getTitle() {
        return "Switch 开关";
    }

    @Override
    public void initView(View rootView) {
        QuickStickerBinder.getInstance().bind(this,rootView);
        sb_weight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int max = 200;
                int w = DisplayUtil.dip2px(mContext,max* progress/100f);
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
    public void onDestroyView() {
        super.onDestroyView();
        QuickStickerBinder.getInstance().unBind(this);
    }

}
