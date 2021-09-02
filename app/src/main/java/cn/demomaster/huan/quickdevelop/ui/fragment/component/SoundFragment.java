package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.SoundHelper;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "音频播放", preViewClass = TextView.class, resType = ResType.Custome)
public class SoundFragment extends BaseFragment {

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_sound, null);
        return mView;
    }

    @Override
    public void initView(View rootView) {
        Button button = rootView.findViewById(R.id.btn_play1);
        button.setOnClickListener(v -> play(1));
        Button button2 = rootView.findViewById(R.id.btn_play2);
        button2.setOnClickListener(v -> play(2));
        Button button3 = rootView.findViewById(R.id.btn_play3);
        button3.setOnClickListener(v -> play(3));
        Button button4 = rootView.findViewById(R.id.btn_play4);
        button4.setOnClickListener(v -> play());
    }

    private void play(int index) {
        SoundHelper.getInstance().playByIndex(index);
        //SoundHelper.getInstance().playByResID(R.raw.beep);
    }

    private void play() {
        SoundHelper.getInstance().playByResID(R.raw.pikaqiu);
    }
}