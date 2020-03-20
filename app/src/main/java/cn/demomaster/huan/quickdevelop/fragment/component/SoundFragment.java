package cn.demomaster.huan.quickdevelop.fragment.component;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.helper.SoundHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "SoundFragment", preViewClass = TextView.class, resType = ResType.Custome)
public class SoundFragment extends QDBaseFragment {
    //Components
    ViewGroup mView;
    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //QDLogger.d("拦截Activity:"+getClass().getName() + "返回事件");
        return super.onKeyDown(keyCode, event);
    }

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

    @Override
    public void initView(View rootView, ActionBarInterface actionBarLayout) {

    }

    private void play(int index) {
        SoundHelper.getInstance().playByIndex(index);

        //SoundHelper.getInstance().playByResID(R.raw.beep);
    }

    private void play() {
        SoundHelper.getInstance().playByResID(R.raw.pikaqiu);
    }
}