package cn.demomaster.huan.quickdevelop.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.helper.AudioRecordHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.SoundHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "SoundFragment",preViewClass = StateView.class,resType = ResType.Custome)
public class SoundFragment extends BaseFragment {
    //Components
    ViewGroup mView;


    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_sound, null);
        }
        Bundle bundle = getArguments();
        String title = "空界面";
        Button button = mView.findViewById(R.id.btn_play1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(1);
            }
        });
        Button button2 = mView.findViewById(R.id.btn_play2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(2);
            }
        });
        Button button3 = mView.findViewById(R.id.btn_play3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(3);
            }
        });
        Button button4 = mView.findViewById(R.id.btn_play4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });

        return mView;
    }

    private String[] titles = {"1", "2", "3", "4"};
    private int[] colors = {Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE};

    public void initActionBarLayout(ActionBarLayout actionBarLayout) {
        int i = (int) (Math.random() * 10 % 4);
        actionBarLayout.setTitle("audio play");
        actionBarLayout.setHeaderBackgroundColor(colors[i]);
    }

    private void play(int index) {
        SoundHelper.getInstance().playByIndex(index);
        //SoundHelper.getInstance().playByResID(R.raw.beep);
    }

    private void play(){
        SoundHelper.getInstance().playByResID(R.raw.pikaqiu);
    }

}