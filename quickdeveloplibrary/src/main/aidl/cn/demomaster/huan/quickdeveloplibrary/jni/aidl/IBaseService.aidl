package cn.demomaster.huan.quickdeveloplibrary.jni.aidl;

import android.graphics.Bitmap;

interface IBaseService
{
    int getQueuePosition();
    boolean isPlaying();
    void stop();

}

