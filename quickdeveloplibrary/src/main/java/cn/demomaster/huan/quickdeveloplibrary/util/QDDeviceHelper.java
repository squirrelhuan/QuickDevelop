package cn.demomaster.huan.quickdeveloplibrary.util;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class QDDeviceHelper {

    //FLAG_PLAY_SOUND
    private static int flagDef = AudioManager.FLAG_SHOW_UI;
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

    public static void setFlagDef(int flagDef) {
        QDDeviceHelper.flagDef = flagDef;
    }

    //1.最大通话音量
    public static int getVioceVolumeMax() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
    }

    //1.当前通话音量
    public static int getVioceVolumeCurrent() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
    }

    //1.设置通话音量
    public static void setVioceVolume(int level) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,level,flagDef);
    }

    //1.设置通话音量
    public static void setVioceVolume(int level,int flag) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,level,flag);
    }

    //2.最大系统音量
    public static int getSystemVolumeMax() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
    }

    //2.当前系统音量
    public static int getSystemVolumeCurrent() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
    }

    //2.设置系统音量
    public static void setSystemVolume(int level) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,level,flagDef);
    }

    //2.设置系统音量
    public static void setSystemVolume(int level,int flag) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,level,flag);
    }

    //3.最大铃声音量
    public static int getRingVolumeMax() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
    }

    //3.当前铃声音量
    public static int getRingVolumeCurrent() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
    }

    //3.设置铃声音量
    public static void setRingVolume(int level) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_RING,level,flagDef);
    }

    //3.设置铃声音量
    public static void setRingVolume(int level,int flag) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_RING,level,flag);
    }

    //4.最大音乐音量
    public static int getMusicVolumeMax() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    //4.当前音乐音量
    public static int getMusicVolumeCurrent() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    //4.设置音乐音量 FLAG_PLAY_SOUND
    public static void setMusicVolume(int level) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,level,flagDef);
    }

    //4.设置音乐音量
    public static void setMusicVolume(int level,int flag) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,level,flag);
    }

    //5.最大提示声音音量
    public static int getAlarmVolumeMax() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
    }

    //5.当前提示声音音量
    public static int getAlarmVolumeCurrent() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
    }

    //5.设置提示声音音量
    public static void setAlarmVolume(int level) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM,level,flagDef);
    }

}
