package cn.demomaster.huan.quickdevelop.fragment.component;

import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBar;
import cn.demomaster.huan.quickdeveloplibrary.helper.SoundHelper;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.ToggleButton;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "Button",preViewClass = QDButton.class,resType = ResType.Custome)
public class QdButtonFragment extends QDFragment {
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
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_button, null);
        }
        Button button = mView.findViewById(R.id.btn_play1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Button button2 = mView.findViewById(R.id.btn_play2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return mView;
    }

    @Override
    public void initView(View rootView, ActionBar actionBarLayout) {

    }

    private void play(int index) {
        SoundHelper.getInstance().playByIndex(index);

        //SoundHelper.getInstance().playByResID(R.raw.beep);
    }

    private void play() {
        SoundHelper.getInstance().playByResID(R.raw.pikaqiu);
    }
}