package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import cn.demomaster.huan.quickdeveloplibrary.helper.AudioRecordHelper;


/**
 * 音频播放view
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "AudioRecordFragment", preViewClass = TextView.class, resType = ResType.Custome)
public class AudioRecordFragment extends BaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Nullable
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_audiorecord, null);
        return mView;
    }

    @Override
    public void initView(View rootView) {
        audioRecordHelper = AudioRecordHelper.getInstance();
        Button button = rootView.findViewById(R.id.btn_start);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
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
        Button btn_set_title = rootView.findViewById(R.id.btn_play);
        btn_set_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (int) (Math.random() * 10 % 4);
                getActionBarTool().setTitle(titles[i] + "");
            }
        });

    }

    private String[] titles = {"1", "2", "3", "4"};
    AudioRecordHelper audioRecordHelper;
    private String path = Environment.getExternalStorageDirectory() + "/buku/audio/record.mp3";
    ;

    private void start() {
        audioRecordHelper.startRecord(path);
    }

    private void stop() {
        audioRecordHelper.stopRecord();
    }

}