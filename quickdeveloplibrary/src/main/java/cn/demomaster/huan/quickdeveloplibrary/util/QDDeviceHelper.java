package cn.demomaster.huan.quickdeveloplibrary.util;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class QDDeviceHelper {

    private static QDDeviceHelper instance;
    static AudioManager mAudioManager;
    private static Context context;

    public static QDDeviceHelper getInstance() {
        if (context == null) {
            try {
                throw new Exception("请先初始化QDDeviceHelper");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (instance == null) {
            instance = new QDDeviceHelper(context);
        }
        return instance;
    }

    public static QDDeviceHelper init(Context context) {
        if (instance == null) {
            instance = new QDDeviceHelper(context);
        }
        return instance;
    }

    private QDDeviceHelper(Context context) {
        this.context = context;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    //最大通话音量
    public static int getVioceVolumeMax() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
    }

    //当前通话音量
    public static int getVioceVolumeCurrent() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
    }

    //最大系统音量
    public static int getSystemVolumeMax() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
    }

    //当前系统音量
    public static int getSystemVolumeCurrent() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
    }

    //最大铃声音量
    public static int getRingVolumeMax() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
    }

    //当前铃声音量
    public static int getRingVolumeCurrent() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
    }

    //最大音乐音量
    public static int getMusicVolumeMax() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    //当前音乐音量
    public static int getMusicVolumeCurrent() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    //最大提示声音音量
    public static int getAlarmVolumeMax() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
    }

    //当前提示声音音量
    public static int getAlarmVolumeCurrent() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
    }

}
