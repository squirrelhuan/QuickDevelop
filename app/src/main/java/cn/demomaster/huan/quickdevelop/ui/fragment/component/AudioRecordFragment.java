package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.adapter.AppListAdapter;
import cn.demomaster.huan.quickdevelop.adapter.AudioAdapter;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.SoundHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.audio.ByteUtils;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.OnClickActionListener;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDDialog;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.quickaudiorecorderlib.AudioRecorder;
import cn.demomaster.quickaudiorecorderlib.RecordStreamListener;
import cn.demomaster.quickaudiorecorderlib.view.WaveLineView;
import cn.demomaster.quickaudiorecorderlib.view.WaveView;
import cn.demomaster.quickaudiorecorderlib.view.wechat.WechatAudioRecordPopup;
import cn.demomaster.quickpermission_library.PermissionHelper;

import static cn.demomaster.quickaudiorecorderlib.AudioUtil.calculateVolume;
import static cn.demomaster.quickaudiorecorderlib.view.wechat.WechatAudioRecordPopup.fitPopupWindowOverStatusBar;


/**
 * 音频播放view
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "音频录制", preViewClass = TextView.class, resType = ResType.Custome)
public class AudioRecordFragment extends BaseFragment {

    View mView;

    @Nullable
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_layout_audiorecord, null);
        return mView;
    }

    @Override
    public void initView(View rootView) {
        Button btn_record = rootView.findViewById(R.id.btn_record);
        btn_record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                PermissionHelper.getInstance().requestPermission(mContext, new String[]{
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionHelper.PermissionListener() {
                    @Override
                    public void onPassed() {
                        showPopWindow(v, event);
                    }

                    @Override
                    public void onRefused() {
                        Toast.makeText(mContext, "拒绝", Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }
        });

        initDatas();
        initUI();
    }

    private WechatAudioRecordPopup pop = null;

    private void showPopWindow(View v, MotionEvent event) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
        if (pop != null && pop.isShowing()) {
            pop.setButtonLocation(location);
            pop.getTouchableViewGroup().dispatchTouchEvent(event);
            return;
        }
        pop = new WechatAudioRecordPopup(getContext());
        //pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setWidth(DisplayUtil.getScreenWidth(mContext));
        pop.setHeight(DisplayUtil.getScreenHeight(mContext));
        //pop.setClippingEnabled(true);
        //pop.setIsClippedToScreen(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(false);
        //pop.showAtLocation(pop.getContentView(), Gravity.BOTTOM, 0, 0);
        pop.showAtLocation(pop.getContentView(), Gravity.NO_GRAVITY, 0, 0);
        pop.setOnRecordListener(new WechatAudioRecordPopup.OnRecordListener() {
            @Override
            public void onFinish() {
                stopRecord();
            }

            @Override
            public void onCancel() {
                releaseRecord();
            }
        });
        pop.setButtonLocation(location);
        pop.getTouchableViewGroup().dispatchTouchEvent(event);

        //振動
        Vibrator vibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
        long[] patter = {0, 30, 10, 30};
        vibrator.vibrate(patter, -1);
        startRecord();
        /*startAnimation(AnimationUtils.loadAnimation(getThemeActivity(),
                R.anim.bottom_up));*/
    }

    Button btn_start_record;
    Button btn_pause_record;
    Button btn_stop_record;
    WaveView waveView;
    WaveLineView waveLineView;
    AudioRecorder audioRecorder;

    //存放的目录路径名称
    //初始化数据
    private void initDatas() {
        audioRecorder = AudioRecorder.getInstance();
        audioRecorder.setRecordStreamListener(mRecordStreamListener);
    }

    RecyclerView recyclerview_audio;
    AudioAdapter adapter;
    List<String> audioFiles = new ArrayList<>();

    //初始化UI
    private void initUI() {
        btn_start_record = mView.findViewById(R.id.btn_start_record);
        btn_start_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecord();
            }
        });
        btn_pause_record = mView.findViewById(R.id.btn_pause_record);
        btn_pause_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseRecord();
            }
        });
        btn_stop_record = mView.findViewById(R.id.btn_stop_record);
        btn_stop_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecord();
            }
        });

        waveView = mView.findViewById(R.id.waveView);
        waveLineView = mView.findViewById(R.id.waveLineView);

        recyclerview_audio = mView.findViewById(R.id.recyclerview_audio);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        recyclerview_audio.setLayoutManager(layoutManager);
        adapter = new AudioAdapter(getContext());
        adapter.setOnItemClickListener(new AudioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                playAudio(fileOutputPath + "/" + audioFiles.get(position));
            }

            @Override
            public void onItemPreview(View view, int position, Image image) {

            }

            @Override
            public void onDelete(View view, int position) {
                QDDialog qdDialog = new QDDialog.Builder(mContext)
                        .setMessage("确定要删除该文件吗？")
                        //.setMinHeight_body((int) getResources().getDimension(R.dimen.dp_100))
                        //.setGravity_body(Gravity.CENTER).setText_size_body((int) getResources().getDimension(R.dimen.sp_10))
                        //.setWidth((int) getResources().getDimension(R.dimen.dp_120))
                        //.setText_color_body(Color.BLUE)
                        .addAction("确定", new OnClickActionListener() {
                            @Override
                            public void onClick(Dialog dialog, View view, Object tag) {
                                String f = (fileOutputPath + "/" + audioFiles.get(position));
                                QDLogger.e("删除：" + (fileOutputPath + "/" + audioFiles.get(position)));
                                QDFileUtil.delete(f);
                                refreshUI();
                                dialog.dismiss();
                            }
                        })
                        .addAction("取消")//.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setText_size_foot((int) getResources().getDimension(R.dimen.sp_6))
                        //.setPadding_foot((int) getResources().getDimension(R.dimen.sp_6))
                        //.setActionButtonPadding((int) getResources().getDimension(R.dimen.sp_6))
                        .setText_color_foot(Color.BLACK).create();
                qdDialog.show();
            }
        });
        recyclerview_audio.setAdapter(adapter);
        refreshUI();
    }

    private void playAudio(String path) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //开始录音
    public void startRecord() {
        PermissionHelper.getInstance().requestPermission(mContext, new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionHelper.PermissionListener() {
            @Override
            public void onPassed() {
                if (audioRecorder.getStatus() == AudioRecorder.Status.STATUS_IDLE) {
                    audioRecorder.createAudio(getContext());
                    //audioRecorder.setOutputFilePath("");
                    //audioRecorder.setFileName("hahaha");
                }
                audioRecorder.startRecord();
            }

            @Override
            public void onRefused() {
                Toast.makeText(mContext, "拒绝", Toast.LENGTH_LONG).show();
            }
        });
    }

    //结束录音
    private void stopRecord() {
        //QdToast.show(getContext(),"结束录音");
        audioRecorder.stopRecord();
        refreshUI();
    }

    String fileOutputPath;

    private void refreshUI() {
        audioFiles.clear();
        fileOutputPath = mContext.getFilesDir().getAbsolutePath() + "/wav";
        File file = new File(fileOutputPath);
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File file1 : file.listFiles()) {
                    audioFiles.add(file1.getName());
                }
            }
        }
        adapter.updateList(audioFiles);
    }

    //暂停录音
    private void pauseRecord() {
        audioRecorder.pauseRecord();
    }

    //取消录音
    private void releaseRecord() {
        //QdToast.show(getContext(),"取消录音");
        audioRecorder.release();
    }

    int count = 0;
    RecordStreamListener mRecordStreamListener = new RecordStreamListener() {
        @Override
        public void onStart() {
            btn_start_record.setVisibility(View.GONE);
            btn_pause_record.setVisibility(View.VISIBLE);
            btn_stop_record.setVisibility(View.VISIBLE);
            waveLineView.startAnim();
        }

        @Override
        public void onPause() {
            btn_start_record.setVisibility(View.VISIBLE);
            btn_pause_record.setVisibility(View.GONE);
            btn_stop_record.setVisibility(View.VISIBLE);
            waveLineView.stopAnim();
        }

        @Override
        public void onStop(boolean isSuccess, String filePath) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btn_start_record.setVisibility(View.VISIBLE);
                    btn_pause_record.setVisibility(View.GONE);
                    btn_stop_record.setVisibility(View.GONE);
                    waveLineView.stopAnim();
                    refreshUI();
                }
            });
        }

        @Override
        public void onRelease() {
            btn_start_record.setVisibility(View.VISIBLE);
            btn_pause_record.setVisibility(View.GONE);
            btn_stop_record.setVisibility(View.GONE);
            waveLineView.stopAnim();
        }

        @Override
        public void recordOfByte(byte[] data, int begin, int end) {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    short[] data1 = ByteUtils.toShorts(data);
                    for (int i = 0; i < data1.length; i += 60) {
                        waveView.addData(data1[i]);
                    }

                    if (count % 1 == 0) {
                        int volume = (calculateVolume(data1));
                        double myVolume = (volume - 40) * 4;
                        //Log.d("MainActivity", "current volume=" + volume + ",myVolume=" + myVolume);
                        if (waveLineView != null) {
                            waveLineView.setVolume((int) myVolume);
                        }
                        if (pop != null) {
                            pop.setVolume(Math.max(0, volume - 40));
                        }
                        //Log.d("MainActivity", "current volume is " + volume);
                    }
                    count++;
                }
            });
        }
    };

}