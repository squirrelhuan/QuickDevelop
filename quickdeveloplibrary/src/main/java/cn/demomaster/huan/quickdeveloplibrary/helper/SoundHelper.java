package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashMap;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * @author squirrel桓
 * @date 2019/2/14.
 * description：
 */
public class SoundHelper {

    private static SoundHelper instance;
    private static Context context;
    private SoundPool soundPool;

    public static SoundHelper init(Context mcontext, boolean autoLoaded, Object rawClass) {
        context = mcontext.getApplicationContext();
        if (autoLoaded) {//自动加载raw下的资源
            Class a = (Class) rawClass;
            Field[] fields = a.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    //得到资源id
                    int id = Integer.valueOf((int) field.get(rawClass));
                    //添加到音频集合中
                    soundMap.put(id, field.getName());
                } catch (IllegalArgumentException e) {
                    QDLogger.e(e);
                } catch (IllegalAccessException e) {
                    QDLogger.e(e);
                }
                //System.out.println(fields[i].getName());
            }
        }
        return getInstance();
    }

    /**
     * 初始化
     *
     * @param mcontext
     * @return
     */
    public static SoundHelper init(Context mcontext) {
        return init(mcontext, false, null);
    }

    public static SoundHelper getInstance() {
        if (instance == null) {
            instance = new SoundHelper();
        }
        return instance;
    }

    /**
     * 添加音頻
     *
     * @param resId
     */
    public void addSound(int resId) {
        soundMap.put(resId, context.getResources().getResourceName(resId));
        instance = new SoundHelper();
    }

    /**
     * 移除音频
     *
     * @param resId
     */
    public void removeSound(int resId) {
        soundMap.remove(resId);
        instance = new SoundHelper();
    }

    private static LinkedHashMap<Integer, String> soundMap = new LinkedHashMap();

    public SoundHelper() {
        if (context == null) {
            //在此前请初始化
            return;
        }
        //soundMap.put(R.raw.timer_start,R.raw.timer_start);
        //soundMap.put(R.raw.timer_start,R.raw.no_kill);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适的属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            soundPool = new SoundPool.Builder().setAudioAttributes(attrBuilder.build()).build();
        } else {
            soundPool = new SoundPool(soundMap.size(), AudioManager.STREAM_MUSIC, 0);//AudioManager.STREAM_SYSTEM
        }
        //加载音频文件
        loadAudio();

    }

    private void loadAudio() {
        Iterator iter = soundMap.keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            //Object val = soundMap.get(key);
            try {
                soundPool.load(context, (Integer) key, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //根据所在位置播放音频
    public void playByIndex(int index) {
        soundPool.play(index, 1, 1, 0, 0, 1);
    }

    //根据资源id播放音频
    public void playByResID(int id) {
        if (soundPool != null) {
            try {
                soundPool.play(getIndexByResID(id), 1, 1, 0, 0, 1);
            } catch (Exception e) {
                QDLogger.e("未找到音频文件:" + e.toString());
            }
        } else {
            QDLogger.e("未找到音频文件");
        }
    }

    /**
     * 获取音频文件在列表中的位置
     *
     * @param id
     * @return
     */
    public int getIndexByResID(int id) {
        int index = 0;
        Iterator iter = soundMap.keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            if ((int) key == id) {
                return index + 1;
            }
            index = index + 1;
        }
        return 1;
    }

    public static int getRawIdByName(String resName) {
        return getResId(resName, R.raw.class);
    }
    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            if(e instanceof NoSuchFieldException){
                QDLogger.e("未找到音频资源："+resName);
            }else {
                QDLogger.e(e);
            }
            return -1;
        }
    }
}
