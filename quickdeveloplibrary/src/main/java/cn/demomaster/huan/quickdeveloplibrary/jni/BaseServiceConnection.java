package cn.demomaster.huan.quickdeveloplibrary.jni;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import cn.demomaster.huan.quickdeveloplibrary.jni.aidl.IBaseService;

/**
 * @author squirrel桓
 * @date 2019/1/24.
 * description：
 */

public class BaseServiceConnection implements ServiceConnection {
    private final ServiceConnection mCallback;

    public BaseServiceConnection(ServiceConnection callback) {
        mCallback = callback;
    }

    @Override
    public void onServiceConnected(ComponentName className, IBinder iBinder) {
        //ServiceHelper.mService = IBaseService.Stub.asInterface(iBinder);
        if (mCallback != null)
            mCallback.onServiceConnected(className, iBinder);
    }

    @Override
    public void onServiceDisconnected(ComponentName className) {
        if (mCallback != null)
            mCallback.onServiceDisconnected(className);
            ServiceHelper.mService = null;
    }
}
