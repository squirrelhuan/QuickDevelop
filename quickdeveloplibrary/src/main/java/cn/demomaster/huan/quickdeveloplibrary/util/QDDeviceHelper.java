package cn.demomaster.huan.quickdeveloplibrary.util;

import android.content.Context;
import android.media.AudioManager;

public class QDDeviceHelper {

    //FLAG_PLAY_SOUND
    private static int flagDef = AudioManager.FLAG_SHOW_UI;
    private static QDDeviceHelper instance;

    private QDDeviceHelper() {
    }

    private static AudioManager getAudioManager(Context context) {
        return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public static void setFlagDef(int flagDef) {
        QDDeviceHelper.flagDef = flagDef;
    }

    //1.最大通话音量
    public static int getVioceVolumeMax(Context context) {
        return getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
    }


    //1.当前通话音量
    public static int getVioceVolumeCurrent(Context context) {
        return getAudioManager(context).getStreamVolume(AudioManager.STREAM_VOICE_CALL);
    }

    //1.设置通话音量
    public static void setVioceVolume(Context context,int level) {
        getAudioManager(context).setStreamVolume(AudioManager.STREAM_VOICE_CALL,level,flagDef);
    }

    //1.设置通话音量
    public static void setVioceVolume(Context context,int level,int flag) {
        getAudioManager(context).setStreamVolume(AudioManager.STREAM_VOICE_CALL,level,flag);
    }

    //2.最大系统音量
    public static int getSystemVolumeMax(Context context) {
        return getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
    }

    //2.当前系统音量
    public static int getSystemVolumeCurrent(Context context) {
        return getAudioManager(context).getStreamVolume(AudioManager.STREAM_SYSTEM);
    }

    //2.设置系统音量
    public static void setSystemVolume(Context context,int level) {
        getAudioManager(context).setStreamVolume(AudioManager.STREAM_SYSTEM,level,flagDef);
    }

    //2.设置系统音量
    public static void setSystemVolume(Context context,int level,int flag) {
        getAudioManager(context).setStreamVolume(AudioManager.STREAM_SYSTEM,level,flag);
    }

    //3.最大铃声音量
    public static int getRingVolumeMax(Context context) {
        return getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_RING);
    }

    //3.当前铃声音量
    public static int getRingVolumeCurrent(Context context) {
        return getAudioManager(context).getStreamVolume(AudioManager.STREAM_RING);
    }

    //3.设置铃声音量
    public static void setRingVolume(Context context,int level) {
        getAudioManager(context).setStreamVolume(AudioManager.STREAM_RING,level,flagDef);
    }

    //3.设置铃声音量
    public static void setRingVolume(Context context,int level,int flag) {
        getAudioManager(context).setStreamVolume(AudioManager.STREAM_RING,level,flag);
    }

    //4.最大音乐音量
    public static int getMusicVolumeMax(Context context) {
        return getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    //4.当前音乐音量
    public static int getMusicVolumeCurrent(Context context) {
        return getAudioManager(context).getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    //4.设置音乐音量 FLAG_PLAY_SOUND
    public static void setMusicVolume(Context context,int level) {
        getAudioManager(context).setStreamVolume(AudioManager.STREAM_MUSIC,level,flagDef);
    }

    //4.设置音乐音量
    public static void setMusicVolume(Context context,int level,int flag) {
        getAudioManager(context).setStreamVolume(AudioManager.STREAM_MUSIC,level,flag);
    }

    //5.最大提示声音音量
    public static int getAlarmVolumeMax(Context context) {
        return getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_ALARM);
    }

    //5.当前提示声音音量
    public static int getAlarmVolumeCurrent(Context context) {
        return getAudioManager(context).getStreamVolume(AudioManager.STREAM_ALARM);
    }

    //5.设置提示声音音量
    public static void setAlarmVolume(Context context,int level) {
        getAudioManager(context).setStreamVolume(AudioManager.STREAM_ALARM,level,flagDef);
    }

}
