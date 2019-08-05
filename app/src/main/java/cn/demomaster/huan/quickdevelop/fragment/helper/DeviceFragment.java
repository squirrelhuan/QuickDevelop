package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout2;
import cn.demomaster.huan.quickdeveloplibrary.util.QDDeviceHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;

import static cn.demomaster.huan.quickdeveloplibrary.constant.EventBusConstant.EVENT_REFRESH_LANGUAGE;
import static cn.demomaster.huan.quickdeveloplibrary.util.system.QDLanguageUtil.changeAppLanguage;
import static cn.demomaster.huan.quickdeveloplibrary.util.system.QDLanguageUtil.setLanguageLocal;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "DeviceHelper", preViewClass = StateView.class, resType = ResType.Custome)
public class DeviceFragment extends QDBaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    //Components
    @BindView(R.id.sb_MusicVolume)
    SeekBar sb_MusicVolume;
    @BindView(R.id.sb_RingVolume)
    SeekBar sb_RingVolume;
    @BindView(R.id.sb_AlarmVolume)
    SeekBar sb_AlarmVolume;
    @BindView(R.id.sb_VioceVolume)
    SeekBar sb_VioceVolume;

    @BindView(R.id.tv_MusicVolume)
    TextView tv_MusicVolume;
    @BindView(R.id.tv_RingVolume)
    TextView tv_RingVolume;
    @BindView(R.id.tv_AlarmVolume)
    TextView tv_AlarmVolume;
    @BindView(R.id.tv_VioceVolume)
    TextView tv_VioceVolume;

    View mView;
    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_device, null);
        }
        ButterKnife.bind(this, mView);
        return (ViewGroup) mView;
    }

    @Override
    public void initView(View rootView, ActionBarInterface actionBarLayoutOld) {
        QDDeviceHelper.init(getContext());
        QDDeviceHelper.setFlagDef(AudioManager.FLAG_PLAY_SOUND);
        //媒体音量
        tv_MusicVolume.setText("媒体"+QDDeviceHelper.getMusicVolumeCurrent()+"/"+QDDeviceHelper.getMusicVolumeMax());
        sb_MusicVolume.setMax(QDDeviceHelper.getMusicVolumeMax());
        sb_MusicVolume.setProgress(QDDeviceHelper.getMusicVolumeCurrent());
        sb_MusicVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                QDDeviceHelper.setMusicVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //铃声音量
        tv_RingVolume.setText("铃声"+QDDeviceHelper.getRingVolumeCurrent()+"/"+QDDeviceHelper.getRingVolumeMax());
        sb_RingVolume.setMax(QDDeviceHelper.getRingVolumeMax());
        sb_RingVolume.setProgress(QDDeviceHelper.getRingVolumeCurrent());
        sb_RingVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                QDDeviceHelper.setRingVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //闹钟音量
        tv_AlarmVolume.setText("闹钟"+QDDeviceHelper.getAlarmVolumeCurrent()+"/"+QDDeviceHelper.getAlarmVolumeMax());
        sb_AlarmVolume.setMax(QDDeviceHelper.getAlarmVolumeMax());
        sb_AlarmVolume.setProgress(QDDeviceHelper.getAlarmVolumeCurrent());
        sb_AlarmVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                QDDeviceHelper.setAlarmVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //通话音量
        tv_VioceVolume.setText("通话"+QDDeviceHelper.getVioceVolumeCurrent()+"/"+QDDeviceHelper.getVioceVolumeMax());
        sb_VioceVolume.setMax(QDDeviceHelper.getVioceVolumeMax());
        sb_VioceVolume.setProgress(QDDeviceHelper.getVioceVolumeCurrent());
        sb_VioceVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                QDDeviceHelper.setVioceVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}