package cn.demomaster.huan.quickdevelop.fragment.component;

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
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.helper.AudioRecordHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;


/**
 * 音频播放view
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "AudioRecordFragment",preViewClass = StateView.class,resType = ResType.Custome)
public class AudioRecordFragment extends BaseFragment {
    //Components
    ViewGroup mView;


    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_audiorecord, null);
        }
        audioRecordHelper = AudioRecordHelper.getInstance();
        Bundle bundle = getArguments();
        String title = "空界面";
        Button button = mView.findViewById(R.id.btn_start);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        start();
                        break;
                    case MotionEvent.ACTION_UP:
                        stop();
                        break;
                }
                return true;
            }
        });
        Button btn_set_title = mView.findViewById(R.id.btn_play);
        btn_set_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (int) (Math.random() * 10 % 4);
                getActionBarLayout().setTitle(titles[i]+"");
            }
        });


        return mView;
    }

    @Override
    public void initView(View rootView, ActionBarLayout actionBarLayout) {

    }

    private String[] titles = {"1", "2", "3", "4"};
    private int[] colors = {Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE};

    public void initActionBarLayout(ActionBarLayout actionBarLayout) {
        int i = (int) (Math.random() * 10 % 4);
        actionBarLayout.setTitle(titles[i]+"---------ASDFGGHHJ");
        actionBarLayout.setHeaderBackgroundColor(colors[i]);
    }

    AudioRecordHelper audioRecordHelper;
    private String path = Environment.getExternalStorageDirectory() + "/buku/audio/record.mp3";;
    private void start() {
        audioRecordHelper.startRecord(path);
    }

    private void stop(){
        audioRecordHelper.stopRecord();
    }
}