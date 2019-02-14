package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.R;

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
        context = mcontext;
        if (autoLoaded) {//自动加载raw下的资源
            Class a = (Class) rawClass;
            Field[] fields = a.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                try {
                    //得到资源id
                    int id = Integer.valueOf((int) fields[i].get(rawClass));
                    //添加到音频集合中
                    soundMap.put(id, fields[i].getName());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
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
            soundPool = new SoundPool.Builder().build();
        } else {
            soundPool = new SoundPool(soundMap.size(), AudioManager.STREAM_SYSTEM, 0);
        }
        //加载音频文件
        Iterator iter = soundMap.keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            //Object val = soundMap.get(key);
            soundPool.load(context, (Integer) key, 1);
        }
    }

    //根据所在位置播放音频
    public void playByIndex(int index) {
        soundPool.play(index, 1, 1, 0, 0, 1);
    }

    //根据资源id播放音频
    public void playByResID(int id) {
        soundPool.play(getIndexByResID(id), 1, 1, 0, 0, 1);
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
                return index+1;
            }
            index = index + 1;
        }
        return 1;
    }
}
