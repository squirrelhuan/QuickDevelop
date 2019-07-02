package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

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
        //媒体音量
        sb_MusicVolume.setMax(QDDeviceHelper.getMusicVolumeMax());
        sb_MusicVolume.setProgress(QDDeviceHelper.getMusicVolumeCurrent());
        //铃声音量
        sb_RingVolume.setMax(QDDeviceHelper.getRingVolumeMax());
        sb_RingVolume.setProgress(QDDeviceHelper.getRingVolumeCurrent());
        //闹钟音量
        sb_AlarmVolume.setMax(QDDeviceHelper.getAlarmVolumeMax());
        sb_AlarmVolume.setProgress(QDDeviceHelper.getAlarmVolumeCurrent());
        //通话音量
        sb_VioceVolume.setMax(QDDeviceHelper.getVioceVolumeMax());
        sb_VioceVolume.setProgress(QDDeviceHelper.getVioceVolumeCurrent());
    }

}